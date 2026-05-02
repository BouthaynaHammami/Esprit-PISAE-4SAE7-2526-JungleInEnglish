import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Schedule } from '../../../../../core/models/schedule.model';
import { ScheduleService } from '../../../../../core/services/schedule.service';
import { ScheduleReportService } from '../../../../../core/services/schedule-report.service';
import { LearnerUserLookupService } from '../../../../../core/services/learner-user-lookup.service';
import { CourseService } from '../../../../../core/services/course.service';
import { ScheduleFormComponent } from './schedule-form.component';
import { map, Observable, switchMap } from 'rxjs';

@Component({
  selector: 'app-schedule-list',
  standalone: true,
  imports: [CommonModule, FormsModule, ScheduleFormComponent],
  templateUrl: './schedule-list.component.html'
})
export class ScheduleListComponent implements OnInit {
  schedules: Schedule[] = [];
  filteredSchedules: Schedule[] = [];
  isLoading = true;
  error: string | null = null;
  successMessage: string | null = null;
  showForm = false;

  // Combined filters
  filterWeekStart = '';
  filterProfessorEmail = '';
  filterRoomName = '';
  filterClassName = '';
  filterSearch = '';

  reportTarget: 'tutor' | 'student' = 'tutor';
  reportTargetEmail = '';
  reportWeekStart = '';
  weeklySchedules: Schedule[] = [];
  selectedScheduleIds = new Set<number>();
  isWeekLoading = false;
  isPdfLoading = false;
  courseTitleById: Record<number, string> = {};

  constructor(
    private scheduleService: ScheduleService,
    private scheduleReportService: ScheduleReportService,
    private learnerUserLookupService: LearnerUserLookupService,
    private courseService: CourseService
  ) { }

  ngOnInit(): void {
    this.loadCourseCatalog();
    this.load();
  }

  load(): void {
    this.isLoading = true;
    this.error = null;
    this.scheduleService.getAll().subscribe({
      next: data => {
        this.schedules = data;
        this.applyFilter();
        this.isLoading = false;
      },
      error: () => { this.error = 'Failed to load schedules.'; this.isLoading = false; }
    });
  }

  applyFilter(): void {
    const professorEmail = this.filterProfessorEmail.trim();
    if (!professorEmail) {
      this.filteredSchedules = this.applyLocalFilters(this.schedules);
      return;
    }

    this.isLoading = true;
    this.resolveUserIdByEmail(professorEmail).subscribe({
      next: (tutorId) => {
        this.filteredSchedules = this.applyLocalFilters(this.schedules, tutorId);
        this.isLoading = false;
      },
      error: () => {
        this.error = 'Professor email is invalid.';
        this.filteredSchedules = [];
        this.isLoading = false;
      }
    });
  }

  clearFilter(): void {
    this.filterWeekStart = '';
    this.filterProfessorEmail = '';
    this.filterRoomName = '';
    this.filterClassName = '';
    this.filterSearch = '';
    this.filteredSchedules = [...this.schedules];
  }

  onSaved(): void {
    this.showForm = false;
    this.load();
    this.flash('Schedule created successfully!');
  }

  onCancelled(): void { this.showForm = false; }

  delete(s: Schedule): void {
    if (!confirm(`Delete schedule "${s.title}"?`)) return;
    this.scheduleService.delete(s.scheduleId!).subscribe({
      next: () => {
        this.schedules = this.schedules.filter(x => x.scheduleId !== s.scheduleId);
        this.applyFilter();
        this.flash('Schedule deleted.');
      },
      error: () => { this.error = 'Failed to delete.'; }
    });
  }

  formatDate(dt: string): string {
    if (!dt) return '-';
    return new Date(dt).toLocaleString();
  }

  getCourseTitle(courseId: number): string {
    return this.courseTitleById[courseId] || 'Unknown course';
  }

  loadWeekSchedules(): void {
    const reportTargetEmail = this.reportTargetEmail.trim();
    if (!reportTargetEmail || !this.reportWeekStart) {
      this.error = 'Please provide target email and week start date.';
      return;
    }

    this.error = null;
    this.isWeekLoading = true;

    this.resolveUserIdByEmail(reportTargetEmail).pipe(
      switchMap((targetUserId) => this.reportTarget === 'tutor'
        ? this.scheduleReportService.getTutorWeekSchedules(targetUserId, this.reportWeekStart)
        : this.scheduleReportService.getStudentWeekSchedules(targetUserId, this.reportWeekStart)
      )
    ).subscribe({
      next: (data) => {
        this.weeklySchedules = data || [];
        this.selectedScheduleIds = new Set(
          this.weeklySchedules
            .map(s => s.scheduleId)
            .filter((id): id is number => !!id)
        );
        this.isWeekLoading = false;
      },
      error: (err) => {
        this.error = typeof err?.error === 'string' ? err.error : 'Failed to load weekly schedules. Email may be invalid.';
        this.weeklySchedules = [];
        this.selectedScheduleIds.clear();
        this.isWeekLoading = false;
      }
    });
  }

  toggleScheduleSelection(scheduleId: number, checked: boolean): void {
    if (checked) {
      this.selectedScheduleIds.add(scheduleId);
      return;
    }
    this.selectedScheduleIds.delete(scheduleId);
  }

  downloadSelectedWeekPdf(): void {
    const reportTargetEmail = this.reportTargetEmail.trim();
    if (!reportTargetEmail || !this.reportWeekStart) {
      this.error = 'Please provide target email and week start date.';
      return;
    }

    const scheduleIds = Array.from(this.selectedScheduleIds);
    if (scheduleIds.length === 0) {
      this.error = 'Select at least one schedule to export.';
      return;
    }

    this.error = null;
    this.isPdfLoading = true;

    this.resolveUserIdByEmail(reportTargetEmail).pipe(
      switchMap((targetUserId) => this.reportTarget === 'tutor'
        ? this.scheduleReportService.exportTutorWeekPdf(targetUserId, this.reportWeekStart, scheduleIds)
        : this.scheduleReportService.exportStudentWeekPdf(targetUserId, this.reportWeekStart, scheduleIds)
      )
    ).subscribe({
      next: (blob) => {
        const target = this.reportTarget;
        const safeEmail = reportTargetEmail.replace(/[^a-zA-Z0-9._-]/g, '_');
        const filename = `${target}-${safeEmail}-week-${this.reportWeekStart}.pdf`;
        this.saveBlob(blob, filename);
        this.flash('Weekly PDF generated successfully!');
        this.isPdfLoading = false;
      },
      error: (err) => {
        this.error = typeof err?.error === 'string' ? err.error : 'Failed to generate PDF.';
        this.isPdfLoading = false;
      }
    });
  }

  private resolveUserIdByEmail(email: string): Observable<number> {
    return this.learnerUserLookupService.getByEmail(email).pipe(
      map(user => {
        const id = Number(user?.userId);
        if (!id) {
          throw new Error('Invalid user email');
        }
        return id;
      })
    );
  }

  private applyLocalFilters(source: Schedule[], tutorId?: number): Schedule[] {
    const weekStart = this.filterWeekStart.trim();
    const roomName = this.filterRoomName.trim().toLowerCase();
    const className = this.filterClassName.trim().toLowerCase();
    const search = this.filterSearch.trim().toLowerCase();

    let filtered = [...source];

    if (weekStart) {
      const start = new Date(`${weekStart}T00:00:00`);
      const end = new Date(start);
      end.setDate(end.getDate() + 7);
      filtered = filtered.filter((s) => {
        const scheduleStart = new Date(s.startTime);
        return scheduleStart >= start && scheduleStart < end;
      });
    }

    if (tutorId) {
      filtered = filtered.filter((s) => Number(s.userId) === tutorId);
    }

    if (roomName) {
      filtered = filtered.filter((s) => (s.room?.name || '').toLowerCase() === roomName);
    }

    if (className) {
      filtered = filtered.filter((s) => (s.classEntity?.name || '').toLowerCase() === className);
    }

    if (search) {
      filtered = filtered.filter((s) => {
        const text = [
          s.title,
          s.type,
          this.getCourseTitle(s.courseId),
          String(s.userId ?? ''),
          String(s.room?.name ?? ''),
          String(s.classEntity?.name ?? ''),
          String(s.classEntity?.level ?? '')
        ].join(' ').toLowerCase();
        return text.includes(search);
      });
    }

    return filtered.sort((a, b) => new Date(a.startTime).getTime() - new Date(b.startTime).getTime());
  }

  private saveBlob(blob: Blob, filename: string): void {
    const url = window.URL.createObjectURL(blob);
    const anchor = document.createElement('a');
    anchor.href = url;
    anchor.download = filename;
    anchor.click();
    window.URL.revokeObjectURL(url);
  }

  private flash(msg: string): void {
    this.successMessage = msg;
    setTimeout(() => this.successMessage = null, 3500);
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
}
