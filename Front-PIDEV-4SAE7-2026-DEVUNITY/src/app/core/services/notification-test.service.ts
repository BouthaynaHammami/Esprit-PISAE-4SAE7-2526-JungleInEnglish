import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class NotificationTestService {
  private readonly baseUrl = `${environment.employeeUrl}/api/notifications`;

  constructor(private http: HttpClient) {}

  // Test if the notification service is reachable
  testConnection(): Observable<any> {
    console.log('[NotificationTest] Testing connection to:', this.baseUrl);
    return this.http.get(`${this.baseUrl}/unread-count`);
  }

  // Test sending a notification
  testSendNotification(): Observable<any> {
    const testNotification = {
      title: 'Test Notification',
      message: 'This is a test notification to verify the system is working',
      type: 'GENERAL',
      recipientEmail: 'test@example.com'
    };
    
    console.log('[NotificationTest] Sending test notification:', testNotification);
    return this.http.post(`${this.baseUrl}/send`, testNotification);
  }

  // Test getting notifications
  testGetNotifications(): Observable<any> {
    console.log('[NotificationTest] Getting notifications from:', `${this.baseUrl}/my?size=5`);
    return this.http.get(`${this.baseUrl}/my?size=5`);
  }

  // Get service info
  getServiceInfo(): {baseUrl: string, environment: string} {
    return {
      baseUrl: this.baseUrl,
      environment: environment.employeeUrl
    };
  }
}