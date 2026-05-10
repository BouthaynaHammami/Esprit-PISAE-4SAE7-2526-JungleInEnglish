// src/app/core/interceptors/auth.interceptor.ts

import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse
} from '@angular/common/http';
import { Observable, throwError, BehaviorSubject } from 'rxjs';
import { catchError, filter, switchMap, take } from 'rxjs/operators';
import { AuthService } from '../services/auth.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  // Prevents multiple simultaneous refresh calls
  private isRefreshing = false;
  private refreshDone$ = new BehaviorSubject<string | null>(null);

  constructor(private authService: AuthService) {}

  intercept(
    request: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<unknown>> {

    const isAuthRequest =
      request.url.includes('/auth/login') ||
      request.url.includes('/auth/register') ||
      request.url.includes('/auth/refresh');

    // Attach valid access token to every non-auth request
    if (!isAuthRequest) {
      const token = this.authService.getToken();

      if (token && this.authService.isLoggedIn()) {
        request = this.addToken(request, token);
      } else if (token && !this.authService.isLoggedIn()) {
        // Token expired → try to refresh silently before sending request
        return this.handleExpiredToken(request, next);
      }
    }

    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          if (isAuthRequest) {
            // Wrong credentials — let the component handle it, no logout
          } else if (request.url.includes('/api/notifications')) {
            console.warn(`[AuthInterceptor] 401 from optional service: ${request.url}`);
          } else {
            // Unexpected 401 on a protected route → force logout
            console.error(`[AuthInterceptor] 401 on protected route: ${request.url} → Logging out`);
            this.authService.logout();
          }
        }
        return throwError(() => error);
      })
    );
  }

  // ─────────────────────────────────────────────────────────────────
  // PRIVATE
  // ─────────────────────────────────────────────────────────────────

  private addToken(request: HttpRequest<unknown>, token: string): HttpRequest<unknown> {
    return request.clone({ setHeaders: { Authorization: `Bearer ${token}` } });
  }

  /**
   * Called when the stored access token is expired.
   * Uses the refresh token to silently obtain a new access token,
   * then retries the original request.
   * If no refresh token is available, logs the user out immediately.
   */
  private handleExpiredToken(
    request: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<unknown>> {

    if (!this.authService.hasRefreshToken()) {
      this.authService.logout();
      return throwError(() => new Error('Session expirée'));
    }

    if (this.isRefreshing) {
      // Another refresh is already in progress — queue this request
      return this.refreshDone$.pipe(
        filter(token => token !== null),
        take(1),
        switchMap(token => next.handle(this.addToken(request, token!)))
      );
    }

    this.isRefreshing = true;
    this.refreshDone$.next(null);

    return this.authService.refreshTokens().pipe(
      switchMap(response => {
        this.isRefreshing = false;
        this.refreshDone$.next(response.token);
        return next.handle(this.addToken(request, response.token));
      }),
      catchError(err => {
        this.isRefreshing = false;
        console.warn('[AuthInterceptor] Refresh failed → logging out');
        this.authService.logout();
        return throwError(() => err);
      })
    );
  }
}
