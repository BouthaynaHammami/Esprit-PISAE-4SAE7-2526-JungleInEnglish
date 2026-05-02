// src/app/features/student/components/profile/student-profile.component.ts
import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../../core/services/auth.service';
import { LearnerUserLookupService } from '../../../../core/services/learner-user-lookup.service';
import { BadgeService } from '../../../../core/services/activity/badge.service';

@Component({
    selector: 'app-student-profile',
    templateUrl: './student-profile.component.html',
    styleUrls: ['./student-profile.component.css']
})
export class StudentProfileComponent implements OnInit {
    email: string | null = null;
    role: string | null = null;
    userId: number | null = null;
    expiryDate: string | null = null;
    avatarLetter: string = '?';
    displayName: string = 'Student';

    // Stats for Premium Design
    totalScore: number = 0;
    rank: string = 'Beginner';
    badgesCount: number = 0;
    completedChallenges: number = 0;
    xpProgress: number = 0;

    constructor(
        private authService: AuthService,
        private userLookup: LearnerUserLookupService,
        private badgeService: BadgeService
    ) { }

    ngOnInit(): void {
        const decoded = this.authService.decodeToken();
        this.role = decoded?.role?.replace('ROLE_', '') ?? null;

        // Use localStorage values (actual email & numeric ID) instead of JWT sub (UUID)
        this.email = this.authService.getUserEmail();
        this.userId = this.authService.getUserId();

        if (decoded?.exp) {
            this.expiryDate = new Date(decoded.exp * 1000).toLocaleDateString('en-US', {
                year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit'
            });
        }

        // Fetch real user data from backend
        if (this.userId) {
            this.badgeService.getStudentStats(this.userId).subscribe({
                next: (stats) => {
                    this.totalScore = stats.totalScore || 0;
                    this.badgesCount = stats.totalBadgesOwned || 0;
                    this.completedChallenges = stats.completedChallenges || 0;
                    this.xpProgress = (this.totalScore % 100);
                    
                    if (this.totalScore >= 1000) this.rank = 'Linguistic Master';
                    else if (this.totalScore >= 500) this.rank = 'Advanced Learner';
                    else if (this.totalScore >= 100) this.rank = 'Intermediate Learner';
                    else this.rank = 'Beginner';
                },
                error: (err) => console.error('Failed to load student stats', err)
            });

            this.userLookup.getById(this.userId).subscribe({
                next: (user) => {
                    if (user.firstName) {
                        this.displayName = user.lastName
                            ? `${user.firstName} ${user.lastName}`
                            : user.firstName;
                        this.avatarLetter = user.firstName.charAt(0).toUpperCase();
                    }
                    if (user.email) {
                        this.email = user.email;
                    }
                },
                error: () => {
                    // Fallback to email-based display
                    if (this.email && this.email.includes('@')) {
                        const localPart = this.email.split('@')[0];
                        this.displayName = localPart.charAt(0).toUpperCase() + localPart.slice(1);
                        this.avatarLetter = localPart.charAt(0).toUpperCase();
                    }
                }
            });
        } else if (this.email) {
            if (this.email.includes('@')) {
                const localPart = this.email.split('@')[0];
                this.displayName = localPart.charAt(0).toUpperCase() + localPart.slice(1);
                this.avatarLetter = localPart.charAt(0).toUpperCase();
            }
        }
    }

    getRoleBadgeClass(): string {
        switch (this.role) {
            case 'STUDENT': return 'badge-student';
            case 'ADMIN': return 'badge-admin';
            default: return 'badge-default';
        }
    }

    logout(): void {
        this.authService.logout();
    }
}
