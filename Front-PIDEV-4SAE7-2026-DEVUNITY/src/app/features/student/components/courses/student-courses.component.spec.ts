import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { StudentCoursesComponent } from './student-courses.component';
import { CourseService } from '../../../../core/services/course.service';
import { EnrollmentService } from '../../../../core/services/enrollment.service';
import { AuthService } from '../../../../core/services/auth.service';

describe('StudentCoursesComponent', () => {
  let component: StudentCoursesComponent;
  let fixture: ComponentFixture<StudentCoursesComponent>;
  let courseServiceSpy: jasmine.SpyObj<CourseService>;
  let enrollmentServiceSpy: jasmine.SpyObj<EnrollmentService>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    courseServiceSpy = jasmine.createSpyObj('CourseService', ['getGeneralCourses']);
    enrollmentServiceSpy = jasmine.createSpyObj('EnrollmentService', ['getByUserId']);
    authServiceSpy = jasmine.createSpyObj('AuthService', ['getUserId']);
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      declarations: [StudentCoursesComponent],
      providers: [
        { provide: CourseService, useValue: courseServiceSpy },
        { provide: EnrollmentService, useValue: enrollmentServiceSpy },
        { provide: AuthService, useValue: authServiceSpy },
        { provide: Router, useValue: routerSpy }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();

    fixture = TestBed.createComponent(StudentCoursesComponent);
    component = fixture.componentInstance;
  });

  it('should load courses and enrollments for authenticated user', () => {
    authServiceSpy.getUserId.and.returnValue(22);
    courseServiceSpy.getGeneralCourses.and.returnValue(of([
      {
        courseId: 1,
        title: 'A2',
        description: 'd',
        level: 'A2',
        type: 'General_English',
        price: 120,
        isHidden: false
      }
    ]));
    enrollmentServiceSpy.getByUserId.and.returnValue(of([
      { enrollmentId: 3, course: { courseId: 1 } as any }
    ]));

    component.ngOnInit();

    expect(component.loading).toBeFalse();
    expect(component.courses.length).toBe(1);
    expect(component.enrolledCourseIds.has(1)).toBeTrue();
    expect(component.error).toBe('');
  });

  it('should set error when user is missing', () => {
    authServiceSpy.getUserId.and.returnValue(null);

    component.ngOnInit();

    expect(component.loading).toBeFalse();
    expect(component.error).toContain('Unable to identify user');
    expect(courseServiceSpy.getGeneralCourses).not.toHaveBeenCalled();
  });

  it('should handle loading error from services', () => {
    authServiceSpy.getUserId.and.returnValue(22);
    courseServiceSpy.getGeneralCourses.and.returnValue(throwError(() => new Error('boom')));
    enrollmentServiceSpy.getByUserId.and.returnValue(of([]));

    component.ngOnInit();

    expect(component.loading).toBeFalse();
    expect(component.error).toContain('Failed to load courses');
  });

  it('should navigate to course details only if enrolled', () => {
    component.enrolledCourseIds.add(10);
    const enrolledCourse = { courseId: 10 } as any;
    const notEnrolledCourse = { courseId: 11 } as any;

    component.openCourse(enrolledCourse);
    component.openCourse(notEnrolledCourse);

    expect(routerSpy.navigate).toHaveBeenCalledOnceWith(['/student/courses', 10]);
  });
});
