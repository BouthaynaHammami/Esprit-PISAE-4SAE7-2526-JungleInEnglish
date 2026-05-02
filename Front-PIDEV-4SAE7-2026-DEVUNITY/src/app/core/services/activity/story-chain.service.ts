import { Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { Observable, Subject } from 'rxjs';
declare var SockJS: any;
declare var Stomp: any;
import { StoryChainGameSession } from '../../models/challenges-competitions.model';

@Injectable({
  providedIn: 'root'
})
export class StoryChainService {
  private socketUrl = `${environment.apiUrl}/activities/api/ws`;
  private stompClient: any;
  private sessionSubject = new Subject<StoryChainGameSession>();

  constructor() {}

  connect(roomId: string): Observable<StoryChainGameSession> {
    const socket = new SockJS(this.socketUrl);
    this.stompClient = Stomp.over(socket);

    this.stompClient.connect({}, () => {
      this.stompClient.subscribe(`/topic/story-chain/${roomId}`, (message: any) => {
        if (message.body) {
          const session: StoryChainGameSession = JSON.parse(message.body);
          this.sessionSubject.next(session);
        }
      });
    });

    return this.sessionSubject.asObservable();
  }

  joinRoom(roomId: string, username: string, userId: number, challengeId: number): void {
    const payload = { username, userId, challengeId };
    this.stompClient.send(`/app/story-chain/${roomId}/join`, {}, JSON.stringify(payload));
  }

  startGame(roomId: string): void {
    this.stompClient.send(`/app/story-chain/${roomId}/start`, {}, {});
  }

  submitSentence(roomId: string, username: string, sentence: string): void {
    const payload = { username, sentence };
    this.stompClient.send(`/app/story-chain/${roomId}/submit`, {}, JSON.stringify(payload));
  }

  disconnect(): void {
    if (this.stompClient) {
      this.stompClient.disconnect();
    }
  }
}
