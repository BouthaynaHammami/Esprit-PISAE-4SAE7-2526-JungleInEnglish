import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Club } from '../../models/club.model';
import { environment } from '../../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ClubService {
  private baseUrl = `${environment.devUnityUrl}/learners/api/api/clubs`;

  constructor(private http: HttpClient) {}

  private authHeaders(): HttpHeaders {
    const token = localStorage.getItem('jwt_token') ?? '';
    return new HttpHeaders({
      Authorization: token ? `Bearer ${token}` : '',
      'Content-Type': 'application/json'
    });
  }

  getAll(): Observable<Club[]> {
    return this.http.get<Club[]>(this.baseUrl, { headers: this.authHeaders() });
  }

  getById(id: number): Observable<Club> {
    return this.http.get<Club>(`${this.baseUrl}/${id}`, { headers: this.authHeaders() });
  }

  create(payload: Club): Observable<Club> {
    return this.http.post<Club>(this.baseUrl, payload, { headers: this.authHeaders() });
  }

  update(payload: Club): Observable<Club> {
    return this.http.put<Club>(this.baseUrl, payload, { headers: this.authHeaders() });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`, { headers: this.authHeaders() });
  }
}
