import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { Observable } from 'rxjs';
import { UserDTO } from '../../models/business-english.model';
import { AuthService } from '../auth.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private base = `${environment.apiUrl}/languages/api/users`;

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) {}

  private getHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders({
      'Accept': 'application/json',
      ...(token ? { 'Authorization': `Bearer ${token}` } : {})
    });
  }

  getAll(): Observable<UserDTO[]> {
    return this.http.get<UserDTO[]>(this.base, {
      headers: this.getHeaders()
    });
  }

  getById(id: number): Observable<UserDTO> {
    return this.http.get<UserDTO>(`${this.base}/${id}`, {
      headers: this.getHeaders()
    });
  }
}