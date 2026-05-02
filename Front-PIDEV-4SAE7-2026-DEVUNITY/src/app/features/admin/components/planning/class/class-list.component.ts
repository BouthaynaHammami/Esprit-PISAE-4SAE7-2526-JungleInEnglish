import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClassEntity } from '../../../../../core/models/class.model';
import { ClassService } from '../../../../../core/services/class.service';
import { ClassFormComponent } from './class-form.component';
import { ClassDetailComponent } from './class-detail.component';

@Component({
    selector: 'app-class-list',
    standalone: true,
    imports: [CommonModule, ClassFormComponent, ClassDetailComponent],
    templateUrl: './class-list.component.html'
})
export class ClassListComponent implements OnInit {
    classes: ClassEntity[] = [];
    isLoading = true;
    error: string | null = null;
    successMessage: string | null = null;
    showForm = false;
    editingClass: ClassEntity | null = null;
    viewingClass: ClassEntity | null = null;

    constructor(private classService: ClassService) { }

    ngOnInit(): void { this.load(); }

    load(): void {
        this.isLoading = true;
        this.error = null;
        this.classService.getAll().subscribe({
            next: data => { this.classes = data; this.isLoading = false; },
            error: () => { this.error = 'Failed to load classes.'; this.isLoading = false; }
        });
    }

    openAdd(): void {
        this.editingClass = null;
        this.viewingClass = null;
        this.showForm = true;
    }

    openEdit(cls: ClassEntity): void {
        this.viewingClass = null;
        this.editingClass = cls;
        this.showForm = true;
    }

    openDetail(cls: ClassEntity): void {
        this.showForm = false;
        this.viewingClass = cls;
        this.editingClass = null;
    }

    onSaved(): void {
        this.showForm = false;
        this.editingClass = null;
        this.load();
        this.flash('Class saved successfully!');
    }

    onCancelled(): void {
        this.showForm = false;
        this.editingClass = null;
    }

    delete(cls: ClassEntity): void {
        if (!confirm(`Delete Class ${cls.name}? This cannot be undone.`)) return;
        this.classService.delete(cls.classId!).subscribe({
            next: () => {
                this.classes = this.classes.filter(c => c.classId !== cls.classId);
                if (this.viewingClass?.classId === cls.classId) this.viewingClass = null;
                this.flash('Class deleted.');
            },
            error: () => { this.error = 'Failed to delete class.'; }
        });
    }

    private flash(msg: string): void {
        this.successMessage = msg;
        setTimeout(() => this.successMessage = null, 3000);
    }
}
