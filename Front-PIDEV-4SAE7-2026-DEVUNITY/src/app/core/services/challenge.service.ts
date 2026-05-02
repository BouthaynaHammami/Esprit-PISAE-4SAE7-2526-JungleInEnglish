import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ChallengeResult } from '../../shared/components/challenge-container/challenge-container.component';

/**
 * DTO for submitting challenge results
 */
export interface ChallengeSubmissionRequest {
  score: number;
  correctAnswers: number;
  wrongAnswers: number;
  timeUsed: number;
  attemptsUsed: number;
  bonus: number;
  feedback: string;
}

/**
 * DTO for API response
 */
export interface ChallengeSubmissionResponse {
  sessionId?: number | string;
  message: string;
  success: boolean;
  score: number;
  pointsAwarded?: number;
  badgesEarned?: string[];
}

/**
 * Service for managing challenge-related API calls
 */
@Injectable({
  providedIn: 'root'
})
export class ChallengeService {
  private readonly apiBaseUrl = '/api/v1/challenges'; // Adjust to your backend URL
  
  constructor(private http: HttpClient) {}

  /**
   * Submit challenge score to backend
   * @param challengeId - ID of the challenge
   * @param result - Challenge result data
   * @returns Observable of the API response
   */
  submitChallengeScore(
    challengeId: number,
    result: ChallengeResult
  ): Observable<ChallengeSubmissionResponse> {
    const url = `${this.apiBaseUrl}/${challengeId}/submit-score`;
    
    const payload: ChallengeSubmissionRequest = {
      score: result.score,
      correctAnswers: result.correctAnswers,
      wrongAnswers: result.wrongAnswers,
      timeUsed: result.timeUsed,
      attemptsUsed: result.attemptsUsed,
      bonus: result.bonus,
      feedback: result.feedback
    };

    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    return this.http.post<ChallengeSubmissionResponse>(url, payload, { headers });
  }

  /**
   * Get challenge details
   * @param challengeId - ID of the challenge
   * @returns Observable of challenge details
   */
  getChallengeDetails(challengeId: number): Observable<any> {
    const url = `${this.apiBaseUrl}/${challengeId}`;
    return this.http.get(url);
  }

  /**
   * Get student's attempt history for a challenge
   * @param challengeId - ID of the challenge
   * @param studentId - ID of the student
   * @returns Observable of attempt history
   */
  getAttemptHistory(challengeId: number, studentId: number): Observable<any> {
    const url = `${this.apiBaseUrl}/${challengeId}/attempts/${studentId}`;
    return this.http.get(url);
  }

  /**
   * Save challenge attempt (for tracking during the session)
   * @param challengeId - ID of the challenge
   * @param attemptData - Data for the current attempt
   * @returns Observable of the API response
   */
  saveAttempt(challengeId: number, attemptData: any): Observable<any> {
    const url = `${this.apiBaseUrl}/${challengeId}/attempts`;
    
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    return this.http.post(url, attemptData, { headers });
  }

  /**
   * Get leaderboard for a challenge
   * @param challengeId - ID of the challenge
   * @param limit - Number of top entries to fetch
   * @returns Observable of leaderboard data
   */
  getLeaderboard(challengeId: number, limit: number = 10): Observable<any> {
    const url = `${this.apiBaseUrl}/${challengeId}/leaderboard?limit=${limit}`;
    return this.http.get(url);
  }
}
