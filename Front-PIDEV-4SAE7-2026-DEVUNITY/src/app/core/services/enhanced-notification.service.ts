import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Notification, NotificationType } from '../models/notification.model';
import { NotificationService } from './notification.service';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class EnhancedNotificationService {
  
  constructor(
    private realNotificationService: NotificationService,
    private authService: AuthService
  ) {
    console.log('[EnhancedNotificationService] Initialized with real backend notifications');
  }

  get unreadCount$(): Observable<number> {
    return this.realNotificationService.unreadCount$;
  }

  get notifications$(): Observable<Notification[]> {
    return this.realNotificationService.notifications$;
  }

  getUnreadCount(): Observable<number> {
    return this.realNotificationService.getUnreadCount();
  }

  loadUnreadNotifications(): void {
    this.realNotificationService.loadUnreadNotifications();
  }

  markAsRead(id: number): Observable<void> {
    return this.realNotificationService.markAsRead(id);
  }

  markAllAsRead(): Observable<void> {
    return this.realNotificationService.markAllAsRead();
  }

  deleteNotification(id: number): Observable<void> {
    return this.realNotificationService.deleteNotification(id);
  }

  sendNotificationToUser(
    recipientEmail: string, 
    title: string, 
    message: string, 
    type: NotificationType, 
    relatedEntityId?: number, 
    relatedEntityType?: string
  ): Observable<void> {
    return this.realNotificationService.sendNotificationToUser(
      recipientEmail, title, message, type, relatedEntityId, relatedEntityType
    );
  }

  isNotificationServiceAvailable(): boolean {
    return this.realNotificationService.isNotificationServiceAvailable();
  }

  getServiceStatus(): {available: boolean, endpoint: string} {
    return this.realNotificationService.getServiceStatus();
  }

  enableService(): void {
    this.realNotificationService.enableService();
  }

  // Debug methods
  testConnection(): Observable<any> {
    return this.realNotificationService.testConnection();
  }

  getDebugInfo(): any {
    return this.realNotificationService.getDebugInfo();
  }
}