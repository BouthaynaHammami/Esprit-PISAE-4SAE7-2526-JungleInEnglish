import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import { UnreadMessagesService } from './unread-messages.service';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class GlobalChatListenerService {
  private stompClient: Client | null = null;
  private readonly WS_URL = 'http://localhost:8081/ws-chat';
  private username = '';
  private openChats = new Set<number>();

  constructor(
    private http: HttpClient,
    private unreadMessagesService: UnreadMessagesService,
    private authService: AuthService
  ) {
    const email = this.authService.getUserEmail() || 'Anonymous';
    this.username = this.extractUsername(email);
  }

  startListening(topicIds: number[]): void {
    if (this.stompClient?.connected) {
      console.log('Already listening to global chat');
      return;
    }

    this.stompClient = new Client({
      webSocketFactory: () => new SockJS(this.WS_URL),
      reconnectDelay: 5000,
      
      onConnect: () => {
        console.log('✅ Global chat listener connected');
        
        // Subscribe to all topics
        topicIds.forEach(topicId => {
          this.stompClient!.subscribe(`/topic/chat/${topicId}`, (message) => {
            const chatMessage = JSON.parse(message.body);
            
            // Only increment if message is from someone else and chat is not open
            if (chatMessage.sender !== this.username && 
                chatMessage.type === 'CHAT' && 
                !this.openChats.has(topicId)) {
              this.unreadMessagesService.incrementUnread(topicId);
            }
          });
        });
      },

      onDisconnect: () => {
        console.log('❌ Global chat listener disconnected');
      }
    });

    this.stompClient.activate();
  }

  markChatAsOpen(topicId: number): void {
    this.openChats.add(topicId);
    this.unreadMessagesService.resetUnread(topicId);
  }

  markChatAsClosed(topicId: number): void {
    this.openChats.delete(topicId);
  }

  stopListening(): void {
    if (this.stompClient?.connected) {
      this.stompClient.deactivate();
    }
  }

  private extractUsername(email: string): string {
    if (email.includes('@')) {
      return email.split('@')[0];
    }
    return email;
  }
}
