import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { StoryChainArenaComponent } from './story-chain-arena.component';
import { StoryChainService } from '../../../../../core/services/activity/story-chain.service';
import { AuthService } from '../../../../../core/services/auth.service';
import { UserService } from '../../../../../core/services/activity/user.service';
import { of, throwError } from 'rxjs';
import { NO_ERRORS_SCHEMA } from '@angular/core';

describe('StoryChainArenaComponent', () => {
  let component: StoryChainArenaComponent;
  let fixture: ComponentFixture<StoryChainArenaComponent>;
  let storyChainService: jasmine.SpyObj<StoryChainService>;
  let authService: jasmine.SpyObj<AuthService>;
  let userService: jasmine.SpyObj<UserService>;
  let router: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    const storyChainServiceSpy = jasmine.createSpyObj('StoryChainService', [
      'connect',
      'joinRoom',
      'submitSentence',
      'disconnect'
    ]);
    const authServiceSpy = jasmine.createSpyObj('AuthService', ['getUserEmail']);
    const userServiceSpy = jasmine.createSpyObj('UserService', ['getByEmail']);
    const routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      declarations: [StoryChainArenaComponent],
      imports: [HttpClientTestingModule, RouterModule],
      providers: [
        { provide: StoryChainService, useValue: storyChainServiceSpy },
        { provide: AuthService, useValue: authServiceSpy },
        { provide: UserService, useValue: userServiceSpy },
        { provide: Router, useValue: routerSpy },
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: { paramMap: { get: (key: string) => key === 'id' ? '2' : null } }
          }
        }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();

    storyChainService = TestBed.inject(StoryChainService) as jasmine.SpyObj<StoryChainService>;
    authService = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
    userService = TestBed.inject(UserService) as jasmine.SpyObj<UserService>;
    router = TestBed.inject(Router) as jasmine.SpyObj<Router>;
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StoryChainArenaComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should extract challenge ID from route and create room ID', () => {
    authService.getUserEmail.and.returnValue('test@example.com');
    userService.getByEmail.and.returnValue(of({ userId: 1 } as any));
    storyChainService.connect.and.returnValue(of({
      players: [],
      currentPlayerIndex: 0,
      currentStory: [],
      roomId: 'challenge_2',
      challengeId: 2,
      maxSentences: 10,
      minWordsPerSentence: 3,
      maxWordsPerSentence: 20,
      timeLimitSeconds: 60,
      allowVoting: false,
      gameStarted: false,
      gameEnded: false
    } as any));

    fixture.detectChanges();

    expect(component.challengeId).toBe(2);
  });

  it('should redirect to challenges if no ID in route', () => {
    (TestBed.inject(ActivatedRoute) as any).snapshot.paramMap.get = () => null;

    fixture.detectChanges();

    expect(router.navigate).toHaveBeenCalledWith(['/student/challenges']);
  });

  it('should load user and connect to arena', () => {
    const mockUser = { userId: 100, firstName: 'Alex', lastName: 'Brown' };
    authService.getUserEmail.and.returnValue('alex@example.com');
    userService.getByEmail.and.returnValue(of(mockUser as any));
    storyChainService.connect.and.returnValue(of({
      players: [],
      currentPlayerIndex: 0,
      currentStory: [],
      roomId: 'challenge_2',
      challengeId: 2,
      maxSentences: 10,
      minWordsPerSentence: 3,
      maxWordsPerSentence: 20,
      timeLimitSeconds: 60,
      allowVoting: false,
      gameStarted: false,
      gameEnded: false
    } as any));

    fixture.detectChanges();

    expect(component.userId).toBe(100);
  });

  it('should fallback to email prefix as username if name not available', () => {
    const mockUser = { userId: 50 };
    authService.getUserEmail.and.returnValue('writer@domain.com');
    userService.getByEmail.and.returnValue(of(mockUser as any));
    storyChainService.connect.and.returnValue(of({
      players: [],
      currentPlayerIndex: 0,
      currentStory: [],
      roomId: 'challenge_2',
      challengeId: 2,
      maxSentences: 10,
      minWordsPerSentence: 3,
      maxWordsPerSentence: 20,
      timeLimitSeconds: 60,
      allowVoting: false,
      gameStarted: false,
      gameEnded: false
    } as any));

    fixture.detectChanges();

    expect(component.userId).toBe(50);
  });

  it('should handle connection error', () => {
    authService.getUserEmail.and.returnValue('test@example.com');
    userService.getByEmail.and.returnValue(of({ userId: 1 } as any));
    storyChainService.connect.and.returnValue(throwError(() => new Error('Connection failed')));

    fixture.detectChanges();

    expect(component.errorMsg).toBe("WebSocket connection error.");
  });

  it('should handle user not found error', () => {
    authService.getUserEmail.and.returnValue('test@example.com');
    userService.getByEmail.and.returnValue(throwError(() => new Error('Not found')));

    fixture.detectChanges();

    expect(component.errorMsg).toBe("Unable to load profile.");
  });

  it('should redirect to login if no email found', () => {
    authService.getUserEmail.and.returnValue(null);

    fixture.detectChanges();

    expect(router.navigate).toHaveBeenCalledWith(['/auth/login']);
  });

  it('should start timer when game is active', fakeAsync(() => {
    const futureTime = new Date();
    futureTime.setSeconds(futureTime.getSeconds() + 60);

    component.session = {
      gameStarted: true,
      gameEnded: false,
      turnEndTime: futureTime.toISOString()
    } as any;

    component.startLocalTimer();

    tick(500);
    expect(component.timeLeft).toBeGreaterThan(0);
    expect(component.timeLeft).toBeLessThanOrEqual(60);
    component.stopLocalTimer();
  }));

  it('should cleanup on component destroy', () => {
    spyOn(component, 'stopLocalTimer');

    component.ngOnDestroy();

    expect(component.stopLocalTimer).toHaveBeenCalled();
  });
});
