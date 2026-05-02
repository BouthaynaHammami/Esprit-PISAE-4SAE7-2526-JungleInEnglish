import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { Observable } from 'rxjs';
import { Excursion, Training } from '../../models/activity.model';

export interface ExcursionParticipation {
  id?: number;
  memberId: number;
  registrationDate?: string;
  status: 'REGISTERED' | 'CONFIRMED' | 'ABSENT';
  excursion: { excursionId: number };
}

export interface TrainingParticipation {
  id?: number;
  memberId: number;
  registrationDate?: string;
  status: 'REGISTERED' | 'CONFIRMED' | 'ABSENT';
  training: { trainingId: number };
}

@Injectable({ providedIn: 'root' })
export class ActivityService {
  private baseUrl = `${environment.apiUrl}/learners/api/api/activities`;

  constructor(private http: HttpClient) {}

  // Excursions
  addExcursion(payload: Excursion): Observable<Excursion> {
    return this.http.post<Excursion>(`${this.baseUrl}/excursions`, payload);
  }

  updateExcursion(payload: Excursion): Observable<Excursion> {
    return this.http.put<Excursion>(`${this.baseUrl}/excursions`, payload);
  }

  getExcursionsByClub(clubId: number): Observable<Excursion[]> {
    return this.http.get<Excursion[]>(`${this.baseUrl}/excursions/club/${clubId}`);
  }

  getExcursion(id: number): Observable<Excursion> {
    return this.http.get<Excursion>(`${this.baseUrl}/excursions/${id}`);
  }

  deleteExcursion(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/excursions/${id}`);
  }

  registerExcursion(memberId: number, excursionId: number): Observable<ExcursionParticipation> {
    const params = new HttpParams().set('memberId', memberId).set('excursionId', excursionId);
    return this.http.post<ExcursionParticipation>(`${this.baseUrl}/excursions/register`, null, { params });
  }

  excursionParticipants(excursionId: number): Observable<ExcursionParticipation[]> {
    return this.http.get<ExcursionParticipation[]>(`${this.baseUrl}/excursions/${excursionId}/participants`);
  }

  // Trainings
  addTraining(payload: Training): Observable<Training> {
    return this.http.post<Training>(`${this.baseUrl}/trainings`, payload);
  }

  updateTraining(payload: Training): Observable<Training> {
    return this.http.put<Training>(`${this.baseUrl}/trainings`, payload);
  }

  getTrainingsByClub(clubId: number): Observable<Training[]> {
    return this.http.get<Training[]>(`${this.baseUrl}/trainings/club/${clubId}`);
  }

  getTraining(id: number): Observable<Training> {
    return this.http.get<Training>(`${this.baseUrl}/trainings/${id}`);
  }

  deleteTraining(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/trainings/${id}`);
  }

  registerTraining(memberId: number, trainingId: number): Observable<TrainingParticipation> {
    const params = new HttpParams().set('memberId', memberId).set('trainingId', trainingId);
    return this.http.post<TrainingParticipation>(`${this.baseUrl}/trainings/register`, null, { params });
  }

  trainingParticipants(trainingId: number): Observable<TrainingParticipation[]> {
    return this.http.get<TrainingParticipation[]>(`${this.baseUrl}/trainings/${trainingId}/participants`);
  }
}
