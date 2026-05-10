// src/app/core/services/request.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { Observable } from 'rxjs';

export interface MembershipRequest {
  requestId?: number;
  memberId: number;
  motivation?: string;
  requestDate?: string;
  decisionDate?: string;
  status: 'PENDING' | 'ACCEPTED' | 'REJECTED';
  decidedByMemberId?: number;
  club: { clubId: number };
}

@Injectable({ providedIn: 'root' })
export class RequestService {
  private baseUrl = `${environment.devUnityUrl}/learners/api/api/requests`;

  constructor(private http: HttpClient) {}

  // âœ… Compatible avec ton backend actuel (@RequestParam)
  create(memberId: number, clubId: number, motivation: string): Observable<MembershipRequest> {
    const params = new HttpParams()
      .set('memberId', String(memberId))
      .set('clubId', String(clubId))
      .set('motivation', motivation ?? 'â€”');

    return this.http.post<MembershipRequest>(this.baseUrl, null, { params });
  }

  accept(requestId: number, decidedByMemberId: number): Observable<MembershipRequest> {
    const params = new HttpParams().set('decidedByMemberId', String(decidedByMemberId));
    return this.http.put<MembershipRequest>(`${this.baseUrl}/${requestId}/accept`, null, { params });
  }

  reject(requestId: number, decidedByMemberId: number): Observable<MembershipRequest> {
    const params = new HttpParams().set('decidedByMemberId', String(decidedByMemberId));
    return this.http.put<MembershipRequest>(`${this.baseUrl}/${requestId}/reject`, null, { params });
  }

  byClub(clubId: number): Observable<MembershipRequest[]> {
    return this.http.get<MembershipRequest[]>(`${this.baseUrl}/club/${clubId}`);
  }

  byMember(memberId: number): Observable<MembershipRequest[]> {
    return this.http.get<MembershipRequest[]>(`${this.baseUrl}/member/${memberId}`);
  }


}
