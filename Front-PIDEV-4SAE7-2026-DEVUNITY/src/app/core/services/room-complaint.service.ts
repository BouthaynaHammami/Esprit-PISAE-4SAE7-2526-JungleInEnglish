import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { RoomScheduleComplaint, RoomScheduleComplaintCreateRequest } from '../models/room-complaint.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class RoomScheduleComplaintService {

    private readonly BASE = `${environment.apiUrl}/activities/api/room-schedule-complaints`;

    constructor(private http: HttpClient) { }

    getAll(): Observable<RoomScheduleComplaint[]> {
        return this.http.get<RoomScheduleComplaint[]>(`${this.BASE}/all`);
    }

    getById(id: number): Observable<RoomScheduleComplaint> {
        return this.http.get<RoomScheduleComplaint>(`${this.BASE}/${id}`);
    }

    getPending(): Observable<RoomScheduleComplaint[]> {
        return this.http.get<RoomScheduleComplaint[]>(`${this.BASE}/pending`);
    }

    getByRoom(roomId: number): Observable<RoomScheduleComplaint[]> {
        return this.http.get<RoomScheduleComplaint[]>(`${this.BASE}/room/${roomId}`);
    }

    getByComplainantEmail(email: string, role?: string): Observable<RoomScheduleComplaint[]> {
        const normalized = (email || '').trim().toLowerCase();
        const normalizedRole = (role || '').trim().toUpperCase();
        return this.getAll().pipe(
            map((complaints) => (complaints || []).filter((c) => {
                const matchesEmail = (c.complainantEmail || '').toLowerCase() === normalized;
                const matchesRole = !normalizedRole || (c.complainantRole || '').toUpperCase() === normalizedRole;
                return matchesEmail && matchesRole;
            }))
        );
    }

    createForSchedule(scheduleId: number, complaint: RoomScheduleComplaintCreateRequest): Observable<RoomScheduleComplaint> {
        return this.http.post<RoomScheduleComplaint>(`${this.BASE}/add/schedule/${scheduleId}`, complaint);
    }

    answer(id: number, answer: string): Observable<RoomScheduleComplaint> {
        const params = new HttpParams().set('answer', answer);
        return this.http.patch<RoomScheduleComplaint>(`${this.BASE}/${id}/answer`, null, { params });
    }

    delete(id: number): Observable<boolean> {
        return this.http.delete<boolean>(`${this.BASE}/delete/${id}`);
    }
}
