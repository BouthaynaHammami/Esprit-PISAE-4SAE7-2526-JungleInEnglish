// src/app/features/student/components/student-shell/student-shell.component.ts
import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';
import { AuthService } from '../../../../core/services/auth.service';

@Component({
    selector: 'app-student-shell',
    templateUrl: './student-shell.component.html',
    styleUrls: ['./student-shell.component.css']
})
export class StudentShellComponent implements OnInit {
  isParent = localStorage.getItem('devunity_user_type') === 'parent';

  // Parent navbar state
  displayName = 'Parent';
  avatarLetter = 'P';
  currentUrl = '';

  parentNav = [
    { label: 'My Kids', icon: 'bi-emoji-smile-fill', link: '/student/parent-dashboard' },
    { label: 'English Kids', icon: 'bi-mortarboard-fill', link: '/student/english-kids' },
    { label: 'Certificates', icon: 'bi-patch-check-fill', link: '/student/certificates' },
    { label: 'Events', icon: 'bi-calendar-event-fill', link: '/student/events' },
  ];

  constructor(private router: Router, private authService: AuthService) {}

  ngOnInit(): void {
    if (this.isParent) {
      // Resolve display name
      const name = this.authService.getUserName();
      if (name) {
        this.displayName = name;
        this.avatarLetter = name.charAt(0).toUpperCase();
      } else {
        const email = this.authService.getUserEmail();
        if (email) {
          const local = email.split('@')[0];
          this.displayName = local.charAt(0).toUpperCase() + local.slice(1);
          this.avatarLetter = local.charAt(0).toUpperCase();
        }
      }

      // Redirect to parent dashboard if landing on /student root
      if (!this.router.url.includes('parent-dashboard') &&
          !this.router.url.includes('english-kids') &&
          !this.router.url.includes('certificates') &&
          !this.router.url.includes('events')) {
        this.router.navigate(['/student/parent-dashboard']);
      }

      this.currentUrl = this.router.url;
      this.router.events.pipe(filter(e => e instanceof NavigationEnd)).subscribe((e: any) => {
        this.currentUrl = e.url;
      });
    }
  }

  isActive(link: string): boolean {
    return this.currentUrl.includes(link.replace('/student/', ''));
  }

  logout(): void { this.authService.logout(); }
}
