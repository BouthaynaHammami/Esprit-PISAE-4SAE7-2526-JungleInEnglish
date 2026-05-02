import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { CourseService } from './course.service';
import { Course } from '../models/course.model';

describe('CourseService', () => {
  let service: CourseService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [CourseService]
    });
    service = TestBed.inject(CourseService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should fetch general english courses', () => {
    const mock: Course[] = [
      {
        courseId: 1,
        title: 'A1 Basics',
        description: 'desc',
        level: 'A1',
        type: 'General_English',
        price: 100,
        isHidden: false
      }
    ];

    service.getGeneralCourses().subscribe(data => {
      expect(data).toEqual(mock);
    });

    const req = httpMock.expectOne(request => request.url.includes('/type/GENERAL_ENGLISH'));
    expect(req.request.method).toBe('GET');
    req.flush(mock);
  });

  it('should toggle course visibility', () => {
    const mock = {
      courseId: 1,
      title: 'A1 Basics',
      description: 'desc',
      level: 'A1',
      type: 'General_English',
      price: 100,
      isHidden: true
    } as Course;

    service.toggleVisibility(1).subscribe(data => {
      expect(data.isHidden).toBeTrue();
    });

    const req = httpMock.expectOne(request => request.url.includes('/hide/1'));
    expect(req.request.method).toBe('PUT');
    req.flush(mock);
  });
});
