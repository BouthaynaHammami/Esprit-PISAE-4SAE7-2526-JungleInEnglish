import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { RecruitmentService } from '../../../../core/services/recruitment.service';
import { Recruitment } from '../../../../core/models/recruitment.model';
import { Router } from '@angular/router';

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

  showRecruitmentDetails = false;
  selectedRecruitment: Recruitment | null = null;
  
  showApplyModal = false;
  selectedRecruitmentForApply: Recruitment | null = null;

  constructor(
    private recruitmentService: RecruitmentService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadRecruitments();
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

  refresh(): void {
    this.loading = true;
    this.loadRecruitments();
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
  }

  closeApplyModal(): void {
    this.showApplyModal = false;
    this.selectedRecruitmentForApply = null;
  }

  proceedToApply(): void {
    if (this.selectedRecruitment?.id) {
      this.closeRecrutmentDetails();
      this.openApplyModal(this.selectedRecruitment);
    }
  }

  proceedToApplication(): void {
    if (this.selectedRecruitmentForApply?.id) {
      // Navigate to applications tab pre-filled with recruitmentId
      this.router.navigate(['/employee/applications'], { 
        queryParams: { recruitmentId: this.selectedRecruitmentForApply.id } 
      });
      this.closeApplyModal();
    }
  }
}
