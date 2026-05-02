// src/app/features/tutor/components/tutor-dashboard/tutor-dashboard.component.ts
import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../../core/services/auth.service';

@Component({
    selector: 'app-tutor-dashboard',
    templateUrl: './tutor-dashboard.component.html'
})
export class TutorDashboardComponent implements OnInit {
    email: string | null = null;

    constructor(private authService: AuthService) { }

    ngOnInit(): void {
        const decoded = this.authService.decodeToken();
        this.email = decoded?.sub ?? null;
    }
}
