import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Progress } from '../models/progress.model';

@Injectable({
  providedIn: 'root'
})
export class ProgressService {
  private apiUrl = 'http://localhost:8081/languages/api/progress';

  constructor(private http: HttpClient) {}

  getAllProgress(): Observable<Progress[]> {
    return this.http.get<Progress[]>(this.apiUrl);
  }

  getProgressById(id: number): Observable<Progress> {
    return this.http.get<Progress>(`${this.apiUrl}/${id}`);
  }

  addProgress(progress: Progress): Observable<Progress> {
    return this.http.post<Progress>(`${this.apiUrl}/add`, progress);
  }

  updateProgress(progress: Progress): Observable<Progress> {
    return this.http.put<Progress>(`${this.apiUrl}/update`, progress);
  }

  deleteProgress(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete/${id}`);
  }

  getProgressByChild(childId: number): Observable<Progress[]> {
    return this.http.get<Progress[]>(`${this.apiUrl}/child/${childId}`);
  }

  getProgressByCourse(courseId: number): Observable<Progress[]> {
    return this.http.get<Progress[]>(`${this.apiUrl}/course/${courseId}`);
  }

  getProgressByChildAndCourse(childId: number, courseId: number): Observable<Progress[]> {
    return this.http.get<Progress[]>(`${this.apiUrl}/child/${childId}/course/${courseId}`);
  }
}
