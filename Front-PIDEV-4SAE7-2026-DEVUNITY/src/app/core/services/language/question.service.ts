import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Question, QuizAttempt } from '../../models/english-kids.model';

@Injectable({
  providedIn: 'root'
})
export class QuestionService {
  private readonly apiUrl = `${environment.apiUrl}/languages/api`;

  constructor(private http: HttpClient) {}

  getQuestionsForActivity(activityId: number): Observable<Question[]> {
    return this.http.get<Question[]>(`${this.apiUrl}/activities/${activityId}/questions`);
  }

  submitQuizAttempt(attempt: QuizAttempt): Observable<QuizAttempt> {
    return this.http.post<QuizAttempt>(`${this.apiUrl}/activities/${attempt.activityId}/submit`, attempt);
  }

  getAttemptHistory(childId: number): Observable<QuizAttempt[]> {
    return this.http.get<QuizAttempt[]>(`${this.apiUrl}/attempts/child/${childId}`);
  }

  getAttemptById(attemptId: number): Observable<QuizAttempt> {
    return this.http.get<QuizAttempt>(`${this.apiUrl}/attempts/${attemptId}`);
  }
}
