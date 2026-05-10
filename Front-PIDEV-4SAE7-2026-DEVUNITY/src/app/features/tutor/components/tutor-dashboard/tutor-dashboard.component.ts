// src/app/features/tutor/components/tutor-dashboard/tutor-dashboard.component.ts
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
    selector: 'app-tutor-dashboard',
    templateUrl: './tutor-dashboard.component.html',
    styleUrls: ['./tutor-dashboard.component.css']
})
export class TutorDashboardComponent implements OnInit {
    email: string | null = null;
    userName: string | null = null;

    stats = [
        { label: 'Total Classes', value: 12, icon: 'users', color: '#006D77' },
        { label: 'Active Students', value: 28, icon: 'zap', color: '#83C5BE' },
        { label: 'Sessions Completed', value: 45, icon: 'award', color: '#E29578' },
        { label: 'Pending', value: 2, icon: 'pause', color: '#FFDDD2' },
    ];

    students: Student[] = [
        { name: 'Ahmed Bouali', level: 'B2', progress: 85, status: 'active', joinDate: '2024-09-01', avatar: 'AB' },
        { name: 'Fatima Ben Salah', level: 'B1', progress: 72, status: 'active', joinDate: '2024-09-05', avatar: 'FB' },
        { name: 'Mohamed Ali', level: 'A2', progress: 45, status: 'active', joinDate: '2024-10-01', avatar: 'MA' },
        { name: 'Yasmine Khorchani', level: 'C1', progress: 95, status: 'completed', joinDate: '2024-08-15', avatar: 'YK' },
        { name: 'Khalid Mansouri', level: 'B2', progress: 60, status: 'paused', joinDate: '2024-09-20', avatar: 'KM' },
    ];

    constructor(private authService: AuthService) { }

    ngOnInit(): void {
        const decoded = this.authService.decodeToken();
        this.email = decoded?.sub ?? null;
        this.userName = this.authService.getUserName();
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
