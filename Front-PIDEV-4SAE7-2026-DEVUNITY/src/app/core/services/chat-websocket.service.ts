import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import SockJS from 'sockjs-client';
import { Client, Message, StompSubscription } from '@stomp/stompjs';
import { ChatMessage } from '../models/chat-message.model';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class ChatWebsocketService {
  private stompClient: Client | null = null;
  private fgRetryCount = 0;
  private readonly FG_MAX_RETRIES = 3;
  private messagesSubject = new BehaviorSubject<ChatMessage[]>([]);
  private connectedSubject = new BehaviorSubject<boolean>(false);
  private unreadCountSubject = new BehaviorSubject<number>(0);
  private subscription: StompSubscription | null = null;
  private backgroundSubscription: StompSubscription | null = null;
  private backgroundClient: Client | null = null;
  private isChatOpen = false;
  private bgRetryCount = 0;
  private readonly BG_MAX_RETRIES = 3;

  public messages$ = this.messagesSubject.asObservable();
  public connected$ = this.connectedSubject.asObservable();
  public unreadCount$ = this.unreadCountSubject.asObservable();

  private readonly WS_URL = environment.socialWsUrl;
  private readonly API_URL = environment.socialApiUrl;

  constructor(private http: HttpClient, private authService: AuthService) {
    // Background listener is only started when the user explicitly calls
    // initBackgroundListener() after login — NOT automatically on construction,
    // to avoid infinite reconnect loops when the backend is not reachable.
  }

  /**
   * Call this once after a successful login to start tracking unread messages.
   * Performs an HTTP preflight check first — if the chat service is down (503/0),
   * no WebSocket connection is attempted and no console spam occurs.
   */
  initBackgroundListener(): void {
    if (!this.authService.isLoggedIn()) return;
    if (this.backgroundClient?.active) return;

    // Preflight: ping the REST history endpoint before opening WebSocket
    this.http.get(`${this.API_URL}/history`, { observe: 'response' }).subscribe({
      next: () => this.startBackgroundListener(),
      error: (err) => {
        if (err.status === 0 || err.status === 503 || err.status === 502 || err.status === 504) {
          console.warn('[Chat] Social service unavailable (preflight failed). Chat features disabled.');
        } else {
          // Service is reachable (e.g. 401/403) — attempt WebSocket anyway
          this.startBackgroundListener();
        }
      }
    });
  }

  /**
   * Stop the background listener (call on logout).
   */
  stopBackgroundListener(): void {
    if (this.backgroundClient?.active) {
      this.backgroundClient.deactivate();
      this.backgroundClient = null;
    }
    if (this.backgroundSubscription) {
      this.backgroundSubscription = null;
    }
  }

  connect(username: string): void {
    this.isChatOpen = true;
    this.unreadCountSubject.next(0);

    if (this.stompClient?.connected) return;

    // Load history — if this fails with 503/0, the service is down: skip WebSocket
    this.loadHistory().subscribe({
      next: (history) => {
        this.messagesSubject.next(history);
        this.connectWebSocket(username);
      },
      error: (err) => {
        if (err.status === 0 || err.status === 503 || err.status === 502 || err.status === 504) {
          console.warn('[Chat] Social service unavailable. Chat is offline.');
          // leave stompClient null — UI should show "disconnected" state
        } else {
          // Service reachable but history failed (e.g. empty) — still try WebSocket
          this.connectWebSocket(username);
        }
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
    this.fgRetryCount = 0;

    this.stompClient = new Client({
      webSocketFactory: () => new SockJS(this.WS_URL),
      reconnectDelay: 0, // managed manually
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
      debug: () => {},

      onConnect: () => {
        this.fgRetryCount = 0;
        console.log('[Chat] Connected to WebSocket');
        this.connectedSubject.next(true);

        this.subscription = this.stompClient!.subscribe('/topic/public', (message: Message) => {
          const chatMessage: ChatMessage = JSON.parse(message.body);
          chatMessage.timestamp = new Date(chatMessage.timestamp || Date.now());

          if (!chatMessage.messageId && chatMessage.type === 'CHAT') {
            chatMessage.messageId = this.generateMessageId();
          }

          const currentMessages = this.messagesSubject.value;
          this.messagesSubject.next([...currentMessages, chatMessage]);
        });

        this.sendJoinMessage(username);
      },

      onDisconnect: () => {
        console.log('[Chat] Disconnected from WebSocket');
        this.connectedSubject.next(false);
        this.scheduleForegroundReconnect(username);
      },

      onStompError: (frame) => {
        console.error('[Chat] STOMP error:', frame);
        this.connectedSubject.next(false);
      },

      onWebSocketError: () => {
        this.connectedSubject.next(false);
        this.scheduleForegroundReconnect(username);
      }
    });

    this.stompClient.activate();
  }

  private scheduleForegroundReconnect(username: string): void {
    if (!this.isChatOpen || !this.stompClient) return;

    this.fgRetryCount++;
    if (this.fgRetryCount > this.FG_MAX_RETRIES) {
      console.warn(`[Chat] Chat service unavailable after ${this.FG_MAX_RETRIES} attempts. Stopped retrying.`);
      this.stompClient?.deactivate();
      this.stompClient = null;
      return;
    }

    const delay = 5000 * Math.pow(2, this.fgRetryCount - 1);
    console.log(`[Chat] Reconnect attempt ${this.fgRetryCount}/${this.FG_MAX_RETRIES} in ${delay / 1000}s`);
    setTimeout(() => {
      if (this.isChatOpen && this.stompClient) {
        this.stompClient.activate();
      }
    }, delay);
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
    this.bgRetryCount = 0;

    this.backgroundClient = new Client({
      webSocketFactory: () => new SockJS(this.WS_URL),
      // reconnectDelay is managed manually below — set to 0 to disable auto-retry
      reconnectDelay: 0,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
      debug: () => {}, // silence STOMP verbose logs

      onConnect: () => {
        this.bgRetryCount = 0; // reset on successful connect
        console.log('[Chat] Background listener connected');

        this.backgroundSubscription = this.backgroundClient!.subscribe('/topic/public', (message: Message) => {
          const chatMessage: ChatMessage = JSON.parse(message.body);

          if (!this.isChatOpen && chatMessage.type === 'CHAT') {
            const currentCount = this.unreadCountSubject.value;
            this.unreadCountSubject.next(currentCount + 1);
          }
        });
      },

      onDisconnect: () => {
        this.scheduleBackgroundReconnect();
      },

      onStompError: () => {
        this.scheduleBackgroundReconnect();
      },

      onWebSocketError: () => {
        this.scheduleBackgroundReconnect();
      }
    });

    this.backgroundClient.activate();
  }

  private scheduleBackgroundReconnect(): void {
    if (!this.backgroundClient) return; // stopped intentionally

    this.bgRetryCount++;
    if (this.bgRetryCount > this.BG_MAX_RETRIES) {
      console.warn(`[Chat] Background listener: chat service unavailable after ${this.BG_MAX_RETRIES} attempts. Giving up.`);
      this.backgroundClient.deactivate();
      this.backgroundClient = null;
      return;
    }

    // Exponential backoff: 5s, 10s, 20s
    const delay = 5000 * Math.pow(2, this.bgRetryCount - 1);
    console.log(`[Chat] Background listener retry ${this.bgRetryCount}/${this.BG_MAX_RETRIES} in ${delay / 1000}s`);
    setTimeout(() => {
      if (this.backgroundClient) {
        this.backgroundClient.activate();
      }
    }, delay);
  }
}
