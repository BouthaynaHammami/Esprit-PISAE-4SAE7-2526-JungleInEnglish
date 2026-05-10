import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Category } from '../../models/book-models';

@Injectable({ providedIn: 'root' })
export class CategoryService {
  private base = `${environment.devUnityUrl}/learners/api`;

  constructor(private http: HttpClient) {}

  private authHeaders(): HttpHeaders {
    const token = localStorage.getItem('jwt_token') ?? '';
    return new HttpHeaders({ Authorization: token ? `Bearer ${token}` : '' });
  }

  getAll(): Observable<Category[]> {
    return this.http.get<Category[]>(`${this.base}/categories`, { headers: this.authHeaders() });
  }

  create(body: Partial<Category>): Observable<Category> {
    return this.http.post<Category>(`${this.base}/categories`, body, { headers: this.authHeaders() });
  }

  update(id: number, body: Partial<Category>): Observable<Category> {
    return this.http.put<Category>(`${this.base}/categories/${id}`, body, { headers: this.authHeaders() });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/categories/${id}`, { headers: this.authHeaders() });
  }
}
