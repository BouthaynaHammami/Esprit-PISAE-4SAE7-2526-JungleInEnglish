// src/app/features/admin/admin-dashboard-home.component.ts
import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../core/services/auth.service';

interface StatCard {
    label: string;
    value: string;
    icon: string;
    color: string;
    trend: string;
}

@Component({
    selector: 'app-admin-dashboard-home',
    templateUrl: './admin-dashboard-home.component.html'
})
export class AdminDashboardHomeComponent implements OnInit {
    email: string | null = null;

    stats: StatCard[] = [
        { label: 'Total Users', value: '3,847', icon: '👥', color: '#006D77', trend: '+12% this month' },
        { label: 'Active Courses', value: '48', icon: '📚', color: '#83C5BE', trend: '+3 new this week' },
        { label: 'Revenue', value: '$24,580', icon: '💰', color: '#E29578', trend: '+8% vs last month' },
        { label: 'Active Sessions', value: '127', icon: '🟢', color: '#4CAF50', trend: 'Right now' },
    ];

    recentActivity = [
        { user: 'Ahmed Bouali', action: 'Enrolled in Business English B2', time: '5 min ago', avatar: '🧑' },
        { user: 'Sara El-Amin', action: 'Completed Grammar Essentials A2', time: '22 min ago', avatar: '👩' },
        { user: 'Mohamed Kacem', action: 'Registered as new student', time: '1 hr ago', avatar: '👨‍🎓' },
        { user: 'Layla Mansouri', action: 'Booked a session with Tutor John', time: '2 hr ago', avatar: '👩‍💼' },
        { user: 'Yassine Trabelsi', action: 'Downloaded Certificate — B1 Level', time: '3 hr ago', avatar: '🧑‍🎓' },
    ];

    constructor(private authService: AuthService) { }

    ngOnInit(): void {
        this.email = this.authService.getUserEmail();
    }
}
