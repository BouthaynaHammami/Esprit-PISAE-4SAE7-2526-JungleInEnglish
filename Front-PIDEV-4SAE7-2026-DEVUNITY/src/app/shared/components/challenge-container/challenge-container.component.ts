import { Component, OnInit, OnDestroy, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { interval, Subject, takeUntil, takeWhile } from 'rxjs';
import { ChallengeService } from '../../../core/services/challenge.service';

export interface ChallengeState {
  status: 'playing' | 'finished' | 'timeout';
  currentAttempt: number;
  maxAttempts: number;
  timeRemaining: number;
  totalTime: number;
  score: number;
  correctAnswers: number;
  wrongAnswers: number;
  progress: number;
}

export interface ChallengeResult {
  score: number;
  correctAnswers: number;
  wrongAnswers: number;
  timeUsed: number;
  attemptsUsed: number;
  bonus: number;
  feedback: string;
  sessionId?: number;
}

@Component({
  selector: 'app-challenge-container',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './challenge-container.component.html',
  styleUrls: ['./challenge-container.component.scss']
})
export class ChallengeContainerComponent implements OnInit, OnDestroy {
  @Input() challengeId: number = 1;
  @Input() maxAttempts: number = 3;
  @Input() totalDurationSeconds: number = 180; // 3 minutes

  @Output() onChallengeComplete = new EventEmitter<ChallengeResult>();
  @Output() onChallengeAbort = new EventEmitter<void>();

  // State management
  state: ChallengeState = {
    status: 'playing',
    currentAttempt: 1,
    maxAttempts: 3,
    timeRemaining: 180,
    totalTime: 180,
    score: 0,
    correctAnswers: 0,
    wrongAnswers: 0,
    progress: 0
  };

  // UI Properties
  isTimeRunningOut: boolean = false; // Last 10 seconds
  isInputDisabled: boolean = false;
  timeFontSize: string = '3rem';
  timerColor: string = '#000';
  animationClass: string = '';

  private destroy$ = new Subject<void>();
  private timerSubscription$ = new Subject<void>();

  constructor(private challengeService: ChallengeService) {}

  ngOnInit(): void {
    this.state.maxAttempts = this.maxAttempts;
    this.state.totalTime = this.totalDurationSeconds;
    this.state.timeRemaining = this.totalDurationSeconds;
    this.startTimer();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
    this.timerSubscription$.next();
    this.timerSubscription$.complete();
  }

  /**
   * Start the countdown timer
   * Timer ticks every second and checks end conditions
   */
  private startTimer(): void {
    interval(1000)
      .pipe(
        takeUntil(this.destroy$),
        takeUntil(this.timerSubscription$),
        takeWhile(() => this.state.timeRemaining > 0 && this.state.status === 'playing')
      )
      .subscribe({
        next: () => {
          this.state.timeRemaining--;
          this.updateTimerUI();
          this.checkEndConditions();
        },
        complete: () => {
          if (this.state.status === 'playing') {
            this.endChallengeWithTimeout();
          }
        }
      });
  }

  /**
   * Update timer UI based on remaining time
   */
  private updateTimerUI(): void {
    // Check if last 10 seconds (critical time)
    if (this.state.timeRemaining <= 10 && !this.isTimeRunningOut) {
      this.isTimeRunningOut = true;
      this.animationClass = 'timer-critical';
      this.timerColor = '#ef4444'; // Red color
    }

    // Update progress
    this.state.progress = Math.round(
      ((this.state.totalTime - this.state.timeRemaining) / this.state.totalTime) * 100
    );
  }

  /**
   * Check if challenge should end
   */
  private checkEndConditions(): void {
    if (this.state.timeRemaining <= 0) {
      this.endChallengeWithTimeout();
    }

    if (this.state.currentAttempt >= this.state.maxAttempts) {
      this.isInputDisabled = true;
    }
  }

  /**
   * Record a correct answer
   */
  recordCorrectAnswer(): void {
    if (this.state.status !== 'playing' || this.isInputDisabled) return;

    this.state.correctAnswers++;
    this.calculateScore();
  }

  /**
   * Record a wrong answer
   */
  recordWrongAnswer(): void {
    if (this.state.status !== 'playing' || this.isInputDisabled) return;

    this.state.wrongAnswers++;
    this.calculateScore();
  }

  /**
   * Move to next attempt
   */
  nextAttempt(): void {
    if (this.state.currentAttempt < this.state.maxAttempts && this.state.timeRemaining > 0) {
      this.state.currentAttempt++;
      this.state.correctAnswers = 0;
      this.state.wrongAnswers = 0;
      this.calculateScore();
    } else if (this.state.currentAttempt >= this.state.maxAttempts) {
      this.finishChallenge();
    }
  }

  /**
   * Calculate score based on correct/wrong answers and time bonus
   */
  private calculateScore(): void {
    const correctWeight = 10; // Points per correct answer
    const wrongPenalty = 2; // Penalty per wrong answer
    const baseScore = Math.max(
      0,
      this.state.correctAnswers * correctWeight - this.state.wrongAnswers * wrongPenalty
    );

    // Bonus for remaining time (1 point per 2 seconds remaining)
    const timeBonus = Math.floor(this.state.timeRemaining / 2);

    this.state.score = baseScore + timeBonus;
  }

  /**
   * Manually finish challenge before time runs out
   */
  finishChallenge(): void {
    if (this.state.status !== 'playing') return;

    this.state.status = 'finished';
    this.timerSubscription$.next();
    this.submitChallengeResult();
  }

  /**
   * Challenge ends due to timeout
   */
  private endChallengeWithTimeout(): void {
    this.state.status = 'timeout';
    this.state.timeRemaining = 0;
    this.timerSubscription$.next();
    this.animationClass = 'challenge-timeout';
    this.isInputDisabled = true;
    this.submitChallengeResult();
  }

  /**
   * Submit final score to backend
   */
  private submitChallengeResult(): void {
    this.calculateScore();

    const result: ChallengeResult = {
      score: this.state.score,
      correctAnswers: this.state.correctAnswers,
      wrongAnswers: this.state.wrongAnswers,
      timeUsed: this.state.totalTime - this.state.timeRemaining,
      attemptsUsed: this.state.currentAttempt,
      bonus: Math.floor(this.state.timeRemaining / 2),
      feedback: this.generateFeedback()
    };

    // Send to backend
    this.challengeService.submitChallengeScore(this.challengeId, result).subscribe({
      next: (response: any) => {
        result.sessionId = response.sessionId;
        this.onChallengeComplete.emit(result);
      },
      error: (error: any) => {
        console.error('Error submitting challenge score:', error);
        // Still emit the result even if API fails
        this.onChallengeComplete.emit(result);
      }
    });
  }

  /**
   * Generate feedback based on performance
   */
  private generateFeedback(): string {
    const accuracy = this.state.correctAnswers + this.state.wrongAnswers > 0
      ? Math.round((this.state.correctAnswers / (this.state.correctAnswers + this.state.wrongAnswers)) * 100)
      : 0;

    if (accuracy >= 80) {
      return 'Excellent! Keep up the great work!';
    } else if (accuracy >= 60) {
      return 'Good job! You can do better next time.';
    } else if (accuracy >= 40) {
      return 'Not bad! Practice more to improve.';
    } else {
      return 'Keep practicing! You\'ll improve with more attempts.';
    }
  }

  /**
   * Format time in MM:SS format
   */
  formatTime(seconds: number): string {
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${String(mins).padStart(2, '0')}:${String(secs).padStart(2, '0')}`;
  }

  /**
   * Get attempt display (e.g., "1/3")
   */
  getAttemptDisplay(): string {
    return `${this.state.currentAttempt}/${this.state.maxAttempts}`;
  }

  /**
   * Get progress percentage
   */
  getProgressPercentage(): number {
    return this.state.progress;
  }

  /**
   * Check if challenge is still active
   */
  isActive(): boolean {
    return this.state.status === 'playing';
  }

  /**
   * Abort challenge
   */
  abortChallenge(): void {
    this.state.status = 'finished';
    this.timerSubscription$.next();
    this.onChallengeAbort.emit();
  }
}
