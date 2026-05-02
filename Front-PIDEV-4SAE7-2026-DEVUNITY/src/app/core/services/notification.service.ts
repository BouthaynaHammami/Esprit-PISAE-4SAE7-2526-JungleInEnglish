import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, timer, of } from 'rxjs';
import { switchMap, tap, catchError } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { Notification, NotificationType } from '../models/notification.model';
import { AuthService } from './auth.service';

@Injectable({ providedIn: 'root' })
export class NotificationService {
  private readonly baseUrl = `${environment.apiUrl}/activities/api/notifications`;
  
  private unreadCountSubject = new BehaviorSubject<number>(0);
  public unreadCount$ = this.unreadCountSubject.asObservable();

  private notificationsSubject = new BehaviorSubject<Notification[]>([]);
  public notifications$ = this.notificationsSubject.asObservable();

  private isServiceAvailable = true;
  private pollingSubscription: any;

  constructor(private http: HttpClient, private authService: AuthService) {
    // Only start polling if user is logged in and has appropriate role
    const userRole = this.authService.getUserRole();
    if (userRole === 'ADMIN' || userRole === 'EMPLOYE' || userRole === 'STUDENT') {
      // Test the service first before starting polling
      this.testServiceAvailability();
    } else {
      console.log('[NotificationService] Notifications not available for role:', userRole);
    }
  }

  private testServiceAvailability(): void {
    console.log('[NotificationService] Testing service availability...');
    
    // Try a simple request first
    this.http.get<number>(`${this.baseUrl}/unread-count`).pipe(
      catchError(err => {
        console.warn('[NotificationService] Service test failed:', err.status);
        
        if (err.status === 401) {
          console.warn('[NotificationService] Authentication issue - notifications may not be configured for this user');
          this.isServiceAvailable = false;
        } else if (err.status === 404) {
          console.warn('[NotificationService] Notification endpoints not found');
          this.isServiceAvailable = false;
        } else if (err.status === 0) {
          console.warn('[NotificationService] Network error - notification service may be down');
          this.isServiceAvailable = false;
        }
        
        return of(0);
      })
    ).subscribe({
      next: (count) => {
        if (this.isServiceAvailable) {
          console.log('[NotificationService] Service available, starting polling');
          this.unreadCountSubject.next(count);
          this.startPolling();
        } else {
          console.log('[NotificationService] Service not available, notifications disabled');
        }
      },
      error: (err) => {
        console.error('[NotificationService] Service test error:', err);
        this.isServiceAvailable = false;
      }
    });
  }

  private startPolling(): void {
    console.log('[NotificationService] Starting polling...');
    
    this.pollingSubscription = timer(0, 15000) // Increased to 15 seconds to reduce load
      .pipe(
        switchMap(() => {
          if (this.authService.isLoggedIn() && this.isServiceAvailable) {
            return this.getUnreadCount().pipe(
              catchError(err => {
                console.warn(`[NotificationService] Error getting unread count:`, err.status, err.message);
                
                if (err.status === 401) {
                  console.warn('[NotificationService] 401 - Authentication issue with notification service, disabling polling');
                  this.isServiceAvailable = false;
                } else if (err.status === 404) {
                  console.warn('[NotificationService] 404 - Notification endpoints not found, disabling polling');
                  this.isServiceAvailable = false;
                } else if (err.status === 0) {
                  console.warn('[NotificationService] Network error - notification service may be down');
                }
                
                return of(0);
              })
            );
          }
          return of(0);
        })
      )
      .subscribe({
        next: count => {
          if (this.isServiceAvailable) {
            console.log(`[NotificationService] Unread count: ${count}`);
            const currentNotifications = this.notificationsSubject.value;
            
            if (count !== this.unreadCountSubject.value || (count > 0 && currentNotifications.length === 0)) {
              this.unreadCountSubject.next(count);
              if (count > 0) {
                this.loadUnreadNotifications();
              }
            }
          }
        },
        error: err => {
          console.error('[NotificationService] Polling error:', err);
          this.isServiceAvailable = false;
        }
      });
  }

  getUnreadCount(): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/unread-count`).pipe(
      tap(count => this.unreadCountSubject.next(count)),
      catchError(err => {
        console.error('[NotificationService] Failed to get unread count:', err);
        return of(0);
      })
    );
  }

  loadUnreadNotifications(): void {
    if (!this.isServiceAvailable) {
      console.log('[NotificationService] Service not available, skipping notification load');
      return;
    }

    console.log('[NotificationService] Loading notifications...');
    this.http.get<any>(`${this.baseUrl}/my?size=20`).pipe(
      catchError(err => {
        console.error('[NotificationService] Failed to load notifications:', err);
        
        if (err.status === 401) {
          console.warn('[NotificationService] 401 error loading notifications - disabling service');
          this.isServiceAvailable = false;
        }
        
        return of([]);
      })
    ).subscribe(response => {
      console.log('[NotificationService] Received response:', response);
      let notifications: Notification[] = [];
      
      if (response && response.content) {
        notifications = response.content;
      } else if (Array.isArray(response)) {
        notifications = response;
      }
      
      console.log(`[NotificationService] Extracted ${notifications.length} notifications`);
      this.notificationsSubject.next(notifications);
    });
  }

  markAsRead(id: number): Observable<void> {
    return this.http.patch<void>(`${this.baseUrl}/${id}/read`, {}).pipe(
      tap(() => {
        // Update local state immediately
        const currentNotifications = this.notificationsSubject.value;
        const updatedNotifications = currentNotifications.map(n => 
          n.id === id ? { ...n, isRead: true, readAt: new Date() } : n
        ).filter(n => !n.isRead); // Remove read notifications from the list
        
        this.notificationsSubject.next(updatedNotifications);
        this.unreadCountSubject.next(updatedNotifications.length);
      }),
      catchError(err => {
        console.error('[NotificationService] Failed to mark as read:', err);
        return of(void 0);
      })
    );
  }

  markAllAsRead(): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/mark-all-read`, {}).pipe(
      tap(() => {
        this.unreadCountSubject.next(0);
        this.notificationsSubject.next([]);
      }),
      catchError(err => {
        console.error('[NotificationService] Failed to mark all as read:', err);
        return of(void 0);
      })
    );
  }

  deleteNotification(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`).pipe(
      tap(() => {
        // Update local state immediately
        const currentNotifications = this.notificationsSubject.value;
        const updatedNotifications = currentNotifications.filter(n => n.id !== id);
        this.notificationsSubject.next(updatedNotifications);
        this.unreadCountSubject.next(updatedNotifications.length);
      }),
      catchError(err => {
        console.error('[NotificationService] Failed to delete notification:', err);
        return of(void 0);
      })
    );
  }

  sendNotificationToUser(
    recipientEmail: string, 
    title: string, 
    message: string, 
    type: NotificationType, 
    relatedEntityId?: number, 
    relatedEntityType?: string
  ): Observable<void> {
    const notification = {
      title,
      message,
      type,
      recipientEmail,
      relatedEntityId,
      relatedEntityType
    };
    
    return this.http.post<void>(`${this.baseUrl}/send`, notification).pipe(
      tap(() => {
        console.log('[NotificationService] Notification sent successfully');
        // Refresh notifications after a short delay
        setTimeout(() => this.loadUnreadNotifications(), 1000);
      }),
      catchError(err => {
        console.error('[NotificationService] Failed to send notification:', err);
        return of(void 0);
      })
    );
  }

  isNotificationServiceAvailable(): boolean {
    return this.isServiceAvailable;
  }

  getServiceStatus(): {available: boolean, endpoint: string} {
    return {
      available: this.isServiceAvailable,
      endpoint: this.baseUrl
    };
  }

  enableService(): void {
    this.isServiceAvailable = true;
    if (!this.pollingSubscription) {
      this.startPolling();
    }
    console.log('[NotificationService] Service enabled');
  }

  disableService(): void {
    this.isServiceAvailable = false;
    if (this.pollingSubscription) {
      this.pollingSubscription.unsubscribe();
      this.pollingSubscription = null;
    }
    console.log('[NotificationService] Service disabled');
  }

  // Debug method to test the connection
  testConnection(): Observable<any> {
    console.log('[NotificationService] Testing connection to:', this.baseUrl);
    return this.http.get(`${this.baseUrl}/unread-count`);
  }

  // Debug method to get service info
  getDebugInfo(): any {
    return {
      baseUrl: this.baseUrl,
      isServiceAvailable: this.isServiceAvailable,
      userRole: this.authService.getUserRole(),
      isLoggedIn: this.authService.isLoggedIn(),
      userEmail: this.authService.getUserEmail(),
      userId: this.authService.getUserId(),
      currentNotificationCount: this.unreadCountSubject.value,
      currentNotifications: this.notificationsSubject.value
    };
  }
}