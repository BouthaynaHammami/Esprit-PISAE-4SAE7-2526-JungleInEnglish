import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import SockJS from 'sockjs-client';
import { Client, Message, StompSubscription } from '@stomp/stompjs';
import { ChatMessage } from '../models/chat-message.model';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ChatWebsocketService {
  private stompClient: Client | null = null;
  private messagesSubject = new BehaviorSubject<ChatMessage[]>([]);
  private connectedSubject = new BehaviorSubject<boolean>(false);
  private unreadCountSubject = new BehaviorSubject<number>(0);
  private subscription: StompSubscription | null = null;
  private backgroundSubscription: StompSubscription | null = null;
  private isChatOpen = false;

  public messages$ = this.messagesSubject.asObservable();
  public connected$ = this.connectedSubject.asObservable();
  public unreadCount$ = this.unreadCountSubject.asObservable();

  private readonly WS_URL = 'http://localhost:8085/socials/api/ws-chat';
  private readonly API_URL = 'http://localhost:8085/socials/api/api/chat';

  constructor(private http: HttpClient) {
    // Start background listener on service creation
    this.startBackgroundListener();
  }

  connect(username: string): void {
    this.isChatOpen = true;
    this.unreadCountSubject.next(0); // Reset unread count when opening chat
    
    if (this.stompClient?.connected) {
      console.log('Already connected');
      return;
    }

    // Load message history first
    this.loadHistory().subscribe({
      next: (history) => {
        this.messagesSubject.next(history);
        this.connectWebSocket(username);
      },
      error: (err) => {
        console.error('Failed to load history:', err);
        this.connectWebSocket(username);
      }
    });
  }

  private loadHistory(): Observable<ChatMessage[]> {
    return this.http.get<ChatMessage[]>(`${this.API_URL}/history`).pipe(
      map(messages => messages.map(msg => {
        // Generate messageId for historical messages if not present
        if (!msg.messageId && msg.type === 'CHAT') {
          msg.messageId = this.generateMessageId();
        }
        return msg;
      }))
    );
  }

  private connectWebSocket(username: string): void {
    this.stompClient = new Client({
      webSocketFactory: () => new SockJS(this.WS_URL),
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
      debug: (str) => console.log('[STOMP]', str),
      
      onConnect: () => {
        console.log('✅ Connected to WebSocket');
        this.connectedSubject.next(true);
        
        // Subscribe to public messages
        this.subscription = this.stompClient!.subscribe('/topic/public', (message: Message) => {
          const chatMessage: ChatMessage = JSON.parse(message.body);
          chatMessage.timestamp = new Date(chatMessage.timestamp || Date.now());
          
          // Generate messageId if not present
          if (!chatMessage.messageId && chatMessage.type === 'CHAT') {
            chatMessage.messageId = this.generateMessageId();
          }
          
          const currentMessages = this.messagesSubject.value;
          this.messagesSubject.next([...currentMessages, chatMessage]);
        });

        // Announce user joined
        this.sendJoinMessage(username);
      },

      onDisconnect: () => {
        console.log('❌ Disconnected from WebSocket');
        this.connectedSubject.next(false);
      },

      onStompError: (frame) => {
        console.error('❌ STOMP error:', frame);
        this.connectedSubject.next(false);
      }
    });

    this.stompClient.activate();
  }

  disconnect(username: string): void {
    this.isChatOpen = false;
    
    if (this.stompClient?.connected) {
      this.sendLeaveMessage(username);
      
      if (this.subscription) {
        this.subscription.unsubscribe();
      }
      
      this.stompClient.deactivate();
      this.connectedSubject.next(false);
      // DON'T clear messages - keep them for when user reopens chat
      // this.messagesSubject.next([]);
    }
  }

  sendMessage(sender: string, content: string): void {
    if (!this.stompClient?.connected) {
      console.error('Not connected to WebSocket');
      return;
    }

    const chatMessage: ChatMessage = {
      sender,
      content,
      type: 'CHAT',
      messageId: this.generateMessageId()
    };

    this.stompClient.publish({
      destination: '/app/chat.sendMessage',
      body: JSON.stringify(chatMessage)
    });
  }

  sendReaction(messageId: string, emoji: string, username: string): void {
    if (!this.stompClient?.connected) {
      console.error('Not connected to WebSocket');
      return;
    }

    const reactionMessage: ChatMessage = {
      sender: username,
      content: emoji,
      type: 'REACTION',
      messageId: messageId
    };

    this.stompClient.publish({
      destination: '/app/chat.sendMessage',
      body: JSON.stringify(reactionMessage)
    });
  }

  private generateMessageId(): string {
    return `${Date.now()}-${Math.random().toString(36).substr(2, 9)}`;
  }

  private sendJoinMessage(username: string): void {
    const joinMessage: ChatMessage = {
      sender: username,
      content: '',
      type: 'JOIN'
    };

    this.stompClient!.publish({
      destination: '/app/chat.addUser',
      body: JSON.stringify(joinMessage)
    });
  }

  private sendLeaveMessage(username: string): void {
    const leaveMessage: ChatMessage = {
      sender: username,
      content: 'left the chat',
      type: 'LEAVE'
    };

    this.stompClient!.publish({
      destination: '/app/chat.sendMessage',
      body: JSON.stringify(leaveMessage)
    });
  }

  isConnected(): boolean {
    return this.connectedSubject.value;
  }

  getMessages(): ChatMessage[] {
    return this.messagesSubject.value;
  }

  getUnreadCount(): number {
    return this.unreadCountSubject.value;
  }

  private startBackgroundListener(): void {
    const backgroundClient = new Client({
      webSocketFactory: () => new SockJS(this.WS_URL),
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
      debug: (str) => console.log('[STOMP Background]', str),
      
      onConnect: () => {
        console.log('✅ Background listener connected for global chat');
        
        // Subscribe to public messages in background
        this.backgroundSubscription = backgroundClient.subscribe('/topic/public', (message: Message) => {
          const chatMessage: ChatMessage = JSON.parse(message.body);
          
          // Only increment unread count if chat is closed and message is CHAT type
          if (!this.isChatOpen && chatMessage.type === 'CHAT') {
            const currentCount = this.unreadCountSubject.value;
            this.unreadCountSubject.next(currentCount + 1);
            console.log('📬 New message while chat closed. Unread count:', currentCount + 1);
          }
        });
      },

      onDisconnect: () => {
        console.log('❌ Background listener disconnected');
      },

      onStompError: (frame) => {
        console.error('❌ Background STOMP error:', frame);
      }
    });

    backgroundClient.activate();
  }
}
