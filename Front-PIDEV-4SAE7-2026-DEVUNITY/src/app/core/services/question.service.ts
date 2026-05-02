import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Question, ActivityWithQuestions } from '../models/english-kids-question.model';

@Injectable({
  providedIn: 'root'
})
export class QuestionService {
  private apiUrl = 'http://localhost:8087/language/api/questions';

  constructor(private http: HttpClient) {}

  getActivityWithQuestions(activityId: number): Observable<ActivityWithQuestions> {
    return this.http.get<ActivityWithQuestions>(`${this.apiUrl}/activity/${activityId}`);
  }

  addQuestion(activityId: number, question: Question): Observable<Question> {
    return this.http.post<Question>(`${this.apiUrl}/activity/${activityId}`, question);
  }

  updateQuestion(questionId: number, question: Question): Observable<Question> {
    return this.http.put<Question>(`${this.apiUrl}/${questionId}`, question);
  }

  deleteQuestion(questionId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${questionId}`);
  }
}
