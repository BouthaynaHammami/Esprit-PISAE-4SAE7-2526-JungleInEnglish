// src/app/features/company/company.component.ts
import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../core/services/auth.service';

interface Student {
  name: string;
  level: string;
  progress: number;
  status: 'active' | 'completed' | 'paused';
  joinDate: string;
  avatar: string;
}

@Component({
  selector: 'app-company',
  templateUrl: './company.component.html'
})
export class CompanyComponent implements OnInit {
  email: string | null = null;

  stats = [
    { label: 'Total Students', value: 24, icon: 'users', color: '#006D77' },
    { label: 'Active Learners', value: 18, icon: 'zap', color: '#83C5BE' },
    { label: 'Certificates', value: 5, icon: 'award', color: '#E29578' },
    { label: 'On Hold', value: 1, icon: 'pause', color: '#FFDDD2' },
  ];

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

  constructor(private authService: AuthService) { }

  ngOnInit(): void {
    this.email = this.authService.getUserEmail();
  }

  logout(): void {
    this.authService.logout();
  }
}