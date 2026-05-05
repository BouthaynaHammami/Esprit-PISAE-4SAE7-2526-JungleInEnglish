// src/app/features/tutor/components/level-test/tutor-level-test.component.ts
import { Component, OnInit } from '@angular/core';
import { LevelTestService } from '../../../../core/services/level-test.service';
import { TestTentative } from '../../../../core/models/test-tentative.model';

@Component({
  selector: 'app-tutor-level-test',
  templateUrl: './tutor-level-test.component.html',
  styleUrls: ['./tutor-level-test.component.css']
})
export class TutorLevelTestComponent implements OnInit {
  pendingTests: TestTentative[] = [];
  loadingTests = true;

  gradingTest: TestTentative | null = null;
  feedbackText = '';
  scoreDraft = 0;
  gradingError = '';

  constructor(private levelTestService: LevelTestService) { }

  ngOnInit(): void {
    this.loadPendingTests();
  }

  loadPendingTests(): void {
    this.loadingTests = true;
    this.levelTestService.getSubmissionsForCorrection().subscribe({
      next: (d) => {
        this.pendingTests = d;
        this.loadingTests = false;
      },
      error: () => {
        this.loadingTests = false;
      }
    });
  }

  startGrading(t: TestTentative): void {
    this.gradingTest = t;
    this.feedbackText = t.tutorFeedback ?? '';
    this.scoreDraft = t.score ?? 0;
    this.gradingError = '';
  }

  submitGrade(): void {
    if (!this.gradingTest?.id) return;

    // Create a very minimal payload to satisfy the backend
    const payload: any = {
      id: this.gradingTest.id,
      paragraph: this.gradingTest.paragraph,
      subject: { id: this.gradingTest.subject?.id }, // Just the ID
      userId: this.gradingTest.userId,
      tutorFeedback: this.feedbackText,
      score: this.scoreDraft,
      status: 'CORRECTED'
    };

    this.levelTestService.updateTentative(this.gradingTest.id, payload).subscribe({
      next: () => {
        this.gradingTest = null;
        this.loadPendingTests();
      },
      error: (err) => {
        this.gradingError = err?.error?.message ?? 'Failed to save grade. Please try again.';
      }
    });
  }

  cancelGrading(): void {
    this.gradingTest = null;
  }

  getStatusClass(status?: string): string {
    switch (status) {
      case 'CORRECTED': return 'badge-success';
      case 'PENDING': return 'badge-warning';
      default: return 'badge-secondary';
    }
  }
}
