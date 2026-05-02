import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil, finalize } from 'rxjs/operators';
import {
  Child,
  Activity,
  Question,
  QuizSession,
  Answer,
  Progress,
  LeaderboardEntry,
  ActivityCategory,
  DifficultyLevel,
  QuizAttempt
} from '../../../../core/models/english-kids.model';
import { ChildService } from '../../../../core/services/language/child.service';
import { EnglishActivityService } from '../../../../core/services/language/english-activity.service';
import { QuestionService } from '../../../../core/services/language/question.service';
import { GamificationService } from '../../../../core/services/language/gamification.service';

type ViewMode = 'profile-select' | 'profile-create' | 'activities' | 'quiz' | 'results' | 'progress';

@Component({
    selector: 'app-student-english-kids',
    templateUrl: './student-english-kids.component.html',
    styleUrls: ['./student-english-kids.component.scss']
})
export class StudentEnglishKidsComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();
  viewMode: ViewMode = 'profile-select';
  loading = false;
  errorMsg = '';
  successMsg = '';
  children: Child[] = [];
  currentChild: Child | null = null;
  newChild: Partial<Child> = { name: '', age: 5 };
  activities: Activity[] = [];
  filteredActivities: Activity[] = [];
  selectedCategory: ActivityCategory | '' = '';
  selectedDifficulty: DifficultyLevel | '' = '';
  searchQuery = '';
  quizSession: QuizSession | null = null;
  selectedAnswer: string = '';
  showFeedback = false;
  isCorrect = false;
  quizTimer: any;
  quizResults: QuizAttempt | null = null;
  newBadges: any[] = [];
  progress: Progress | null = null;
  leaderboard: LeaderboardEntry[] = [];
  ActivityCategory = ActivityCategory;
  DifficultyLevel = DifficultyLevel;
  categories = Object.values(ActivityCategory);
  difficulties = Object.values(DifficultyLevel);

  constructor(
    private childService: ChildService,
    private activityService: EnglishActivityService,
    private questionService: QuestionService,
    private gamificationService: GamificationService
  ) {}

  ngOnInit(): void {
    console.log('🎓 English Kids Component Initialized');
    this.loadChildren();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
    if (this.quizTimer) {
      clearInterval(this.quizTimer);
    }
  }

  loadChildren(): void {
    console.log('📞 Calling getAllChildren API...');
    this.loading = true;
    this.childService.getAllChildren()
      .pipe(takeUntil(this.destroy$), finalize(() => this.loading = false))
      .subscribe({
        next: (children) => {
          console.log('✅ Received children:', children);
          this.children = children;
          if (children.length === 0) {
            this.viewMode = 'profile-create';
          } else {
            const stored = this.childService.getCurrentChild();
            if (stored && children.find(c => c.childId === stored.childId)) {
              this.selectChild(stored);
            }
          }
        },
        error: (err) => {
          console.error('❌ Error loading children:', err);
          this.errorMsg = 'Failed to load children profiles';
          console.error(err);
        }
      });
  }

  selectChild(child: Child): void {
    this.currentChild = child;
    this.childService.setCurrentChild(child);
    this.viewMode = 'activities';
    this.loadActivities();
    this.loadProgress();
  }

  showCreateProfile(): void {
    this.viewMode = 'profile-create';
    this.newChild = { name: '', age: 5 };
  }

  createChildProfile(): void {
    if (!this.newChild.name || !this.newChild.age) {
      this.errorMsg = 'Please fill in all fields';
      return;
    }
    this.loading = true;
    const childData: Child = {
      name: this.newChild.name,
      age: this.newChild.age,
      avatar: this.newChild.avatar || 'default-avatar.png',
      parentId: 1,
      xp: 0,
      level: 1
    };
    this.childService.createChild(childData)
      .pipe(takeUntil(this.destroy$), finalize(() => this.loading = false))
      .subscribe({
        next: (child) => {
          this.successMsg = 'Profile created successfully!';
          this.children.push(child);
          this.selectChild(child);
          setTimeout(() => this.successMsg = '', 3000);
        },
        error: (err) => {
          this.errorMsg = 'Failed to create profile';
          console.error(err);
        }
      });
  }

  backToProfileSelect(): void {
    this.viewMode = 'profile-select';
    this.currentChild = null;
    this.childService.clearCurrentChild();
  }

  loadActivities(): void {
    if (!this.currentChild) return;
    this.loading = true;
    this.activityService.getActivitiesForChild(this.currentChild.childId!)
      .pipe(takeUntil(this.destroy$), finalize(() => this.loading = false))
      .subscribe({
        next: (activities) => {
          this.activities = activities;
          this.filterActivities();
        },
        error: (err) => {
          this.errorMsg = 'Failed to load activities';
          console.error(err);
        }
      });
  }

  filterActivities(): void {
    this.filteredActivities = this.activities.filter(activity => {
      const matchCategory = !this.selectedCategory || activity.category === this.selectedCategory;
      const matchDifficulty = !this.selectedDifficulty || activity.difficulty === this.selectedDifficulty;
      const matchSearch = !this.searchQuery ||
        activity.title.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
        activity.description.toLowerCase().includes(this.searchQuery.toLowerCase());
      return matchCategory && matchDifficulty && matchSearch;
    });
  }

  onCategoryChange(): void { this.filterActivities(); }
  onDifficultyChange(): void { this.filterActivities(); }
  onSearchChange(): void { this.filterActivities(); }

  startActivity(activity: Activity): void {
    if (!activity || !activity.activityId) {
      this.errorMsg = 'Invalid activity selected';
      return;
    }
    this.loading = true;
    this.questionService.getQuestionsForActivity(activity.activityId)
      .pipe(takeUntil(this.destroy$), finalize(() => this.loading = false))
      .subscribe({
        next: (questions) => {
          if (questions.length === 0) {
            this.errorMsg = 'No questions available for this activity yet';
            return;
          }
          this.quizSession = {
            activity, questions, currentQuestionIndex: 0,
            answers: [], startTime: new Date(), timeRemaining: 60
          };
          this.viewMode = 'quiz';
          this.startQuizTimer();
        },
        error: (err) => {
          this.errorMsg = 'Failed to load quiz questions';
          console.error(err);
        }
      });
  }

  startQuizTimer(): void {
    this.quizTimer = setInterval(() => {
      if (this.quizSession && this.quizSession.timeRemaining > 0) {
        this.quizSession.timeRemaining--;
      } else if (this.quizSession) {
        this.submitAnswer('');
      }
    }, 1000);
  }

  get currentQuestion(): Question | null {
    if (!this.quizSession) return null;
    return this.quizSession.questions[this.quizSession.currentQuestionIndex];
  }

  get questionProgress(): string {
    if (!this.quizSession) return '';
    return `${this.quizSession.currentQuestionIndex + 1} / ${this.quizSession.questions.length}`;
  }

  selectAnswer(answer: string): void { this.selectedAnswer = answer; }

  submitAnswer(answer: string): void {
    if (!this.quizSession || !this.currentQuestion) return;
    const question = this.currentQuestion;
    const isCorrect = answer === question.correctAnswer;
    const timeSpent = 60 - this.quizSession.timeRemaining;
    const answerObj: Answer = {
      questionId: question.questionId, selectedAnswer: answer, isCorrect, timeSpent
    };
    this.quizSession.answers.push(answerObj);
    this.isCorrect = isCorrect;
    this.showFeedback = true;
    setTimeout(() => {
      this.showFeedback = false;
      this.selectedAnswer = '';
      this.nextQuestion();
    }, 2000);
  }

  nextQuestion(): void {
    if (!this.quizSession) return;
    if (this.quizSession.currentQuestionIndex < this.quizSession.questions.length - 1) {
      this.quizSession.currentQuestionIndex++;
      this.quizSession.timeRemaining = 60;
    } else {
      this.finishQuiz();
    }
  }

  finishQuiz(): void {
    if (!this.quizSession || !this.currentChild) return;
    clearInterval(this.quizTimer);
    const correctAnswers = this.quizSession.answers.filter(a => a.isCorrect).length;
    const totalQuestions = this.quizSession.questions.length;
    const totalTimeSpent = this.quizSession.answers.reduce((sum, a) => sum + a.timeSpent, 0);
    const xpEarned = this.gamificationService.calculateXp(correctAnswers, totalQuestions, totalTimeSpent);
    const attempt: QuizAttempt = {
      childId: this.currentChild.childId!, activityId: this.quizSession.activity.activityId,
      answers: this.quizSession.answers, score: correctAnswers,
      totalQuestions, xpEarned, timeSpent: totalTimeSpent
    };
    this.loading = true;
    this.questionService.submitQuizAttempt(attempt)
      .pipe(takeUntil(this.destroy$), finalize(() => this.loading = false))
      .subscribe({
        next: (result) => {
          this.quizResults = result;
          this.viewMode = 'results';
          this.updateChildXp(xpEarned);
          this.checkForNewBadges();
        },
        error: (err) => {
          this.errorMsg = 'Failed to submit quiz';
          console.error(err);
        }
      });
  }

  updateChildXp(xp: number): void {
    if (!this.currentChild) return;
    this.childService.updateChildXp(this.currentChild.childId!, xp)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (updatedChild) => { 
          this.currentChild = updatedChild;
          this.loadProgress(); // Reload progress to reflect XP changes
        },
        error: (err) => console.error('Failed to update XP', err)
      });
  }

  checkForNewBadges(): void {
    if (!this.currentChild) return;
    this.gamificationService.checkForNewBadges(this.currentChild.childId!)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (badges) => { this.newBadges = badges; },
        error: (err) => console.error('Failed to check badges', err)
      });
  }

  retryQuiz(): void {
    if (this.quizResults) {
      const activity = this.activities.find(a => a.activityId === this.quizResults!.activityId);
      if (activity) { this.startActivity(activity); }
    }
  }

  backToActivities(): void {
    this.viewMode = 'activities';
    this.quizSession = null;
    this.quizResults = null;
    this.newBadges = [];
    this.loadActivities();
  }

  showProgress(): void {
    this.viewMode = 'progress';
    this.loadProgress();
    this.loadLeaderboard();
  }

  loadProgress(): void {
    if (!this.currentChild) return;
    this.gamificationService.getChildProgress(this.currentChild.childId!)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (progress) => { 
          this.progress = progress;
          // Initialize badges as empty array if undefined
          if (!this.progress.badges) {
            this.progress.badges = [];
          }
        },
        error: (err) => { 
          console.error('Failed to load progress', err);
          // Initialize with default values on error
          this.progress = {
            childId: this.currentChild!.childId!,
            totalXp: 0,
            currentLevel: 1,
            xpToNextLevel: 1000,
            totalActivitiesCompleted: 0,
            accuracyPercentage: 0,
            currentStreak: 0,
            longestStreak: 0,
            badges: [],
            recentActivities: []
          };
        }
      });
  }

  loadLeaderboard(): void {
    this.gamificationService.getLeaderboard(10)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (leaderboard) => { this.leaderboard = leaderboard; },
        error: (err) => { 
          console.error('Failed to load leaderboard', err);
          // Initialize as empty array on error
          this.leaderboard = [];
        }
      });
  }

  get xpPercentage(): number {
    if (!this.progress) return 0;
    const xpInCurrentLevel = this.progress.totalXp % 1000;
    return (xpInCurrentLevel / 1000) * 100;
  }

  getCategoryIcon(category: ActivityCategory): string {
    const icons: Record<ActivityCategory, string> = {
      [ActivityCategory.VOCABULARY]: '📚', [ActivityCategory.GRAMMAR]: '✏️',
      [ActivityCategory.LISTENING]: '👂', [ActivityCategory.READING]: '📖',
      [ActivityCategory.SPEAKING]: '🗣️', [ActivityCategory.WRITING]: '✍️'
    };
    return icons[category] || '📝';
  }

  getDifficultyColor(difficulty: DifficultyLevel): string {
    const colors: Record<DifficultyLevel, string> = {
      [DifficultyLevel.BEGINNER]: 'bg-green-100 text-green-800',
      [DifficultyLevel.INTERMEDIATE]: 'bg-yellow-100 text-yellow-800',
      [DifficultyLevel.ADVANCED]: 'bg-red-100 text-red-800'
    };
    return colors[difficulty] || 'bg-gray-100 text-gray-800';
  }
}
