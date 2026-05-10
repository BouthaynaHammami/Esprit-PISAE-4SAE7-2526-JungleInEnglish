// src/app/core/services/auth.service.ts

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';

import { environment } from '../../../environments/environment';

import { LoginRequest } from '../models/login-request.model';
import { AuthResponse } from '../models/auth-response.model';
import { RegisterRequest, Role, JwtPayload } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly TOKEN_KEY         = 'jwt_token';
  private readonly REFRESH_TOKEN_KEY = 'jwt_refresh_token';
  private readonly ROLE_KEY          = 'user_role';
  private readonly EMAIL_KEY         = 'user_email';
  private readonly USER_ID_KEY       = 'user_id';
  private readonly FIRST_NAME_KEY    = 'user_first_name';
  private readonly LAST_NAME_KEY     = 'user_last_name';

  private readonly apiUrl = environment.apiUrl;

  constructor(
    private http: HttpClient,
    private router: Router
  ) { }

  // ─────────────────────────────────
  // AUTH API
  // ─────────────────────────────────

  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(
      `${this.apiUrl}/learners/api/auth/login`,
      credentials
    ).pipe(
      tap(response => this.saveAuthData(response))
    );
  }

  register(request: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(
      `${this.apiUrl}/learners/api/auth/register`,
      request
    ).pipe(
      tap(response => this.saveAuthData(response))
    );
  }

  /** Appelle le backend pour obtenir un nouveau access token via le refresh token. */
  refreshTokens(): Observable<AuthResponse> {
    const refreshToken = this.getRefreshToken();
    return this.http.post<AuthResponse>(
      `${this.apiUrl}/learners/api/auth/refresh`,
      { refreshToken }
    ).pipe(
      tap(response => {
        // Met à jour uniquement les tokens, pas le profil
        if (response.token) {
          localStorage.setItem(this.TOKEN_KEY, response.token);
        }
        if (response.refreshToken) {
          localStorage.setItem(this.REFRESH_TOKEN_KEY, response.refreshToken);
        }
      })
    );
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.REFRESH_TOKEN_KEY);
    localStorage.removeItem(this.ROLE_KEY);
    localStorage.removeItem(this.EMAIL_KEY);
    localStorage.removeItem(this.USER_ID_KEY);
    localStorage.removeItem(this.FIRST_NAME_KEY);
    localStorage.removeItem(this.LAST_NAME_KEY);
    this.router.navigate(['/']);
  }

  // ─────────────────────────────────
  // TOKEN MANAGEMENT
  // ─────────────────────────────────

  private saveToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  getRefreshToken(): string | null {
    return localStorage.getItem(this.REFRESH_TOKEN_KEY);
  }

  isLoggedIn(): boolean {
    const token = this.getToken();
    if (!token) return false;
    return !this.isTokenExpired(token);
  }

  hasRefreshToken(): boolean {
    const rt = this.getRefreshToken();
    return !!rt && rt.length > 0;
  }

  // ─────────────────────────────────
  // USER INFO
  // ─────────────────────────────────

  getUserEmail(): string | null {
    return localStorage.getItem(this.EMAIL_KEY)
      ?? this.decodeToken()?.sub
      ?? null;
  }

  getUserId(): number | null {
    const stored = localStorage.getItem(this.USER_ID_KEY);
    if (stored) return Number(stored);
    const decoded = this.decodeToken();
    if (!decoded) return null;
    return decoded.userId ?? decoded.id ?? null;
  }

  getUserName(): string | null {
    const storedFirstName = localStorage.getItem(this.FIRST_NAME_KEY)?.trim() ?? '';
    const storedLastName  = localStorage.getItem(this.LAST_NAME_KEY)?.trim() ?? '';
    const storedFullName  = `${storedFirstName} ${storedLastName}`.trim();
    if (storedFullName) return storedFullName;

    const decoded = this.decodeToken();
    if (!decoded) return null;

    const firstName = decoded.firstName?.trim() ?? '';
    const lastName  = decoded.lastName?.trim() ?? '';
    const fullName  = `${firstName} ${lastName}`.trim();
    return fullName || null;
  }

  getUserRole(): Role | null {
    const stored = localStorage.getItem(this.ROLE_KEY);
    if (stored) return stored.replace('ROLE_', '') as Role;

    const decoded = this.decodeToken();
    if (!decoded) return null;

    if (decoded.role) return decoded.role.replace('ROLE_', '') as Role;
    if (decoded.authorities?.length) return decoded.authorities[0].replace('ROLE_', '') as Role;
    if (decoded.roles?.length) return decoded.roles[0].replace('ROLE_', '') as Role;

    return null;
  }

  // ─────────────────────────────────
  // JWT DECODING
  // ─────────────────────────────────

  decodeToken(): JwtPayload | null {
    const token = this.getToken();
    if (!token) return null;
    try {
      const payload = token.split('.')[1];
      return JSON.parse(atob(payload)) as JwtPayload;
    } catch {
      return null;
    }
  }

  // ─────────────────────────────────
  // REDIRECTION
  // ─────────────────────────────────

  redirectByRole(): void {
    const role = this.getUserRole();
    const routes: Record<string, string> = {
      STUDENT: '/student',
      TUTOR: '/tutor',
      EMPLOYE: '/employee',
      COMPANY: '/company',
      ADMIN: '/admin'
    };
    const destination = role ? (routes[role] ?? '/') : '/';
    console.log('[AuthService] redirect role:', role, '→', destination);
    this.router.navigate([destination]);
  }

  // ─────────────────────────────────
  // PRIVATE HELPERS
  // ─────────────────────────────────

  private saveAuthData(response: AuthResponse): void {
    this.saveToken(response.token);

    if (response.refreshToken) {
      localStorage.setItem(this.REFRESH_TOKEN_KEY, response.refreshToken);
    }
    if (response.role) {
      localStorage.setItem(this.ROLE_KEY, response.role.replace('ROLE_', ''));
    }
    if (response.email) {
      localStorage.setItem(this.EMAIL_KEY, response.email);
    }
    if (response.firstName) {
      localStorage.setItem(this.FIRST_NAME_KEY, response.firstName);
    }
    if (response.lastName) {
      localStorage.setItem(this.LAST_NAME_KEY, response.lastName);
    }
    if (response.userId) {
      localStorage.setItem(this.USER_ID_KEY, String(response.userId));
    }
  }

  private isTokenExpired(token: string): boolean {
    try {
      const payload = JSON.parse(atob(token.split('.')[1])) as JwtPayload;
      return Date.now() >= payload.exp * 1000;
    } catch {
      return true;
    }
  }

  getUserFullName(): string {
    const user = JSON.parse(localStorage.getItem('user') || 'null');
    if (!user) return '';
    return `${user.firstName || ''} ${user.lastName || ''}`.trim();
  }
}