import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '../auth.service';

@Injectable({
  providedIn: 'root'
})
export class RegistrationService {

private readonly apiUrl = 'http://localhost:8081/communities/api/registrations';;
private readonly userUrl = 'http://localhost:8081/learners/api/users';
constructor(
    private http: HttpClient,
    private authService: AuthService
  ) {}

  private get userId(): number {
    return this.authService.getUserId()!;
  }

  register(eventId: number, comment?: string): Observable<any> {
    let params = new HttpParams()
      .set('eventId', eventId)
      .set('userId', this.userId);

    if (comment) {
      params = params.set('comment', comment);
    }

    return this.http.post<any>(`${this.apiUrl}/register`, null, { params });
  }

  getMyRegistrations(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/my/${this.userId}`);
  }

  cancelRegistration(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}/cancel`);
  }

  getAllRegistrations(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/all`);
  }

  updateStatus(id: number, status: string): Observable<any> {
    const params = new HttpParams().set('status', status);
    return this.http.put<any>(`${this.apiUrl}/${id}/status`, null, { params });
  }

  getUserById(userId: number): Observable<any> {
    return this.http.get<any>(`${this.userUrl}/${userId}`);
  }

  getEventAvailability(eventId: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/events/${eventId}/availability`);
  }

  getTicket(registrationId: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${registrationId}/ticket`);
  }
}