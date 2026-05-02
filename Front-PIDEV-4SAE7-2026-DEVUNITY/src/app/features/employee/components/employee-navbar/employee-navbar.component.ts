import { Component } from '@angular/core';
import { AuthService } from '../../../../core/services/auth.service';
import { EnhancedNotificationService } from '../../../../core/services/enhanced-notification.service';
import { Observable } from 'rxjs';
import { Notification } from '../../../../core/models/notification.model';

@Component({
    selector: 'app-employee-navbar',
    templateUrl: './employee-navbar.component.html',
    styles: [`
        .slide-in {
            animation: slideIn 0.2s ease-out;
        }
        @keyframes slideIn {
            from { opacity: 0; transform: translateY(-10px); }
            to { opacity: 1; transform: translateY(0); }
        }
    `]
})
export class EmployeeNavbarComponent {
    unreadCount$: Observable<number>;
    notifications$: Observable<Notification[]>;
    showNotifications = false;

    constructor(
        public authService: AuthService,
        public notificationService: EnhancedNotificationService
    ) {
        this.unreadCount$ = this.notificationService.unreadCount$;
        this.notifications$ = this.notificationService.notifications$;
    }

    toggleNotifications(): void {
        this.showNotifications = !this.showNotifications;
        if (this.showNotifications) {
            this.notificationService.loadUnreadNotifications();
        }
    }

    markAsRead(id: number): void {
        this.notificationService.markAsRead(id).subscribe();
    }

    markAllAsRead(): void {
        this.notificationService.markAllAsRead().subscribe();
    }

    deleteNotification(id: number, event: Event): void {
        event.stopPropagation();
        this.notificationService.deleteNotification(id).subscribe({
            next: () => this.notificationService.loadUnreadNotifications()
        });
    }

    logout(): void {
        this.authService.logout();
    }
}
