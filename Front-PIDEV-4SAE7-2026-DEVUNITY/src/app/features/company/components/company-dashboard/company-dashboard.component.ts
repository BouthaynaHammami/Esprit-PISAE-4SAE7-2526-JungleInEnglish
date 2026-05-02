// src/app/features/company/components/company-dashboard/company-dashboard.component.ts
import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../../core/services/auth.service';

interface Student {
  name: string;
  level: string;
  progress: number;
  status: 'active' | 'completed' | 'paused';
  joinDate: string;
  avatar: string;
}

@Component({
  selector: 'app-company-dashboard',
  templateUrl: './company-dashboard.component.html',
  styleUrls: ['./company-dashboard.component.css']
})
export class CompanyDashboardComponent implements OnInit {
  email: string | null = null;

  stats = [
    { label: 'Total Students', value: 24, icon: 'users', color: '#006D77' },
    { label: 'Active Learners', value: 18, icon: 'zap', color: '#83C5BE' },
    { label: 'Certificates', value: 5, icon: 'award', color: '#E29578' },
    { label: 'On Hold', value: 1, icon: 'pause', color: '#FFDDD2' },
  ];

  students: Student[] = [
    { name: 'Ahmed Bouali', level: 'B2', progress: 85, status: 'active', joinDate: '2024-09-01', avatar: 'AB' }
  ];

  constructor(private authService: AuthService) { }

  ngOnInit(): void {
    this.email = this.authService.getUserEmail();
  }

  getStatusClass(status: string): string {
    const map: Record<string, string> = {
      active: 'status-active',
      completed: 'status-completed',
      paused: 'status-paused'
    };
    return map[status] ?? '';
  }

  getProgressColor(progress: number): string {
    if (progress >= 90) return '#006D77';
    if (progress >= 60) return '#83C5BE';
    return '#E29578';
  }
}
