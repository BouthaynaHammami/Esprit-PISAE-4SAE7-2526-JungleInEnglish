import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Lesson } from '../models/lesson.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class LessonService {

  private readonly BASE = `${environment.apiUrl}/academics/api/lessons`;

  constructor(private http: HttpClient) { }

  getAll(): Observable<Lesson[]> {
    return this.http.get<Lesson[]>(`${this.BASE}/all`);
  }

  getByCourseId(courseId: number): Observable<Lesson[]> {
    return this.http.get<Lesson[]>(`${this.BASE}/course/${courseId}`);
  }

  getById(id: number): Observable<Lesson> {
    return this.http.get<Lesson>(`${this.BASE}/${id}`);
  }

  create(lesson: Lesson): Observable<Lesson> {
    return this.http.post<Lesson>(`${this.BASE}/add`, lesson);
  }

  update(id: number, lesson: Lesson): Observable<Lesson> {
    return this.http.put<Lesson>(`${this.BASE}/update/${id}`, lesson);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.BASE}/delete/${id}`);
  }
}