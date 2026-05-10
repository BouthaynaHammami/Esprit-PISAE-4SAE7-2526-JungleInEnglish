import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Order, Currency } from '../../models/book-models';

@Injectable({ providedIn: 'root' })
export class OrderService {
  private base = `${environment.devUnityUrl}/learners/api`;

  constructor(private http: HttpClient) {}

  private authHeaders(): HttpHeaders {
    const token = localStorage.getItem('jwt_token') ?? '';
    return new HttpHeaders({ Authorization: token ? `Bearer ${token}` : '' });
  }

  getAll(): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.base}/orders`, { headers: this.authHeaders() });
  }

  create(currency: Currency, userId?: number | null): Observable<Order> {
    let params = new HttpParams().set('currency', currency);
    if (userId != null) params = params.set('userId', userId);
    return this.http.post<Order>(`${this.base}/orders`, null, { headers: this.authHeaders(), params });
  }

  addItem(orderId: number, bookId: number, qty: number): Observable<any> {
    const params = new HttpParams()
      .set('bookId', bookId)
      .set('qty', qty);
    return this.http.post(`${this.base}/orders/${orderId}/items`, null, { headers: this.authHeaders(), params });
  }

  pay(orderId: number): Observable<any> {
    return this.http.post(`${this.base}/orders/${orderId}/pay`, null, { headers: this.authHeaders() });
  }

  cancel(orderId: number): Observable<any> {
    return this.http.post(`${this.base}/orders/${orderId}/cancel`, null, { headers: this.authHeaders() });
  }

  delete(orderId: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/orders/${orderId}`, { headers: this.authHeaders() });
  }
}
