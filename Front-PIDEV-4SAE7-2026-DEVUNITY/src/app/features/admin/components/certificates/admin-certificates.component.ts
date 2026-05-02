import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CertificationService } from '../../../../core/services/certif-event/certification.service';
import { HttpClient } from '@angular/common/http';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Component({
  selector: 'app-admin-certificates',
  templateUrl: './admin-certificates.component.html',
  styleUrls: ['./admin-certificates.component.scss'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class AdminCertificatesComponent implements OnInit {

  activeTab: 'questions' | 'add' | 'results' | 'certificates' = 'questions';

  questions: any[]     = [];
  sessions: any[]      = [];
  certificates: any[]  = [];

  searchQ        = '';
  filterLevel    = '';
  filterCategory = '';

  editingQuestion: any      = null;
  confirmDeleteId: number | null = null;

  successMessage = '';
  errorMessage   = '';

  form = {
    questionText: '',
    optiona: '', optionb: '', optionc: '', optiond: '',
    correctAnswer: '', level: '', category: '', points: 5
  };

  levels = [
    { key: 'A1', count: 0 }, { key: 'A2', count: 0 },
    { key: 'B1', count: 0 }, { key: 'B2', count: 0 },
    { key: 'C1', count: 0 }, { key: 'C2', count: 0 }
  ];

  constructor(
    private certService: CertificationService,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    this.loadQuestions();
    this.loadSessions();
    this.loadCertificates();
  }

  loadQuestions(): void {
    this.certService.getQuestions().subscribe((data: any[]) => {
      this.questions = data || [];
      this.updateLevelCounts();
    });
  }

  loadSessions(): void {
    this.certService.getAllSessions().subscribe((data: any[]) => {
      const raw = data || [];

      const uniqueIds = [
        ...new Set(
          raw
            .map((s: any) => Number(s.studentId))
            .filter((id: number) => !isNaN(id) && id > 0)
        )
      ] as number[];

      if (uniqueIds.length === 0) {
        this.sessions = raw;
        return;
      }

      const requests = uniqueIds.map(id =>
        this.http.get<any>(`http://localhost:8081/learners/api/users/${id}`).pipe(
          catchError(() => of({ firstName: '?', lastName: '' }))
        )
      );

      // ✅ FIX : utilise uniqueIds[index] au lieu de u.userId
      forkJoin(requests).subscribe((users: any[]) => {
        const userMap: Record<number, any> = {};
        users.forEach((u, index) => {
          userMap[uniqueIds[index]] = u;
        });

        this.sessions = raw.map((s: any) => {
          const user = userMap[Number(s.studentId)];
          return {
            ...s,
            firstName: user?.firstName ?? '—',
            lastName:  user?.lastName  ?? ''
          };
        });
      });
    });
  }

  loadCertificates(): void {
    this.certService.getAllCertificates().subscribe((data: any[]) => {
      const raw = data || [];

      const uniqueIds = [
        ...new Set(
          raw
            .map((c: any) => Number(c.studentId))
            .filter((id: number) => !isNaN(id) && id > 0)
        )
      ] as number[];

      if (uniqueIds.length === 0) {
        this.certificates = raw;
        return;
      }

      const requests = uniqueIds.map(id =>
        this.http.get<any>(`http://localhost:8081/learners/api/users/${id}`).pipe(
          catchError(() => of({ firstName: '?', lastName: '' }))
        )
      );

      // ✅ FIX : utilise uniqueIds[index] au lieu de u.userId
      forkJoin(requests).subscribe((users: any[]) => {
        const userMap: Record<number, any> = {};
        users.forEach((u, index) => {
          userMap[uniqueIds[index]] = u;
        });

        this.certificates = raw.map((c: any) => {
          const user = userMap[Number(c.studentId)];
          return {
            ...c,
            firstName: user?.firstName ?? '—',
            lastName:  user?.lastName  ?? ''
          };
        });
      });
    });
  }

  updateLevelCounts(): void {
    this.levels.forEach(l => {
      l.count = this.questions.filter(q => q?.level === l.key).length;
    });
  }

  get passedCount(): number {
    return this.sessions.filter(s => !!s?.passed).length;
  }

  getPassRate(): number {
    if (!this.sessions.length) return 0;
    return Math.round((this.passedCount / this.sessions.length) * 100);
  }

  getAvgScore(): number {
    if (!this.sessions.length) return 0;
    const total = this.sessions.reduce((sum: number, x: any) => {
      return sum + (Number(x?.score) || 0);
    }, 0);
    return Math.round(total / this.sessions.length);
  }

  filteredQuestions(): any[] {
    return this.questions.filter(q => {
      const matchSearch =
        !this.searchQ ||
        (q?.questionText || '').toLowerCase().includes(this.searchQ.toLowerCase());
      const matchLevel    = !this.filterLevel    || q?.level    === this.filterLevel;
      const matchCategory = !this.filterCategory || q?.category === this.filterCategory;
      return matchSearch && matchLevel && matchCategory;
    });
  }

  onSubmitQuestion(): void {
    if (!this.form.questionText.trim()) {
      this.errorMessage = 'Question required';
      return;
    }

    if (this.editingQuestion) {
      this.certService.updateQuestion(this.editingQuestion.id, this.form)
        .subscribe(() => {
          this.successMessage = 'Question updated ✅';
          this.resetForm();
          this.loadAll();
          this.activeTab = 'questions';
        });
    } else {
      this.certService.addQuestion(this.form)
        .subscribe(() => {
          this.successMessage = 'Question added ✅';
          this.resetForm();
          this.loadAll();
          this.activeTab = 'questions';
        });
    }
  }

  editQuestion(q: any): void {
    this.editingQuestion = q;
    this.form = { ...q };
    this.activeTab = 'add';
  }

  confirmDeleteQuestion(id: number): void { this.confirmDeleteId = id; }
  cancelDelete(): void { this.confirmDeleteId = null; }

  deleteQuestion(id: number): void {
    this.certService.deleteQuestion(id).subscribe(() => {
      this.confirmDeleteId = null;
      this.successMessage  = 'Deleted successfully ✅';
      this.loadAll();
    });
  }

  resetForm(): void {
    this.editingQuestion = null;
    this.form = {
      questionText: '',
      optiona: '', optionb: '', optionc: '', optiond: '',
      correctAnswer: '', level: '', category: '', points: 5
    };
  }
}