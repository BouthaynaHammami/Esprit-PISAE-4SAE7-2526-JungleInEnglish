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

  applyTo(r: Recruitment): void {
    // Navigate to applications tab pre-filled with recruitmentId
    this.router.navigate(['/employee/applications'], { queryParams: { recruitmentId: r.id } });
  }
}
