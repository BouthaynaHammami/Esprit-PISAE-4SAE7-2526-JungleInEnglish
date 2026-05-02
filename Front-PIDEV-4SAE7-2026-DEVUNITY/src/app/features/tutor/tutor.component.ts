// src/app/features/tutor/tutor.component.ts
import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../core/services/auth.service';

interface TimeSlot {
  hour: string;
  sessions: (SessionItem | null)[];
}

interface SessionItem {
  title: string;
  students: number;
  color: string;
}

interface UpcomingSession {
  title: string;
  time: string;
  students: number;
  level: string;
  icon: string;
}

@Component({
  selector: 'app-tutor',
  templateUrl: './tutor.component.html'
})
export class TutorComponent implements OnInit {
  email: string | null = null;

  days = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'];

  timeSlots: TimeSlot[] = [
    { hour: '08:00', sessions: [null, { title: 'A1 Basics', students: 8, color: '#83C5BE' }, null, null, null, null, null] },
    { hour: '09:00', sessions: [{ title: 'B2 Business', students: 5, color: '#006D77' }, null, null, { title: 'B2 Business', students: 5, color: '#006D77' }, null, null, null] },
    { hour: '10:00', sessions: [null, null, { title: 'A2 Grammar', students: 12, color: '#E29578' }, null, null, { title: 'A2 Grammar', students: 12, color: '#E29578' }, null] },
    { hour: '11:00', sessions: [{ title: 'B1 Convo', students: 6, color: '#FFDDD2' }, null, null, null, { title: 'B1 Convo', students: 6, color: '#FFDDD2' }, null, null] },
    { hour: '12:00', sessions: [null, null, null, null, null, null, null] },
    { hour: '13:00', sessions: [null, { title: 'C1 Advanced', students: 4, color: '#334155' }, null, null, null, { title: 'C1 Advanced', students: 4, color: '#334155' }, null] },
    { hour: '14:00', sessions: [{ title: 'A2 Grammar', students: 10, color: '#E29578' }, null, { title: 'B2 Business', students: 5, color: '#006D77' }, null, null, null, null] },
    { hour: '15:00', sessions: [null, null, null, { title: 'A1 Basics', students: 8, color: '#83C5BE' }, null, null, null] },
    { hour: '16:00', sessions: [null, { title: 'B1 Convo', students: 7, color: '#FFDDD2' }, null, null, { title: 'A2 Grammar', students: 10, color: '#E29578' }, null, null] },
    { hour: '17:00', sessions: [null, null, null, null, null, null, { title: 'B2 Business', students: 5, color: '#006D77' }] },
  ];

  upcomingSessions: UpcomingSession[] = [
    { title: 'B2 Business English', time: 'Today, 09:00', students: 5, level: 'B2', icon: '💼' },
    { title: 'B1 Conversational', time: 'Today, 11:00', students: 6, level: 'B1', icon: '💬' },
    { title: 'A1 Beginners', time: 'Tomorrow, 08:00', students: 8, level: 'A1', icon: '🌱' },
    { title: 'C1 Advanced Writing', time: 'Tue, 13:00', students: 4, level: 'C1', icon: '✍️' },
  ];

  constructor(private authService: AuthService) { }

  ngOnInit(): void {
    this.email = this.authService.getUserEmail();
  }

  logout(): void {
    this.authService.logout();
  }

  isDark(color: string): boolean {
    return color === '#006D77' || color === '#334155';
  }
}