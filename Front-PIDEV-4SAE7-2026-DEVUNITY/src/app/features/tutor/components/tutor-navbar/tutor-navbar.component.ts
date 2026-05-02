// src/app/features/tutor/components/tutor-navbar/tutor-navbar.component.ts
import { Component } from '@angular/core';
import { AuthService } from '../../../../core/services/auth.service';

@Component({
    selector: 'app-tutor-navbar',
    templateUrl: './tutor-navbar.component.html'
})
export class TutorNavbarComponent {
    constructor(public authService: AuthService) { }

    logout(): void {
        this.authService.logout();
    }
}
