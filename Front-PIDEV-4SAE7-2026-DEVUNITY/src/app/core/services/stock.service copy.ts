// src/app/core/services/stock.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface Stock {
  stockId:     number;
  quantity:    number;
  reservedQty: number;
  lastUpdate:  string;
  book?: { bookId: number; title: string; isbn: string; status: string; };
}

@Injectable({ providedIn: 'root' })
export class StockService {

  // ✅ FIX — Toujours utiliser l'URL complète du backend.
  // Avec '/api' (proxy Angular), le proxy supprime le header Authorization → 403.
  // En pointant directement vers le backend, l'intercepteur Angular
  // peut attacher le header Bearer token sans qu'il soit strippé.
  private base = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getByBookId(bookId: number): Observable<Stock> {
    return this.http.get<Stock>(`${this.base}/stocks/${bookId}`);
  }

  update(bookId: number, quantity: number): Observable<Stock> {
    const params = new HttpParams().set('quantity', String(quantity));
    return this.http.put<Stock>(`${this.base}/stocks/${bookId}`, null, { params });
  }

  add(bookId: number, qty: number): Observable<Stock> {
    const params = new HttpParams().set('qty', String(qty));
    return this.http.post<Stock>(`${this.base}/stocks/${bookId}/add`, null, { params });
  }

  remove(bookId: number, qty: number): Observable<Stock> {
    const params = new HttpParams().set('qty', String(qty));
    return this.http.post<Stock>(`${this.base}/stocks/${bookId}/remove`, null, { params });
  }
}