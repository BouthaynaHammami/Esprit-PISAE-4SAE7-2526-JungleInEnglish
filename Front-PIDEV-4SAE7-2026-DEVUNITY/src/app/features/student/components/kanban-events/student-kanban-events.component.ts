import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { KanbanEventService } from '../../../../core/services/certif-event/kanban-event.service';
import { AuthService } from '../../../../core/services/auth.service';
import { KanbanReminderService } from '../../../../core/services/certif-event/kanban-reminder.service';
import { KanbanTask, KanbanStatus, ReminderNotification } from '../../../../core/models/kanban-task.model';

@Component({
  selector: 'app-student-kanban-events',
  templateUrl: './student-kanban-events.component.html',
  styleUrls: ['./student-kanban-events.component.scss']
})
export class StudentKanbanEventsComponent implements OnInit, OnDestroy {

  columns: { status: KanbanStatus; title: string; tasks: KanbanTask[] }[] = [
    { status: 'TODO', title: 'To Do', tasks: [] },
    { status: 'DOING', title: 'In Progress', tasks: [] },
    { status: 'DONE', title: 'Done', tasks: [] }
  ];

  tasks: KanbanTask[] = [];
  userId!: number;
  loading = true;

  // View management
  activeView: 'board' | 'stats' = 'board';

  // Form management
  showAddForm = false;
  editingTask: KanbanTask | null = null;
  formTitle = '';
  formDescription = '';
  formPriority: 'LOW' | 'MEDIUM' | 'HIGH' = 'MEDIUM';
  formDeadline = '';

  // Alert system
  showAlert = false;
  alertType: 'success' | 'info' | 'error' = 'info';
  alertMessage = '';

  draggedTask: KanbanTask | null = null;

  // ── Reminder notifications ─────
  reminderNotifications: ReminderNotification[] = [];
  private reminderSub!: Subscription;

  constructor(
    private kanbanService: KanbanEventService,
    private authService: AuthService,
    private reminderService: KanbanReminderService
  ) {}

  ngOnInit(): void {
    this.userId = this.authService.getUserId() ?? 0;
    if (!this.userId) {
      this.loading = false;
      this.showAlertMessage('Could not identify user. Please log in again.', 'error');
      return;
    }
    this.loadBoard();

    // Connect to WebSocket and listen for reminder notifications
    this.reminderService.connect(this.userId);
    this.reminderSub = this.reminderService.reminder$.subscribe(notification => {
      this.reminderNotifications.push(notification);
      // Auto-dismiss after 10 seconds
      setTimeout(() => {
        this.dismissReminder(notification);
      }, 10000);
    });
  }

  ngOnDestroy(): void {
    if (this.reminderSub) {
      this.reminderSub.unsubscribe();
    }
    this.reminderService.disconnect();
  }

  dismissReminder(notification: ReminderNotification): void {
    const idx = this.reminderNotifications.indexOf(notification);
    if (idx > -1) {
      this.reminderNotifications.splice(idx, 1);
    }
  }

  // ── Computed properties for stats ──
  get totalTasks(): number {
    return this.tasks.length;
  }

  get doingTasks(): number {
    return this.tasks.filter(t => t.status === 'DOING').length;
  }

  get doneTasks(): number {
    return this.tasks.filter(t => t.status === 'DONE').length;
  }

  get overdueTasks(): number {
    return this.tasks.filter(t => this.isOverdue(t)).length;
  }

  loadBoard(): void {
    this.loading = true;
    this.kanbanService.getBoard(this.userId).subscribe({
      next: (data) => {
        this.tasks = data;
        this.updateColumns();
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.showAlertMessage('Error loading board', 'error');
      }
    });
  }

  updateColumns(): void {
    this.columns.forEach(col => {
      col.tasks = this.tasks.filter(t => t.status === col.status).sort((a, b) => (a.position ?? 0) - (b.position ?? 0));
    });
  }

  showAlertMessage(message: string, type: 'success' | 'info' | 'error' = 'info'): void {
    this.alertMessage = message;
    this.alertType = type;
    this.showAlert = true;
    setTimeout(() => this.showAlert = false, 5000);
  }

  onDragStart(event: DragEvent, task: KanbanTask): void {
    this.draggedTask = task;
    if (event.dataTransfer) {
      event.dataTransfer.effectAllowed = 'move';
      event.dataTransfer.setData('text/plain', String(task.id));
    }
  }

  onDragOver(event: DragEvent): void {
    event.preventDefault();
    if (event.dataTransfer) event.dataTransfer.dropEffect = 'move';
  }

  onDragEnter(event: DragEvent): void {
    (event.currentTarget as HTMLElement).classList.add('drag-over');
  }

  onDragLeave(event: DragEvent): void {
    (event.currentTarget as HTMLElement).classList.remove('drag-over');
  }

  onDrop(event: DragEvent, targetStatus: KanbanStatus): void {
    event.preventDefault();
    (event.currentTarget as HTMLElement).classList.remove('drag-over');
    if (!this.draggedTask || !this.draggedTask.id) return;
    if (this.draggedTask.status === targetStatus) {
      this.draggedTask = null;
      return;
    }
    const newPosition = this.tasks.filter(t => t.status === targetStatus).length;
    this.kanbanService.moveTask(this.draggedTask.id, { newStatus: targetStatus, newPosition }).subscribe({
      next: () => {
        this.loadBoard();
        this.showAlertMessage('Task moved successfully', 'success');
      },
      error: (err) => {
        console.error('Move failed', err);
        this.showAlertMessage('Failed to move task', 'error');
      }
    });
    this.draggedTask = null;
  }

  openAddForm(): void {
    this.editingTask = null;
    this.formTitle = '';
    this.formDescription = '';
    this.formPriority = 'MEDIUM';
    this.formDeadline = '';
    this.showAddForm = true;
  }

  closeAddForm(): void {
    this.showAddForm = false;
    this.editingTask = null;
  }

  openEditForm(task: KanbanTask): void {
    this.editingTask = task;
    this.formTitle = task.title;
    this.formDescription = task.description ?? '';
    this.formPriority = task.priority || 'MEDIUM';
    this.formDeadline = task.deadline ? task.deadline.substring(0, 16) : '';
    this.showAddForm = false; // Close add form if open
  }

  closeEditForm(): void {
    this.editingTask = null;
  }

  addTask(): void {
    if (!this.formTitle.trim()) return;

    const payload: KanbanTask = {
      title: this.formTitle,
      description: this.formDescription,
      status: 'TODO',
      priority: this.formPriority,
      userId: this.userId,
      deadline: this.formDeadline ? this.formDeadline + ':00' : undefined,
      position: this.tasks.filter(t => t.status === 'TODO').length
    };

    this.kanbanService.createTask(payload).subscribe({
      next: () => {
        this.closeAddForm();
        this.loadBoard();
        this.showAlertMessage('Task created successfully', 'success');
      },
      error: () => {
        this.showAlertMessage('Failed to create task', 'error');
      }
    });
  }

  updateTask(): void {
    if (!this.editingTask || !this.editingTask.id || !this.formTitle.trim()) return;

    const payload: KanbanTask = {
      ...this.editingTask,
      title: this.formTitle,
      description: this.formDescription,
      priority: this.formPriority,
      deadline: this.formDeadline ? this.formDeadline + ':00' : undefined
    };

    this.kanbanService.updateTask(this.editingTask.id, payload).subscribe({
      next: () => {
        this.closeEditForm();
        this.loadBoard();
        this.showAlertMessage('Task updated successfully', 'success');
      },
      error: () => {
        this.showAlertMessage('Failed to update task', 'error');
      }
    });
  }

  deleteTask(task: KanbanTask): void {
    if (!task.id) return;

    if (confirm(`Delete task "${task.title}"?`)) {
      this.kanbanService.deleteTask(task.id).subscribe({
        next: () => {
          this.loadBoard();
          this.showAlertMessage('Task deleted successfully', 'success');
        },
        error: () => {
          this.showAlertMessage('Failed to delete task', 'error');
        }
      });
    }
  }

  isOverdue(task: KanbanTask): boolean {
    if (!task.deadline || task.status === 'DONE') return false;
    return new Date(task.deadline) < new Date();
  }

  isDueSoon(task: KanbanTask): boolean {
    if (!task.deadline || task.status === 'DONE') return false;
    const d = new Date(task.deadline).getTime() - Date.now();
    return d > 0 && d < 86400000;
  }
}
