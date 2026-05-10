// src/app/features/tutor/components/planning/tutor-planning.component.ts
import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../../core/services/auth.service';
import { ScheduleService } from '../../../../core/services/schedule.service';
import { LearnerUserLookupService } from '../../../../core/services/learner-user-lookup.service';
import { Schedule } from '../../../../core/models/schedule.model';
import { CourseService } from '../../../../core/services/course.service';
import { RoomScheduleComplaintService } from '../../../../core/services/room-complaint.service';

@Component({
    selector: 'app-tutor-planning',
    templateUrl: './tutor-planning.component.html',
    styleUrls: ['./tutor-planning.component.css']
})
export class TutorPlanningComponent implements OnInit {
    isLoading = true;
    error: string | null = null;

    tutorId: number | null = null;
    weekStart = '';

    schedules: Schedule[] = [];
    weekSchedules: Schedule[] = [];
    courseTitleById: Record<number, string> = {};

    complaintSuccess: string | null = null;
    complaintError: string | null = null;
    isComplaintSubmitting = false;
    showComplaintModal = false;
    selectedScheduleForComplaint: Schedule | null = null;
    complaintSubject = '';
    complaintDescription = '';

    showDetailsModal = false;
    selectedScheduleForDetails: Schedule | null = null;

    constructor(
        private authService: AuthService,
        private scheduleService: ScheduleService,
        private learnerUserLookupService: LearnerUserLookupService,
        private courseService: CourseService,
        private complaintService: RoomScheduleComplaintService
    ) {
        this.weekStart = this.getMondayOfCurrentWeek();
    }

    ngOnInit(): void {
        this.loadCourseCatalog();
        this.loadTutorSchedules();
    }

    loadTutorSchedules(): void {
        this.isLoading = true;
        this.error = null;

        const userId = this.authService.getUserId();
        if (userId) {
            this.fetchSchedulesByTutorId(userId);
            return;
        }

        const email = this.authService.getUserEmail();
        if (!email) {
            this.error = 'Unable to resolve connected tutor account.';
            this.isLoading = false;
            return;
        }

        this.learnerUserLookupService.getByEmail(email).subscribe({
            next: (user) => {
                const resolvedId = Number(user?.userId);
                if (!resolvedId) {
                    this.error = 'Unable to resolve connected tutor account.';
                    this.isLoading = false;
                    return;
                }
                this.fetchSchedulesByTutorId(resolvedId);
            },
            error: () => {
                this.error = 'Unable to resolve connected tutor account.';
                this.isLoading = false;
            }
        });
    }

    applyWeekFilter(): void {
        if (!this.weekStart) {
            this.weekSchedules = [...this.schedules];
            return;
        }

        const start = new Date(`${this.weekStart}T00:00:00`);
        const end = new Date(start);
        end.setDate(end.getDate() + 7);

        this.weekSchedules = this.schedules
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

    openSessionDetails(schedule: Schedule): void {
        this.selectedScheduleForDetails = schedule;
        this.showDetailsModal = true;
    }

    closeSessionDetails(): void {
        this.showDetailsModal = false;
        this.selectedScheduleForDetails = null;
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

        if (!this.complaintSubject.trim() || !this.complaintDescription.trim()) {
            this.complaintError = 'Subject and description are required.';
            return;
        }

        const complainantEmail = this.authService.getUserEmail() || undefined;

        this.complaintError = null;
        this.complaintSuccess = null;
        this.isComplaintSubmitting = true;

        this.complaintService.createForSchedule(this.selectedScheduleForComplaint.scheduleId, {
            subject: this.complaintSubject.trim(),
            description: this.complaintDescription.trim(),
            status: false,
            complainantEmail,
            complainantRole: 'TUTOR'
        }).subscribe({
            next: () => {
                this.isComplaintSubmitting = false;
                this.closeComplaintModal();
                this.complaintSuccess = 'Complaint submitted successfully.';
                setTimeout(() => this.complaintSuccess = null, 3500);
            },
            error: () => {
                this.isComplaintSubmitting = false;
                this.complaintError = 'Failed to submit complaint.';
            }
        });
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

    private fetchSchedulesByTutorId(tutorId: number): void {
        this.tutorId = tutorId;
        this.scheduleService.getByProfessor(tutorId).subscribe({
            next: (data) => {
                this.schedules = data || [];
                this.applyWeekFilter();
                this.isLoading = false;
            },
            error: () => {
                this.error = 'Failed to load tutor schedules.';
                this.isLoading = false;
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
