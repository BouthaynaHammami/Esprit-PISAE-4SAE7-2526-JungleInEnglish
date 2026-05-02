import { Component, OnInit } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { AuthService } from '../../../../core/services/auth.service';

@Component({
    selector: 'app-admin-dashboard',
    templateUrl: './admin-dashboard.component.html'
})
export class AdminDashboardComponent implements OnInit {
    email: string | null = null;
    kibanaUrl: SafeResourceUrl;

    constructor(
        private authService: AuthService,
        private sanitizer: DomSanitizer
    ) {
        const rawUrl = "http://localhost:5601/app/dashboards#/view/e9bc1a8d-f55e-4fa2-a5d0-cf65ec9767e4?embed=true&_g=%28refreshInterval%3A%28pause%3A%21t%2Cvalue%3A60000%29%2Ctime%3A%28from%3Anow-30d%2Fd%2Cto%3Anow%29%29&show-top-menu=true&show-query-input=true&show-time-filter=true";
        this.kibanaUrl = this.sanitizer.bypassSecurityTrustResourceUrl(rawUrl);
    }

    ngOnInit(): void {
        const decoded = this.authService.decodeToken();
        this.email = decoded?.sub ?? null;
    }
}
