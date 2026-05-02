import { Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { Observable, Subject } from 'rxjs';
declare var SockJS: any;
declare var Stomp: any;

import { WordBattleGameSession, Player } from '../../models/challenges-competitions.model';

@Injectable({
  providedIn: 'root'
})
export class WordBattleService {
  private socketUrl = `${environment.apiUrl}/activities/api/ws`; // URL du WebSocket
  private stompClient: any;
  private sessionSubject = new Subject<WordBattleGameSession>();

  constructor() {}

  /**
   * Se connecte au WebSocket et s'abonne à une room
   */
  connect(roomId: string): Observable<WordBattleGameSession> {
    const socket = new SockJS(this.socketUrl);

    this.stompClient = Stomp.over(socket);

    this.stompClient.connect({}, () => {
      this.stompClient.subscribe(`/topic/word-battle/${roomId}`, (message: any) => {
        if (message.body) {
          const session: WordBattleGameSession = JSON.parse(message.body);
          this.sessionSubject.next(session);
        }
      });
    });

    return this.sessionSubject.asObservable();
  }

  /**
   * Rejoint une room
   */
  joinRoom(roomId: string, username: string, userId: number, challengeId: number): void {
    const payload = { username, userId, challengeId };
    this.stompClient.send(`/app/word-battle/${roomId}/join`, {}, JSON.stringify(payload));
  }

  /**
   * Démarre manuellement la partie (si admin/créateur)
   */
  startGame(roomId: string): void {
    this.stompClient.send(`/app/word-battle/${roomId}/start`, {}, {});
  }

  /**
   * Soumet un mot
   */
  submitWord(roomId: string, username: string, word: string): void {
    const payload = { username, word };
    this.stompClient.send(`/app/word-battle/${roomId}/submit`, {}, JSON.stringify(payload));
  }

  /**
   * Se déconnecte du WebSocket
   */
  disconnect(): void {
    if (this.stompClient) {
      this.stompClient.disconnect();
    }
  }
}
