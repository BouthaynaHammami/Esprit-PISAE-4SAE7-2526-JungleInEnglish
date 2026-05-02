import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AnalyticsData, OverviewStats } from '../models/analytics.model';

@Injectable({
  providedIn: 'root'
})
export class AnalyticsService {
  private apiUrl = 'http://localhost:8087/language/api/analytics';

  constructor(private http: HttpClient) {}

  getAnalytics(startDate?: string, endDate?: string): Observable<AnalyticsData> {
    let params = new HttpParams();
    if (startDate) {
      params = params.set('startDate', startDate);
    }
    if (endDate) {
      params = params.set('endDate', endDate);
    }
    return this.http.get<AnalyticsData>(this.apiUrl, { params });
  }

  getOverviewStats(): Observable<OverviewStats> {
    return this.http.get<OverviewStats>(`${this.apiUrl}/overview`);
  }
}
