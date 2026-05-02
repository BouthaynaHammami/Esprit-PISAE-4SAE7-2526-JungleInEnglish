import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { Observable } from 'rxjs';
import { Challenge, ChallengeType, Level, ChallengeAttempt } from '../../models/challenges-competitions.model';

/**
 * Service de gestion des Challenges
 * Synchronisé avec le backend Spring Boot:
 * @see Activity_Management_Service/ChallengesCompetitions/Controllers/ChallengeController.java
 */
@Injectable({ providedIn: 'root' })
export class ChallengeService {
  private readonly baseUrl = `${environment.apiUrl}/activities/api/challenges`;

  constructor(private http: HttpClient) {}

  /**
   * Récupère tous les challenges
   * GET /challenges
   */
  getAll(): Observable<Challenge[]> {
    return this.http.get<Challenge[]>(this.baseUrl);
  }

  /**
   * Récupère un challenge par son ID
   * GET /challenges/{id}
   */
  getById(idChallenge: number): Observable<Challenge> {
    return this.http.get<Challenge>(`${this.baseUrl}/${idChallenge}`);
  }

  /**
   * Crée un nouveau challenge
   * POST /challenges
   * @param payload - Challenge partiellement rempli (sans ID)
   */
  create(payload: Partial<Challenge>): Observable<Challenge> {
    return this.http.post<Challenge>(this.baseUrl, payload);
  }

  /**
   * Modifie un challenge existant
   * PUT /challenges/{id}
   * @param idChallenge - ID du challenge à modifier
   * @param payload - Données à mettre à jour
   */
  update(idChallenge: number, payload: Partial<Challenge>): Observable<Challenge> {
    return this.http.put<Challenge>(`${this.baseUrl}/${idChallenge}`, payload);
  }

  /**
   * Supprime un challenge
   * DELETE /challenges/{id}
   * @param idChallenge - ID du challenge à supprimer
   */
  delete(idChallenge: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${idChallenge}`);
  }

  /**
   * Récupère les challenges disponibles selon les critères
   * GET /challenges/available
   * @param opts - Filtres optionnels (type, level, date)
   * 
   * @example
   * getAvailable({ type: 'MYSTERY_WORD', level: 'B1' })
   * getAvailable({ date: '2026-04-28' })
   */
  getAvailable(opts: { type?: ChallengeType; level?: Level; date?: string }): Observable<Challenge[]> {
    let params = new HttpParams();
    if (opts.type) params = params.set('type', opts.type);
    if (opts.level) params = params.set('level', opts.level);
    if (opts.date) params = params.set('date', opts.date); // Format: YYYY-MM-DD

    return this.http.get<Challenge[]>(`${this.baseUrl}/available`, { params });
  }

  /**
   * Déclenche la synchronisation de tous les challenges avec les services externes
   * POST /challenges/sync
   * Utile pour sync avec les services en arrière-plan (Kafka, etc.)
   */
  syncChallenges(): Observable<any> {
    return this.http.post(`${this.baseUrl}/sync`, {});
  }
}