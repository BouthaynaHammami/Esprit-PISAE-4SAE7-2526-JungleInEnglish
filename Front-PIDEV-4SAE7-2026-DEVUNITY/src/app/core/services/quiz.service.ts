import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Quiz } from '../models/quiz.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class QuizService {

  private readonly BASE = `${environment.apiUrl}/academics/api/quizzes`;

  constructor(private http: HttpClient) { }

  getAll(): Observable<Quiz[]> {
    return this.http.get<Quiz[]>(`${this.BASE}/all`);
  }

  getById(id: number): Observable<Quiz> {
    return this.http.get<Quiz>(`${this.BASE}/${id}`);
  }

  getByCourseId(courseId: number): Observable<Quiz> {
    return this.http.get<Quiz>(`${this.BASE}/course/${courseId}`);
  }

  create(quiz: Quiz): Observable<Quiz> {
    return this.http.post<Quiz>(`${this.BASE}/add`, quiz);
  }

  update(id: number, quiz: Quiz): Observable<Quiz> {
    return this.http.put<Quiz>(`${this.BASE}/update/${id}`, quiz);
  }

  delete(id: number): Observable<boolean> {
    return this.http.delete<boolean>(`${this.BASE}/delete/${id}`);
  }
}