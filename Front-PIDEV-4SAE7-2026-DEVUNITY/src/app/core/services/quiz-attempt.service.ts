import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { QuizAttempt } from '../models/quiz-attempt.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class QuizAttemptService {

  private readonly BASE = `${environment.apiUrl}/academics/api/quiz-attempts`;

  constructor(private http: HttpClient) { }

  submit(userId: number, quizId: number, answers: string[]): Observable<QuizAttempt> {
    return this.http.post<QuizAttempt>(
      `${this.BASE}/submit?userId=${userId}&quizId=${quizId}`,
      answers
    );
  }

  getByUserId(userId: number): Observable<QuizAttempt[]> {
    return this.http.get<QuizAttempt[]>(`${this.BASE}/user/${userId}`);
  }

  getByQuizId(quizId: number): Observable<QuizAttempt[]> {
    return this.http.get<QuizAttempt[]>(`${this.BASE}/quiz/${quizId}`);
  }
}