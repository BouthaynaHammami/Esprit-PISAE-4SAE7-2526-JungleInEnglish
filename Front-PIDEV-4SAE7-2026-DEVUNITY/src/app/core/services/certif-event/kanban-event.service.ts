import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { KanbanTask, KanbanMoveDTO, DailyAnalysisDTO } from '../../models/kanban-task.model';

@Injectable({ providedIn: 'root' })
export class KanbanEventService {

  private readonly api = 'http://localhost:8081/communities/api/events/kanban';

  constructor(private http: HttpClient) {}

  // ── CRUD ──────────────────────────────────────

  createTask(task: KanbanTask): Observable<KanbanTask> {
    return this.http.post<KanbanTask>(`${this.api}/tasks`, task);
  }

  updateTask(id: number, task: KanbanTask): Observable<KanbanTask> {
    return this.http.put<KanbanTask>(`${this.api}/tasks/${id}`, task);
  }

  deleteTask(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/tasks/${id}`);
  }

  getTask(id: number): Observable<KanbanTask> {
    return this.http.get<KanbanTask>(`${this.api}/tasks/${id}`);
  }

  // ── Board ─────────────────────────────────────

  getBoard(userId: number): Observable<KanbanTask[]> {
    return this.http.get<KanbanTask[]>(`${this.api}/board/${userId}`);
  }

  getColumn(userId: number, status: string): Observable<KanbanTask[]> {
    return this.http.get<KanbanTask[]>(`${this.api}/board/${userId}/column/${status}`);
  }

  // ── Drag & Drop ───────────────────────────────

  moveTask(id: number, move: KanbanMoveDTO): Observable<KanbanTask> {
    return this.http.patch<KanbanTask>(`${this.api}/tasks/${id}/move`, move);
  }

  // ── Daily Analysis ────────────────────────────

  getDailyAnalysis(userId: number): Observable<DailyAnalysisDTO> {
    return this.http.get<DailyAnalysisDTO>(`${this.api}/daily-analysis/${userId}`);
  }
}
