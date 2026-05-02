import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { Observable } from 'rxjs';
import { Badge, BadgeEvaluationResultDTO } from '../../models/challenges-competitions.model';

@Injectable({ providedIn: 'root' })
export class BadgeService {
  private baseUrl = `${environment.apiUrl}/activities/api/badges`;
  constructor(private http: HttpClient) {}

  getAll(): Observable<Badge[]> {
    return this.http.get<Badge[]>(this.baseUrl);
  }

  getById(idBadge: number): Observable<Badge> {
    return this.http.get<Badge>(`${this.baseUrl}/${idBadge}`);
  }

  create(payload: Partial<Badge>): Observable<Badge> {
    return this.http.post<Badge>(this.baseUrl, payload);
  }

  update(idBadge: number, payload: Partial<Badge>): Observable<Badge> {
    return this.http.put<Badge>(`${this.baseUrl}/${idBadge}`, payload);
  }

  delete(idBadge: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${idBadge}`);
  }

  getStudentStats(userId: number): Observable<BadgeEvaluationResultDTO> {
    return this.http.get<BadgeEvaluationResultDTO>(`${this.baseUrl}/stats/${userId}`);
  }
}
