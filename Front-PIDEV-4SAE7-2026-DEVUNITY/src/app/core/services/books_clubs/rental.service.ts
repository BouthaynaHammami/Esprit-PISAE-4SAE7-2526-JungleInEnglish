import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Rental, Currency } from '../../models/book-models';

@Injectable({ providedIn: 'root' })
export class RentalService {
  private base = `${environment.devUnityUrl}/learners/api`;

  constructor(private http: HttpClient) {}

  private authHeaders(): HttpHeaders {
    const token = localStorage.getItem('jwt_token') ?? '';
    return new HttpHeaders({ Authorization: token ? `Bearer ${token}` : '' });
  }

  getAll(): Observable<Rental[]> {
    return this.http.get<Rental[]>(`${this.base}/rentals`, { headers: this.authHeaders() });
  }

  create(
    bookId: number,
    startDate: string,
    dueDate: string,
    dailyPrice: number,
    currency: Currency,
    userId?: number | null
  ): Observable<Rental> {
    let params = new HttpParams()
      .set('bookId', bookId)
      .set('startDate', startDate)
      .set('dueDate', dueDate)
      .set('dailyPrice', dailyPrice)
      .set('currency', currency);
    if (userId != null) params = params.set('userId', userId);
    return this.http.post<Rental>(`${this.base}/rentals`, null, { headers: this.authHeaders(), params });
  }

  pay(id: number): Observable<any> {
    return this.http.post(`${this.base}/rentals/${id}/pay`, null, { headers: this.authHeaders() });
  }

  return(id: number, returnDate: string): Observable<any> {
    const params = new HttpParams().set('returnDate', returnDate);
    return this.http.post(`${this.base}/rentals/${id}/return`, null, { headers: this.authHeaders(), params });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/rentals/${id}`, { headers: this.authHeaders() });
  }
}
