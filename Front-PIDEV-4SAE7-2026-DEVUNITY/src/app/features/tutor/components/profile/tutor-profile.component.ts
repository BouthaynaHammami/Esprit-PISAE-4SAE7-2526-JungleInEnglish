// src/app/features/tutor/components/profile/tutor-profile.component.ts
import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../../core/services/auth.service';

@Component({
    selector: 'app-tutor-profile',
    templateUrl: './tutor-profile.component.html'
})
export class TutorProfileComponent implements OnInit {
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
        return 'bg-green-100 text-green-700';
    }

    logout(): void {
        this.authService.logout();
    }
}
