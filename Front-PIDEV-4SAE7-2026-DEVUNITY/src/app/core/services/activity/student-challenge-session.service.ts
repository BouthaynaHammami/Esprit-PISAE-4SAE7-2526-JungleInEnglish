import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { BehaviorSubject, Observable } from 'rxjs';
import {
  StudentChallengeSessionDTO,
  ChallengeType,
  Level,
  SubmitAnswerRequest
} from '../../models/challenges-competitions.model';

/**
 * Service for managing 3-minute challenge sessions
 * Handles session lifecycle: start → submit answers → complete → finalize
 * Synchronized with Backend: StudentChallengeSessionServiceImpl
 */
@Injectable({ providedIn: 'root' })
export class StudentChallengeSessionService {
  private baseUrl = `${environment.apiUrl}/activities/api/sessions`;
  
  // BehaviorSubject to track active session across components
  private activeSessionSubject = new BehaviorSubject<StudentChallengeSessionDTO | null>(null);
  public activeSession$ = this.activeSessionSubject.asObservable();

  constructor(private http: HttpClient) {}

  /**
   * Start a new 3-minute session
   * POST /sessions/start?userId=X&type=Y&level=Z
   * 
   * @param userId - Student ID
   * @param type - Challenge type (MYSTERY_WORD, SENTENCE_BUILDER, EMOJI_WORD)
   * @param level - Difficulty level (A1-C2)
   * @returns Observable<StudentChallengeSessionDTO>
   */
  startSession(
    userId: number,
    type: ChallengeType,
    level: Level
  ): Observable<StudentChallengeSessionDTO> {
    const params = new HttpParams()
      .set('userId', userId.toString())
      .set('type', type)
      .set('level', level);
    
    return this.http.post<StudentChallengeSessionDTO>(
      `${this.baseUrl}/start`,
      null,
      { params }
    );
  }

  /**
   * Get active session for a user (if any)
   * GET /sessions/active?userId=X
   */
  getActiveSession(userId: number): Observable<StudentChallengeSessionDTO | null> {
    const params = new HttpParams().set('userId', userId.toString());
    return this.http.get<StudentChallengeSessionDTO | null>(
      `${this.baseUrl}/active`,
      { params }
    );
  }

  /**
   * Get session by ID
   * GET /sessions/{sessionId}
   */
  getSessionById(sessionId: number): Observable<StudentChallengeSessionDTO> {
    return this.http.get<StudentChallengeSessionDTO>(`${this.baseUrl}/${sessionId}`);
  }

  /**
   * Get all sessions for a user (history)
   * GET /sessions/user/{userId}
   */
  getUserSessions(userId: number): Observable<StudentChallengeSessionDTO[]> {
    return this.http.get<StudentChallengeSessionDTO[]>(`${this.baseUrl}/user/${userId}`);
  }

  /**
   * Submit an answer for current challenge in session
   * POST /sessions/{sessionId}/submit
   * 
   * Returns updated session with:
   * - Score incremented (+3 for correct, +0 for wrong)
   * - Challenge index advanced
   * - Status updated if session complete
   * 
   * @param sessionId - Current session ID
   * @param request - Answer submission (challengeId, answer, isCorrect)
   * @returns Observable<StudentChallengeSessionDTO> - Updated session
   */
  submitAnswer(
    sessionId: number,
    request: SubmitAnswerRequest
  ): Observable<StudentChallengeSessionDTO> {
    return this.http.post<StudentChallengeSessionDTO>(
      `${this.baseUrl}/${sessionId}/submit`,
      request
    );
  }

  /**
   * Complete session (marks as COMPLETED and finalizes scores)
   * POST /sessions/{sessionId}/complete
   * 
   * Triggers:
   * - Final score calculation
   * - Badge evaluation (backend will evaluate)
   * 
   */
  completeSession(sessionId: number): Observable<StudentChallengeSessionDTO> {
    return this.http.post<StudentChallengeSessionDTO>(
      `${this.baseUrl}/${sessionId}/complete`,
      {}
    );
  }

  /**
   * Evaluate and assign badges after session completion
   * GET /sessions/{sessionId}/badges
   * 
   * Called after completeSession() to fetch newly earned badges
   * 
   * @param sessionId - Completed session ID
   * @param userId - Student ID (for badge assignment)
   * @returns Observable with newly earned badges
   */
  evaluateAndAssignBadges(
    sessionId: number,
    userId: number
  ): Observable<any> {
    const params = new HttpParams()
      .set('sessionId', sessionId.toString())
      .set('userId', userId.toString());
    
    return this.http.get<any>(
      `${this.baseUrl}/${sessionId}/badges`,
      { params }
    );
  }

  /**
   * Update global student score (add session score to total)
   * POST /sessions/{sessionId}/updateGlobalScore?userId=X
   * 
   * @param sessionId - Completed session
   * @param userId - Student ID
   * @returns Observable with updated global score
   */
  updateGlobalScore(
    sessionId: number,
    userId: number
  ): Observable<any> {
    const params = new HttpParams()
      .set('userId', userId.toString());
    
    return this.http.post<any>(
      `${this.baseUrl}/${sessionId}/updateGlobalScore`,
      {},
      { params }
    );
  }

  /**
   * Store active session in shared state (for component communication)
   */
  setActiveSession(session: StudentChallengeSessionDTO | null): void {
    this.activeSessionSubject.next(session);
  }

  /**
   * Get active session from shared state
   */
  getActiveSessionValue(): StudentChallengeSessionDTO | null {
    return this.activeSessionSubject.value;
  }
}

// Export types for component usage
export type { StudentChallengeSessionDTO, SubmitAnswerRequest };
