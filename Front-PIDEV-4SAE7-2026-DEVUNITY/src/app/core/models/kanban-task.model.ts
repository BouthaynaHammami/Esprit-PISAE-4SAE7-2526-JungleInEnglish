export type KanbanStatus = 'TODO' | 'DOING' | 'DONE';

export type KanbanPriority = 'LOW' | 'MEDIUM' | 'HIGH';

export interface KanbanTask {
  id?: number;
  title: string;
  description?: string;
  status: KanbanStatus;
  priority?: KanbanPriority;
  userId: number;
  deadline?: string;   // ISO date-time string
  position?: number;
  reminderSent?: boolean;
  createdAt?: string;
  updatedAt?: string;
}

export interface KanbanMoveDTO {
  newStatus: KanbanStatus;
  newPosition: number;
}

export interface DailyAnalysisDTO {
  date: string;
  module: string;
  totalTasks: number;
  todoCount: number;
  doingCount: number;
  doneCount: number;
  overdueCount: number;
  tasksDueToday: KanbanTask[];
  overdueTasks: KanbanTask[];
}

export interface ReminderNotification {
  taskId: number;
  title: string;
  description: string;
  module: string;
  deadline: string;
  message: string;
}
