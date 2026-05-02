import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Course } from '../models/course.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class CourseService {

  private readonly BASE = `${environment.apiUrl}/academics/api/courses`;

  constructor(private http: HttpClient) { }

  getAll(): Observable<Course[]> {
    return this.http.get<Course[]>(`${this.BASE}/all`);
  }

  getById(id: number): Observable<Course> {
    return this.http.get<Course>(`${this.BASE}/${id}`);
  }

  getByLevel(level: string): Observable<Course[]> {
    return this.http.get<Course[]>(`${this.BASE}/level/${level}`);
  }

  getCatalog(): Observable<Course[]> {
    return this.http.get<Course[]>(`${this.BASE}/catalog`);
  }

  getEnrolledCourses(userId: number): Observable<Course[]> {
    return this.http.get<Course[]>(`${this.BASE}/enrolled/${userId}`);
  }

  checkAccess(courseId: number, userId: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.BASE}/${courseId}/access/${userId}`);
  }

  create(course: Course): Observable<Course> {
    return this.http.post<Course>(`${this.BASE}/add`, course);
  }

  update(id: number, course: Course): Observable<Course> {
    return this.http.put<Course>(`${this.BASE}/update/${id}`, course);
  }

  delete(id: number): Observable<boolean> {
    return this.http.delete<boolean>(`${this.BASE}/delete/${id}`);
  }

  assignLessons(courseId: number, lessonIds: number[]): Observable<Course> {
    return this.http.post<Course>(`${this.BASE}/${courseId}/lessons`, lessonIds);
  }

  assignQuiz(courseId: number, quizId: number): Observable<Course> {
    return this.http.post<Course>(`${this.BASE}/${courseId}/quiz/${quizId}`, {});
  }

  toggleVisibility(id: number): Observable<Course> {
    return this.http.put<Course>(`${this.BASE}/hide/${id}`, {});
  }

  uploadImage(courseId: number, file: File): Observable<Course> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<Course>(`${this.BASE}/${courseId}/image`, formData);
  }

  getBusinessCourses(): Observable<Course[]> {
    return this.http.get<Course[]>(`${this.BASE}/type/BUSINESS_ENGLISH`);
  }

  getGeneralCourses(): Observable<Course[]> {
    return this.http.get<Course[]>(`${this.BASE}/type/GENERAL_ENGLISH`);
  }
}