import { Component, OnInit } from '@angular/core';
import { KanbanCertificationService } from '../../../../core/services/certif-event/kanban-certification.service';
import { KanbanEventService } from '../../../../core/services/certif-event/kanban-event.service';
import { AuthService } from '../../../../core/services/auth.service';
import { DailyAnalysisDTO } from '../../../../core/models/kanban-task.model';

@Component({
  selector: 'app-student-daily-analysis',
  templateUrl: './student-daily-analysis.component.html',
  styleUrls: ['./student-daily-analysis.component.scss']
})
export class StudentDailyAnalysisComponent implements OnInit {

  userId!: number;
  loading = true;

  certifAnalysis: DailyAnalysisDTO | null = null;
  eventsAnalysis: DailyAnalysisDTO | null = null;

  constructor(
    private certifService: KanbanCertificationService,
    private eventsService: KanbanEventService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.userId = this.authService.getUserId() ?? 0;
    this.loadAnalysis();
  }

  loadAnalysis(): void {
    this.loading = true;
    let loaded = 0;
    const done = () => { loaded++; if (loaded >= 2) this.loading = false; };

    this.certifService.getDailyAnalysis(this.userId).subscribe({
      next: (data) => { this.certifAnalysis = data; done(); },
      error: () => done()
    });

    this.eventsService.getDailyAnalysis(this.userId).subscribe({
      next: (data) => { this.eventsAnalysis = data; done(); },
      error: () => done()
    });
  }

  get totalTasks(): number { return (this.certifAnalysis?.totalTasks ?? 0) + (this.eventsAnalysis?.totalTasks ?? 0); }
  get totalTodo(): number { return (this.certifAnalysis?.todoCount ?? 0) + (this.eventsAnalysis?.todoCount ?? 0); }
  get totalDoing(): number { return (this.certifAnalysis?.doingCount ?? 0) + (this.eventsAnalysis?.doingCount ?? 0); }
  get totalDone(): number { return (this.certifAnalysis?.doneCount ?? 0) + (this.eventsAnalysis?.doneCount ?? 0); }
  get totalOverdue(): number { return (this.certifAnalysis?.overdueCount ?? 0) + (this.eventsAnalysis?.overdueCount ?? 0); }
  get completionRate(): number { if (this.totalTasks === 0) return 0; return Math.round((this.totalDone / this.totalTasks) * 100); }
}
