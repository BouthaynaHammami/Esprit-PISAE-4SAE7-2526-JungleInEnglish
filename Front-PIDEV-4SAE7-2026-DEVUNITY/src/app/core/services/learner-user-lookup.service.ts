import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { UserDTO } from '../models/user-dto.model';

@Injectable({ providedIn: 'root' })
export class LearnerUserLookupService {
  private readonly base = `${environment.apiUrl}/learners/api/users`;

  constructor(private http: HttpClient) {}

  getByEmail(email: string): Observable<UserDTO> {
    return this.http.get<UserDTO>(`${this.base}/email/${encodeURIComponent(email)}`);
  }

  getById(userId: number): Observable<UserDTO> {
    return this.http.get<UserDTO>(`${this.base}/${userId}`);
  }
}
