import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, of, timer } from 'rxjs';
import { Notification, NotificationType } from '../models/notification.model';

@Injectable({
  providedIn: 'root'
})
export class MockNotificationService {
  private unreadCountSubject = new BehaviorSubject<number>(0);
  public unreadCount$ = this.unreadCountSubject.asObservable();

  private notificationsSubject = new BehaviorSubject<Notification[]>([]);
  public notifications$ = this.notificationsSubject.asObservable();

  private mockNotifications: Notification[] = [
    {
      id: 1,
      title: 'Employee Request Approved',
      message: 'Your request for Business English Course has been approved! Activation codes have been sent to your email.',
      type: 'EMPLOYEE_APPROVED',
      recipientEmail: 'employee@example.com',
      isRead: false,
      createdAt: new Date(Date.now() - 1000 * 60 * 30), // 30 minutes ago
      relatedEntityId: 1,
      relatedEntityType: 'COMPANY_OFFER'
    },
    {
      id: 2,
      title: 'New Job Application Received',
      message: 'A new application has been submitted for Software Developer in IT Department by john.doe@example.com',
      type: 'APPLICANT_CREATED',
      recipientEmail: 'admin@devunity.com',
      isRead: false,
      createdAt: new Date(Date.now() - 1000 * 60 * 60 * 2), // 2 hours ago
      relatedEntityId: 2,
      relatedEntityType: 'APPLICANT'
    },
    {
      id: 3,
      title: 'Application Accepted - Interview Scheduled',
      message: 'Congratulations! Your application for Frontend Developer has been accepted. An interview has been scheduled for tomorrow at 2:00 PM.',
      type: 'APPLICANT_ACCEPTED',
      recipientEmail: 'candidate@example.com',
      isRead: false,
      createdAt: new Date(Date.now() - 1000 * 60 * 60 * 4), // 4 hours ago
      relatedEntityId: 3,
      relatedEntityType: 'APPLICANT'
    }
  ];

  private nextId = 4;

  constructor() {
    // Initialize with mock data
    this.loadMockNotifications();
  }

  private loadMockNotifications(): void {
    const unreadNotifications = this.mockNotifications.filter(n => !n.isRead);
    this.notificationsSubject.next(unreadNotifications);
    this.unreadCountSubject.next(unreadNotifications.length);
  }

  getUnreadCount(): Observable<number> {
    return of(this.unreadCountSubject.value);
  }

  loadUnreadNotifications(): void {
    const unreadNotifications = this.mockNotifications.filter(n => !n.isRead);
    this.notificationsSubject.next(unreadNotifications);
  }

  markAsRead(id: number): Observable<void> {
    const notification = this.mockNotifications.find(n => n.id === id);
    if (notification) {
      notification.isRead = true;
      notification.readAt = new Date();
      this.loadMockNotifications();
    }
    return of(void 0);
  }

  markAllAsRead(): Observable<void> {
    this.mockNotifications.forEach(n => {
      n.isRead = true;
      n.readAt = new Date();
    });
    this.loadMockNotifications();
    return of(void 0);
  }

  deleteNotification(id: number): Observable<void> {
    const index = this.mockNotifications.findIndex(n => n.id === id);
    if (index > -1) {
      this.mockNotifications.splice(index, 1);
      this.loadMockNotifications();
    }
    return of(void 0);
  }

  sendNotificationToUser(
    recipientEmail: string, 
    title: string, 
    message: string, 
    type: NotificationType, 
    relatedEntityId?: number, 
    relatedEntityType?: string
  ): Observable<void> {
    const newNotification: Notification = {
      id: this.nextId++,
      title,
      message,
      type,
      recipientEmail,
      isRead: false,
      createdAt: new Date(),
      relatedEntityId,
      relatedEntityType
    };

    this.mockNotifications.unshift(newNotification); // Add to beginning
    this.loadMockNotifications();
    
    console.log('[MockNotificationService] Notification sent:', newNotification);
    return of(void 0);
  }

  isNotificationServiceAvailable(): boolean {
    return true; // Mock service is always available
  }

  getServiceStatus(): {available: boolean, endpoint: string} {
    return {
      available: true,
      endpoint: 'mock://notifications'
    };
  }

  enableService(): void {
    console.log('[MockNotificationService] Service enabled');
  }

  // Method to add a test notification for demo purposes
  addTestNotification(): void {
    this.sendNotificationToUser(
      'test@example.com',
      'Test Notification',
      'This is a test notification created at ' + new Date().toLocaleTimeString(),
      'GENERAL'
    ).subscribe();
  }
}