import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
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
  userId: number | null;
  walletId: number | null;
}

@Injectable({ providedIn: 'root' })
export class WalletService {

  private readonly BASE = `${environment.devUnityUrl}/learners/api/wallet`;

  constructor(private http: HttpClient) {}

  private getToken(): string {
    return localStorage.getItem('jwt_token') ?? '';
  }

  private authHeaders(): HttpHeaders {
    const token = this.getToken();
    return new HttpHeaders({
      Authorization: token ? `Bearer ${token}` : '',
      'Content-Type': 'application/json'
    });
  }

  private authOptions(params?: HttpParams): { headers: HttpHeaders; params?: HttpParams } {
    const headers = this.authHeaders();
    return params ? { headers, params } : { headers };
  }

  private mapWallet(w: any, fallbackUserId?: number): Wallet {
    return {
      id: Number(w?.id ?? 0),
      balance: Number(w?.balance ?? 0),
      userId: Number(
        w?.user?.userId ??
        w?.userId ??
        fallbackUserId ??
        0
      )
    };
  }

  private mapTransaction(t: any): Transaction {
    return {
      id: Number(t?.id ?? 0),
      amount: Number(t?.amount ?? 0),
      type: t?.type as 'CREDIT' | 'DEBIT',
      date: t?.date ?? '',
      userId: t?.user?.userId ?? t?.userId ?? null,
      walletId: t?.wallet?.id ?? t?.walletId ?? null
    };
  }

  private handleError(operation: string) {
    return (error: any) => {
      console.error(`${operation} error`, error);
      return throwError(() => error);
    };
  }

  createWallet(userId: number): Observable<Wallet> {
    return this.http.post<any>(
      `${this.BASE}/create/${userId}`,
      {},
      { headers: this.authHeaders() }
    ).pipe(
      map((w) => this.mapWallet(w, userId)),
      catchError(this.handleError('createWallet'))
    );
  }

  getWallet(userId: number): Observable<Wallet> {
    return this.http.get<any>(
      `${this.BASE}/${userId}`,
      this.authOptions()
    ).pipe(
      map((w) => this.mapWallet(w, userId)),
      catchError(this.handleError('getWallet'))
    );
  }

  recharge(userId: number, amount: number): Observable<Wallet> {
    const params = new HttpParams()
      .set('userId', userId.toString())
      .set('amount', amount.toString());

    return this.http.post<any>(
      `${this.BASE}/recharge`,
      {},
      this.authOptions(params)
    ).pipe(
      map((w) => this.mapWallet(w, userId)),
      catchError(this.handleError('recharge'))
    );
  }

  payFromWallet(userId: number, amount: number): Observable<void> {
    const params = new HttpParams()
      .set('userId', userId.toString())
      .set('amount', amount.toString());

    return this.http.post<void>(
      `${this.BASE}/pay`,
      {},
      this.authOptions(params)
    ).pipe(
      catchError(this.handleError('payFromWallet'))
    );
  }

  getTransactions(userId: number): Observable<Transaction[]> {
    return this.http.get<any[]>(
      `${this.BASE}/transactions/${userId}`,
      this.authOptions()
    ).pipe(
      map((res) => Array.isArray(res) ? res.map((t) => this.mapTransaction(t)) : []),
      catchError(this.handleError('getTransactions'))
    );
  }
}