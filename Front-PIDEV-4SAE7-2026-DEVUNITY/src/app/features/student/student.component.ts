// src/app/features/student/student.component.ts
import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../core/services/auth.service';

interface CourseCard {
  title: string;
  level: string;
  progress: number;
  nextLesson: string;
  icon: string;
  color: string;
}

@Component({
  selector: 'app-student',
  templateUrl: './student.component.html'
})
export class StudentComponent implements OnInit {
  email: string | null = null;

  courses: CourseCard[] = [
    { title: 'Business English', level: 'B2', progress: 72, nextLesson: 'Negotiations & Meetings', icon: '💼', color: '#006D77' },
    { title: 'Conversational English', level: 'B1', progress: 45, nextLesson: 'Everyday Expressions', icon: '💬', color: '#83C5BE' },
    { title: 'Grammar Essentials', level: 'A2', progress: 90, nextLesson: 'Conditional Clauses', icon: '📝', color: '#E29578' },
  ];

  stats = [
    { label: 'Lessons Completed', value: '24', icon: '✅' },
    { label: 'Hours Studied', value: '38', icon: '⏱️' },
    { label: 'Streak Days', value: '7', icon: '🔥' },
    { label: 'Certificates', value: '2', icon: '🏆' },
  ];

  constructor(private authService: AuthService) { }

  ngOnInit(): void {
    this.email = this.authService.getUserEmail();
  }

  logout(): void {
    this.authService.logout();
  }
}