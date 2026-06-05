import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../../core/services/auth.service';
import { LearnerUserLookupService } from '../../../../core/services/learner-user-lookup.service';

@Component({
    selector: 'app-tutor-profile',
    templateUrl: './tutor-profile.component.html',
    styleUrls: ['./tutor-profile.component.css']
})
export class TutorProfileComponent implements OnInit {
    email: string = '';
    role: string = '';
    firstName: string = '';
    lastName: string = '';
    expiryDate: string = '';
    avatarLetter: string = '?';

    constructor(
        private authService: AuthService,
        private userLookup: LearnerUserLookupService
    ) { }

    ngOnInit(): void {
        this.email     = this.authService.getUserEmail() ?? '';
        this.role      = this.authService.getUserRole()  ?? '';
        this.firstName = localStorage.getItem('user_first_name') ?? '';
        this.lastName  = localStorage.getItem('user_last_name')  ?? '';

        if (this.firstName) {
            this.avatarLetter = this.firstName.charAt(0).toUpperCase();
        }

        const decoded = this.authService.decodeToken();
        if (decoded?.exp) {
            this.expiryDate = new Date(decoded.exp * 1000).toLocaleDateString('en-US', {
                year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit'
            });
        }

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
}
