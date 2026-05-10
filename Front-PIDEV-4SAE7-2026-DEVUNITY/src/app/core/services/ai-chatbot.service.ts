import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

export interface AiMessage {
  role: 'user' | 'assistant';
  content: string;
}

@Injectable({
  providedIn: 'root'
})
export class AiChatbotService {

  // Proxy through Spring Boot backend — no CORS issues
  private readonly proxyUrl = 'http://localhost:8081/learners/api/ai/chat';

  constructor(private http: HttpClient) {}

  sendMessage(history: AiMessage[], userMessage: string): Observable<string> {
    const body = {
      history,
      message: userMessage
    };

    return this.http.post<{ reply?: string; error?: string }>(this.proxyUrl, body).pipe(
      map(res => {
        if (res.error) throw new Error(res.error);
        return res.reply ?? '';
      }),
      catchError(err => {
        console.error('AI proxy error:', err);
        const msg = err?.message ?? 'AI service unavailable. Please try again later.';
        return throwError(() => new Error(msg));
      })
    );
  }
}
