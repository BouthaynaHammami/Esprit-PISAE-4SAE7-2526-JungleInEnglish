import { HTTP_INTERCEPTORS, HttpClient } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { AuthInterceptor } from './auth.interceptor';
import { AuthService } from '../services/auth.service';

describe('AuthInterceptor', () => {
  let http: HttpClient;
  let httpMock: HttpTestingController;
  let authServiceSpy: jasmine.SpyObj<AuthService>;

  beforeEach(() => {
    authServiceSpy = jasmine.createSpyObj('AuthService', ['getToken', 'isLoggedIn', 'logout']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
        {
          provide: HTTP_INTERCEPTORS,
          useClass: AuthInterceptor,
          multi: true
        }
      ]
    });

    http = TestBed.inject(HttpClient);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should attach bearer token for non-auth requests', () => {
    authServiceSpy.getToken.and.returnValue('jwt-token');
    authServiceSpy.isLoggedIn.and.returnValue(true);

    http.get('/api/data').subscribe();

    const req = httpMock.expectOne('/api/data');
    expect(req.request.headers.get('Authorization')).toBe('Bearer jwt-token');
    req.flush({});
  });

  it('should not attach token for login endpoint', () => {
    authServiceSpy.getToken.and.returnValue('jwt-token');
    authServiceSpy.isLoggedIn.and.returnValue(true);

    http.post('/auth/login', {}).subscribe();

    const req = httpMock.expectOne('/auth/login');
    expect(req.request.headers.has('Authorization')).toBeFalse();
    req.flush({});
  });

  it('should logout on 401 for non-optional endpoint', () => {
    authServiceSpy.getToken.and.returnValue('jwt-token');
    authServiceSpy.isLoggedIn.and.returnValue(true);

    http.get('/api/secure').subscribe({ error: () => {} });

    const req = httpMock.expectOne('/api/secure');
    req.flush({}, { status: 401, statusText: 'Unauthorized' });

    expect(authServiceSpy.logout).toHaveBeenCalled();
  });

  it('should not logout on 401 for notifications endpoint', () => {
    authServiceSpy.getToken.and.returnValue('jwt-token');
    authServiceSpy.isLoggedIn.and.returnValue(true);

    http.get('/api/notifications').subscribe({ error: () => {} });

    const req = httpMock.expectOne('/api/notifications');
    req.flush({}, { status: 401, statusText: 'Unauthorized' });

    expect(authServiceSpy.logout).not.toHaveBeenCalled();
  });
});
