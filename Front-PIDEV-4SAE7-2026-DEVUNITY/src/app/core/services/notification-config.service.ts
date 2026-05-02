import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class NotificationConfigService {
  
  // Default admin email - this could be made configurable via environment or API
  private readonly DEFAULT_ADMIN_EMAIL = 'admin@devunity.com';
  
  getAdminEmail(): string {
    // In the future, this could fetch from an API or environment config
    return this.DEFAULT_ADMIN_EMAIL;
  }
  
  // You can add more notification configuration here
  getNotificationSettings() {
    return {
      adminEmail: this.getAdminEmail(),
      enableEmployeeApprovalNotifications: true,
      enableApplicationNotifications: true,
      enableInterviewNotifications: true
    };
  }
}