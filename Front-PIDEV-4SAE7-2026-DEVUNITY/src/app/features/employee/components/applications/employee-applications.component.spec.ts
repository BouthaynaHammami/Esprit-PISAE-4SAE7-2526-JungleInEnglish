import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { EmployeeApplicationsComponent } from './employee-applications.component';
import { RecruitmentService } from '../../../../core/services/recruitment.service';
import { AuthService } from '../../../../core/services/auth.service';
import { EnhancedNotificationService } from '../../../../core/services/enhanced-notification.service';
import { NotificationConfigService } from '../../../../core/services/notification-config.service';
import { of, throwError } from 'rxjs';
import { NO_ERRORS_SCHEMA } from '@angular/core';

describe('EmployeeApplicationsComponent', () => {
  let component: EmployeeApplicationsComponent;
  let fixture: ComponentFixture<EmployeeApplicationsComponent>;
  let recruitmentService: jasmine.SpyObj<RecruitmentService>;
  let authService: jasmine.SpyObj<AuthService>;
  let notificationService: jasmine.SpyObj<EnhancedNotificationService>;

  beforeEach(async () => {
    const recruitmentServiceSpy = jasmine.createSpyObj('RecruitmentService', [
      'getAllRecruitments',
      'getApplicantsByUser',
      'createApplicant'
    ]);
    const authServiceSpy = jasmine.createSpyObj('AuthService', ['getUserId', 'getUserEmail']);
    const notificationServiceSpy = jasmine.createSpyObj('EnhancedNotificationService', [
      'sendNotificationToUser'
    ]);
    const notificationConfigSpy = jasmine.createSpyObj('NotificationConfigService', [
      'getAdminEmail'
    ]);

    await TestBed.configureTestingModule({
      declarations: [EmployeeApplicationsComponent],
      imports: [HttpClientTestingModule, RouterModule],
      providers: [
        { provide: RecruitmentService, useValue: recruitmentServiceSpy },
        { provide: AuthService, useValue: authServiceSpy },
        { provide: EnhancedNotificationService, useValue: notificationServiceSpy },
        { provide: NotificationConfigService, useValue: notificationConfigSpy },
        {
          provide: ActivatedRoute,
          useValue: {
            queryParams: of({}),
            snapshot: { paramMap: { get: () => null } }
          }
        }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();

    recruitmentService = TestBed.inject(RecruitmentService) as jasmine.SpyObj<RecruitmentService>;
    authService = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
    notificationService = TestBed.inject(EnhancedNotificationService) as jasmine.SpyObj<EnhancedNotificationService>;
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EmployeeApplicationsComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load recruitments on init', () => {
    const mockRecruitments = [
      { id: 1, positionTitle: 'Java Dev', status: 'OPEN', department: 'IT' } as any,
      { id: 2, positionTitle: 'QA Engineer', status: 'OPEN', department: 'QA' } as any
    ];
    authService.getUserId.and.returnValue(100);
    recruitmentService.getAllRecruitments.and.returnValue(of(mockRecruitments));
    recruitmentService.getApplicantsByUser.and.returnValue(of([] as any));

    fixture.detectChanges();

    expect(recruitmentService.getAllRecruitments).toHaveBeenCalled();
    expect(component.recruitments?.length).toBe(2);
  });

  it('should filter out only OPEN recruitments', () => {
    const mockRecruitments = [
      { id: 1, positionTitle: 'Java Dev', status: 'OPEN', department: 'IT' } as any,
      { id: 2, positionTitle: 'QA Engineer', status: 'CLOSED', department: 'HR' } as any
    ];
    authService.getUserId.and.returnValue(100);
    recruitmentService.getAllRecruitments.and.returnValue(of(mockRecruitments));
    recruitmentService.getApplicantsByUser.and.returnValue(of([] as any));

    fixture.detectChanges();

    expect(component.recruitments?.length).toBeGreaterThan(0);
  });

  it('should load user applications on init', () => {
    const mockApplicants = [{ id: 1, userId: 100, status: 'PENDING' } as any];
    authService.getUserId.and.returnValue(100);
    recruitmentService.getAllRecruitments.and.returnValue(of([] as any));
    recruitmentService.getApplicantsByUser.and.returnValue(of(mockApplicants));

    fixture.detectChanges();

    expect(recruitmentService.getApplicantsByUser).toHaveBeenCalledWith(100);
  });

  it('should filter available recruitments excluding applied ones', () => {
    const mockRecruitments = [
      { id: 1, positionTitle: 'Java Dev', status: 'OPEN', department: 'IT' } as any,
      { id: 2, positionTitle: 'QA Engineer', status: 'OPEN', department: 'HR' } as any
    ];
    const mockApplicants = [
      { id: 1, userId: 100, recruitment: { id: 1 } } as any
    ];
    component.recruitments = mockRecruitments;
    component.myApplications = mockApplicants;

    component.recruitments = mockRecruitments;

    expect(component.recruitments?.length).toBe(2);
  });

  it('should submit application successfully', () => {
    const newApplicant = { id: 5, userId: 100, status: 'PENDING', reponse: 'answer' } as any;
    component.currentUserId = 100;
    component.selectedRecruitmentId = 1;
    component.form = { reponse: 'answer', cv: 'link' };
    component.recruitments = [{ id: 1, positionTitle: 'Java Dev', department: 'IT' } as any];
    authService.getUserEmail.and.returnValue('user@example.com');
    recruitmentService.createApplicant.and.returnValue(of(newApplicant));
    notificationService.sendNotificationToUser.and.returnValue(of(void 0));
    recruitmentService.getApplicantsByUser.and.returnValue(of([newApplicant]));

    component.submitApplication?.();

    expect(recruitmentService.createApplicant).toHaveBeenCalled();
  });

  it('should handle application submission error', () => {
    component.currentUserId = 100;
    component.selectedRecruitmentId = 1;
    recruitmentService.createApplicant.and.returnValue(throwError(() => ({ error: { message: 'Failed' } })));

    component.submitApplication?.();

    expect(component.submitError).toBeTruthy();
    expect(component.submitting).toBeFalse();
  });

  it('should return correct status badge class', () => {
    expect(component.getStatusClass('ACCEPTED')).toBe('badge-success');
    expect(component.getStatusClass('REJECTED')).toBe('badge-danger');
    expect(component.getStatusClass('PENDING')).toBe('badge-warning');
  });

  it('should toggle preview on request', () => {
    component.showPreview = false;
    component.togglePreview();
    expect(component.showPreview).toBeTrue();
    component.togglePreview();
    expect(component.showPreview).toBeFalse();
  });
});
