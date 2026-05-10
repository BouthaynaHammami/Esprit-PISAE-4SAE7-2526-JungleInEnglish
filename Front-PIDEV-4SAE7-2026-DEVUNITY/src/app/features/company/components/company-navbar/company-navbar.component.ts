// src/app/features/company/components/company-navbar/company-navbar.component.ts
import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../../core/services/auth.service';

@Component({
    selector: 'app-company-navbar',
    templateUrl: './company-navbar.component.html',
    styleUrl: './company-navbar.component.css'
})
export class CompanyNavbarComponent implements OnInit {
    name: string | null = null;

    constructor(private authService: AuthService) { }

    ngOnInit(): void {
        this.name = this.authService.getUserName();
    }

    logout(): void {
        this.authService.logout();
    }
}
