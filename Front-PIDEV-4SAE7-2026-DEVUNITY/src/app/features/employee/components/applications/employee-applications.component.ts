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

  form: Applicant = { reponse: '', cv: '' };
  selectedRecruitmentId: number | null = null;
  submitting = false;
  submitSuccess = false;
  submitError = '';
  showPreview = false;

  currentUserId: number | null = null;
  showApplyModal = false;
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

    // Pre-select recruitmentId if navigated with query param
    this.route.queryParams.subscribe(params => {
      if (params['recruitmentId']) {
        this.selectedRecruitmentId = +params['recruitmentId'];
      }
    });
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

  submitApplication(): void {
    if (!this.selectedRecruitmentId) return;
    this.submitting = true;
    this.submitSuccess = false;
    this.submitError = '';

    const applicant: Applicant = {
      ...this.form,
      userId: this.currentUserId ?? undefined,
      status: 'PENDING',
      recruitment: { id: this.selectedRecruitmentId }
    };

    this.recruitmentService.createApplicant(applicant).subscribe({
      next: (createdApplicant) => {
        // Send notification to admin about new application
        const selectedRecruitment = this.recruitments.find(r => r.id === this.selectedRecruitmentId);
        const userEmail = this.authService.getUserEmail();
        
        if (selectedRecruitment && userEmail) {
          // Send notification to admin
          this.notificationService.sendNotificationToUser(
            this.notificationConfig.getAdminEmail(),
            'New Job Application Received',
            `A new application has been submitted for ${selectedRecruitment.positionTitle} in ${selectedRecruitment.department} by ${userEmail}`,
            'APPLICANT_CREATED',
            createdApplicant.id,
            'APPLICANT'
          ).subscribe({
            next: () => console.log('Notification sent to admin'),
            error: (err) => console.error('Failed to send notification:', err)
          });
        }

        this.submitting = false;
        this.submitSuccess = true;
        this.form = { reponse: '', cv: '' };
        this.selectedRecruitmentId = null;
        this.loadMyApplications();
      },
      error: (err) => {
        this.submitting = false;
        this.submitError = err?.error?.message ?? 'Application failed. Please try again.';
      }
    });
  }

  getStatusClass(status?: string): string {
    switch (status) {
      case 'ACCEPTED': return 'badge-success';
      case 'REJECTED': return 'badge-danger';
      default:         return 'badge-warning';
    }
  }

  togglePreview(): void {
    this.showPreview = !this.showPreview;
  }

  getSafeViewerUrl(url: string): SafeResourceUrl {
    if (!url) return '';
    let target = url;

    // 1. Handle Google Drive Files (PDF, etc.)
    if (url.includes('drive.google.com')) {
      target = url.replace(/\/view(\?.*)?$/, '/preview');
    } 
    // 2. Handle Google-native Docs/Slides/Sheets
    else if (url.includes('docs.google.com') && (url.includes('/document/') || url.includes('/presentation/') || url.includes('/spreadsheets/'))) {
      target = url.replace(/\/(edit|view|copy)(\?.*)?$/, '/preview');
    }
    // 3. Fallback for external direct file links (PDF, DOCX)
    else {
      target = `https://docs.google.com/viewer?url=${encodeURIComponent(url)}&embedded=true`;
    }

    return this.sanitizer.bypassSecurityTrustResourceUrl(target);
  }

  openApplyModal(): void {
    this.showApplyModal = true;
  }

  closeApplyModal(): void {
    this.showApplyModal = false;
    this.submitSuccess = false;
    this.submitError = '';
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
