import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { QuizService } from '../../../../core/services/quiz.service';
import { QuizAttemptService } from '../../../../core/services/quiz-attempt.service';
import { Quiz } from '../../../../core/models/quiz.model';
import { Question } from '../../../../core/models/question.model';
import { QuizAttempt } from '../../../../core/models/quiz-attempt.model';

@Component({
    selector: 'app-student-quiz',
    templateUrl: './student-quiz.component.html',
    styleUrls: []
})
export class StudentQuizComponent implements OnInit {

    quiz: Quiz | null = null;
    userId!: number;
    answers: string[] = [];
    currentIndex = 0;
    loading = true;
    submitting = false;
    error = '';
    result: QuizAttempt | null = null;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private quizService: QuizService,
        private quizAttemptService: QuizAttemptService
    ) { }

    ngOnInit(): void {
        const quizId = Number(this.route.snapshot.paramMap.get('id'));
        this.userId = Number(this.route.snapshot.queryParamMap.get('userId'));

        this.quizService.getById(quizId).subscribe({
            next: quiz => {
                this.quiz = quiz;
                this.answers = new Array(quiz.questions?.length ?? 0).fill('');
                this.loading = false;
            },
            error: () => { this.error = 'Failed to load quiz.'; this.loading = false; }
        });
    }

    get currentQuestion(): Question | undefined {
        return this.quiz?.questions?.[this.currentIndex];
    }

    get totalQuestions(): number {
        return this.quiz?.questions?.length ?? 0;
    }

    get isFirst(): boolean { return this.currentIndex === 0; }
    get isLast(): boolean { return this.currentIndex === this.totalQuestions - 1; }

    get allAnswered(): boolean {
        return this.answers.every(a => a !== '');
    }

    getOption(option: string): string | null {
        if (!this.currentQuestion) return null;
        const map: Record<string, string | undefined> = {
            A: this.currentQuestion.optionA,
            B: this.currentQuestion.optionB,
            C: this.currentQuestion.optionC,
            D: this.currentQuestion.optionD,
        };
        return map[option] ?? null;
    }

    selectAnswer(option: string): void {
        this.answers[this.currentIndex] = option;
    }

    next(): void { if (!this.isLast) this.currentIndex++; }
    prev(): void { if (!this.isFirst) this.currentIndex--; }
    goToQuestion(index: number): void { this.currentIndex = index; }

    submit(): void {
        if (!this.quiz?.quizId || !this.allAnswered) return;
        this.submitting = true;

        this.quizAttemptService.submit(this.userId, this.quiz.quizId, this.answers).subscribe({
            next: result => { this.result = result; this.submitting = false; },
            error: () => { this.error = 'Failed to submit quiz.'; this.submitting = false; }
        });
    }

    tryAgain(): void {
        this.result = null;
        this.answers = new Array(this.totalQuestions).fill('');
        this.currentIndex = 0;
    }

    goBack(): void {
        this.router.navigate(['/student/courses']);
    }
}