import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { Observable } from 'rxjs';
import { StudentChallenge, Level, ChallengeType, ChallengeResponseDTO, StartOrGetRequest } from '../../models/challenges-competitions.model';

@Injectable({
  providedIn: 'root'
})
export class StudentChallengeService {
  private baseUrl = `${environment.apiUrl}/activities/api/studentChallenges`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<StudentChallenge[]> {
    return this.http.get<StudentChallenge[]>(this.baseUrl);
  }

  getById(id: number): Observable<StudentChallenge> {
    return this.http.get<StudentChallenge>(`${this.baseUrl}/${id}`);
  }

  create(sc: StudentChallenge): Observable<StudentChallenge> {
    return this.http.post<StudentChallenge>(this.baseUrl, sc);
  }

  update(id: number, sc: StudentChallenge): Observable<StudentChallenge> {
    return this.http.put<StudentChallenge>(`${this.baseUrl}/${id}`, sc);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  getByUser(userId: number): Observable<StudentChallenge[]> {
    return this.http.get<StudentChallenge[]>(`${this.baseUrl}/byUser/${userId}`);
  }

  startOrGet(userId: number, challengeId: number): Observable<ChallengeResponseDTO> {
    const params = new HttpParams()
      .set('userId', userId.toString())
      .set('challengeId', challengeId.toString());
    return this.http.post<ChallengeResponseDTO>(`${this.baseUrl}/startOrGet`, null, { params });
  }

  /**
   * Start attempt by challenge type and optional level
   * POST /studentChallenges/startByType?userId=X&type=Y&level=Z
   * Returns ChallengeResponseDTO with challengeAttempt wrapped with Challenge details
   */
  startByType(userId: number, type: ChallengeType, level?: Level): Observable<ChallengeResponseDTO> {
    let params = new HttpParams().set('userId', userId).set('type', type);
    if (level) params = params.set('level', level);
    return this.http.post<ChallengeResponseDTO>(`${this.baseUrl}/startByType`, null, { params });
  }

  /**
   * Submit user answer for challenge attempt
   * POST /studentChallenges/{id}/submit with answer as request body
   * Returns ChallengeResponseDTO with:
   *   - challengeAttempt: ChallengeAttemptDTO (the updated attempt)
   *   - totalScore: updated total score
   *   - newBadges: newly earned badges
   *   - allBadges: all user badges
   */
  submit(studentChallengeId: number, input: string): Observable<ChallengeResponseDTO> {
    const headers = new HttpHeaders({ 'Content-Type': 'text/plain' });
    return this.http.post<ChallengeResponseDTO>(`${this.baseUrl}/${studentChallengeId}/submit`, input ?? '', { headers });
  }
}
