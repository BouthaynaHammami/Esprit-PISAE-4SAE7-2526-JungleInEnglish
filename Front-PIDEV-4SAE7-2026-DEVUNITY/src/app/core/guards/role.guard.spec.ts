import { TestBed } from '@angular/core/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { RoleGuard } from './role.guard';
import { AuthService } from '../services/auth.service';

describe('RoleGuard', () => {
  let guard: RoleGuard;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(() => {
    authServiceSpy = jasmine.createSpyObj('AuthService', ['getUserRole']);
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    TestBed.configureTestingModule({
      providers: [
        RoleGuard,
        { provide: AuthService, useValue: authServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    });

    guard = TestBed.inject(RoleGuard);
  });

  it('should allow activation when role matches route role', () => {
    const route = ({ data: { role: 'ADMIN' } } as unknown) as ActivatedRouteSnapshot;
    authServiceSpy.getUserRole.and.returnValue('ADMIN' as any);

    const canActivate = guard.canActivate(route);

    expect(canActivate).toBeTrue();
    expect(routerSpy.navigate).not.toHaveBeenCalled();
  });

  it('should redirect to unauthorized when role does not match', () => {
    const route = ({ data: { role: 'ADMIN' } } as unknown) as ActivatedRouteSnapshot;
    authServiceSpy.getUserRole.and.returnValue('STUDENT' as any);

    const canActivate = guard.canActivate(route);

    expect(canActivate).toBeFalse();
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/unauthorized']);
  });
});
