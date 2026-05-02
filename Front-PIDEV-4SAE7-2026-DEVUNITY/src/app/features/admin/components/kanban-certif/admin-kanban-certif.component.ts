import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { KanbanCertificationService } from '../../../../core/services/certif-event/kanban-certification.service';
import { AuthService } from '../../../../core/services/auth.service';
import { KanbanReminderService } from '../../../../core/services/certif-event/kanban-reminder.service';
import { KanbanTask, KanbanStatus, ReminderNotification } from '../../../../core/models/kanban-task.model';

@Component({
  selector: 'app-admin-kanban-certif',
  templateUrl: './admin-kanban-certif.component.html',
  styleUrls: ['./admin-kanban-certif.component.scss']
})
export class AdminKanbanCertifComponent implements OnInit, OnDestroy {

  columns: { status: KanbanStatus; label: string; color: string }[] = [
    { status: 'TODO',  label: 'To Do',  color: '#e74c3c' },
    { status: 'DOING', label: 'Doing',  color: '#f39c12' },
    { status: 'DONE',  label: 'Done',   color: '#27ae60' }
  ];

  tasks: KanbanTask[] = [];
  userId!: number;
  loading = true;

  // ── Add / Edit form ─────────────
  showForm = false;
  editingTask: KanbanTask | null = null;
  formTitle = '';
  formDescription = '';
  formDeadline = '';
  formStatus: KanbanStatus = 'TODO';

  // ── Drag state ──────────────────
  draggedTask: KanbanTask | null = null;

  // ── Reminder notifications ─────
  reminderNotifications: ReminderNotification[] = [];
  private reminderSub!: Subscription;

  constructor(
    private kanbanService: KanbanCertificationService,
    private authService: AuthService,
    private reminderService: KanbanReminderService
  ) {}

  ngOnInit(): void {
    this.userId = this.authService.getUserId() ?? 0;
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

  loadBoard(): void {
    this.loading = true;
    this.kanbanService.getBoard(this.userId).subscribe({
      next: (data) => { this.tasks = data; this.loading = false; },
      error: () => this.loading = false
    });
  }

  getTasksByStatus(status: KanbanStatus): KanbanTask[] {
    return this.tasks
      .filter(t => t.status === status)
      .sort((a, b) => (a.position ?? 0) - (b.position ?? 0));
  }

  countByStatus(status: KanbanStatus): number {
    return this.tasks.filter(t => t.status === status).length;
  }

  // ── Drag & Drop ────────────────────────────

  onDragStart(event: DragEvent, task: KanbanTask): void {
    this.draggedTask = task;
    if (event.dataTransfer) {
      event.dataTransfer.effectAllowed = 'move';
      event.dataTransfer.setData('text/plain', String(task.id));
    }
  }

  onDragOver(event: DragEvent): void {
    event.preventDefault();
    if (event.dataTransfer) {
      event.dataTransfer.dropEffect = 'move';
    }
  }

  onDragEnter(event: DragEvent): void {
    const el = (event.currentTarget as HTMLElement);
    el.classList.add('drag-over');
  }

  onDragLeave(event: DragEvent): void {
    const el = (event.currentTarget as HTMLElement);
    el.classList.remove('drag-over');
  }

  onDrop(event: DragEvent, targetStatus: KanbanStatus): void {
    event.preventDefault();
    const el = (event.currentTarget as HTMLElement);
    el.classList.remove('drag-over');

    if (!this.draggedTask || !this.draggedTask.id) return;
    if (this.draggedTask.status === targetStatus) {
      this.draggedTask = null;
      return;
    }

    const newPosition = this.getTasksByStatus(targetStatus).length;

    this.kanbanService.moveTask(this.draggedTask.id, {
      newStatus: targetStatus,
      newPosition
    }).subscribe({
      next: () => this.loadBoard(),
      error: (err) => console.error('Move failed', err)
    });

    this.draggedTask = null;
  }

  // ── CRUD ────────────────────────────────────

  openAddForm(status: KanbanStatus = 'TODO'): void {
    this.editingTask = null;
    this.formTitle = '';
    this.formDescription = '';
    this.formDeadline = '';
    this.formStatus = status;
    this.showForm = true;
  }

  openEditForm(task: KanbanTask): void {
    this.editingTask = task;
    this.formTitle = task.title;
    this.formDescription = task.description ?? '';
    this.formDeadline = task.deadline ? task.deadline.substring(0, 16) : '';
    this.formStatus = task.status;
    this.showForm = true;
  }

  closeForm(): void {
    this.showForm = false;
    this.editingTask = null;
  }

  saveTask(): void {
    const payload: KanbanTask = {
      title: this.formTitle,
      description: this.formDescription,
      status: this.formStatus,
      userId: this.userId,
      deadline: this.formDeadline ? this.formDeadline + ':00' : undefined,
      position: this.editingTask?.position ?? this.getTasksByStatus(this.formStatus).length
    };

    if (this.editingTask && this.editingTask.id) {
      this.kanbanService.updateTask(this.editingTask.id, payload).subscribe({
        next: () => { this.closeForm(); this.loadBoard(); }
      });
    } else {
      this.kanbanService.createTask(payload).subscribe({
        next: () => { this.closeForm(); this.loadBoard(); }
      });
    }
  }

  deleteTask(task: KanbanTask): void {
    if (!task.id) return;
    if (!confirm(`Delete task "${task.title}"?`)) return;
    this.kanbanService.deleteTask(task.id).subscribe({
      next: () => this.loadBoard()
    });
  }

  isOverdue(task: KanbanTask): boolean {
    if (!task.deadline || task.status === 'DONE') return false;
    return new Date(task.deadline) < new Date();
  }

  isDueSoon(task: KanbanTask): boolean {
    if (!task.deadline || task.status === 'DONE') return false;
    const diff = new Date(task.deadline).getTime() - Date.now();
    return diff > 0 && diff < 24 * 60 * 60 * 1000; // within 24h
  }
}
