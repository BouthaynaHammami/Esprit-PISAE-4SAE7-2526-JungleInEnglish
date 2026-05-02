import { Component, OnInit } from '@angular/core';
import { QuizService } from '../../../../core/services/quiz.service';
import { Quiz } from '../../../../core/models/quiz.model';
import { Question } from '../../../../core/models/question.model';

@Component({
    selector: 'app-admin-quizzes',
    templateUrl: './admin-quizzes.component.html',
    styleUrls: ['./admin-quizzes.component.scss']
})
export class AdminQuizzesComponent implements OnInit {
    quizzes: Quiz[] = [];
    isLoading = true;
    error: string | null = null;
    successMessage: string | null = null;

    showForm = false;
    isEditing = false;
    editingId: number | null = null;
    isSaving = false;

    formData: Quiz = { title: '', questions: [] };
    newQuestion: Question = { text: '', optionA: '', optionB: '', optionC: '', optionD: '', correctAnswer: 'A' };

    constructor(private quizService: QuizService) { }

    ngOnInit(): void { this.load(); }

    load(): void {
        this.isLoading = true;
        this.error = null;
        this.quizService.getAll().subscribe({
            next: data => { this.quizzes = data; this.isLoading = false; },
            error: () => { this.error = 'Failed to load quizzes. Make sure the backend is running.'; this.isLoading = false; }
        });
    }

    openAdd(): void {
        this.isEditing = false;
        this.editingId = null;
        this.formData = { title: '', questions: [] };
        this.resetNewQuestion();
        this.showForm = true;
        this.error = null;
    }

    openEdit(q: Quiz): void {
        this.isEditing = true;
        this.editingId = q.quizId!;
        this.formData = { ...q, questions: [...(q.questions ?? [])] };
        this.resetNewQuestion();
        this.showForm = true;
        this.error = null;
    }

    cancel(): void { this.showForm = false; }

    addQuestion(): void {
        if (!this.newQuestion.text.trim()) { this.error = 'Question text is required.'; return; }
        if (!this.newQuestion.optionA.trim() || !this.newQuestion.optionB.trim()) { this.error = 'Options A and B are required.'; return; }
        this.formData.questions = [...(this.formData.questions ?? []), { ...this.newQuestion }];
        this.resetNewQuestion();
        this.error = null;
    }

    removeQuestion(index: number): void {
        this.formData.questions = this.formData.questions!.filter((_, i) => i !== index);
    }

    private resetNewQuestion(): void {
        this.newQuestion = { text: '', optionA: '', optionB: '', optionC: '', optionD: '', correctAnswer: 'A' };
    }

    save(): void {
        if (!this.formData.title?.trim()) { this.error = 'Title is required.'; return; }
        if (!this.formData.questions?.length) { this.error = 'Add at least one question.'; return; }
        this.isSaving = true;
        this.error = null;

        const obs = this.isEditing && this.editingId !== null
            ? this.quizService.update(this.editingId, this.formData)
            : this.quizService.create(this.formData);

        obs.subscribe({
            next: () => {
                this.isSaving = false;
                this.showForm = false;
                this.load();
                this.showSuccess(this.isEditing ? 'Quiz updated successfully!' : 'Quiz created successfully!');
            },
            error: () => { this.error = 'Failed to save quiz.'; this.isSaving = false; }
        });
    }

    delete(q: Quiz): void {
        if (!confirm('Delete this quiz? This action cannot be undone.')) return;
        this.quizService.delete(q.quizId!).subscribe({
            next: () => {
                this.quizzes = this.quizzes.filter(x => x.quizId !== q.quizId);
                this.showSuccess('Quiz deleted successfully!');
            },
            error: () => { this.error = 'Failed to delete quiz.'; }
        });
    }

    private showSuccess(message: string): void {
        this.successMessage = message;
        this.error = null;
        setTimeout(() => this.successMessage = null, 3000);
    }
}