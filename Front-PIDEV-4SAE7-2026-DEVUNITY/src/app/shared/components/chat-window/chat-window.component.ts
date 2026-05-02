import { Component, OnInit, OnDestroy, ViewChild, ElementRef, AfterViewChecked } from '@angular/core';
import { ChatWebsocketService } from '../../../core/services/chat-websocket.service';
import { AuthService } from '../../../core/services/auth.service';
import { ChatMessage } from '../../../core/models/chat-message.model';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-chat-window',
  templateUrl: './chat-window.component.html',
  styleUrls: ['./chat-window.component.css']
})
export class ChatWindowComponent implements OnInit, OnDestroy, AfterViewChecked {
  @ViewChild('messagesContainer') private messagesContainer!: ElementRef;

  isOpen = false;
  isConnected = false;
  messages: ChatMessage[] = [];
  filteredMessages: ChatMessage[] = [];
  newMessage = '';
  username = '';
  showEmojiPicker = false;
  
  // New features
  darkMode = false;
  showParticipants = false;
  showSearch = false;
  searchQuery = '';
  onlineUsers: string[] = [];
  showReactionsForMessage: any = null;
  unreadCount = 0;

  // Popular emojis (80+)
  emojis = [
    '😀', '😃', '😄', '😁', '😆', '😅', '🤣', '😂',
    '😊', '😇', '🙂', '🙃', '😉', '😌', '😍', '🥰',
    '😘', '😗', '😙', '😚', '😋', '😛', '😝', '😜',
    '🤪', '🤨', '🧐', '🤓', '😎', '🤩', '🥳', '😏',
    '👍', '👎', '👌', '✌️', '🤞', '🤟', '🤘', '🤙',
    '👏', '🙌', '👐', '🤲', '🤝', '🙏', '✍️', '💪',
    '❤️', '🧡', '💛', '💚', '💙', '💜', '🖤', '🤍',
    '💔', '❣️', '💕', '💞', '💓', '💗', '💖', '💘',
    '🎉', '🎊', '🎈', '🎁', '🏆', '🥇', '🥈', '🥉',
    '⚽', '🏀', '🏈', '⚾', '🥎', '🎾', '🏐', '🏉',
    '🔥', '⭐', '✨', '💫', '🌟', '💥', '💯', '✅'
  ];

  private messagesSubscription?: Subscription;
  private connectedSubscription?: Subscription;
  private unreadSubscription?: Subscription;
  private shouldScrollToBottom = false;

  constructor(
    private chatService: ChatWebsocketService,
    private authService: AuthService
  ) {
    // Load dark mode preference
    this.darkMode = localStorage.getItem('globalChatDarkMode') === 'true';
  }

  ngOnInit(): void {
    // Get username from auth service
    const email = this.authService.getUserEmail() || 'Anonymous';
    this.username = this.extractUsername(email);

    // Subscribe to unread count
    this.unreadSubscription = this.chatService.unreadCount$.subscribe(count => {
      this.unreadCount = count;
      console.log('📬 Unread count updated:', count);
    });

    // Subscribe to messages
    this.messagesSubscription = this.chatService.messages$.subscribe(messages => {
      // Store all messages including REACTION types
      const allMessages = messages;
      
      // Process reaction messages
      allMessages.forEach(msg => {
        if (msg.type === 'REACTION' && msg.messageId) {
          // Find the target message in our current messages array
          const targetMessage = this.messages.find(m => m.messageId === msg.messageId);
          if (targetMessage) {
            if (!targetMessage.reactions) {
              targetMessage.reactions = [];
            }
            // Add reaction if not already present from this user
            const existingReaction = targetMessage.reactions.find(
              r => r.emoji === msg.content && r.user === msg.sender
            );
            if (!existingReaction) {
              targetMessage.reactions.push({
                emoji: msg.content,
                user: msg.sender
              });
            }
          }
        }
      });

      // Filter out REACTION type messages for display
      const displayMessages = allMessages.filter(m => m.type !== 'REACTION');
      
      // No need to check for new messages here - background listener handles it
      
      this.messages = displayMessages;
      this.filterMessages();
      this.shouldScrollToBottom = true;
      this.updateOnlineUsers();
    });

    // Subscribe to connection status
    this.connectedSubscription = this.chatService.connected$.subscribe(connected => {
      this.isConnected = connected;
    });

    // Initialize notification sound
    this.initNotificationSound();
  }

  ngAfterViewChecked(): void {
    if (this.shouldScrollToBottom) {
      this.scrollToBottom();
      this.shouldScrollToBottom = false;
    }
  }

  ngOnDestroy(): void {
    this.messagesSubscription?.unsubscribe();
    this.connectedSubscription?.unsubscribe();
    this.unreadSubscription?.unsubscribe();
    
    if (this.isConnected) {
      this.chatService.disconnect(this.username);
    }
  }

  toggleChat(): void {
    this.isOpen = !this.isOpen;
    
    if (this.isOpen) {
      // Opening chat - reset unread count and connect
      if (!this.isConnected) {
        this.chatService.connect(this.username);
      }
      // Mark all messages as read
      this.chatService.unreadCount$.subscribe(count => {
        if (count > 0) {
          // Reset unread count when opening
          (this.chatService as any).unreadCountSubject.next(0);
        }
      });
    }
    // Don't disconnect when closing - just hide the window
    // This keeps messages in memory
  }

  sendMessage(): void {
    if (this.newMessage.trim() && this.isConnected) {
      this.chatService.sendMessage(this.username, this.newMessage.trim());
      this.newMessage = '';
      this.showEmojiPicker = false;
    }
  }

  toggleEmojiPicker(): void {
    this.showEmojiPicker = !this.showEmojiPicker;
  }

  addEmoji(emoji: string): void {
    this.newMessage += emoji;
    this.showEmojiPicker = false;
  }

  onKeyPress(event: KeyboardEvent): void {
    if (event.key === 'Enter' && !event.shiftKey) {
      event.preventDefault();
      this.sendMessage();
    }
  }

  toggleDarkMode(): void {
    this.darkMode = !this.darkMode;
    localStorage.setItem('globalChatDarkMode', this.darkMode.toString());
  }

  toggleParticipants(): void {
    this.showParticipants = !this.showParticipants;
    if (this.showParticipants) {
      this.showSearch = false;
    }
  }

  toggleSearch(): void {
    this.showSearch = !this.showSearch;
    if (this.showSearch) {
      this.showParticipants = false;
    } else {
      this.clearSearch();
    }
  }

  filterMessages(): void {
    if (!this.searchQuery.trim()) {
      this.filteredMessages = this.messages;
    } else {
      const query = this.searchQuery.toLowerCase();
      this.filteredMessages = this.messages.filter(msg => 
        msg.content.toLowerCase().includes(query) ||
        msg.sender.toLowerCase().includes(query)
      );
    }
  }

  clearSearch(): void {
    this.searchQuery = '';
    this.filterMessages();
  }

  toggleReactions(message: ChatMessage): void {
    if (message.type !== 'CHAT') return;
    
    if (this.showReactionsForMessage === message.timestamp) {
      this.showReactionsForMessage = null;
    } else {
      this.showReactionsForMessage = message.timestamp;
    }
  }

  closeReactions(): void {
    this.showReactionsForMessage = null;
  }

  addReaction(message: ChatMessage, reaction: string): void {
    if (!message.messageId) {
      console.error('❌ Message has no ID');
      return;
    }

    console.log('➕ Adding reaction:', reaction, 'to message:', message.messageId);
    
    // Add reaction locally
    if (!message.reactions) {
      message.reactions = [];
    }
    
    // Check if user already reacted with this emoji
    const existingIndex = message.reactions.findIndex(
      r => r.emoji === reaction && r.user === this.username
    );
    
    if (existingIndex === -1) {
      // Add new reaction
      message.reactions.push({
        emoji: reaction,
        user: this.username
      });
      console.log('✅ Reaction added locally');
    }
    
    // Also send via WebSocket for other users
    this.chatService.sendReaction(message.messageId, reaction, this.username);
    
    this.closeReactions();
  }

  getReactionCounts(message: ChatMessage): any[] {
    if (!message.reactions || message.reactions.length === 0) {
      return [];
    }
    
    // Count reactions by emoji
    const counts = new Map<string, { emoji: string, count: number, users: string[] }>();
    
    message.reactions.forEach((reaction) => {
      if (!counts.has(reaction.emoji)) {
        counts.set(reaction.emoji, { emoji: reaction.emoji, count: 0, users: [] });
      }
      const current = counts.get(reaction.emoji)!;
      current.count++;
      current.users.push(reaction.user);
    });
    
    return Array.from(counts.values());
  }

  hasUserReacted(message: ChatMessage, emoji: string): boolean {
    if (!message.reactions) return false;
    return message.reactions.some((r) => r.emoji === emoji && r.user === this.username);
  }

  removeReaction(message: ChatMessage, emoji: string): void {
    if (!message.reactions || !message.messageId) return;
    
    // Remove locally (will be handled by backend in production)
    const index = message.reactions.findIndex((r) => 
      r.emoji === emoji && r.user === this.username
    );
    
    if (index !== -1) {
      message.reactions.splice(index, 1);
    }
  }

  getMessageClass(message: ChatMessage): string {
    if (message.type === 'JOIN') return 'message-join';
    if (message.type === 'LEAVE') return 'message-leave';
    return message.sender === this.username ? 'message-own' : 'message-other';
  }

  getMessageIcon(message: ChatMessage): string {
    if (message.type === 'JOIN') return '👋';
    if (message.type === 'LEAVE') return '👋';
    return '💬';
  }

  getMessageText(message: ChatMessage): string {
    if (message.type === 'JOIN') return `${message.sender} joined the chat`;
    if (message.type === 'LEAVE') return `${message.sender} ${message.content}`;
    return message.content;
  }

  formatMessageTime(timestamp: Date | string): string {
    const date = new Date(timestamp);
    const now = new Date();
    const diffMs = now.getTime() - date.getTime();
    const diffMins = Math.floor(diffMs / 60000);
    
    if (diffMins < 1) return 'Just now';
    if (diffMins < 60) return `${diffMins}m ago`;
    
    const diffHours = Math.floor(diffMins / 60);
    if (diffHours < 24) return `${diffHours}h ago`;
    
    return date.toLocaleDateString() + ' ' + date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
  }

  formatMessageWithMentions(text: string): string {
    return text.replace(/@(\w+)/g, '<span class="mention">@$1</span>');
  }

  private updateOnlineUsers(): void {
    const userStatus = new Map<string, boolean>();
    
    this.messages.forEach(msg => {
      if (msg.type === 'JOIN') {
        userStatus.set(msg.sender, true);
      } else if (msg.type === 'LEAVE') {
        userStatus.set(msg.sender, false);
      }
    });
    
    this.onlineUsers = Array.from(userStatus.entries())
      .filter(([_, isOnline]) => isOnline)
      .map(([username, _]) => username);
  }

  private extractUsername(email: string): string {
    if (email.includes('@')) {
      return email.split('@')[0];
    }
    return email;
  }

  private scrollToBottom(): void {
    try {
      if (this.messagesContainer) {
        this.messagesContainer.nativeElement.scrollTop = 
          this.messagesContainer.nativeElement.scrollHeight;
      }
    } catch (err) {
      console.error('Scroll error:', err);
    }
  }

  private initNotificationSound(): void {
    const audioContext = new (window.AudioContext || (window as any).webkitAudioContext)();
    const sampleRate = audioContext.sampleRate;
    const duration = 0.4;
    
    const buffer = audioContext.createBuffer(1, duration * sampleRate, sampleRate);
    const data = buffer.getChannelData(0);
    
    // First note (E5 - 659 Hz)
    const freq1 = 659;
    const note1Duration = 0.1;
    for (let i = 0; i < note1Duration * sampleRate; i++) {
      const envelope = Math.max(0, 1 - (i / (note1Duration * sampleRate)));
      data[i] = Math.sin(2 * Math.PI * freq1 * i / sampleRate) * envelope * 0.3;
    }
    
    // Second note (A5 - 880 Hz)
    const freq2 = 880;
    const note2Start = (note1Duration + 0.05) * sampleRate;
    const note2Duration = 0.15;
    for (let i = 0; i < note2Duration * sampleRate; i++) {
      const envelope = Math.max(0, 1 - (i / (note2Duration * sampleRate)));
      data[note2Start + i] = Math.sin(2 * Math.PI * freq2 * i / sampleRate) * envelope * 0.3;
    }
    
    (this as any).audioBuffer = buffer;
    (this as any).audioContext = audioContext;
  }

  private playNotificationSound(): void {
    try {
      const audioContext = (this as any).audioContext;
      const audioBuffer = (this as any).audioBuffer;
      
      if (!audioContext || !audioBuffer) return;
      
      if (audioContext.state === 'suspended') {
        audioContext.resume();
      }
      
      const source = audioContext.createBufferSource();
      source.buffer = audioBuffer;
      source.connect(audioContext.destination);
      source.start(0);
    } catch (e) {
      console.error('Could not play sound:', e);
    }
  }
}
