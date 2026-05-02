import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Author } from '../../models/book-models';

@Injectable({ providedIn: 'root' })
export class AuthorService {
  private base = `${environment.apiUrl}/learners/api`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Author[]> {
    return this.http.get<Author[]>(`${this.base}/authors`);
  }

  create(body: Partial<Author>): Observable<Author> {
    return this.http.post<Author>(`${this.base}/authors`, body);
  }

  update(id: number, body: Partial<Author>): Observable<Author> {
    return this.http.put<Author>(`${this.base}/authors/${id}`, body);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/authors/${id}`);
  }
}
