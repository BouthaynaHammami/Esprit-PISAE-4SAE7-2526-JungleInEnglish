import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Badge, ChildBadge, Progress, LeaderboardEntry } from '../../models/english-kids.model';

@Injectable({
  providedIn: 'root'
})
export class GamificationService {
  private readonly apiUrl = `${environment.apiUrl}/language/api`;

  constructor(private http: HttpClient) {}

  // Progress
  getChildProgress(childId: number): Observable<Progress> {
    return this.http.get<Progress>(`${this.apiUrl}/progress/child/${childId}/summary`);
  }

  // Badges
  getAllBadges(): Observable<Badge[]> {
    return this.http.get<Badge[]>(`${this.apiUrl}/badges`);
  }

  getChildBadges(childId: number): Observable<ChildBadge[]> {
    return this.http.get<ChildBadge[]>(`${this.apiUrl}/rewards/${childId}`);
  }

  // Leaderboard
  getLeaderboard(limit: number = 10): Observable<LeaderboardEntry[]> {
    return this.http.get<LeaderboardEntry[]>(`${this.apiUrl}/leaderboard?limit=${limit}`);
  }

  // XP Calculation
  calculateXp(score: number, totalQuestions: number, timeSpent: number): number {
    const baseXp = Math.floor((score / totalQuestions) * 100);
    const timeBonus = timeSpent < 60 ? 20 : timeSpent < 120 ? 10 : 0;
    const perfectBonus = score === totalQuestions ? 50 : 0;
    return baseXp + timeBonus + perfectBonus;
  }

  // Level Calculation
  calculateLevel(xp: number): number {
    return Math.floor(xp / 1000) + 1;
  }

  calculateXpToNextLevel(xp: number): number {
    const currentLevel = this.calculateLevel(xp);
    const nextLevelXp = currentLevel * 1000;
    return nextLevelXp - xp;
  }

  // Check for new badges
  checkForNewBadges(childId: number): Observable<ChildBadge[]> {
    return this.http.post<ChildBadge[]>(`${this.apiUrl}/badges/check/${childId}`, {});
  }
}
