import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { forkJoin } from 'rxjs';
import { CourseService } from '../../../../../core/services/course.service';
import { LessonService } from '../../../../../core/services/lesson.service';
import { QuizService } from '../../../../../core/services/quiz.service';
import { Course } from '../../../../../core/models/course.model';
import { Lesson } from '../../../../../core/models/lesson.model';
import { Quiz } from '../../../../../core/models/quiz.model';

@Component({
  selector: 'app-course-details',
  templateUrl: './course-details.component.html',
  styleUrls: ['./course-details.component.scss']
})
export class CourseDetailsComponent implements OnInit {

  courseId!: number;
  course: Course | null = null;

  allLessons: Lesson[] = [];
  allQuizzes: Quiz[] = [];

  selectedLessonIds: Set<number> = new Set();
  selectedQuizId: number | null = null;

  isLoading = true;
  isAssigningLessons = false;
  isAssigningQuiz = false;
  error: string | null = null;
  successMessage: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private courseService: CourseService,
    private lessonService: LessonService,
    private quizService: QuizService
  ) {}

  ngOnInit(): void {
    this.courseId = Number(this.route.snapshot.paramMap.get('id'));
    this.load();
  }

  goBack(): void {
    this.router.navigate(['/admin/courses']);
  }

  load(): void {
    this.isLoading = true;
    this.error = null;

    forkJoin({
      course: this.courseService.getById(this.courseId),
      allLessons: this.lessonService.getAll(),
      allQuizzes: this.quizService.getAll()
    }).subscribe({
      next: ({ course, allLessons, allQuizzes }) => {
        this.course = course;
        this.allLessons = allLessons;
        this.allQuizzes = allQuizzes;
        this.selectedLessonIds = new Set((course.lessons ?? []).map((l: Lesson) => l.lessonId!).filter(Boolean));
        this.selectedQuizId = course.quiz?.quizId ?? null;
        this.isLoading = false;
      },
      error: () => { this.error = 'Failed to load course.'; this.isLoading = false; }
    });
  }

  // ─── Lesson Assignment ───────────────────────────────────────────────────────

  toggleLesson(id: number): void {
    if (this.selectedLessonIds.has(id)) {
      this.selectedLessonIds.delete(id);
    } else {
      this.selectedLessonIds.add(id);
    }
  }

  isLessonSelected(id: number): boolean {
    return this.selectedLessonIds.has(id);
  }

  saveAssignedLessons(): void {
    this.isAssigningLessons = true;
    const ids = Array.from(this.selectedLessonIds);
    this.courseService.assignLessons(this.courseId, ids).subscribe({
      next: updated => {
        this.course = updated;
        this.selectedLessonIds = new Set((updated.lessons ?? []).map((l: Lesson) => l.lessonId!).filter(Boolean));
        this.isAssigningLessons = false;
        this.showSuccess('Lessons assigned successfully!');
      },
      error: () => { this.error = 'Failed to assign lessons.'; this.isAssigningLessons = false; }
    });
  }

  // ─── Quiz Assignment ─────────────────────────────────────────────────────────

  saveAssignedQuiz(): void {
    if (this.selectedQuizId === null) return;
    this.isAssigningQuiz = true;
    this.courseService.assignQuiz(this.courseId, this.selectedQuizId).subscribe({
      next: updated => {
        this.course = updated;
        this.selectedQuizId = updated.quiz?.quizId ?? null;
        this.isAssigningQuiz = false;
        this.showSuccess('Quiz assigned successfully!');
      },
      error: () => { this.error = 'Failed to assign quiz.'; this.isAssigningQuiz = false; }
    });
  }

  private showSuccess(message: string): void {
    this.successMessage = message;
    this.error = null;
    setTimeout(() => this.successMessage = null, 3000);
  }
}