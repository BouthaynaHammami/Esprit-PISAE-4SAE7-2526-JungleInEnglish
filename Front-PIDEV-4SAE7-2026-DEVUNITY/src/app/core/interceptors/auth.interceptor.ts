// src/app/core/interceptors/auth.interceptor.ts

import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AuthService } from '../services/auth.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private authService: AuthService) {}

  intercept(
    request: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<unknown>> {

    const isAuthRequest =
      request.url.includes('/auth/login') ||
      request.url.includes('/auth/register');

    let token = this.authService.getToken();

    // 🚀 Skip attaching token for login/register
    if (!isAuthRequest && token) {

      // If token expired → logout and don't attach it
      if (this.authService.isLoggedIn()) {
        request = request.clone({
          setHeaders: {
            Authorization: `Bearer ${token}`
          }
        });
      } else {
        console.warn(`[AuthInterceptor] Token expired/invalid for: ${request.url} → Logging out`);
        this.authService.logout();
      }
    }

    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {

        if (error.status === 401) {
          // Only auto-logout for core authentication endpoints, not for optional services
          const isNotificationEndpoint = request.url.includes('/api/notifications');
          const isOptionalService = isNotificationEndpoint; // Add other optional services here
          
          if (!isOptionalService) {
            console.error(`[AuthInterceptor] 401 Unauthorized from: ${request.url} → Logging out`);
            this.authService.logout();
          } else {
            console.warn(`[AuthInterceptor] 401 from optional service: ${request.url} - service may not be available for this user`);
          }
        }

        return throwError(() => error);
      })
    );
  }
}