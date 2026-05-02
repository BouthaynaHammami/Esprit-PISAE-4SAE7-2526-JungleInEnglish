// src/app/features/company/components/profile/company-profile.component.ts
import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../../core/services/auth.service';

@Component({
    selector: 'app-company-profile',
    templateUrl: './company-profile.component.html',
    styleUrls: ['./company-profile.component.css']
})
export class CompanyProfileComponent implements OnInit {
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
        return 'bg-orange-100 text-orange-700';
    }

    logout(): void {
        this.authService.logout();
    }
}
