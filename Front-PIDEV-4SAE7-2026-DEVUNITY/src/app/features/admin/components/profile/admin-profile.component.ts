// src/app/features/admin/components/profile/admin-profile.component.ts
import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../../core/services/auth.service';

@Component({
    selector: 'app-admin-profile',
    templateUrl: './admin-profile.component.html'
})
export class AdminProfileComponent implements OnInit {
    email: string | null = null;
    role: string | null = null;
    userId: number | null = null;
    expiryDate: string | null = null;
    avatarLetter: string = '?';

    constructor(private authService: AuthService) { }

    ngOnInit(): void {
        const decoded = this.authService.decodeToken();
        this.email = decoded?.sub ?? null;
        this.role = decoded?.role?.replace('ROLE_', '') ?? null;
        this.userId = decoded?.userId ?? null;
        if (decoded?.exp) {
            this.expiryDate = new Date(decoded.exp * 1000).toLocaleDateString('en-US', {
                year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit'
            });
        }
        if (this.email) {
            this.avatarLetter = this.email.charAt(0).toUpperCase();
        }
    }

    getRoleBadgeClass(): string {
        switch (this.role) {
            case 'ADMIN': return 'bg-red-100 text-red-700';
            default: return 'bg-gray-100 text-gray-700';
        }
    }

    logout(): void {
        this.authService.logout();
    }
}
