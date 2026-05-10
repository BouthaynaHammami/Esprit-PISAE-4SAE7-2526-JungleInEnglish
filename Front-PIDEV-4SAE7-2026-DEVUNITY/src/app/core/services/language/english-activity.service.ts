import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Activity, ActivityCategory, DifficultyLevel } from '../../models/english-kids.model';

@Injectable({
  providedIn: 'root'
})
export class EnglishActivityService {
  private readonly apiUrl = `${environment.apiUrl}/languages/api/activities`;

  constructor(private http: HttpClient) {}

  getAllActivities(): Observable<Activity[]> {
    return this.http.get<Activity[]>(this.apiUrl);
  }

  getActivityById(id: number): Observable<Activity> {
    return this.http.get<Activity>(`${this.apiUrl}/${id}`);
  }

  getActivitiesByCategory(category: ActivityCategory): Observable<Activity[]> {
    const params = new HttpParams().set('category', category);
    return this.http.get<Activity[]>(this.apiUrl, { params });
  }

  getActivitiesByDifficulty(difficulty: DifficultyLevel): Observable<Activity[]> {
    const params = new HttpParams().set('difficulty', difficulty);
    return this.http.get<Activity[]>(this.apiUrl, { params });
  }

  getActivitiesForChild(childId: number): Observable<Activity[]> {
    return this.http.get<Activity[]>(`${this.apiUrl}/child/${childId}`);
  }

  markActivityCompleted(childId: number, activityId: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${activityId}/complete`, { childId });
  }
}
