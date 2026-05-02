import { Component, OnInit, OnDestroy, ViewChild, ElementRef, AfterViewChecked } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil, finalize } from 'rxjs/operators';
import { Topic } from '../../../../core/models/topic.model';
import { TopicService } from '../../../../core/services/topic.service';
import { TopicChatService } from '../../../../core/services/topic-chat.service';
import { AuthService } from '../../../../core/services/auth.service';
import { ChatMessage } from '../../../../core/models/chat-message.model';

@Component({
  selector: 'app-student-topics',
  templateUrl: './student-topics.component.html',
  styleUrls: ['./student-topics.component.scss']
})
export class StudentTopicsComponent implements OnInit, OnDestroy, AfterViewChecked {
  @ViewChild('messagesContainer') private messagesContainer!: ElementRef;
  
  private destroy$ = new Subject<void>();
  private shouldScrollToBottom = false;

  topics: Topic[] = [];
  filteredTopics: Topic[] = [];
  loading = false;
  searchQuery = '';
  
  showCreateModal = false;
  newTopic: Partial<Topic> = {
    title: '',
    description: ''
  };
  
  currentUserId: number | null = null;
  currentUserName = '';
  
  selectedTopic: Topic | null = null;
  showChatModal = false;
  
  // Topic Chat
  topicMessages: ChatMessage[] = [];
  topicMessageInput = '';
  topicChatConnected = false;
  showReactionPicker = false;
  selectedMessageForReaction: ChatMessage | null = null;
  
  quickReactions = ['👍', '❤️', '😂', '😮', '😢', '🎉', '🔥', '✨'];

  constructor(
    private topicService: TopicService,
    private topicChatService: TopicChatService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.currentUserId = this.authService.getUserId();
    this.currentUserName = this.authService.getUserFullName();
    this.loadTopics();
    
    // Subscribe to topic chat messages
    this.topicChatService.messages$
      .pipe(takeUntil(this.destroy$))
      .subscribe(messages => {
        this.topicMessages = messages;
        this.shouldScrollToBottom = true;
      });
    
    // Subscribe to topic chat connection status
    this.topicChatService.connected$
      .pipe(takeUntil(this.destroy$))
      .subscribe(connected => {
        this.topicChatConnected = connected;
      });
  }

  ngAfterViewChecked(): void {
    if (this.shouldScrollToBottom) {
      this.scrollToBottom();
      this.shouldScrollToBottom = false;
    }
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
    
    // Disconnect from topic chat if connected
    if (this.selectedTopic?.topicId && this.topicChatConnected) {
      this.topicChatService.disconnect(this.selectedTopic.topicId, this.currentUserName);
    }
  }

  loadTopics(): void {
    this.loading = true;
    this.topicService.getAllTopics()
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => this.loading = false)
      )
      .subscribe({
        next: (topics) => {
          this.topics = topics;
          this.filterTopics();
        },
        error: (err) => {
          console.error('Error loading topics:', err);
        }
      });
  }

  filterTopics(): void {
    if (!this.searchQuery.trim()) {
      this.filteredTopics = [...this.topics];
    } else {
      const query = this.searchQuery.toLowerCase();
      this.filteredTopics = this.topics.filter(topic =>
        topic.title.toLowerCase().includes(query) ||
        (topic.description && topic.description.toLowerCase().includes(query))
      );
    }
  }

  onSearchChange(): void {
    this.filterTopics();
  }

  openCreateModal(): void {
    this.newTopic = {
      title: '',
      description: '',
      userId: this.currentUserId || undefined
    };
    this.showCreateModal = true;
  }

  closeCreateModal(): void {
    this.showCreateModal = false;
    this.newTopic = { title: '', description: '' };
  }

  createTopic(): void {
    if (!this.newTopic.title?.trim()) {
      alert('Please enter a title');
      return;
    }

    if (!this.currentUserId) {
      alert('You must be logged in to create a topic');
      return;
    }

    this.loading = true;
    const topicData: Topic = {
      title: this.newTopic.title,
      description: this.newTopic.description || '',
      userId: this.currentUserId,
      createdAt: new Date().toISOString().split('T')[0] as any
    };

    this.topicService.addTopic(topicData)
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => this.loading = false)
      )
      .subscribe({
        next: () => {
          this.closeCreateModal();
          this.loadTopics();
        },
        error: (err) => {
          console.error('Error creating topic:', err);
          alert('Failed to create topic');
        }
      });
  }

  openTopicChat(topic: Topic): void {
    this.selectedTopic = topic;
    this.showChatModal = true;
    this.topicMessages = [];
    this.topicMessageInput = '';
    
    // Connect to topic chat
    if (topic.topicId) {
      this.topicChatService.connectToTopic(topic.topicId, this.currentUserName);
    }
  }

  closeChatModal(): void {
    if (this.selectedTopic?.topicId) {
      this.topicChatService.disconnect(this.selectedTopic.topicId, this.currentUserName);
    }
    this.showChatModal = false;
    this.selectedTopic = null;
    this.topicMessages = [];
    this.closeReactionPicker();
  }
  
  sendTopicMessage(): void {
    if (!this.topicMessageInput.trim() || !this.selectedTopic?.topicId || !this.topicChatConnected) {
      return;
    }
    
    this.topicChatService.sendMessage(
      this.selectedTopic.topicId,
      this.currentUserName,
      this.topicMessageInput.trim()
    );
    
    this.topicMessageInput = '';
  }
  
  onTopicChatKeyPress(event: KeyboardEvent): void {
    if (event.key === 'Enter' && !event.shiftKey) {
      event.preventDefault();
      this.sendTopicMessage();
    }
  }
  
  showReactionsFor(message: ChatMessage): void {
    if (message.type !== 'CHAT') return;
    this.selectedMessageForReaction = message;
    this.showReactionPicker = true;
  }
  
  closeReactionPicker(): void {
    this.showReactionPicker = false;
    this.selectedMessageForReaction = null;
  }
  
  addReactionToMessage(message: ChatMessage | null, emoji: string): void {
    if (!message || !message.messageId || !this.selectedTopic?.topicId) return;
    
    this.topicChatService.sendReaction(
      this.selectedTopic.topicId,
      message.messageId,
      emoji,
      this.currentUserName
    );
    
    this.closeReactionPicker();
  }
  
  toggleReactionOnMessage(message: ChatMessage, emoji: string): void {
    if (!message.messageId || !this.selectedTopic?.topicId) return;
    
    const hasReacted = this.hasUserReacted(message, emoji);
    
    if (hasReacted) {
      // Remove reaction locally
      if (message.reactions) {
        const index = message.reactions.findIndex(
          r => r.emoji === emoji && r.user === this.currentUserName
        );
        if (index !== -1) {
          message.reactions.splice(index, 1);
        }
      }
    } else {
      // Add reaction
      this.topicChatService.sendReaction(
        this.selectedTopic.topicId,
        message.messageId,
        emoji,
        this.currentUserName
      );
    }
  }
  
  getReactionCounts(message: ChatMessage): any[] {
    if (!message.reactions || message.reactions.length === 0) {
      return [];
    }
    
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
    return message.reactions.some(r => r.emoji === emoji && r.user === this.currentUserName);
  }
  
  getMessageClass(message: ChatMessage): string {
    if (message.type === 'JOIN' || message.type === 'LEAVE') return 'message-system';
    return message.sender === this.currentUserName ? 'message-own' : 'message-other';
  }
  
  formatMessageTime(timestamp: Date | string | undefined): string {
    if (!timestamp) return '';
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

  getTopicIcon(index: number): string {
    const icons = ['💬', '🗣️', '💭', '🎯', '📢', '🌟', '💡', '🔥'];
    return icons[index % icons.length];
  }

  formatDate(date: any): string {
    if (!date) return '';
    const d = new Date(date);
    return d.toLocaleDateString('fr-FR', { day: '2-digit', month: 'short', year: 'numeric' });
  }
}
