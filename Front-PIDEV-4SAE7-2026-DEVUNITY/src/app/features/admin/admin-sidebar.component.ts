// src/app/features/admin/admin-sidebar.component.ts
import { Component } from '@angular/core';
import { AuthService } from '../../core/services/auth.service';

@Component({
    selector: 'app-admin-sidebar',
    templateUrl: './admin-sidebar.component.html'
})
export class AdminSidebarComponent {
    constructor(private authService: AuthService) { }

    logout(): void {
        this.authService.logout();
    }
}
