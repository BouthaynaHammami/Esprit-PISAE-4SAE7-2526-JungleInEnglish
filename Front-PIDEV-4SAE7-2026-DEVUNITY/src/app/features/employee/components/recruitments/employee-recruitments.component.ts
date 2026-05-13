import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { RecruitmentService } from '../../../../core/services/recruitment.service';
import { Recruitment, Applicant } from '../../../../core/models/recruitment.model';
import { Router } from '@angular/router';
import { AuthService } from '../../../../core/services/auth.service';

@Component({
  selector: 'app-employee-recruitments',
  templateUrl: './employee-recruitments.component.html',
  styleUrls: ['./employee-recruitments.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class EmployeeRecruitmentsComponent implements OnInit {
  recruitments: Recruitment[] = [];
  loading = true;
  filterStatus = 'OPEN';
  myAppliedRecruitmentIds: number[] = [];

  showRecruitmentDetails = false;
  selectedRecruitment: Recruitment | null = null;
  
  showApplyModal = false;
  selectedRecruitmentForApply: Recruitment | null = null;
  
  selectedFile: File | null = null;
  motivationLetter: string = '';
  isUploading = false;
  applyMethod: 'file' | 'url' = 'file';
  cvUrlInput: string = '';

  constructor(
    private recruitmentService: RecruitmentService,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loadRecruitments();
    this.loadMyApplications();
  }

  loadRecruitments(): void {
    this.loading = true;
    this.recruitmentService.getAllRecruitments().subscribe({
      next: (data) => {
        this.recruitments = data.filter(r => r.status === this.filterStatus || !this.filterStatus);
        this.loading = false;
      },
      error: () => { this.loading = false; }
    });
  }

  loadMyApplications(): void {
    const userId = this.authService.getUserId();
    if (userId) {
      this.recruitmentService.getApplicantsByUser(userId).subscribe({
        next: (apps) => {
          this.myAppliedRecruitmentIds = apps
            .map(a => a.recruitment?.id)
            .filter((id): id is number => !!id);
        },
        error: (err) => console.error('Failed to load user applications', err)
      });
    }
  }

  hasAlreadyApplied(recruitmentId?: number): boolean {
    if (!recruitmentId) return false;
    return this.myAppliedRecruitmentIds.includes(recruitmentId);
  }

  refresh(): void {
    this.loading = true;
    this.loadRecruitments();
    this.loadMyApplications();
  }

  openRecrutmentDetails(r: Recruitment): void {
    this.selectedRecruitment = r;
    this.showRecruitmentDetails = true;
  }

  closeRecrutmentDetails(): void {
    this.showRecruitmentDetails = false;
    this.selectedRecruitment = null;
  }

  openApplyModal(r: Recruitment): void {
    this.selectedRecruitmentForApply = r;
    this.showApplyModal = true;
    this.selectedFile = null;
    this.motivationLetter = '';
    this.applyMethod = 'file';
    this.cvUrlInput = '';
  }

  closeApplyModal(): void {
    this.showApplyModal = false;
    this.selectedRecruitmentForApply = null;
    this.selectedFile = null;
    this.motivationLetter = '';
  }

  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0];
  }

  proceedToApply(): void {
    if (this.selectedRecruitment?.id) {
      const rec = this.selectedRecruitment;
      this.closeRecrutmentDetails();
      this.openApplyModal(rec);
    }
  }

  proceedToApplication(): void {
    if (this.applyMethod === 'file') {
      this.submitWithFile();
    } else {
      this.submitWithUrl();
    }
  }

  private submitWithFile(): void {
    if (!this.selectedRecruitmentForApply?.id || !this.selectedFile) {
      alert('Please select a CV to apply.');
      return;
    }

    this.isUploading = true;
    const formData = new FormData();
    formData.append('file', this.selectedFile);

    this.recruitmentService.uploadCv(formData).subscribe({
      next: (cvUrl: string) => this.finalizeApplication(cvUrl),
      error: (err: any) => {
        this.isUploading = false;
        console.error('Upload failed', err);
        alert('Failed to upload CV. Please try again.');
      }
    });
  }

  private submitWithUrl(): void {
    if (!this.selectedRecruitmentForApply?.id || !this.cvUrlInput.trim()) {
      alert('Please provide a valid CV URL.');
      return;
    }
    this.finalizeApplication(this.cvUrlInput);
  }

  private finalizeApplication(cvUrl: string): void {
    this.isUploading = true;
    const applicant: Applicant = {
      reponse: this.motivationLetter,
      cv: cvUrl,
      userId: this.authService.getUserId() ?? undefined,
      recruitment: { id: this.selectedRecruitmentForApply!.id },
      date: new Date()
    };

    this.recruitmentService.createApplicant(applicant).subscribe({
      next: () => {
        this.isUploading = false;
        alert('Application submitted successfully!');
        this.loadMyApplications(); // Refresh local list
        this.closeApplyModal();
        this.router.navigate(['/employee/applications']);
      },
      error: (err: any) => {
        this.isUploading = false;
        console.error('Application failed', err);
        const msg = err?.error?.message || err?.error || 'Unknown error';
        alert('Failed to submit application: ' + msg);
      }
    });
  }
}
