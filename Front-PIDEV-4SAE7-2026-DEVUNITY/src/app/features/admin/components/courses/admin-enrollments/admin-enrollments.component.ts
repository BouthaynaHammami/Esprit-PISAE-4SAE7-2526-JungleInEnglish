import { Component, OnInit } from '@angular/core';
import { EnrollmentService } from '../../../../../core/services/enrollment.service';
import { CourseService } from '../../../../../core/services/course.service';
import { LearnerUserLookupService } from '../../../../../core/services/learner-user-lookup.service';
import { Enrollment } from '../../../../../core/models/enrollment.model';
import { Course } from '../../../../../core/models/course.model';

@Component({
  selector: 'app-admin-enrollments',
  templateUrl: './admin-enrollments.component.html',
  styleUrls: ['./admin-enrollments.component.css']
})
export class AdminEnrollmentsComponent implements OnInit {
  pageTitle: string = 'Enrollments Management';
  pageIcon: string = '🎓';
  enrollments: Enrollment[] = [];
  courses: Course[] = [];
  isLoading = true;
  error: string | null = null;
  successMessage: string | null = null;

  // Filters & Search
  searchTerm: string = '';
  filterCourseId: number | null = null;

  selectedEnrollment: Enrollment | null = null;
  isViewModalOpen: boolean = false;
  showForm = false;
  isSaving = false;

  // For new enrollment
  newUserEmail = '';
  newCourseId: number | null = null;
  userEmailById: Record<number, string> = {};

  constructor(
    private enrollmentService: EnrollmentService,
    private courseService: CourseService,
    private learnerUserLookupService: LearnerUserLookupService
  ) { }

  ngOnInit(): void { this.load(); }

  viewEnrollment(e: Enrollment): void {
    this.selectedEnrollment = e;
    this.isViewModalOpen = true;
  }

  load(): void {
    this.isLoading = true;
    this.error = null;

    this.courseService.getAll().subscribe({
      next: data => this.courses = data,
      error: () => { }
    });

    const obs = this.filterCourseId
      ? this.enrollmentService.getByCourseId(this.filterCourseId)
      : this.enrollmentService.getAll();

    obs.subscribe({
      next: data => {
        this.enrollments = data;
        this.hydrateUserEmails();
        this.isLoading = false;
      },
      error: () => { this.error = 'Failed to load enrollments.'; this.isLoading = false; }
    });
  }

  applyFilter(): void { this.load(); }

  clearFilter(): void { this.filterCourseId = null; this.load(); }

  openAdd(): void {
    this.newUserEmail = '';
    this.newCourseId = null;
    this.showForm = true;
    this.error = null;
  }

  cancel(): void { this.showForm = false; }

  save(): void {
    const email = this.newUserEmail.trim();
    if (!email) { this.error = 'User email is required.'; return; }
    if (!this.newCourseId) { this.error = 'Course is required.'; return; }
    this.isSaving = true;
    this.error = null;

    this.learnerUserLookupService.getByEmail(email).subscribe({
      next: (user) => {
        const userId = Number(user?.userId);
        if (!userId) {
          this.error = 'User email was not found.';
          this.isSaving = false;
          return;
        }

        this.enrollmentService.enroll(userId, this.newCourseId!).subscribe({
          next: () => {
            this.isSaving = false;
            this.showForm = false;
            this.load();
            this.showSuccess('Student enrolled successfully!');
          },
          error: () => { this.error = 'Failed to enroll student.'; this.isSaving = false; }
        });
      },
      error: () => {
        this.error = 'User email was not found.';
        this.isSaving = false;
      }
    });
  }

  delete(e: Enrollment): void {
    if (!confirm('Remove this enrollment? The student will lose access.')) return;
    this.enrollmentService.delete(e.enrollmentId!).subscribe({
      next: () => {
        this.enrollments = this.enrollments.filter(x => x.enrollmentId !== e.enrollmentId);
        this.showSuccess('Enrollment removed.');
      },
      error: () => { this.error = 'Failed to delete enrollment.'; }
    });
  }

  getCourseName(courseId: number | undefined): string {
    if (!courseId) return '—';
    return this.courses.find(c => c.courseId === courseId)?.title ?? '—';
  }

  getUserEmail(userId: number | undefined): string {
    if (!userId) {
      return '—';
    }
    return this.userEmailById[userId] ?? `User #${userId}`;
  }

  private hydrateUserEmails(): void {
    const ids = Array.from(new Set(this.enrollments.map(e => e.userId).filter((id): id is number => !!id)));

    ids.forEach((id) => {
      if (this.userEmailById[id]) {
        return;
      }

      this.learnerUserLookupService.getById(id).subscribe({
        next: (user) => {
          this.userEmailById[id] = user?.email || `User #${id}`;
        },
        error: () => {
          this.userEmailById[id] = `User #${id}`;
        }
      });
    });
  }

  private showSuccess(msg: string): void {
    this.successMessage = msg;
    this.error = null;
    setTimeout(() => this.successMessage = null, 3000);
  }
}