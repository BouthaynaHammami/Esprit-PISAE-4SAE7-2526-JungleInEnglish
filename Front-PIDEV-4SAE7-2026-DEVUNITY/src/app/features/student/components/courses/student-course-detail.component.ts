import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { forkJoin } from 'rxjs';
import { Course } from '../../../../core/models/course.model';
import { Lesson } from '../../../../core/models/lesson.model';
import { Enrollment } from '../../../../core/models/enrollment.model';
import { CourseService } from '../../../../core/services/course.service';
import { LessonService } from '../../../../core/services/lesson.service';
import { EnrollmentService } from '../../../../core/services/enrollment.service';
import { AuthService } from '../../../../core/services/auth.service';

@Component({
    selector: 'app-student-course-detail',
    templateUrl: './student-course-detail.component.html',
    styleUrls: ['./student-course-detail.component.scss']
})
export class StudentCourseDetailComponent implements OnInit {

    course: Course | null = null;
    lessons: Lesson[] = [];
    enrollment: Enrollment | null = null;
    selectedLesson: Lesson | null = null;
    loading = true;
    error = '';
    userId!: number;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private courseService: CourseService,
        private lessonService: LessonService,
        private enrollmentService: EnrollmentService,
        private authService: AuthService
    ) { }

    ngOnInit(): void {
        const courseId = Number(this.route.snapshot.paramMap.get('id'));
        this.userId = this.authService.getUserId()!;

        if (!courseId || !this.userId) {
            this.error = 'Invalid course or user session.';
            this.loading = false;
            return;
        }

        forkJoin({
            course: this.courseService.getById(courseId),
            lessons: this.lessonService.getByCourseId(courseId),
            enrollments: this.enrollmentService.getByUserId(this.userId)
        }).subscribe({
            next: ({ course, lessons, enrollments }) => {
                this.course = course;
                this.lessons = lessons.sort((a, b) => (a.order ?? 0) - (b.order ?? 0));
                this.enrollment = enrollments.find(e => e.course?.courseId === courseId) ?? null;
                if (this.lessons.length > 0) this.selectedLesson = this.lessons[0];
                this.loading = false;
            },
            error: () => { this.error = 'Failed to load course details.'; this.loading = false; }
        });
    }

    startQuiz(): void {
        if (this.course?.quiz?.quizId) {
            this.router.navigate(['/student/quiz', this.course.quiz.quizId], {
                queryParams: { userId: this.userId }
            });
        }
    }

    selectLesson(lesson: Lesson): void { this.selectedLesson = lesson; }
    goBack(): void { this.router.navigate(['/student/courses']); }

    getLevelColor(level?: string): string {
        const map: Record<string, string> = {
            A1: '#4CAF50', A2: '#8BC34A',
            B1: '#2196F3', B2: '#03A9F4',
            C1: '#9C27B0', C2: '#673AB7'
        };
        return level ? (map[level] ?? '#006D77') : '#006D77';
    }

    getProgressLabel(progress?: number): string {
        if (!progress || progress === 0) return 'Just started';
        if (progress < 30) return 'Beginner';
        if (progress < 70) return 'Intermediate';
        if (progress < 100) return 'Almost there!';
        return 'Completed ✓';
    }

    formatDate(dateStr?: string): string {
        if (!dateStr) return '—';
        return new Date(dateStr).toLocaleDateString('en-GB', {
            day: '2-digit', month: 'long', year: 'numeric'
        });
    }
}