import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil, finalize } from 'rxjs/operators';
import { ChildService } from '../../../../core/services/language/child.service';
import { GamificationService } from '../../../../core/services/language/gamification.service';
import { EnglishActivityService } from '../../../../core/services/language/english-activity.service';
import { QuestionService } from '../../../../core/services/language/question.service';
import { AuthService } from '../../../../core/services/auth.service';
import {
  Child, Progress, ChildBadge, Activity, Question,
  QuizSession, Answer, QuizAttempt, ActivityCategory, DifficultyLevel
} from '../../../../core/models/english-kids.model';

interface ChildForm {
  name: string;
  age: number;
  avatar: string;
}

const AVATARS = ['🐯', '🦊', '🐻', '🐼', '🦁', '🐸', '🦉', '🐨', '🦋', '🐬'];

type ActiveTab = 'progress' | 'play';
type PlayView = 'activities' | 'quiz' | 'results';

@Component({
  selector: 'app-parent-dashboard',
  templateUrl: './parent-dashboard.component.html',
  styleUrls: ['./parent-dashboard.component.scss']
})
export class ParentDashboardComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();

  // ── Children ──────────────────────────────────────────────────────────────
  children: Child[] = [];
  selectedChild: Child | null = null;
  loading = true;
  error: string | null = null;

  // ── Progress tab ──────────────────────────────────────────────────────────
  selectedProgress: Progress | null = null;
  selectedBadges: ChildBadge[] = [];
  progressLoading = false;

  // ── Tabs ──────────────────────────────────────────────────────────────────
  activeTab: ActiveTab = 'progress';
  playView: PlayView = 'activities';

  // ── Activities ────────────────────────────────────────────────────────────
  activities: Activity[] = [];
  filteredActivities: Activity[] = [];
  activitiesLoading = false;
  searchQuery = '';
  selectedCategory: ActivityCategory | '' = '';
  selectedDifficulty: DifficultyLevel | '' = '';
  ActivityCategory = ActivityCategory;
  DifficultyLevel = DifficultyLevel;
  categories = Object.values(ActivityCategory);
  difficulties = Object.values(DifficultyLevel);

  // ── Quiz ──────────────────────────────────────────────────────────────────
  quizSession: QuizSession | null = null;
  selectedAnswer = '';
  showFeedback = false;
  isCorrect = false;
  quizTimer: any;
  quizResults: QuizAttempt | null = null;
  quizLoading = false;
  quizError = '';

  // ── Child modal ───────────────────────────────────────────────────────────
  showModal = false;
  editingChild: Child | null = null;
  form: ChildForm = { name: '', age: 6, avatar: AVATARS[0] };
  avatarOptions = AVATARS;
  savingChild = false;
  saveError = '';
  deleteConfirmId: number | null = null;

  // ── Parent info ───────────────────────────────────────────────────────────
  parentId = 0;

  constructor(
    private childService: ChildService,
    private gamificationService: GamificationService,
    private activityService: EnglishActivityService,
    private questionService: QuestionService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.parentId = this.authService.getUserId() ?? 0;
    this.loadChildren();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
    if (this.quizTimer) clearInterval(this.quizTimer);
  }

  // ── Children ──────────────────────────────────────────────────────────────
  loadChildren(): void {
    this.loading = true;
    this.error = null;
    this.childService.getAllChildren()
      .pipe(takeUntil(this.destroy$), finalize(() => this.loading = false))
      .subscribe({
        next: (children) => {
          this.children = this.parentId
            ? children.filter(c => c.parentId === this.parentId)
            : children;
          if (this.children.length > 0 && !this.selectedChild) {
            this.selectChild(this.children[0]);
          }
        },
        error: () => { this.error = 'Failed to load children. Please try again.'; }
      });
  }

  selectChild(child: Child): void {
    this.selectedChild = child;
    this.selectedProgress = null;
    this.selectedBadges = [];
    this.activeTab = 'progress';
    this.resetPlayState();
    this.loadProgress(child);
  }

  // ── Progress ──────────────────────────────────────────────────────────────
  loadProgress(child: Child): void {
    if (!child.childId) return;
    this.progressLoading = true;
    this.gamificationService.getChildProgress(child.childId)
      .pipe(takeUntil(this.destroy$), finalize(() => this.progressLoading = false))
      .subscribe({
        next: (progress) => {
          this.selectedProgress = progress;
          this.selectedBadges = progress.badges || [];
        },
        error: () => { /* progress stays null */ }
      });
  }

  // ── Tabs ──────────────────────────────────────────────────────────────────
  switchTab(tab: ActiveTab): void {
    this.activeTab = tab;
    if (tab === 'play' && this.activities.length === 0) {
      this.loadActivities();
    }
  }

  // ── Activities ────────────────────────────────────────────────────────────
  loadActivities(): void {
    this.activitiesLoading = true;
    this.activityService.getAllActivities()
      .pipe(takeUntil(this.destroy$), finalize(() => this.activitiesLoading = false))
      .subscribe({
        next: (acts) => { this.activities = acts; this.filterActivities(); },
        error: () => { this.quizError = 'Failed to load activities.'; }
      });
  }

  filterActivities(): void {
    this.filteredActivities = this.activities.filter(a => {
      const matchCat = !this.selectedCategory || a.category === this.selectedCategory;
      const matchDiff = !this.selectedDifficulty || a.difficulty === this.selectedDifficulty;
      const matchSearch = !this.searchQuery ||
        a.title.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
        a.description.toLowerCase().includes(this.searchQuery.toLowerCase());
      return matchCat && matchDiff && matchSearch;
    });
  }

  onSearchChange(): void { this.filterActivities(); }
  onCategoryChange(): void { this.filterActivities(); }
  onDifficultyChange(): void { this.filterActivities(); }

  // ── Quiz ──────────────────────────────────────────────────────────────────
  startActivity(activity: Activity): void {
    if (!this.selectedChild) return;
    this.quizLoading = true;
    this.quizError = '';
    this.questionService.getQuestionsForActivity(activity.activityId)
      .pipe(takeUntil(this.destroy$), finalize(() => this.quizLoading = false))
      .subscribe({
        next: (questions) => {
          if (questions.length === 0) {
            this.quizError = 'No questions available for this activity yet.';
            return;
          }
          this.quizSession = {
            activity, questions, currentQuestionIndex: 0,
            answers: [], startTime: new Date(), timeRemaining: 60
          };
          this.playView = 'quiz';
          this.startQuizTimer();
        },
        error: () => { this.quizError = 'Failed to load quiz. Try again.'; }
      });
  }

  startQuizTimer(): void {
    if (this.quizTimer) clearInterval(this.quizTimer);
    this.quizTimer = setInterval(() => {
      if (!this.quizSession) return;
      if (this.quizSession.timeRemaining > 0) {
        this.quizSession.timeRemaining--;
      } else {
        this.submitAnswer('');
      }
    }, 1000);
  }

  get currentQuestion(): Question | null {
    return this.quizSession ? this.quizSession.questions[this.quizSession.currentQuestionIndex] : null;
  }

  get questionProgress(): string {
    if (!this.quizSession) return '';
    return `${this.quizSession.currentQuestionIndex + 1} / ${this.quizSession.questions.length}`;
  }

  selectAnswer(answer: string): void { this.selectedAnswer = answer; }

  submitAnswer(answer: string): void {
    if (!this.quizSession || !this.currentQuestion || this.showFeedback) return;
    const q = this.currentQuestion;
    const correct = answer === q.correctAnswer;
    const timeSpent = 60 - this.quizSession.timeRemaining;
    this.quizSession.answers.push({ questionId: q.questionId, selectedAnswer: answer, isCorrect: correct, timeSpent });
    this.isCorrect = correct;
    this.showFeedback = true;
    setTimeout(() => {
      this.showFeedback = false;
      this.selectedAnswer = '';
      if (this.quizSession!.currentQuestionIndex < this.quizSession!.questions.length - 1) {
        this.quizSession!.currentQuestionIndex++;
        this.quizSession!.timeRemaining = 60;
      } else {
        this.finishQuiz();
      }
    }, 1500);
  }

  finishQuiz(): void {
    if (!this.quizSession || !this.selectedChild) return;
    clearInterval(this.quizTimer);
    const correct = this.quizSession.answers.filter(a => a.isCorrect).length;
    const total = this.quizSession.questions.length;
    const timeSpent = this.quizSession.answers.reduce((s, a) => s + a.timeSpent, 0);
    const xpEarned = this.gamificationService.calculateXp(correct, total, timeSpent);
    const attempt: QuizAttempt = {
      childId: this.selectedChild.childId!, activityId: this.quizSession.activity.activityId,
      answers: this.quizSession.answers, score: correct, totalQuestions: total,
      xpEarned, timeSpent
    };
    this.quizLoading = true;
    this.questionService.submitQuizAttempt(attempt)
      .pipe(takeUntil(this.destroy$), finalize(() => this.quizLoading = false))
      .subscribe({
        next: (result) => {
          this.quizResults = result;
          this.playView = 'results';
          // Update XP
          this.childService.updateChildXp(this.selectedChild!.childId!, xpEarned)
            .pipe(takeUntil(this.destroy$))
            .subscribe({ next: (updated) => {
              this.selectedChild = updated;
              const idx = this.children.findIndex(c => c.childId === updated.childId);
              if (idx !== -1) this.children[idx] = updated;
              this.loadProgress(updated);
            }});
        },
        error: () => {
          // Show results anyway with local data
          this.quizResults = attempt;
          this.playView = 'results';
        }
      });
  }

  backToActivities(): void {
    this.playView = 'activities';
    this.quizSession = null;
    this.quizResults = null;
    this.quizError = '';
    this.selectedAnswer = '';
    this.showFeedback = false;
    if (this.quizTimer) clearInterval(this.quizTimer);
  }

  retryQuiz(): void {
    if (this.quizResults) {
      const act = this.activities.find(a => a.activityId === this.quizResults!.activityId);
      if (act) this.startActivity(act);
    }
  }

  resetPlayState(): void {
    this.playView = 'activities';
    this.quizSession = null;
    this.quizResults = null;
    this.quizError = '';
    this.selectedAnswer = '';
    this.showFeedback = false;
    this.activities = [];
    this.filteredActivities = [];
    if (this.quizTimer) clearInterval(this.quizTimer);
  }

  // ── Child CRUD ────────────────────────────────────────────────────────────
  openAddModal(): void {
    this.editingChild = null;
    this.form = { name: '', age: 6, avatar: AVATARS[0] };
    this.showModal = true;
  }

  openEditModal(child: Child): void {
    this.editingChild = child;
    this.form = { name: child.name, age: child.age, avatar: child.avatar || AVATARS[0] };
    this.showModal = true;
  }

  closeModal(): void { this.showModal = false; this.editingChild = null; this.saveError = ''; }

  saveChild(): void {
    if (!this.form.name.trim()) return;
    this.savingChild = true;
    this.saveError = '';
    const childData: Child = {
      name: this.form.name.trim(), age: this.form.age,
      avatar: this.form.avatar, parentId: this.parentId, xp: 0, level: 1
    };
    if (this.editingChild && this.editingChild.childId) {
      this.childService.updateChild(this.editingChild.childId, { ...this.editingChild, ...childData })
        .pipe(takeUntil(this.destroy$), finalize(() => this.savingChild = false))
        .subscribe({
          next: (updated) => {
            const idx = this.children.findIndex(c => c.childId === updated.childId);
            if (idx !== -1) this.children[idx] = updated;
            if (this.selectedChild?.childId === updated.childId) this.selectedChild = updated;
            this.closeModal();
          },
          error: (err) => {
            this.saveError = err?.error?.message || 'Failed to update child. Please try again.';
          }
        });
    } else {
      this.childService.createChild(childData)
        .pipe(takeUntil(this.destroy$), finalize(() => this.savingChild = false))
        .subscribe({
          next: (created) => { this.children.push(created); this.closeModal(); this.selectChild(created); },
          error: (err) => {
            this.saveError = err?.error?.message || 'Failed to add child. Please try again.';
          }
        });
    }
  }

  confirmDelete(childId: number): void { this.deleteConfirmId = childId; }
  cancelDelete(): void { this.deleteConfirmId = null; }

  deleteChild(childId: number): void {
    this.childService.deleteChild(childId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
          this.children = this.children.filter(c => c.childId !== childId);
          if (this.selectedChild?.childId === childId) {
            this.selectedChild = this.children[0] || null;
            if (this.selectedChild) this.selectChild(this.selectedChild);
            else { this.selectedProgress = null; this.selectedBadges = []; }
          }
          this.deleteConfirmId = null;
        },
        error: () => { this.deleteConfirmId = null; }
      });
  }

  // ── Helpers ───────────────────────────────────────────────────────────────
  getXpPercent(progress: Progress): number {
    const currentLevelXp = (progress.currentLevel - 1) * 1000;
    const nextLevelXp = progress.currentLevel * 1000;
    return Math.min(100, Math.round(((progress.totalXp - currentLevelXp) / (nextLevelXp - currentLevelXp)) * 100));
  }

  getLevelLabel(level: number): string {
    if (level <= 2) return 'Beginner';
    if (level <= 4) return 'Elementary';
    if (level <= 6) return 'Intermediate';
    if (level <= 8) return 'Advanced';
    return 'Master';
  }

  getCategoryIcon(cat: ActivityCategory): string {
    const icons: Record<ActivityCategory, string> = {
      [ActivityCategory.VOCABULARY]: '📚', [ActivityCategory.GRAMMAR]: '✏️',
      [ActivityCategory.LISTENING]: '👂', [ActivityCategory.READING]: '📖',
      [ActivityCategory.SPEAKING]: '🗣️', [ActivityCategory.WRITING]: '✍️'
    };
    return icons[cat] || '📝';
  }

  getDifficultyClass(diff: DifficultyLevel): string {
    if (diff === DifficultyLevel.BEGINNER) return 'badge-beginner';
    if (diff === DifficultyLevel.INTERMEDIATE) return 'badge-intermediate';
    return 'badge-advanced';
  }

  getChildAvatar(child: Child): string { return child.avatar || '🐯'; }

  formatDate(date: Date | string): string {
    return new Date(date).toLocaleDateString('en-US', { month: 'short', day: 'numeric' });
  }

  getScorePercent(results: QuizAttempt): number {
    return Math.round((results.score / results.totalQuestions) * 100);
  }

  getScoreEmoji(results: QuizAttempt): string {
    const pct = this.getScorePercent(results);
    if (pct === 100) return '🏆';
    if (pct >= 80) return '⭐';
    if (pct >= 60) return '👍';
    return '💪';
  }
}
