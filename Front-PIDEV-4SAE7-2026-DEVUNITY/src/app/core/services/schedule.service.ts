import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Schedule } from '../models/schedule.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ScheduleService {

  private readonly BASE = `${environment.apiUrl}/activities/api/schedules`;
  private readonly PLANNING_BASE = `${environment.apiUrl}/activities/api/schedules/course-planning`;

  constructor(private http: HttpClient) { }

  // ── ScheduleController endpoints ──────────────────────────────────────────

  getAll(): Observable<Schedule[]> {
    return this.http.get<Schedule[]>(`${this.BASE}/all`);
  }

  getById(id: number): Observable<Schedule> {
    return this.http.get<Schedule>(`${this.BASE}/${id}`);
  }

  getByProfessor(userId: number): Observable<Schedule[]> {
    return this.http.get<Schedule[]>(`${this.BASE}/professor/${userId}`);
  }

  getByRoom(roomId: number): Observable<Schedule[]> {
    return this.http.get<Schedule[]>(`${this.BASE}/room/${roomId}`);
  }

  getByClass(classId: number): Observable<Schedule[]> {
    return this.http.get<Schedule[]>(`${this.BASE}/class/${classId}`);
  }

  getByType(type: string): Observable<Schedule[]> {
    return this.http.get<Schedule[]>(`${this.BASE}/type/${type}`);
  }

  getBetween(start: string, end: string): Observable<Schedule[]> {
    const params = new HttpParams().set('start', start).set('end', end);
    return this.http.get<Schedule[]>(`${this.BASE}/between`, { params });
  }

  delete(id: number): Observable<boolean> {
    return this.http.delete<boolean>(`${this.BASE}/delete/${id}`);
  }

  // ── CourseSchedulingController endpoint ───────────────────────────────────

  createCourseSchedule(
    classId: number,
    tutorId: number,
    courseId: number,
    roomId: number,
    startTime: string
  ): Observable<Schedule> {
    const params = new HttpParams()
      .set('classId', classId)
      .set('tutorId', tutorId)
      .set('courseId', courseId)
      .set('roomId', roomId)
      .set('startTime', startTime);
    return this.http.post<Schedule>(`${this.PLANNING_BASE}/add`, null, { params });
  }
}
