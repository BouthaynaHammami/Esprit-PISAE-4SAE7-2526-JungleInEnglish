import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { ChildService } from '../../../../core/services/language/child.service';
import { EnglishActivityService } from '../../../../core/services/language/english-activity.service';
import { AuthService } from '../../../../core/services/auth.service';
import { Child, Activity, ActivityCategory, DifficultyLevel } from '../../../../core/models/english-kids.model';

@Component({
  selector: 'app-student-english-kids',
  templateUrl: './student-english-kids.component.html',
  styleUrls: ['./student-english-kids.component.scss']
})
export class StudentEnglishKidsComponent implements OnInit, OnDestroy {

  children: Child[] = [];
  selectedChild: Child | null = null;
  activities: Activity[] = [];
  filteredActivities: Activity[] = [];

  loading = false;
  activitiesLoading = false;

  searchQuery = '';
  selectedCategory: ActivityCategory | '' = '';
  selectedDifficulty: DifficultyLevel | '' = '';

  isParent = false;
  parentId: number | null = null;
  noChildSelectedError = false;

  categories = Object.values(ActivityCategory);
  difficulties = Object.values(DifficultyLevel);

  private subs = new Subscription();

  constructor(
    private childService: ChildService,
    private activityService: EnglishActivityService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.isParent = localStorage.getItem('devunity_user_type') === 'parent';
    this.parentId = this.authService.getUserId();
    this.loadChildren();
    this.loadActivities();
  }

  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }

  loadChildren(): void {
    this.loading = true;
    const sub = this.childService.getAllChildren().subscribe({
      next: (all) => {
        if (this.isParent && this.parentId) {
          this.children = all.filter(c => c.parentId === this.parentId);
        } else {
          this.children = all;
        }
        // Auto-select first child
        if (this.children.length > 0 && !this.selectedChild) {
          this.selectedChild = this.children[0];
        }
        this.loading = false;
      },
      error: () => { this.loading = false; }
    });
    this.subs.add(sub);
  }

  loadActivities(): void {
    this.activitiesLoading = true;
    const sub = this.activityService.getAllActivities().subscribe({
      next: (acts) => {
        this.activities = acts || [];
        this.applyFilters();
        this.activitiesLoading = false;
      },
      error: () => { this.activitiesLoading = false; }
    });
    this.subs.add(sub);
  }

  selectChild(child: Child): void {
    this.selectedChild = child;
  }

  applyFilters(): void {
    let result = [...this.activities];
    const q = this.searchQuery.toLowerCase().trim();
    if (q) {
      result = result.filter(a =>
        a.title.toLowerCase().includes(q) ||
        a.description.toLowerCase().includes(q)
      );
    }
    if (this.selectedCategory) {
      result = result.filter(a => a.category === this.selectedCategory);
    }
    if (this.selectedDifficulty) {
      result = result.filter(a => a.difficulty === this.selectedDifficulty);
    }
    this.filteredActivities = result;
  }

  clearFilters(): void {
    this.searchQuery = '';
    this.selectedCategory = '';
    this.selectedDifficulty = '';
    this.applyFilters();
  }

  startActivity(activity: Activity): void {
    // If parent view, a child must be selected
    if (this.isParent && !this.selectedChild) {
      this.noChildSelectedError = true;
      setTimeout(() => this.noChildSelectedError = false, 3000);
      return;
    }
    this.noChildSelectedError = false;
    // Store pending activity so parent-dashboard can auto-start it
    sessionStorage.setItem('pendingActivityId', String(activity.activityId));
    // Store selected child so parent-dashboard picks it up
    if (this.selectedChild) {
      this.childService.setCurrentChild(this.selectedChild);
    }
    this.router.navigate(['/student/parent-dashboard']);
  }

  getCategoryIcon(category: ActivityCategory): string {
    const map: Record<ActivityCategory, string> = {
      [ActivityCategory.VOCABULARY]: '📖',
      [ActivityCategory.GRAMMAR]: '✏️',
      [ActivityCategory.LISTENING]: '🎧',
      [ActivityCategory.READING]: '📚',
      [ActivityCategory.SPEAKING]: '🎙️',
      [ActivityCategory.WRITING]: '📝'
    };
    return map[category] || '📘';
  }

  getDifficultyColor(difficulty: DifficultyLevel): string {
    const map: Record<DifficultyLevel, string> = {
      [DifficultyLevel.BEGINNER]: '#22c55e',
      [DifficultyLevel.INTERMEDIATE]: '#f59e0b',
      [DifficultyLevel.ADVANCED]: '#ef4444'
    };
    return map[difficulty] || '#83C5BE';
  }

  getChildLevel(child: Child): number {
    return child.level || 1;
  }

  getChildXp(child: Child): number {
    return child.xp || 0;
  }
}
