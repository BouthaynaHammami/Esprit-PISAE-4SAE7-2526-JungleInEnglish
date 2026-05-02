import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Enrollment } from '../models/enrollment.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class EnrollmentService {

  private readonly BASE = `${environment.apiUrl}/academics/api/enrollments`;

  constructor(private http: HttpClient) { }

  getAll(): Observable<Enrollment[]> {
    return this.http.get<Enrollment[]>(`${this.BASE}/all`);
  }

  getById(id: number): Observable<Enrollment> {
    return this.http.get<Enrollment>(`${this.BASE}/${id}`);
  }

  getByCourseId(courseId: number): Observable<Enrollment[]> {
    return this.http.get<Enrollment[]>(`${this.BASE}/course/${courseId}`);
  }

  getByUserId(userId: number): Observable<Enrollment[]> {
    return this.http.get<Enrollment[]>(`${this.BASE}/user/${userId}`);
  }

  enroll(userId: number, courseId: number): Observable<Enrollment> {
    return this.http.post<Enrollment>(`${this.BASE}/enroll?userId=${userId}&courseId=${courseId}`, {});
  }

  update(id: number, enrollment: Enrollment): Observable<Enrollment> {
    return this.http.put<Enrollment>(`${this.BASE}/update/${id}`, enrollment);
  }

  delete(id: number): Observable<boolean> {
    return this.http.delete<boolean>(`${this.BASE}/delete/${id}`);
  }
}