// src/app/features/employee/employee.component.ts
import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../core/services/auth.service';

interface Job {
  title: string;
  department: string;
  type: 'Full-time' | 'Part-time' | 'Contract';
  location: string;
  icon: string;
  posted: string;
  description: string;
  urgent: boolean;
}

@Component({
  selector: 'app-employee',
  templateUrl: './employee.component.html'
})
export class EmployeeComponent implements OnInit {
  email: string | null = null;

  jobs: Job[] = [
    {
      title: 'English Language Tutor',
      department: 'Teaching',
      type: 'Full-time',
      location: 'Tunis, Tunisia',
      icon: '📚',
      posted: '2 days ago',
      description: 'Deliver engaging B1–C1 English lessons for adult learners in small groups (≤12 students).',
      urgent: false
    },
    {
      title: 'Academic Coordinator',
      department: 'Administration',
      type: 'Full-time',
      location: 'Tunis, Tunisia',
      icon: '🗂️',
      posted: '5 days ago',
      description: 'Oversee curriculum design and coordinate between tutors and management to ensure quality delivery.',
      urgent: true
    },
    {
      title: 'Business English Coach',
      department: 'Teaching',
      type: 'Part-time',
      location: 'Remote',
      icon: '💼',
      posted: '1 week ago',
      description: 'Coach professionals and executives in business communication, negotiations, and presentations.',
      urgent: false
    },
    {
      title: 'Customer Success Agent',
      department: 'Support',
      type: 'Full-time',
      location: 'Tunis, Tunisia',
      icon: '🤝',
      posted: '3 days ago',
      description: 'Be the first point of contact for enrolled students — handle onboarding, feedback, and inquiries.',
      urgent: false
    },
    {
      title: 'Social Media & Content Creator',
      department: 'Marketing',
      type: 'Contract',
      location: 'Remote',
      icon: '📱',
      posted: '1 day ago',
      description: 'Create engaging short-form content in English to grow our social presence across platforms.',
      urgent: true
    },
    {
      title: 'Junior IT Support',
      department: 'IT',
      type: 'Full-time',
      location: 'Tunis, Tunisia',
      icon: '💻',
      posted: '4 days ago',
      description: 'Maintain digital infrastructure, LMS platform, and provide technical support to staff and students.',
      urgent: false
    },
  ];

  typeColor(type: string): string {
    const map: Record<string, string> = {
      'Full-time': 'bg-[#006D77]/10 text-[#006D77]',
      'Part-time': 'bg-[#83C5BE]/20 text-[#005560]',
      'Contract': 'bg-[#E29578]/20 text-[#b06040]'
    };
    return map[type] ?? '';
  }

  constructor(private authService: AuthService) { }

  ngOnInit(): void {
    this.email = this.authService.getUserEmail();
  }

  logout(): void {
    this.authService.logout();
  }
}