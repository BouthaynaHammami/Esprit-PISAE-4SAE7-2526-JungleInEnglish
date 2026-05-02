import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { RecruitmentService } from './recruitment.service';
import { Recruitment, Interview, Applicant } from '../models/recruitment.model';

describe('RecruitmentService', () => {
  let service: RecruitmentService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [RecruitmentService]
    });
    service = TestBed.inject(RecruitmentService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should fetch recruitments list', () => {
    const mock: Recruitment[] = [{ id: 1, positionTitle: 'Tutor', department: 'Language' }];

    service.getAllRecruitments().subscribe(data => {
      expect(data).toEqual(mock);
    });

    const req = httpMock.expectOne(request => request.url.endsWith('/recruitments'));
    expect(req.request.method).toBe('GET');
    req.flush(mock);
  });

  it('should create an interview', () => {
    const payload: Interview = { id: 5, title: 'HR Screening' };

    service.createInterview(payload).subscribe(data => {
      expect(data).toEqual(payload);
    });

    const req = httpMock.expectOne(request => request.url.endsWith('/interviews'));
    expect(req.request.method).toBe('POST');
    req.flush(payload);
  });

  it('should fetch applicants by user', () => {
    const mock: Applicant[] = [{ id: 4, firstName: 'Ali' }];

    service.getApplicantsByUser(12).subscribe(data => {
      expect(data).toEqual(mock);
    });

    const req = httpMock.expectOne(request => request.url.endsWith('/applicants/user/12'));
    expect(req.request.method).toBe('GET');
    req.flush(mock);
  });
});
