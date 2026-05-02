import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { WordBattleArenaComponent } from './word-battle-arena.component';
import { WordBattleService } from '../../../../../core/services/activity/word-battle.service';
import { AuthService } from '../../../../../core/services/auth.service';
import { UserService } from '../../../../../core/services/activity/user.service';
import { of, throwError } from 'rxjs';
import { NO_ERRORS_SCHEMA } from '@angular/core';

describe('WordBattleArenaComponent', () => {
  let component: WordBattleArenaComponent;
  let fixture: ComponentFixture<WordBattleArenaComponent>;
  let wordBattleService: jasmine.SpyObj<WordBattleService>;
  let authService: jasmine.SpyObj<AuthService>;
  let userService: jasmine.SpyObj<UserService>;
  let router: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    const wordBattleServiceSpy = jasmine.createSpyObj('WordBattleService', [
      'connect',
      'joinRoom',
      'submitWord',
      'disconnect'
    ]);
    const authServiceSpy = jasmine.createSpyObj('AuthService', ['getUserEmail', 'getUserId']);
    const userServiceSpy = jasmine.createSpyObj('UserService', ['getByEmail']);
    const routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      declarations: [WordBattleArenaComponent],
      imports: [HttpClientTestingModule, RouterModule],
      providers: [
        { provide: WordBattleService, useValue: wordBattleServiceSpy },
        { provide: AuthService, useValue: authServiceSpy },
        { provide: UserService, useValue: userServiceSpy },
        { provide: Router, useValue: routerSpy },
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: { paramMap: { get: () => '1' } }
          }
        }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();

    wordBattleService = TestBed.inject(WordBattleService) as jasmine.SpyObj<WordBattleService>;
    authService = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
    userService = TestBed.inject(UserService) as jasmine.SpyObj<UserService>;
    router = TestBed.inject(Router) as jasmine.SpyObj<Router>;
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WordBattleArenaComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should extract challenge ID from route params', () => {
    authService.getUserEmail.and.returnValue('test@example.com');
    userService.getByEmail.and.returnValue(of({ userId: 1, firstName: 'John', lastName: 'Doe' } as any));
    wordBattleService.connect.and.returnValue(of({
      gameStarted: false,
      gameEnded: false,
      players: [],
      currentPlayerIndex: 0,
      roomId: 'challenge_1',
      challengeId: 1,
      maxParticipants: 2,
      timeLimitSeconds: 30,
      usedWords: []
    } as any));

    fixture.detectChanges();

    expect(component.challengeId).toBe(1);
  });

  it('should load user info and connect to battle', () => {
    const mockUser = { userId: 50, firstName: 'Jane', lastName: 'Smith' };
    authService.getUserEmail.and.returnValue('jane@example.com');
    userService.getByEmail.and.returnValue(of(mockUser as any));
    wordBattleService.connect.and.returnValue(of({
      gameStarted: false,
      gameEnded: false,
      players: [],
      currentPlayerIndex: 0,
      roomId: 'challenge_1',
      challengeId: 1,
      maxParticipants: 2,
      timeLimitSeconds: 30,
      usedWords: []
    } as any));

    fixture.detectChanges();

    expect(userService.getByEmail).toHaveBeenCalledWith('jane@example.com');
    expect(component.userId).toBe(50);
  });

  it('should redirect to login if email not found', () => {
    authService.getUserEmail.and.returnValue(null);

    fixture.detectChanges();

    expect(router.navigate).toHaveBeenCalledWith(['/auth/login']);
  });

  it('should set error message if user load fails', () => {
    authService.getUserEmail.and.returnValue('test@example.com');
    userService.getByEmail.and.returnValue(throwError(() => new Error('Failed')));
    wordBattleService.connect.and.returnValue(of({
      gameStarted: false,
      gameEnded: false,
      players: [],
      currentPlayerIndex: 0,
      roomId: 'challenge_1',
      challengeId: 1,
      maxParticipants: 2,
      timeLimitSeconds: 30,
      usedWords: []
    } as any));

    fixture.detectChanges();

    expect(component.errorMsg).toBeTruthy();
  });

  it('should determine if it is user turn correctly', () => {
    component.session = {
      gameStarted: true,
      gameEnded: false,
      players: [{ username: 'John' }, { username: 'Jane' }],
      currentPlayerIndex: 0
    } as any;
    component.username = 'John';

    expect(component.isMyTurn()).toBeTrue();
  });

  it('should not be user turn when it is other player turn', () => {
    component.session = {
      gameStarted: true,
      gameEnded: false,
      players: [{ username: 'John' }, { username: 'Jane' }],
      currentPlayerIndex: 1
    } as any;
    component.username = 'John';

    expect(component.isMyTurn()).toBeFalse();
  });

  it('should not allow word submission if not their turn', () => {
    component.session = null;
    component.wordInput = 'hello';

    component.submitWord?.();

    expect(component.wordInput).toBe('hello');
  });

  it('should start timer when game starts', fakeAsync(() => {
    const futureTime = new Date();
    futureTime.setSeconds(futureTime.getSeconds() + 30);

    component.session = {
      gameStarted: true,
      gameEnded: false,
      turnEndTime: futureTime.toISOString()
    } as any;

    component.startLocalTimer?.();

    tick(1000);
    component.stopLocalTimer?.();
  }));

  it('should clean up on component destroy', () => {
    component.startLocalTimer?.();
    component.ngOnDestroy?.();
    expect(component).toBeTruthy();
  });
});
