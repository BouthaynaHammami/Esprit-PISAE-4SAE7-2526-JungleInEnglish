import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import SockJS from 'sockjs-client';
import { Client, Message, StompSubscription } from '@stomp/stompjs';
import { ChatMessage } from '../models/chat-message.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TopicChatService {
  private stompClient: Client | null = null;
  private messagesSubject = new BehaviorSubject<ChatMessage[]>([]);
  private connectedSubject = new BehaviorSubject<boolean>(false);
  private subscription: StompSubscription | null = null;
  private currentTopicId: number | null = null;

  public messages$ = this.messagesSubject.asObservable();
  public connected$ = this.connectedSubject.asObservable();

  private readonly WS_URL = environment.socialWsUrl;
  private readonly API_URL = environment.socialApiUrl;

  constructor(private http: HttpClient) {}

  connectToTopic(topicId: number, username: string): void {
    if (this.stompClient?.connected && this.currentTopicId === topicId) {
      console.log('Already connected to this topic');
      return;
    }

    // Disconnect from previous topic if any
    if (this.stompClient?.connected && this.currentTopicId !== topicId) {
      this.disconnect(this.currentTopicId!, username);
    }

    this.currentTopicId = topicId;
    
    // Load history first
    this.loadTopicHistory(topicId).subscribe({
      next: (history) => {
        this.messagesSubject.next(history);
        this.connectWebSocket(topicId, username);
      },
      error: (err) => {
        console.error('Failed to load history:', err);
        this.connectWebSocket(topicId, username);
      }
    });
  }

  private loadTopicHistory(topicId: number): Observable<ChatMessage[]> {
    return this.http.get<ChatMessage[]>(`${this.API_URL}/history/topic/${topicId}`).pipe(
      map(messages => messages.map(msg => {
        // Generate messageId for historical messages if not present
        if (!msg.messageId && msg.type === 'CHAT') {
          msg.messageId = this.generateMessageId();
        }
        return msg;
      }))
    );
  }

  private connectWebSocket(topicId: number, username: string): void {
    this.stompClient = new Client({
      webSocketFactory: () => new SockJS(this.WS_URL),
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
      debug: (str) => console.log('[STOMP Topic]', str),
      
      onConnect: () => {
        console.log(`✅ Connected to Topic ${topicId} chat`);
        this.connectedSubject.next(true);
        
        // Subscribe to topic-specific messages
        this.subscription = this.stompClient!.subscribe(
          `/topic/chat/${topicId}`, 
          (message: Message) => {
            const chatMessage: ChatMessage = JSON.parse(message.body);
            chatMessage.timestamp = new Date(chatMessage.timestamp || Date.now());
            
            // Generate messageId if not present
            if (!chatMessage.messageId && chatMessage.type === 'CHAT') {
              chatMessage.messageId = this.generateMessageId();
            }
            
            const currentMessages = this.messagesSubject.value;
            this.messagesSubject.next([...currentMessages, chatMessage]);
          }
        );

        // Announce user joined
        this.sendJoinMessage(topicId, username);
      },

      onDisconnect: () => {
        console.log('❌ Disconnected from topic chat');
        this.connectedSubject.next(false);
      },

      onStompError: (frame) => {
        console.error('❌ STOMP error:', frame);
        this.connectedSubject.next(false);
      }
    });

    this.stompClient.activate();
  }

  sendMessage(topicId: number, sender: string, content: string): void {
    if (!this.stompClient?.connected) {
      console.error('Not connected to WebSocket');
      return;
    }

    const chatMessage: ChatMessage = {
      sender,
      content,
      type: 'CHAT',
      topicId,
      messageId: this.generateMessageId()
    };

    this.stompClient.publish({
      destination: `/app/chat.sendMessage.topic.${topicId}`,
      body: JSON.stringify(chatMessage)
    });
  }

  sendReaction(topicId: number, messageId: string, emoji: string, username: string): void {
    if (!this.stompClient?.connected) {
      console.error('Not connected to WebSocket');
      return;
    }

    const reactionMessage: ChatMessage = {
      sender: username,
      content: emoji,
      type: 'REACTION',
      topicId,
      messageId: messageId
    };

    this.stompClient.publish({
      destination: `/app/chat.sendMessage.topic.${topicId}`,
      body: JSON.stringify(reactionMessage)
    });
  }

  private generateMessageId(): string {
    return `${Date.now()}-${Math.random().toString(36).substr(2, 9)}`;
  }

  private sendJoinMessage(topicId: number, username: string): void {
    const joinMessage: ChatMessage = {
      sender: username,
      content: '',
      type: 'JOIN',
      topicId
    };

    this.stompClient!.publish({
      destination: `/app/chat.addUser.topic.${topicId}`,
      body: JSON.stringify(joinMessage)
    });
  }

  disconnect(topicId: number, username: string): void {
    if (this.stompClient?.connected) {
      const leaveMessage: ChatMessage = {
        sender: username,
        content: 'left the chat',
        type: 'LEAVE',
        topicId
      };

      this.stompClient.publish({
        destination: `/app/chat.sendMessage.topic.${topicId}`,
        body: JSON.stringify(leaveMessage)
      });
      
      if (this.subscription) {
        this.subscription.unsubscribe();
      }
      
      this.stompClient.deactivate();
      this.connectedSubject.next(false);
      this.messagesSubject.next([]);
      this.currentTopicId = null;
    }
  }

  isConnected(): boolean {
    return this.connectedSubject.value;
  }

  getMessages(): ChatMessage[] {
    return this.messagesSubject.value;
  }
}
