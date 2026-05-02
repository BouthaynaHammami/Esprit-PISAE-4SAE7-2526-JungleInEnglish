import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { Offer } from '../../models/business-english.model';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class OfferService {
  private base = `${environment.apiUrl}/languages/api/offers`;

  constructor(private http: HttpClient) { }

  getAll(): Observable<Offer[]> {
    return this.http.get<Offer[]>(this.base);
  }

  getById(id: number): Observable<Offer> {
    return this.http.get<Offer>(`${this.base}/${id}`);
  }

  getCoursesByOffer(id: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.base}/${id}/courses`);
  }

  create(body: Offer): Observable<Offer> {
    return this.http.post<Offer>(this.base, body);
  }

  update(id: number, body: Offer): Observable<Offer> {
    return this.http.put<Offer>(`${this.base}/${id}`, body);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }

  getActive(): Observable<Offer[]> {
    return this.http.get<Offer[]>(`${this.base}/active`);
  }

  getOffersByStudent(studentId: number): Observable<Offer[]> {
    return this.http.get<Offer[]>(`${this.base}/student/${studentId}`);
  }
}