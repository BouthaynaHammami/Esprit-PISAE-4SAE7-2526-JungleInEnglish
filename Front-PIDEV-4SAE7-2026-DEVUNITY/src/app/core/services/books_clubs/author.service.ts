import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Author } from '../../models/book-models';

@Injectable({ providedIn: 'root' })
export class AuthorService {
  private base = `${environment.devUnityUrl}/learners/api`;

  constructor(private http: HttpClient) {}

  private authHeaders(): HttpHeaders {
    const token = localStorage.getItem('jwt_token') ?? '';
    return new HttpHeaders({ Authorization: token ? `Bearer ${token}` : '' });
  }

  getAll(): Observable<Author[]> {
    return this.http.get<Author[]>(`${this.base}/authors`, { headers: this.authHeaders() });
  }

  create(body: Partial<Author>): Observable<Author> {
    return this.http.post<Author>(`${this.base}/authors`, body, { headers: this.authHeaders() });
  }

  update(id: number, body: Partial<Author>): Observable<Author> {
    return this.http.put<Author>(`${this.base}/authors/${id}`, body, { headers: this.authHeaders() });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/authors/${id}`, { headers: this.authHeaders() });
  }
}
