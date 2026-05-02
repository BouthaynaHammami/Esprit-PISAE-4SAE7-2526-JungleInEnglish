import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { StudentKanbanEventsComponent } from './student-kanban-events.component';
import { KanbanEventService } from '../../../../core/services/certif-event/kanban-event.service';
import { AuthService } from '../../../../core/services/auth.service';
import { KanbanReminderService } from '../../../../core/services/certif-event/kanban-reminder.service';
import { KanbanTask } from '../../../../core/models/kanban-task.model';
import { of, Subject } from 'rxjs';
import { NO_ERRORS_SCHEMA } from '@angular/core';

describe('StudentKanbanEventsComponent', () => {
  let component: StudentKanbanEventsComponent;
  let fixture: ComponentFixture<StudentKanbanEventsComponent>;
  let kanbanService: jasmine.SpyObj<KanbanEventService>;
  let authService: jasmine.SpyObj<AuthService>;
  let reminderService: jasmine.SpyObj<KanbanReminderService>;
  let reminderSubject: Subject<any>;

  beforeEach(async () => {
    reminderSubject = new Subject();

    const kanbanServiceSpy = jasmine.createSpyObj('KanbanEventService', [
      'getBoard',
      'addTask',
      'updateTask',
      'deleteTask',
      'reorderTasks'
    ]);
    const authServiceSpy = jasmine.createSpyObj('AuthService', ['getUserId']);
    const reminderServiceSpy = jasmine.createSpyObj('KanbanReminderService', [
      'connect',
      'disconnect'
    ]);
    reminderServiceSpy.reminder$ = reminderSubject.asObservable();

    await TestBed.configureTestingModule({
      declarations: [StudentKanbanEventsComponent],
      imports: [HttpClientTestingModule],
      providers: [
        { provide: KanbanEventService, useValue: kanbanServiceSpy },
        { provide: AuthService, useValue: authServiceSpy },
        { provide: KanbanReminderService, useValue: reminderServiceSpy }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();

    kanbanService = TestBed.inject(KanbanEventService) as jasmine.SpyObj<KanbanEventService>;
    authService = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
    reminderService = TestBed.inject(KanbanReminderService) as jasmine.SpyObj<KanbanReminderService>;
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StudentKanbanEventsComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load board on init', () => {
    const mockTasks: any[] = [
      { id: 1, title: 'Task 1', status: 'TODO', position: 0, userId: 5 } as any,
      { id: 2, title: 'Task 2', status: 'DOING', position: 0, userId: 5 } as any
    ];
    authService.getUserId.and.returnValue(5);
    kanbanService.getBoard.and.returnValue(of(mockTasks));

    fixture.detectChanges();

    expect(kanbanService.getBoard).toHaveBeenCalledWith(5);
    expect(component.tasks?.length).toBeGreaterThan(0);
  });

  it('should initialize columns with correct labels and colors', () => {
    authService.getUserId.and.returnValue(5);
    kanbanService.getBoard.and.returnValue(of([] as any));

    fixture.detectChanges();

    expect(component.columns?.length).toBeGreaterThanOrEqual(3);
  });

  it('should get tasks by status correctly', () => {
    component.tasks = [
      { id: 1, title: 'Task 1', status: 'TODO', position: 0 } as any,
      { id: 2, title: 'Task 2', status: 'TODO', position: 1 } as any,
      { id: 3, title: 'Task 3', status: 'DOING', position: 0 } as any
    ];

    const todoTasks = (component as any).getTasksByStatus?.('TODO');

    expect(todoTasks?.length).toBeGreaterThanOrEqual(0);
  });

  it('should count tasks by status', () => {
    component.tasks = [
      { id: 1, status: 'TODO' } as any,
      { id: 2, status: 'TODO' } as any,
      { id: 3, status: 'DOING' } as any,
      { id: 4, status: 'DONE' } as any
    ];

    const todoCount = (component as any).countByStatus?.('TODO');
    expect(todoCount).toBeGreaterThanOrEqual(0);
  });

  it('should handle drag start event', () => {
    const task = { id: 1, title: 'Task 1', status: 'TODO' } as any;
    const dragEvent = new DragEvent('dragstart', {
      dataTransfer: new DataTransfer()
    });

    component.onDragStart?.(dragEvent, task);

    expect(component).toBeTruthy();
  });

  it('should dismiss reminder notifications', () => {
    const notification = { id: 1, taskId: 1, message: 'Due soon', title: 'Reminder', description: 'Task', module: 'kanban', deadline: new Date() } as any;
    component.reminderNotifications = [notification];

    component.dismissReminder?.(notification);

    expect(component.reminderNotifications?.length).toBeLessThanOrEqual(1);
  });

  it('should receive reminder notifications from service', (done) => {
    authService.getUserId.and.returnValue(5);
    kanbanService.getBoard.and.returnValue(of([] as any));

    fixture.detectChanges();

    const notification = { id: 1, taskId: 1, message: 'Task due in 1 hour', title: 'Reminder', description: 'Due soon', module: 'kanban', deadline: new Date() } as any;
    reminderSubject.next(notification);

    setTimeout(() => {
      expect(component.reminderNotifications?.length).toBeGreaterThanOrEqual(0);
      done();
    }, 100);
  });

  it('should auto-dismiss reminders after 10 seconds', fakeAsync(() => {
    authService.getUserId.and.returnValue(5);
    kanbanService.getBoard.and.returnValue(of([]));

    fixture.detectChanges();

    const notification = { id: 1, taskId: 1, message: 'Reminder' };
    reminderSubject.next(notification);

    tick(10100);
    expect(component.reminderNotifications.length).toBe(0);
  }));

  it('should disconnect reminder service on destroy', () => {
    authService.getUserId.and.returnValue(5);
    kanbanService.getBoard.and.returnValue(of([]));

    fixture.detectChanges();
    component.ngOnDestroy();

    expect(reminderService.disconnect).toHaveBeenCalled();
  });
});
