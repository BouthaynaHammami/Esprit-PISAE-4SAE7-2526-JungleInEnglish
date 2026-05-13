// src/app/features/employee/components/applications/employee-applications.component.ts
import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import { RecruitmentService } from '../../../../core/services/recruitment.service';
import { AuthService } from '../../../../core/services/auth.service';
import { EnhancedNotificationService } from '../../../../core/services/enhanced-notification.service';
import { NotificationConfigService } from '../../../../core/services/notification-config.service';
import { Applicant, Recruitment } from '../../../../core/models/recruitment.model';

@Component({
  selector: 'app-employee-applications',
  templateUrl: './employee-applications.component.html',
  styleUrls: ['./employee-applications.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class EmployeeApplicationsComponent implements OnInit {
  recruitments: Recruitment[] = [];
  myApplications: Applicant[] = [];
  loadingRecruitments = true;
  loadingApplications = true;

  currentUserId: number | null = null;
  selectedApplication: Applicant | null = null;
  showApplicationDetails = false;

  constructor(
    private recruitmentService: RecruitmentService,
    private authService: AuthService,
    private notificationService: EnhancedNotificationService,
    private notificationConfig: NotificationConfigService,
    private route: ActivatedRoute,
    private sanitizer: DomSanitizer
  ) {}

  getAvailableRecruitments(): Recruitment[] {
    if (!this.recruitments || !this.myApplications) return this.recruitments;
    
    const appliedIds = this.myApplications
      .map(a => a.recruitment?.id)
      .filter((id): id is number => !!id);
      
    return this.recruitments.filter(r => r.id && !appliedIds.includes(r.id));
  }

  ngOnInit(): void {
    this.currentUserId = this.authService.getUserId();
    
    if (this.currentUserId) {
      this.loadRecruitments();
      this.loadMyApplications();
    } else {
      console.warn('[EmployeeApplications] No userId found on init. User might need to log in again.');
      this.loadingRecruitments = false;
      this.loadingApplications = false;
    }
  }

  loadRecruitments(): void {
    this.loadingRecruitments = true;
    this.recruitmentService.getAllRecruitments().subscribe({
      next: (d) => { this.recruitments = d.filter(r => r.status === 'OPEN'); this.loadingRecruitments = false; },
      error: () => { this.loadingRecruitments = false; }
    });
  }

  loadMyApplications(): void {
    if (!this.currentUserId) {
      console.warn('[EmployeeApplications] No currentUserId found; skipping history load.');
      this.loadingApplications = false;
      return;
    }
    this.loadingApplications = true;
    this.recruitmentService.getApplicantsByUser(this.currentUserId).subscribe({
      next: (d) => { this.myApplications = d; this.loadingApplications = false; },
      error: () => { this.loadingApplications = false; }
    });
  }


  getStatusClass(status?: string): string {
    switch (status) {
      case 'ACCEPTED': return 'badge-success';
      case 'REJECTED': return 'badge-danger';
      default:         return 'badge-warning';
    }
  }

  getScoreClass(score?: number): string {
    if (!score) return 'score-low';
    if (score >= 70) return 'score-high';
    if (score >= 40) return 'score-medium';
    return 'score-low';
  }


  openApplicationDetails(app: Applicant): void {
    this.selectedApplication = app;
    this.showApplicationDetails = true;
  }

  closeApplicationDetails(): void {
    this.showApplicationDetails = false;
    this.selectedApplication = null;
  }

  refresh(): void {
    this.loadingApplications = true;
    this.loadRecruitments();
    this.loadMyApplications();
  }
}
