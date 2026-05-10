import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Book } from '../../models/book-models';

@Injectable({ providedIn: 'root' })
export class BookService {
  private base = `${environment.devUnityUrl}/learners/api`;

  constructor(private http: HttpClient) {}

  private authHeaders(): HttpHeaders {
    const token = localStorage.getItem('jwt_token') ?? '';
    return new HttpHeaders({ Authorization: token ? `Bearer ${token}` : '' });
  }

  getAll(): Observable<Book[]> {
    return this.http.get<Book[]>(`${this.base}/books`, { headers: this.authHeaders() });
  }

  getById(id: number): Observable<Book> {
    return this.http.get<Book>(`${this.base}/books/${id}`, { headers: this.authHeaders() });
  }

  addBook(body: any, authorId: number, categoryId: number, qte: number): Observable<Book> {
    const params = new HttpParams()
      .set('authorId', authorId)
      .set('categoryId', categoryId)
      .set('qte', qte);
    return this.http.post<Book>(`${this.base}/books`, body, { headers: this.authHeaders(), params });
  }

  updateBook(id: number, body: any, authorId: number, categoryId: number): Observable<Book> {
    const params = new HttpParams()
      .set('authorId', authorId)
      .set('categoryId', categoryId);
    return this.http.put<Book>(`${this.base}/books/${id}`, body, { headers: this.authHeaders(), params });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/books/${id}`, { headers: this.authHeaders() });
  }
}
