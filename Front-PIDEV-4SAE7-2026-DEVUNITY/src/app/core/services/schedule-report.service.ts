import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Schedule } from '../models/schedule.model';

@Injectable({ providedIn: 'root' })
export class ScheduleReportService {
  private readonly BASE = `${environment.apiUrl}/activities/api/schedules/course-planning`;

  constructor(private http: HttpClient) {}

  getTutorWeekSchedules(tutorId: number, weekStart: string): Observable<Schedule[]> {
    const params = new HttpParams()
      .set('tutorId', tutorId)
      .set('weekStart', weekStart);
    return this.http.get<Schedule[]>(`${this.BASE}/weekly/tutor`, { params });
  }

  getStudentWeekSchedules(studentId: number, weekStart: string): Observable<Schedule[]> {
    const params = new HttpParams()
      .set('studentId', studentId)
      .set('weekStart', weekStart);
    return this.http.get<Schedule[]>(`${this.BASE}/weekly/student`, { params });
  }

  exportTutorWeekPdf(tutorId: number, weekStart: string, scheduleIds: number[]): Observable<Blob> {
    let params = new HttpParams()
      .set('tutorId', tutorId)
      .set('weekStart', weekStart);

    scheduleIds.forEach(id => {
      params = params.append('scheduleIds', id);
    });

    return this.http.post(`${this.BASE}/weekly-pdf/tutor`, null, {
      params,
      responseType: 'blob'
    });
  }

  exportStudentWeekPdf(studentId: number, weekStart: string, scheduleIds: number[]): Observable<Blob> {
    let params = new HttpParams()
      .set('studentId', studentId)
      .set('weekStart', weekStart);

    scheduleIds.forEach(id => {
      params = params.append('scheduleIds', id);
    });

    return this.http.post(`${this.BASE}/weekly-pdf/student`, null, {
      params,
      responseType: 'blob'
    });
  }
}
