// src/app/features/employee/components/employee-dashboard/employee-dashboard.component.ts
import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../../core/services/auth.service';

interface JobApplication {
  position: string;
  company: string;
  department: string;
  experience: string;
  status: 'active' | 'completed' | 'paused';
  appliedDate: string;
  avatar: string;
}

@Component({
    selector: 'app-employee-dashboard',
    templateUrl: './employee-dashboard.component.html',
    styleUrls: ['./employee-dashboard.component.css']
})
export class EmployeeDashboardComponent implements OnInit {
    email: string | null = null;
    userName: string | null = null;

    stats = [
        { label: 'Open Positions', value: 15, icon: 'users', color: '#006D77' },
        { label: 'Applied Jobs', value: 8, icon: 'zap', color: '#83C5BE' },
        { label: 'Interviews', value: 3, icon: 'award', color: '#E29578' },
        { label: 'Pending', value: 2, icon: 'pause', color: '#FFDDD2' },
    ];

    applications: JobApplication[] = [
        { position: 'Senior Developer', company: 'Tech Solutions', department: 'IT', experience: '5+ years', status: 'active', appliedDate: '2024-09-01', avatar: 'SD' },
        { position: 'Product Manager', company: 'Innovation Labs', department: 'Product', experience: '3+ years', status: 'active', appliedDate: '2024-09-05', avatar: 'PM' },
        { position: 'UX Designer', company: 'Creative Studio', department: 'Design', experience: '4+ years', status: 'completed', appliedDate: '2024-08-15', avatar: 'UX' },
        { position: 'Data Analyst', company: 'Big Data Inc', department: 'Analytics', experience: '2+ years', status: 'active', appliedDate: '2024-10-01', avatar: 'DA' },
        { position: 'QA Engineer', company: 'Quality Systems', department: 'Testing', experience: '3+ years', status: 'paused', appliedDate: '2024-09-20', avatar: 'QA' },
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
}
