// src/app/features/student/components/courses/student-courses.component.ts
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { forkJoin } from 'rxjs';
import { Course } from '../../../../core/models/course.model';
import { Enrollment } from '../../../../core/models/enrollment.model';
import { CourseService } from '../../../../core/services/course.service';
import { EnrollmentService } from '../../../../core/services/enrollment.service';
import { AuthService } from '../../../../core/services/auth.service';

@Component({
    selector: 'app-student-courses',
    templateUrl: './student-courses.component.html'
    ,
    styleUrls: ['./student-courses.component.scss']
})
export class StudentCoursesComponent implements OnInit {

    courses: Course[] = [];
    enrollments: Enrollment[] = [];
    enrolledCourseIds = new Set<number>();
    loading = true;
    error = '';
    searchTerm = '';

    constructor(
        private courseService: CourseService,
        private enrollmentService: EnrollmentService,
        private authService: AuthService,
        private router: Router
    ) { }

ngOnInit(): void {
    const userId = this.authService.getUserId();
    if (!userId) {
        this.error = 'Unable to identify user. Please log in again.';
        this.loading = false;
        return;
    }
    

    forkJoin({
        courses: this.courseService.getGeneralCourses(),  // only GENERAL_ENGLISH courses for students
        enrollments: this.enrollmentService.getByUserId(userId)
    }).subscribe({
        next: ({ courses, enrollments }) => {
                console.log('courses:', courses);
    console.log('enrollments:', enrollments);
    console.log('userId used:', userId);
            this.courses = courses;
            this.enrollments = enrollments;
            enrollments.forEach(e => {
                if (e.course?.courseId != null) {
                    this.enrolledCourseIds.add(e.course.courseId);
                }
            });
            this.loading = false;
        },
        error: () => {
            this.error = 'Failed to load courses. Please try again later.';
            this.loading = false;
        }
    });
}

    isEnrolled(course: Course): boolean {
        return course.courseId != null && this.enrolledCourseIds.has(course.courseId);
    }

    getEnrollment(course: Course): Enrollment | undefined {
        return this.enrollments.find(e => e.course?.courseId === course.courseId);
    }

    openCourse(course: Course): void {
        if (this.isEnrolled(course) && course.courseId != null) {
            this.router.navigate(['/student/courses', course.courseId]);
        }
    }

    getLevelColor(level: string): string {
        const map: Record<string, string> = {
            A1: '#4CAF50', A2: '#8BC34A',
            B1: '#2196F3', B2: '#03A9F4',
            C1: '#9C27B0', C2: '#673AB7'
        };
        return map[level] ?? '#006D77';
    }

    get filteredCourses(): Course[] {
        if (!this.searchTerm.trim()) {
            return this.courses;
        }
        const term = this.searchTerm.toLowerCase();
        return this.courses.filter(course =>
            course.title.toLowerCase().includes(term)
        );
    }
}
