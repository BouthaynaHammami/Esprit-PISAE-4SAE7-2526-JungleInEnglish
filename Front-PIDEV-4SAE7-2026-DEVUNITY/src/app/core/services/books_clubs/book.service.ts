import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Book } from '../../models/book-models';

@Injectable({ providedIn: 'root' })
export class BookService {
  private base = `${environment.apiUrl}/learners/api`;

  constructor(private http: HttpClient) {}

  // GET /books
  getAll(): Observable<Book[]> {
    return this.http.get<Book[]>(`${this.base}/books`);
  }

  // GET /books/{id}
  getById(id: number): Observable<Book> {
    return this.http.get<Book>(`${this.base}/books/${id}`);
  }

  // POST /books?authorId=&categoryId=&qte=
  addBook(body: any, authorId: number, categoryId: number, qte: number): Observable<Book> {
    const params = new HttpParams()
      .set('authorId', authorId)
      .set('categoryId', categoryId)
      .set('qte', qte);

    return this.http.post<Book>(`${this.base}/books`, body, { params });
  }

  // PUT /books/{id}?authorId=&categoryId=
  updateBook(id: number, body: any, authorId: number, categoryId: number): Observable<Book> {
    const params = new HttpParams()
      .set('authorId', authorId)
      .set('categoryId', categoryId);

    return this.http.put<Book>(`${this.base}/books/${id}`, body, { params });
  }

  // DELETE /books/{id}
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/books/${id}`);
  }
}
