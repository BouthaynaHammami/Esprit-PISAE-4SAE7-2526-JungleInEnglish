// src/app/features/tutor/components/tutor-navbar/tutor-navbar.component.ts
import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../../core/services/auth.service';

@Component({
    selector: 'app-tutor-navbar',
    templateUrl: './tutor-navbar.component.html',
    styleUrl: './tutor-navbar.component.scss'   // ← ajout du fichier de style
})
export class TutorNavbarComponent implements OnInit {
    name: string | null = null;

    constructor(public authService: AuthService) { }

    ngOnInit(): void {
        this.name = this.authService.getUserName();
    }

    logout(): void {
        this.authService.logout();
    }
}