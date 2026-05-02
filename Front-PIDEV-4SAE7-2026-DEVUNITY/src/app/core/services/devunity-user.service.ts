// src/app/core/services/devunity-user.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { UserDTO } from '../models/recruitment.model';

@Injectable({ providedIn: 'root' })
export class DevUnityUserService {

  private readonly usersUrl = `${environment.devUnityUrl}/users`;

  constructor(private http: HttpClient) {}

  getAllUsers(): Observable<UserDTO[]> {
    return this.http.get<UserDTO[]>(`${this.usersUrl}/`);
  }

  getUserById(id: number): Observable<UserDTO> {
    return this.http.get<UserDTO>(`${this.usersUrl}/${id}`);
  }
}
