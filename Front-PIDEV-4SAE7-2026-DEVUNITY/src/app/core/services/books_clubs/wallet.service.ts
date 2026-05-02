import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { environment } from '../../../../environments/environment';

export interface Wallet {
  id: number;
  balance: number;
  userId: number;
}

export interface Transaction {
  id: number;
  amount: number;
  type: 'CREDIT' | 'DEBIT';
  date: string;
  userId: number;
  walletId: number;
}

@Injectable({ providedIn: 'root' })
export class WalletService {

  private readonly BASE = `${environment.apiUrl}/learners/api/wallet`;

  constructor(private http: HttpClient) {}

  private authHeaders(): { headers: HttpHeaders } {
    const token = localStorage.getItem('jwt_token'); // ✅ FIX : était 'token'
    return {
      headers: new HttpHeaders({
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      })
    };
  }

  private authOptions(params?: HttpParams): { headers: HttpHeaders; params?: HttpParams } {
    const token = localStorage.getItem('jwt_token'); // ✅ FIX : était 'token'
    const headers = new HttpHeaders({ 'Authorization': `Bearer ${token}` });
    return params ? { headers, params } : { headers };
  }

  // ✅ mappe w.user.id → userId
  private mapWallet(w: any, fallbackUserId?: number): Wallet {
    return {
      id:      w.id,
      balance: w.balance ?? 0,
      userId:  w.user?.userId ?? w.userId ?? fallbackUserId ?? 0
    };
  }

  createWallet(userId: number): Observable<Wallet> {
    return this.http.post<any>(
      `${this.BASE}/create/${userId}`,
      {},
      this.authHeaders()
    ).pipe(
      map(w => this.mapWallet(w, userId)),
      catchError(() => of({ id: 0, balance: 0, userId }))
    );
  }

  getWallet(userId: number): Observable<Wallet> {
    // The create endpoint is idempotent server-side (returns existing wallet or creates one),
    // which avoids initial 404 responses when a student has no wallet yet.
    return this.createWallet(userId);
  }

  recharge(userId: number, amount: number): Observable<Wallet> {
    const params = new HttpParams()
      .set('userId', userId.toString())
      .set('amount', amount.toString());
    return this.http.post<any>(
      `${this.BASE}/recharge`,
      null,
      this.authOptions(params)
    ).pipe(
      map(w => this.mapWallet(w, userId)),
      catchError(() => of({ id: 0, balance: 0, userId }))
    );
  }

  payFromWallet(userId: number, amount: number): Observable<void> {
    const params = new HttpParams()
      .set('userId', userId.toString())
      .set('amount', amount.toString());
    return this.http.post<void>(
      `${this.BASE}/pay`,
      null,
      this.authOptions(params)
    ).pipe(
      catchError(() => of(undefined as void))
    );
  }

  // ✅ mappe t.user.id → userId et t.wallet.id → walletId
  getTransactions(userId: number): Observable<Transaction[]> {
    return this.http.get<any[]>(
      `${this.BASE}/transactions/${userId}`,
      this.authOptions()
    ).pipe(
      map(res => Array.isArray(res) ? res.map(t => ({
        id:       t.id,
        amount:   t.amount,
        type:     t.type as 'CREDIT' | 'DEBIT',
        date:     t.date,
        userId:   t.user?.userId   ?? t.userId   ?? null,
        walletId: t.wallet?.id ?? t.walletId ?? null
      })) : []),
      catchError(() => of([]))
    );
  }
}