import { Component, OnInit } from '@angular/core';
import { LessonService } from '../../../../core/services/lesson.service';
import { Lesson } from '../../../../core/models/lesson.model';

@Component({
    selector: 'app-admin-lessons',
    templateUrl: './admin-lessons.component.html',
    styleUrls: ['./admin-lessons.component.scss']
})
export class AdminLessonsComponent implements OnInit {
    pageTitle: string = 'Lessons Management';
    pageIcon: string = '📖';
    lessons: Lesson[] = [];
    isLoading = true;
    error: string | null = null;
    successMessage: string | null = null;

    // Filters & Search
    searchTerm: string = '';
    sortBy: string = 'order';

    selectedLesson: Lesson | null = null;
    isViewModalOpen: boolean = false;
    showForm = false;
    isEditing = false;
    editingId: number | null = null;
    isSaving = false;

    formData: Lesson = { title: '', content: '', file: '', order: 1 };

    constructor(private lessonService: LessonService) { }

    ngOnInit(): void { this.load(); }

    get filteredLessons(): Lesson[] {
        let filtered = [...this.lessons];

        if (this.searchTerm) {
            const term = this.searchTerm.toLowerCase();
            filtered = filtered.filter(l => 
                l.title.toLowerCase().includes(term) || 
                l.content?.toLowerCase().includes(term)
            );
        }

        filtered.sort((a, b) => {
            if (this.sortBy === 'order') return (a.order || 0) - (b.order || 0);
            if (this.sortBy === 'title') return a.title.localeCompare(b.title);
            return 0;
        });

        return filtered;
    }

    viewLesson(lesson: Lesson): void {
        this.selectedLesson = lesson;
        this.isViewModalOpen = true;
    }

    load(): void {
        this.isLoading = true;
        this.error = null;
        this.lessonService.getAll().subscribe({
            next: data => { this.lessons = data; this.isLoading = false; },
            error: () => { this.error = 'Failed to load lessons. Make sure the backend is running.'; this.isLoading = false; }
        });
    }

    openAdd(): void {
        this.isEditing = false;
        this.editingId = null;
        this.formData = { title: '', content: '', file: '', order: this.lessons.length + 1 };
        this.showForm = true;
        this.error = null;
    }

    openEdit(l: Lesson): void {
        this.isEditing = true;
        this.editingId = l.lessonId!;
        this.formData = { ...l };
        this.showForm = true;
        this.error = null;
    }

    cancel(): void { this.showForm = false; }

    save(): void {
        if (!this.formData.title?.trim()) { this.error = 'Title is required.'; return; }
        this.isSaving = true;
        this.error = null;

        const obs = this.isEditing && this.editingId !== null
            ? this.lessonService.update(this.editingId, this.formData)
            : this.lessonService.create(this.formData);

        obs.subscribe({
            next: () => {
                this.isSaving = false;
                this.showForm = false;
                this.load();
                this.showSuccess(this.isEditing ? 'Lesson updated!' : 'Lesson created!');
            },
            error: () => { this.error = 'Failed to save lesson.'; this.isSaving = false; }
        });
    }

    delete(l: Lesson): void {
        if (!confirm(`Delete "${l.title}"? This action cannot be undone.`)) return;
        this.lessonService.delete(l.lessonId!).subscribe({
            next: () => {
                this.lessons = this.lessons.filter(x => x.lessonId !== l.lessonId);
                this.showSuccess('Lesson deleted!');
            },
            error: () => { this.error = 'Failed to delete lesson.'; }
        });
    }

    private showSuccess(message: string): void {
        this.successMessage = message;
        this.error = null;
        setTimeout(() => this.successMessage = null, 3000);
    }
}