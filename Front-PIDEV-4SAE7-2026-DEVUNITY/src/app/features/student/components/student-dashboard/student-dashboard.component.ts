// src/app/features/student/components/student-dashboard/student-dashboard.component.ts
import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../../core/services/auth.service';
import { LearnerUserLookupService } from '../../../../core/services/learner-user-lookup.service';

@Component({
    selector: 'app-student-dashboard',
    templateUrl: './student-dashboard.component.html',
    styleUrls: ['./student-dashboard.component.scss']
})
export class StudentDashboardComponent implements OnInit {
    email: string | null = null;
    role: string | null = null;
    displayName: string = 'Student';

    constructor(
        private authService: AuthService,
        private userLookup: LearnerUserLookupService
    ) { }

    ngOnInit(): void {
        const decoded = this.authService.decodeToken();
        this.email = decoded?.sub ?? null;
        this.role = decoded?.role?.replace('ROLE_', '') ?? 'STUDENT';

        // Fetch real user name from backend via user ID
        const userId = this.authService.getUserId();
        if (userId) {
            this.userLookup.getById(userId).subscribe({
                next: (user) => {
                    if (user.firstName) {
                        this.displayName = user.lastName
                            ? `${user.firstName} ${user.lastName}`
                            : user.firstName;
                    }
                },
                error: () => {
                    // Fallback: use stored email local part
                    const storedEmail = this.authService.getUserEmail();
                    if (storedEmail && storedEmail.includes('@')) {
                        const localPart = storedEmail.split('@')[0];
                        this.displayName = localPart.charAt(0).toUpperCase() + localPart.slice(1);
                    }
                }
            });
        }
    }
}
