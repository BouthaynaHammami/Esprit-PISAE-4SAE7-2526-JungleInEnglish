import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Order, Currency } from '../../models/book-models';

@Injectable({ providedIn: 'root' })
export class OrderService {
  private base = `${environment.apiUrl}/learners/api`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.base}/orders`);
  }

  create(currency: Currency, userId?: number | null): Observable<Order> {
    let params = new HttpParams().set('currency', currency);
    if (userId != null) params = params.set('userId', userId);
    return this.http.post<Order>(`${this.base}/orders`, null, { params });
  }

  addItem(orderId: number, bookId: number, qty: number): Observable<any> {
    const params = new HttpParams()
      .set('bookId', bookId)
      .set('qty', qty);
    return this.http.post(`${this.base}/orders/${orderId}/items`, null, { params });
  }

  pay(orderId: number): Observable<any> {
    return this.http.post(`${this.base}/orders/${orderId}/pay`, null);
  }

  cancel(orderId: number): Observable<any> {
    return this.http.post(`${this.base}/orders/${orderId}/cancel`, null);
  }

  delete(orderId: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/orders/${orderId}`);
  }
}
