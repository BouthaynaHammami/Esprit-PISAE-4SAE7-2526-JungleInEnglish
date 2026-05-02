import { Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { Observable, Subject } from 'rxjs';
declare var SockJS: any;
declare var Stomp: any;

import { TranslationRaceSession } from '../../models/challenges-competitions.model';

@Injectable({
  providedIn: 'root'
})
export class SpeedTranslationService {
  private socketUrl = `${environment.apiUrl}/activities/api/ws`;
  private stompClient: any;
  private sessionSubject = new Subject<TranslationRaceSession>();

  constructor() {}

  connect(roomId: string): Observable<TranslationRaceSession> {
    const socket = new SockJS(this.socketUrl);
    this.stompClient = Stomp.over(socket);

    // Disable debug logging in production
    if (environment.production) {
      this.stompClient.debug = () => {};
    }

    this.stompClient.connect({}, () => {
      this.stompClient.subscribe(`/topic/speed-translation/${roomId}`, (message: any) => {
        if (message.body) {
          const session: TranslationRaceSession = JSON.parse(message.body);
          this.sessionSubject.next(session);
        }
      });
    });

    return this.sessionSubject.asObservable();
  }

  joinRoom(roomId: string, username: string, userId: number, challengeId: number): void {
    const payload = { username, userId, challengeId };
    this.stompClient.send(`/app/speed-translation/${roomId}/join`, {}, JSON.stringify(payload));
  }

  startGame(roomId: string): void {
    this.stompClient.send(`/app/speed-translation/${roomId}/start`, {}, {});
  }

  submitTranslation(roomId: string, username: string, translation: string): void {
    const payload = { username, translation };
    this.stompClient.send(`/app/speed-translation/${roomId}/submit`, {}, JSON.stringify(payload));
  }

  disconnect(): void {
    if (this.stompClient) {
      this.stompClient.disconnect();
    }
  }
}
