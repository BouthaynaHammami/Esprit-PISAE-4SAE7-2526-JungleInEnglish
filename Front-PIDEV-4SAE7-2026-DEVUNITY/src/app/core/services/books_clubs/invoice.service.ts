import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { Observable } from 'rxjs';

export interface ParticipationClub {
  participationId?: number;
  memberId: number;
  joinDate?: string;
  endDate?: string;
  role: 'PRESIDENT' | 'SECRETARY' | 'TREASURER' | 'MEMBER';
  status: 'ACTIVE' | 'SUSPENDED' | 'LEFT';
  club: {
    clubId: number;
    name?: string;
    type?: string;       // ← AJOUT ESSENTIEL
    status?: string;
    description?: string;
    creationDate?: string;
  };
}

@Injectable({ providedIn: 'root' })
export class MembershipService {
  private baseUrl = `${environment.apiUrl}/memberships`;

  constructor(private http: HttpClient) {}

  join(memberId: number, clubId: number): Observable<ParticipationClub> {
    const params = new HttpParams().set('memberId', memberId).set('clubId', clubId);
    return this.http.post<ParticipationClub>(`${this.baseUrl}/join`, null, { params });
  }

  membersOfClub(clubId: number): Observable<ParticipationClub[]> {
    return this.http.get<ParticipationClub[]>(`${this.baseUrl}/club/${clubId}`);
  }

  clubsOfMember(memberId: number): Observable<ParticipationClub[]> {
    return this.http.get<ParticipationClub[]>(`${this.baseUrl}/member/${memberId}`);
  }
}