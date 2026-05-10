import { Component, OnInit } from '@angular/core';
import { CourseService } from '../../../../../core/services/course.service';
import { Course } from '../../../../../core/models/course.model';
import { Router } from '@angular/router';

@Component({
    selector: 'app-admin-courses',
    templateUrl: './courses.component.html',
    styleUrls: ['./courses.component.scss']
})
export class AdminCoursesComponent implements OnInit {
    pageTitle: string = 'Courses Management';
    pageIcon: string = '📚';
    courses: Course[] = [];
    isLoading = true;
    error: string | null = null;
    successMessage: string | null = null;

    // Filters & Search
    searchTerm: string = '';
    levelFilter: string = 'ALL';
    typeFilter: string = 'ALL';
    sortBy: string = 'title';

    selectedCourse: Course | null = null;
    isViewModalOpen: boolean = false;
    showForm = false;
    isEditing = false;
    editingId: number | null = null;
    isSaving = false;

    newImageFile: File | null = null;
    imagePreviewUrl: string | null = null;

    levels: Array<'A1' | 'A2' | 'B1' | 'B2' | 'C1' | 'C2'> = ['A1', 'A2', 'B1', 'B2', 'C1', 'C2'];

    formData: Course = {
        title: '',
        description: '',
        level: 'A1',
        type: 'General_English',
        price: 0,
        lessonsNumber: 0,
        isHidden: false,
        imageUrl: ''
    };

    constructor(private courseService: CourseService, private router: Router) { }

    ngOnInit(): void { this.loadCourses(); }

    get filteredCourses(): Course[] {
        let filtered = [...this.courses];

        // Search filter
        if (this.searchTerm) {
            const term = this.searchTerm.toLowerCase();
            filtered = filtered.filter(c => 
                c.title.toLowerCase().includes(term) || 
                c.description?.toLowerCase().includes(term)
            );
        }

        // Level filter
        if (this.levelFilter !== 'ALL') {
            filtered = filtered.filter(c => c.level === this.levelFilter);
        }

        // Type filter
        if (this.typeFilter !== 'ALL') {
            filtered = filtered.filter(c => c.type === this.typeFilter);
        }

        // Sort
        filtered.sort((a, b) => {
            if (this.sortBy === 'title') {
                return a.title.localeCompare(b.title);
            }
            if (this.sortBy === 'price') {
                return (a.price || 0) - (b.price || 0);
            }
            if (this.sortBy === 'lessons') {
                return (a.lessonsNumber || 0) - (b.lessonsNumber || 0);
            }
            return 0;
        });

        return filtered;
    }

    getLevelBadgeClass(level: string): string {
        switch (level) {
            case 'A1':
            case 'A2': return 'bg-green-50 text-green-600 border border-green-100';
            case 'B1':
            case 'B2': return 'bg-blue-50 text-blue-600 border border-blue-100';
            case 'C1':
            case 'C2': return 'bg-[#FFDDD2] text-[#E29578] shadow-sm';
            default: return 'bg-gray-100 text-gray-500';
        }
    }

    viewCourse(course: Course): void {
        this.selectedCourse = course;
        this.isViewModalOpen = true;
    }

    loadCourses(): void {
        this.isLoading = true;
        this.error = null;
        this.courseService.getAll().subscribe({
            next: data => { this.courses = data; this.isLoading = false; },
            error: () => { this.error = 'Failed to load courses. Make sure the backend is running.'; this.isLoading = false; }
        });
    }

    openAddForm(): void {
        this.isEditing = false;
        this.editingId = null;
        this.formData = { title: '', description: '', level: 'A1', type: 'General_English', price: 0, lessonsNumber: 0, isHidden: false, imageUrl: '' };
        this.newImageFile = null;
        this.imagePreviewUrl = null;
        this.showForm = true;
        this.successMessage = null;
        this.error = null;
    }

    openEditForm(course: Course): void {
        this.isEditing = true;
        this.editingId = course.courseId!;
        this.formData = {
            title: course.title,
            description: course.description,
            level: course.level,
            type: course.type,
            price: course.price,
            lessonsNumber: course.lessonsNumber,
            isHidden: course.isHidden,
            imageUrl: course.imageUrl ?? ''
        };
        this.newImageFile = null;
        this.imagePreviewUrl = null;
        this.showForm = true;
        this.successMessage = null;
        this.error = null;
    }

    cancelForm(): void {
        this.showForm = false;
        this.newImageFile = null;
        this.imagePreviewUrl = null;
    }

    onImageSelected(event: Event): void {
        const input = event.target as HTMLInputElement;
        if (!input.files?.length) return;
        const file = input.files[0];

        if (!file.type.startsWith('image/')) {
            this.error = 'Only image files are allowed.';
            return;
        }

        this.newImageFile = file;
        const reader = new FileReader();
        reader.onload = () => this.imagePreviewUrl = reader.result as string;
        reader.readAsDataURL(file);
    }

    saveCourse(): void {
        if (!this.formData.title.trim()) { this.error = 'Title is required.'; return; }
        this.isSaving = true;
        this.error = null;

        const obs = this.isEditing && this.editingId !== null
            ? this.courseService.update(this.editingId, this.formData)
            : this.courseService.create(this.formData);

        obs.subscribe({
            next: (saved) => {
                if (this.newImageFile && saved.courseId) {
                    this.courseService.uploadImage(saved.courseId, this.newImageFile).subscribe({
                        next: () => {
                            this.isSaving = false;
                            this.showForm = false;
                            this.newImageFile = null;
                            this.imagePreviewUrl = null;
                            this.loadCourses();
                            this.showSuccess(this.isEditing ? 'Course updated successfully!' : 'Course created successfully!');
                        },
                        error: () => {
                            this.error = 'Course saved but image upload failed.';
                            this.isSaving = false;
                            this.loadCourses();
                        }
                    });
                } else {
                    this.isSaving = false;
                    this.showForm = false;
                    this.loadCourses();
                    this.showSuccess(this.isEditing ? 'Course updated successfully!' : 'Course created successfully!');
                }
            },
            error: () => { this.error = 'Failed to save course.'; this.isSaving = false; }
        });
    }

    deleteCourse(course: Course): void {
        if (!confirm(`Delete "${course.title}"? This action cannot be undone.`)) return;
        this.courseService.delete(course.courseId!).subscribe({
            next: () => {
                this.courses = this.courses.filter(c => c.courseId !== course.courseId);
                this.showSuccess('Course deleted successfully!');
            },
            error: () => { this.error = 'Failed to delete course.'; }
        });
    }

    manageCourse(courseId: number | undefined): void {
        if (!courseId) return;
        this.router.navigate(['/admin/courses', courseId]);
    }

    toggleVisibility(course: Course): void {
        if (course.courseId === undefined) return;
        this.courseService.toggleVisibility(course.courseId).subscribe({
            next: saved => {
                const idx = this.courses.findIndex(c => c.courseId === course.courseId);
                if (idx !== -1) this.courses[idx] = saved;
                this.showSuccess(`Course ${saved.isHidden ? 'hidden' : 'made visible'} successfully!`);
            },
            error: () => { this.error = 'Failed to update visibility.'; }
        });
    }

    private showSuccess(message: string): void {
        this.successMessage = message;
        this.error = null;
        setTimeout(() => this.successMessage = null, 3000);
    }
}