import { Component } from '@angular/core';
import { AuthService } from '../../../../core/services/auth.service';
import { EnhancedNotificationService } from '../../../../core/services/enhanced-notification.service';
import { Observable } from 'rxjs';
import { Notification } from '../../../../core/models/notification.model';

@Component({
    selector: 'app-admin-sidebar',
    templateUrl: './admin-sidebar.component.html',
    styleUrls: ['./admin-sidebar.component.css']
})
export class AdminSidebarComponent {

    constructor(
        public authService: AuthService
    ) {}

    logout(): void {
        console.log('[AdminSidebar] Logout clicked.');
        this.authService.logout();
    }
}
