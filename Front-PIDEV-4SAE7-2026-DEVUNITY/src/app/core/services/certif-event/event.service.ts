import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EventPayload } from '../../models/event.model';

@Injectable({
  providedIn: 'root'
})
export class EventService {

private readonly apiUrl = 'http://localhost:8081/communities/api/events';
  constructor(private http: HttpClient) {}

  addEvent(payload: EventPayload): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/add`, payload);
  }

  updateEvent(id: number, payload: EventPayload): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/update/${id}`, payload);
  }

  addEventWithImage(formData: FormData): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/add-with-image`, formData);
  }

  updateEventWithImage(id: number, formData: FormData): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/update-with-image/${id}`, formData);
  }

  getAll(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/all`);
  }

  getEventById(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`);
  }

  deleteEvent(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete/${id}`);
  }
}