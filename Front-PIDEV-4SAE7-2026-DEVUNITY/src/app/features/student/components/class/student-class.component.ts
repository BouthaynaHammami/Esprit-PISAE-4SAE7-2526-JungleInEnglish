import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../../core/services/auth.service';
import { LearnerUserLookupService } from '../../../../core/services/learner-user-lookup.service';
import { ScheduleService } from '../../../../core/services/schedule.service';
import { Schedule } from '../../../../core/models/schedule.model';
import { CourseService } from '../../../../core/services/course.service';
import { RoomScheduleComplaintService } from '../../../../core/services/room-complaint.service';

@Component({
  selector: 'app-student-class',
  templateUrl: './student-class.component.html',
  styleUrls: ['./student-class.component.scss']
})
export class StudentClassComponent implements OnInit {
  isLoading = true;
  error: string | null = null;

  classId: number | null = null;
  className = '';
  classLevel = '';
  classSize: number | null = null;

  weekStart = '';
  classSchedules: Schedule[] = [];
  weekSchedules: Schedule[] = [];
  courseTitleById: Record<number, string> = {};

  complaintSuccess: string | null = null;
  complaintError: string | null = null;
  isComplaintSubmitting = false;
  showComplaintModal = false;
  selectedScheduleForComplaint: Schedule | null = null;
  complaintSubject = '';
  complaintDescription = '';

  // Toast notifications
  showAlert = false;
  alertType: 'success' | 'error' | 'info' = 'info';
  alertMessage = '';

  // Current date for sidebar
  now = new Date();

  constructor(
    private authService: AuthService,
    private learnerUserLookupService: LearnerUserLookupService,
    private scheduleService: ScheduleService,
    private courseService: CourseService,
    private complaintService: RoomScheduleComplaintService
  ) {
    this.weekStart = this.getMondayOfCurrentWeek();
  }

  ngOnInit(): void {
    this.loadCourseCatalog();
    this.loadClassSchedules();
  }

  loadClassSchedules(): void {
    const userId = this.authService.getUserId();
    if (!userId) {
      this.error = 'Unable to detect the connected student.';
      this.isLoading = false;
      return;
    }

    this.isLoading = true;
    this.error = null;

    this.learnerUserLookupService.getById(userId).subscribe({
      next: (user) => {
        const classId = Number(user?.classId);
        if (!classId) {
          this.error = 'No class is assigned to your account yet.';
          this.isLoading = false;
          return;
        }

        this.classId = classId;

        this.scheduleService.getByClass(classId).subscribe({
          next: (schedules) => {
            this.classSchedules = schedules || [];
            this.className = this.classSchedules[0]?.classEntity?.name || '';
            this.classLevel = this.classSchedules[0]?.classEntity?.level || '';
            this.classSize = this.classSchedules[0]?.classEntity?.numberStudents ?? null;
            this.applyWeekFilter();
            this.isLoading = false;
          },
          error: () => {
            this.error = 'Failed to load class schedules.';
            this.isLoading = false;
          }
        });
      },
      error: () => {
        this.error = 'Failed to load student profile.';
        this.isLoading = false;
      }
    });
  }

  applyWeekFilter(): void {
    if (!this.weekStart) {
      this.weekSchedules = [...this.classSchedules];
      return;
    }

    const start = new Date(`${this.weekStart}T00:00:00`);
    const end = new Date(start);
    end.setDate(end.getDate() + 7);

    this.weekSchedules = this.classSchedules
      .filter((s) => {
        const startTime = new Date(s.startTime);
        return startTime >= start && startTime < end;
      })
      .sort((a, b) => new Date(a.startTime).getTime() - new Date(b.startTime).getTime());
  }

  formatDate(dateTime: string): string {
    if (!dateTime) {
      return '-';
    }
    return new Date(dateTime).toLocaleString();
  }

  getCourseTitle(courseId: number): string {
    return this.courseTitleById[courseId] || 'Unknown course';
  }

  openComplaintModal(schedule: Schedule): void {
    this.selectedScheduleForComplaint = schedule;
    this.complaintSubject = '';
    this.complaintDescription = '';
    this.complaintError = null;
    this.showComplaintModal = true;
  }

  closeComplaintModal(): void {
    this.showComplaintModal = false;
    this.selectedScheduleForComplaint = null;
    this.isComplaintSubmitting = false;
  }

  submitComplaint(): void {
    if (!this.selectedScheduleForComplaint?.scheduleId) {
      this.complaintError = 'Schedule is missing. Please try again.';
      return;
    }

    if (!this.complaintDescription.trim()) {
      this.complaintError = 'Description is required.';
      return;
    }

    const complainantEmail = this.authService.getUserEmail() || undefined;

    this.complaintError = null;
    this.complaintSuccess = null;
    this.isComplaintSubmitting = true;

    this.complaintService.createForSchedule(this.selectedScheduleForComplaint.scheduleId, {
      subject: this.selectedScheduleForComplaint.title || 'Class Complaint',
      description: this.complaintDescription.trim(),
      status: false,
      complainantEmail,
      complainantRole: 'STUDENT'
    }).subscribe({
      next: () => {
        this.isComplaintSubmitting = false;
        this.closeComplaintModal();
        this.showToast('success', 'Complaint submitted successfully');
      },
      error: () => {
        this.isComplaintSubmitting = false;
        this.complaintError = 'Failed to submit complaint.';
        this.showToast('error', 'Failed to submit complaint');
      }
    });
  }

  private showToast(type: 'success' | 'error' | 'info', message: string): void {
    this.alertType = type;
    this.alertMessage = message;
    this.showAlert = true;
    setTimeout(() => {
      this.showAlert = false;
    }, 3500);
  }

  private loadCourseCatalog(): void {
    this.courseService.getAll().subscribe({
      next: (courses) => {
        this.courseTitleById = (courses || []).reduce((acc, c) => {
          if (c.courseId) {
            acc[c.courseId] = c.title;
          }
          return acc;
        }, {} as Record<number, string>);
      },
      error: () => {
        this.courseTitleById = {};
      }
    });
  }

  private getMondayOfCurrentWeek(): string {
    const today = new Date();
    const day = today.getDay();
    const diff = day === 0 ? -6 : 1 - day;
    const monday = new Date(today);
    monday.setDate(today.getDate() + diff);
    return monday.toISOString().split('T')[0];
  }
}
