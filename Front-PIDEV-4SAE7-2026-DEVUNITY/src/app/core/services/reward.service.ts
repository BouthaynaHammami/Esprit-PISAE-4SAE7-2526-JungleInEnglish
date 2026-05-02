import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Reward, TypeReward } from '../models/reward.model';

@Injectable({
  providedIn: 'root'
})
export class RewardService {
  private apiUrl = 'http://localhost:8087/language/api/rewards';

  constructor(private http: HttpClient) {}

  getAllRewards(): Observable<Reward[]> {
    return this.http.get<Reward[]>(this.apiUrl);
  }

  getRewardById(id: number): Observable<Reward> {
    return this.http.get<Reward>(`${this.apiUrl}/${id}`);
  }

  addReward(reward: Reward): Observable<Reward> {
    return this.http.post<Reward>(`${this.apiUrl}/add`, reward);
  }

  updateReward(reward: Reward): Observable<Reward> {
    return this.http.put<Reward>(`${this.apiUrl}/update`, reward);
  }

  deleteReward(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete/${id}`);
  }

  getRewardsByType(type: TypeReward): Observable<Reward[]> {
    return this.http.get<Reward[]>(`${this.apiUrl}/type/${type}`);
  }

  getAvailableRewards(points: number): Observable<Reward[]> {
    return this.http.get<Reward[]>(`${this.apiUrl}/available/${points}`);
  }
}
