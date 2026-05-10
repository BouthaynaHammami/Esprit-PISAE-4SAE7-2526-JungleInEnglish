import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Excursion, Training } from '../../models/activity.model';

export interface ExcursionParticipation {
  id?: number;
  participationId?: number;
  memberId: number;
  registrationDate?: string;
  status: 'REGISTERED' | 'CONFIRMED' | 'FAILED';
  excursion: { excursionId: number };
}

export interface TrainingParticipation {
  id?: number;
  participationId?: number;
  memberId: number;
  registrationDate?: string;
  status: 'REGISTERED' | 'CONFIRMED' | 'FAILED';
  paymentStatus?: 'PENDING' | 'PAID' | 'FAILED';
  paymentMethod?: 'CASH' | 'WALLET';
  completed?: boolean;
  score?: number;
  rewardAmount?: number;
  rewardTransferred?: boolean;
  training: { trainingId: number };
}

@Injectable({ providedIn: 'root' })
export class ActivityService {
  private activitiesUrl = `${environment.devUnityUrl}/learners/api/api/activities`;
  private trainingManagementUrl = `${environment.devUnityUrl}/learners/api/api/training-management`;

  constructor(private http: HttpClient) {}

  private authHeaders(): HttpHeaders {
    const token = localStorage.getItem('jwt_token') ?? '';
    return new HttpHeaders({
      Authorization: token ? `Bearer ${token}` : '',
      'Content-Type': 'application/json'
    });
  }

  private authOptions(params?: HttpParams): { headers: HttpHeaders; params?: HttpParams } {
    const headers = this.authHeaders();
    return params ? { headers, params } : { headers };
  }

  // ===================== EXCURSIONS =====================

  addExcursion(payload: Excursion): Observable<Excursion> {
    return this.http.post<Excursion>(
      `${this.activitiesUrl}/excursions`,
      payload,
      { headers: this.authHeaders() }
    );
  }

  updateExcursion(payload: Excursion): Observable<Excursion> {
    return this.http.put<Excursion>(
      `${this.activitiesUrl}/excursions`,
      payload,
      { headers: this.authHeaders() }
    );
  }

  getExcursionsByClub(clubId: number): Observable<Excursion[]> {
    return this.http.get<Excursion[]>(
      `${this.activitiesUrl}/excursions/club/${clubId}`,
      { headers: this.authHeaders() }
    );
  }

  getExcursion(id: number): Observable<Excursion> {
    return this.http.get<Excursion>(
      `${this.activitiesUrl}/excursions/${id}`,
      { headers: this.authHeaders() }
    );
  }

  deleteExcursion(id: number): Observable<void> {
    return this.http.delete<void>(
      `${this.activitiesUrl}/excursions/${id}`,
      { headers: this.authHeaders() }
    );
  }

  registerExcursion(memberId: number, excursionId: number): Observable<ExcursionParticipation> {
    const params = new HttpParams()
      .set('memberId', memberId.toString())
      .set('excursionId', excursionId.toString());

    return this.http.post<ExcursionParticipation>(
      `${this.activitiesUrl}/excursions/register`,
      {},
      this.authOptions(params)
    );
  }

  excursionParticipants(excursionId: number): Observable<ExcursionParticipation[]> {
    return this.http.get<ExcursionParticipation[]>(
      `${this.activitiesUrl}/excursions/${excursionId}/participants`,
      { headers: this.authHeaders() }
    );
  }

  // ===================== TRAININGS =====================

  addTraining(payload: Training): Observable<Training> {
    return this.http.post<Training>(
      `${this.activitiesUrl}/trainings`,
      payload,
      { headers: this.authHeaders() }
    );
  }

  updateTraining(payload: Training): Observable<Training> {
    return this.http.put<Training>(
      `${this.activitiesUrl}/trainings`,
      payload,
      { headers: this.authHeaders() }
    );
  }

  getTrainingsByClub(clubId: number): Observable<Training[]> {
    return this.http.get<Training[]>(
      `${this.activitiesUrl}/trainings/club/${clubId}`,
      { headers: this.authHeaders() }
    );
  }

  getTraining(id: number): Observable<Training> {
    return this.http.get<Training>(
      `${this.activitiesUrl}/trainings/${id}`,
      { headers: this.authHeaders() }
    );
  }

  deleteTraining(id: number): Observable<void> {
    return this.http.delete<void>(
      `${this.activitiesUrl}/trainings/${id}`,
      { headers: this.authHeaders() }
    );
  }

  registerTraining(memberId: number, trainingId: number): Observable<TrainingParticipation> {
    const params = new HttpParams()
      .set('memberId', memberId.toString())
      .set('trainingId', trainingId.toString());

    return this.http.post<TrainingParticipation>(
      `${this.activitiesUrl}/trainings/register`,
      {},
      this.authOptions(params)
    );
  }

  trainingParticipants(trainingId: number): Observable<TrainingParticipation[]> {
    return this.http.get<TrainingParticipation[]>(
      `${this.activitiesUrl}/trainings/${trainingId}/participants`,
      { headers: this.authHeaders() }
    );
  }

  getTrainingParticipants(trainingId: number): Observable<TrainingParticipation[]> {
    return this.trainingParticipants(trainingId);
  }

  // ===================== PAYMENTS =====================

  payWithCash(memberId: number, trainingId: number): Observable<any> {
    const params = new HttpParams()
      .set('memberId', memberId.toString())
      .set('trainingId', trainingId.toString());

    return this.http.post<any>(
      `${this.trainingManagementUrl}/pay/cash`,
      {},
      this.authOptions(params)
    );
  }

  confirmCashPayment(paymentId: number): Observable<any> {
    return this.http.put<any>(
      `${this.trainingManagementUrl}/pay/cash/${paymentId}/confirm`,
      {},
      { headers: this.authHeaders() }
    );
  }

  payWithWallet(memberId: number, trainingId: number): Observable<any> {
    const params = new HttpParams()
      .set('memberId', memberId.toString())
      .set('trainingId', trainingId.toString());

    return this.http.post<any>(
      `${this.trainingManagementUrl}/pay/wallet`,
      {},
      this.authOptions(params)
    );
  }

  // ===================== COMPLETE TRAINING =====================

  completeTraining(participationId: number, score: number): Observable<TrainingParticipation> {
    const params = new HttpParams().set('score', score.toString());

    return this.http.put<TrainingParticipation>(
      `${this.trainingManagementUrl}/complete/${participationId}`,
      {},
      this.authOptions(params)
    );
  }

  // ===================== TEST =====================

  trainingManagementStatus(): Observable<any> {
    return this.http.get<any>(
      `${this.trainingManagementUrl}/status`,
      { headers: this.authHeaders() }
    );
  }
}