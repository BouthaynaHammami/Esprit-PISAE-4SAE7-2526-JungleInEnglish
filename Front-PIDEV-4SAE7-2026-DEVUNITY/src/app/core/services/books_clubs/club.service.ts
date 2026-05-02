import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Club } from '../../models/club.model';
import { environment } from '../../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ClubService {
 private baseUrl = `${environment.apiUrl}/learners/api/api/clubs`;


  constructor(private http: HttpClient) {}

  getAll(): Observable<Club[]> {
    return this.http.get<Club[]>(this.baseUrl);
  }

  getById(id: number): Observable<Club> {
    return this.http.get<Club>(`${this.baseUrl}/${id}`);
  }

  create(payload: Club): Observable<Club> {
    return this.http.post<Club>(this.baseUrl, payload);
  }

  update(payload: Club): Observable<Club> {
    return this.http.put<Club>(this.baseUrl, payload);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
