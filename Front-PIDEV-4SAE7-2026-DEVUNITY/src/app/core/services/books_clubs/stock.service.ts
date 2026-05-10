import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';

export interface Stock {
  stockId:     number;
  quantity:    number;
  reservedQty: number;
  lastUpdate:  string;
  book?: { bookId: number; title: string; isbn: string; status: string; };
}

@Injectable({ providedIn: 'root' })
export class StockService {
  // StockController uses @RequestMapping("/api/stocks") → full path: /learners/api/api/stocks
  private base = `${environment.devUnityUrl}/learners/api/api`;

  constructor(private http: HttpClient) {}

  private authHeaders(): HttpHeaders {
    const token = localStorage.getItem('jwt_token') ?? '';
    return new HttpHeaders({ Authorization: token ? `Bearer ${token}` : '' });
  }

  getByBookId(bookId: number): Observable<Stock> {
    return this.http.get<Stock>(`${this.base}/stocks/${bookId}`, { headers: this.authHeaders() });
  }

  update(bookId: number, quantity: number): Observable<Stock> {
    const params = new HttpParams().set('quantity', String(quantity));
    return this.http.put<Stock>(`${this.base}/stocks/${bookId}`, null, { headers: this.authHeaders(), params });
  }

  add(bookId: number, qty: number): Observable<Stock> {
    const params = new HttpParams().set('qty', String(qty));
    return this.http.post<Stock>(`${this.base}/stocks/${bookId}/add`, null, { headers: this.authHeaders(), params });
  }

  remove(bookId: number, qty: number): Observable<Stock> {
    const params = new HttpParams().set('qty', String(qty));
    return this.http.post<Stock>(`${this.base}/stocks/${bookId}/remove`, null, { headers: this.authHeaders(), params });
  }
}
