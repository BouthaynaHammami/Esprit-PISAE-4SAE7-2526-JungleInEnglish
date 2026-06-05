import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../../core/services/auth.service';
import { LearnerUserLookupService } from '../../../../core/services/learner-user-lookup.service';

@Component({
    selector: 'app-admin-profile',
    templateUrl: './admin-profile.component.html',
    styleUrls: ['./admin-profile.component.css']
})
export class AdminProfileComponent implements OnInit {
    email: string = '';
    role: string = '';
    firstName: string = '';
    lastName: string = '';
    expiryDate: string = '';
    avatarLetter: string = '?';
    accountStatus: string = 'Active';
    lastLogin: string = new Date().toLocaleDateString();

    constructor(
        private authService: AuthService,
        private userLookup: LearnerUserLookupService
    ) { }

    ngOnInit(): void {
        // 1) Read from localStorage first (instant)
        this.email     = this.authService.getUserEmail() ?? '';
        this.role      = this.authService.getUserRole()  ?? '';
        this.firstName = localStorage.getItem('user_first_name') ?? '';
        this.lastName  = localStorage.getItem('user_last_name')  ?? '';

        if (this.firstName) {
            this.avatarLetter = this.firstName.charAt(0).toUpperCase();
        }

        const decoded = this.authService.decodeToken();
        if (decoded?.exp) {
            this.expiryDate = new Date(decoded.exp * 1000).toLocaleTimeString('en-US', {
                hour: '2-digit', minute: '2-digit'
            }) + ' Today';
        }

        // 2) If firstName is missing, fetch from backend API
        if (!this.firstName && this.email) {
            this.userLookup.getByEmail(this.email).subscribe({
                next: (user) => {
                    this.firstName = user.firstName;
                    this.lastName  = user.lastName;
                    this.email     = user.email;
                    this.role      = user.role?.replace('ROLE_', '');
                    this.avatarLetter = this.firstName.charAt(0).toUpperCase();
                },
                error: () => {
                    // Fallback: use email prefix
                    if (this.email.includes('@')) {
                        this.firstName = this.email.split('@')[0];
                        this.avatarLetter = this.firstName.charAt(0).toUpperCase();
                    }
                }
            });
        }
    }

    logout(): void {
        this.authService.logout();
    }

    editProfile(): void {
        console.log('Edit profile triggered for:', this.email);
    }
}
