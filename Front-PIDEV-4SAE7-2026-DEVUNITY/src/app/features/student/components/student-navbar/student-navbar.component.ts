import { Component, HostListener, OnInit } from '@angular/core';
import { AuthService } from '../../../../core/services/auth.service';
import { LearnerUserLookupService } from '../../../../core/services/learner-user-lookup.service';
import { BadgeService } from '../../../../core/services/activity/badge.service';

type NavItem = { label: string; short: string; icon: string; link: string };

@Component({
  selector: 'app-student-navbar',
  templateUrl: './student-navbar.component.html',
  styleUrls: ['./student-navbar.component.css']
})
export class StudentNavbarComponent implements OnInit {
  // UI
  searchOpen = false;
  dropdownOpen = false;
  // Mobile menu
  mobileMenuOpen = false;

  // User data
  displayName = 'Student';
  avatarLetter = 'S';
  levelLabel = 'Level A2';

  // Gamification
  streakDays = 0;
  xp = 0;
  xpPercent = 0;

  // search
  query = '';

  nav: NavItem[] = [
    { label: 'Dashboard', short: 'Home', icon: 'bi-grid-fill', link: '/student/dashboard' },
    { label: 'Join Offer', short: 'Join', icon: 'bi-rocket-takeoff-fill', link: '/student/join-offer' },
    { label: 'Help out the platform', short: 'Help', icon: 'bi-heart-fill', link: '/student/dropout-form' },
    { label: 'Courses', short: 'Courses', icon: 'bi-book-fill', link: '/student/courses' },
    { label: 'Class', short: 'Class', icon: 'bi-calendar-week-fill', link: '/student/class' },
    { label: 'Complaints', short: 'Compl', icon: 'bi-chat-square-text-fill', link: '/student/complaints' },
    { label: 'Challenges', short: 'Chall', icon: 'bi-trophy-fill', link: '/student/challenges' },
    { label: 'Level Test', short: 'Test', icon: 'bi-clipboard-check-fill', link: '/student/test-niveau' },
    { label: 'Certificates', short: 'Cert', icon: 'bi-patch-check-fill', link: '/student/certificates' },
    { label: 'Library', short: 'Library', icon: 'bi-collection-play-fill', link: '/student/library' },
    { label: 'English Kids', short: 'Kids', icon: 'bi-mortarboard-fill', link: '/student/english-kids' },
    { label: 'Clubs', short: 'Clubs', icon: 'bi-people-fill', link: '/student/clubs' },
    { label: 'Topics', short: 'Topics', icon: 'bi-chat-dots-fill', link: '/student/topics' },
    { label: 'Events', short: 'Events', icon: 'bi-calendar-event-fill', link: '/student/events' },

    { label: 'Certif Board', short: 'CBoard', icon: 'bi-kanban-fill', link: '/student/kanban-certif' },
    { label: 'Events Board', short: 'EBoard', icon: 'bi-kanban-fill', link: '/student/kanban-events' },
    { label: 'Daily Analysis', short: 'Analysis', icon: 'bi-bar-chart-fill', link: '/student/daily-analysis' },
  ];

  get mainNav() {
    // Top 5 pour l'étudiant : Dashboard, Courses, Challenges, Certificates, Class
    const important = ['Dashboard', 'Courses', 'Challenges', 'Certificates', 'Class'];
    return this.nav.filter(item => important.includes(item.label))
                   .sort((a, b) => important.indexOf(a.label) - important.indexOf(b.label));
  }

  get moreNav() {
    // Le reste dans l'ordre original ou logique
    const important = ['Dashboard', 'Courses', 'Challenges', 'Certificates', 'Class'];
    return this.nav.filter(item => !important.includes(item.label));
  }

  constructor(
    private authService: AuthService,
    private userLookup: LearnerUserLookupService,
    private badgeService: BadgeService
  ) {}

  ngOnInit(): void {
    const userId = this.authService.getUserId();
    if (userId) {
      this.badgeService.getStudentStats(userId).subscribe({
        next: (stats) => {
          this.xp = stats.totalScore || 0;
          this.streakDays = stats.completedChallenges || 0;
          this.xpPercent = (this.xp % 100);
        },
        error: (err) => console.error('Failed to load stats for navbar', err)
      });

      this.userLookup.getById(userId).subscribe({
        next: (user) => {
          if (user.firstName) {
            this.displayName = user.lastName
              ? `${user.firstName} ${user.lastName}`
              : user.firstName;
            this.avatarLetter = user.firstName.charAt(0).toUpperCase();
          }
        },
        error: () => {
          const email = this.authService.getUserEmail();
          if (email && email.includes('@')) {
            const localPart = email.split('@')[0];
            this.displayName = localPart.charAt(0).toUpperCase() + localPart.slice(1);
            this.avatarLetter = localPart.charAt(0).toUpperCase();
          }
        }
      });
    }
  }

  toggleDropdown() {
    this.dropdownOpen = !this.dropdownOpen;
  }

  closeDropdown() {
    this.dropdownOpen = false;
  }

  toggleMobileMenu() {
    this.mobileMenuOpen = !this.mobileMenuOpen;
  }

  @HostListener('document:click', ['$event'])
  onClickOutside(event: MouseEvent) {
    const target = event.target as HTMLElement;
    if (!target.closest('.nav-more-container')) {
      this.closeDropdown();
    }
    // Ferme le menu mobile si on clique en dehors
    if (this.mobileMenuOpen && !target.closest('.mobile-menu')) {
      this.mobileMenuOpen = false;
    }
  }

  logout() {
    this.authService.logout();
  }
}