// src/app/features/admin/components/test-niveau/admin-test-niveau.component.ts
import { Component, OnInit } from '@angular/core';
import { LevelTestService } from '../../../../core/services/level-test.service';
import { UserService } from '../../../../core/services/activity/user.service';
import { Subject } from '../../../../core/models/subject.model';
import { TestTentative } from '../../../../core/models/test-tentative.model';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

type AdminTab = 'subjects' | 'submissions';

@Component({
  selector: 'app-admin-test-niveau',
  templateUrl: './admin-test-niveau.component.html',
  styleUrls: ['./admin-test-niveau.component.css']
})
export class AdminTestNiveauComponent implements OnInit {
  activeTab: AdminTab = 'subjects';
  mode: 'LIST' | 'FORM' = 'LIST';
  isEdit = false;

  // Subjects
  subjects: Subject[] = [];
  loadingSubjects = true;
  editingSubject: Subject | null = null;
  subjectForm: any = { tstitle: '', description: '', time: '15' };

  // Submissions (read-only view)
  allTentatives: TestTentative[] = [];
  loadingTentatives = true;
  expandedId: number | null = null;
  studentNames: Record<number, string> = {};

  constructor(
    private levelTestService: LevelTestService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.loadSubjects();
    this.loadAllSubmissions();
  }

  loadSubjects(): void {
    this.loadingSubjects = true;
    this.levelTestService.getAllSubjects().subscribe({
      next: (d) => { this.subjects = d; this.loadingSubjects = false; },
      error: () => { this.loadingSubjects = false; }
    });
  }

  openNewSubject(): void {
    this.isEdit = false;
    this.editingSubject = null;
    this.subjectForm = { tstitle: '', description: '', time: '15' };
    this.mode = 'FORM';
  }

  openEditSubject(s: Subject): void {
    this.isEdit = true;
    this.editingSubject = s;
    this.subjectForm = { 
      tstitle: s.title, 
      description: s.description, 
      time: s.time
    };
    this.mode = 'FORM';
  }

  saveSubject(): void {
    const payload: Subject = {
      title: this.subjectForm.tstitle,
      description: this.subjectForm.description,
      time: this.subjectForm.time
    };

    if (this.editingSubject?.id) {
      this.levelTestService.updateSubject(this.editingSubject.id, payload).subscribe(() => {
        this.mode = 'LIST';
        this.loadSubjects();
      });
    } else {
      this.levelTestService.createSubject(payload).subscribe(() => {
        this.mode = 'LIST';
        this.loadSubjects();
      });
    }
  }

  cancelForm(): void {
    this.mode = 'LIST';
  }

  deleteSubject(id: number): void {
    if (!confirm('Delete this subject?')) return;
    this.levelTestService.deleteSubject(id).subscribe(() => this.loadSubjects());
  }

  // ─── Submissions ──────────────────────────────────────
  loadAllSubmissions(): void {
    this.loadingTentatives = true;
    this.levelTestService.getAllTentatives().subscribe({
      next: (tentatives) => {
        this.allTentatives = tentatives;
        this.loadingTentatives = false;

        // Resolve unique student IDs to names
        const uniqueIds = [...new Set(tentatives.map(t => t.userId).filter((id): id is number => !!id))];
        if (uniqueIds.length === 0) return;

        forkJoin(uniqueIds.map(id => this.userService.getById(id).pipe(catchError(() => of(null))))).subscribe(users => {
          users.forEach((u, i) => {
            if (u) this.studentNames[uniqueIds[i]] = `${u.firstName} ${u.lastName}`;
          });
        });
      },
      error: () => { this.loadingTentatives = false; }
    });
  }

  getStudentName(userId?: number): string {
    if (!userId) return 'Unknown';
    return this.studentNames[userId] ?? `Student #${userId}`;
  }

  toggleExpand(id?: number): void {
    this.expandedId = this.expandedId === id ? null : (id ?? null);
  }

  getStatusClass(status?: string): string {
    switch (status) {
      case 'CORRECTED': return 'badge-success';
      case 'PENDING':   return 'badge-warning';
      case 'PASSED':    return 'badge-success';
      case 'FAILED':    return 'badge-danger';
      default:          return 'badge-secondary';
    }
  }

  get correctedCount(): number {
    return this.allTentatives.filter(t => t.status === 'CORRECTED').length;
  }

  get pendingCount(): number {
    return this.allTentatives.filter(t => t.status === 'PENDING').length;
  }
}
