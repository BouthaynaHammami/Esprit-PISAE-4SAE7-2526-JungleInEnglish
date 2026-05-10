import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../../core/services/auth.service';
import { LearnerUserLookupService } from '../../../../core/services/learner-user-lookup.service';
import { UserDTO } from '../../../../core/models/user-dto.model';

@Component({
    selector: 'app-admin-profile',
    templateUrl: './admin-profile.component.html'
})
export class AdminProfileComponent implements OnInit {
    user: UserDTO | null = null;
    email: string | null = null;
    role: string | null = null;
    firstName: string = 'Loading...';
    lastName: string = '';
    expiryDate: string | null = null;
    avatarLetter: string = '?';
    accountStatus: string = 'Active';
    lastLogin: string = new Date().toLocaleDateString();
    loading: boolean = true;

    constructor(
        private authService: AuthService,
        private userService: LearnerUserLookupService
    ) { }

    ngOnInit(): void {
        const decoded = this.authService.decodeToken();
        this.email = decoded?.sub ?? null;
        
        if (this.email) {
            this.loading = true;
            this.userService.getByEmail(this.email).subscribe({
                next: (userData) => {
                    this.user = userData;
                    this.firstName = userData.firstName;
                    this.lastName = userData.lastName;
                    this.role = userData.role;
                    this.avatarLetter = this.firstName.charAt(0).toUpperCase();
                    this.loading = false;
                },
                error: (err) => {
                    console.error('Failed to load real user data:', err);
                    this.loading = false;
                    // Fallback to token data if service fails
                    this.role = decoded?.role?.replace('ROLE_', '') ?? null;
                    if (this.email) {
                        this.firstName = this.email.split('@')[0];
                        this.avatarLetter = this.firstName.charAt(0).toUpperCase();
                    }
                }
            });
        }

        if (decoded?.exp) {
            this.expiryDate = new Date(decoded.exp * 1000).toLocaleTimeString('en-US', {
                hour: '2-digit', minute: '2-digit'
            }) + ' Today';
        }
    }

    getRoleBadgeClass(): string {
        const r = this.role || this.user?.role;
        switch (r?.toUpperCase()) {
            case 'ADMIN': return 'bg-[#FFDDD2] text-[#E29578] shadow-sm';
            case 'STUDENT': return 'bg-green-50 text-green-600 border border-green-100';
            case 'TUTOR': return 'bg-blue-50 text-blue-600 border border-blue-100';
            case 'COMPANY': return 'bg-[#83C5BE] text-[#006D77]';
            case 'EMPLOYEE': return 'bg-gray-100 text-gray-600 border border-gray-200';
            default: return 'bg-gray-100 text-gray-500';
        }
    }

    logout(): void {
        this.authService.logout();
    }

    editProfile(): void {
        console.log('Edit profile triggered for:', this.email);
    }
}
