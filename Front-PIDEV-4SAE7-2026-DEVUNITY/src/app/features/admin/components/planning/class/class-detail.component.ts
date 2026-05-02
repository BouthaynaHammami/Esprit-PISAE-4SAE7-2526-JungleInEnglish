import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ClassEntity } from '../../../../../core/models/class.model';
import { ClassService } from '../../../../../core/services/class.service';
import { LearnerUserLookupService } from '../../../../../core/services/learner-user-lookup.service';

@Component({
    selector: 'app-class-detail',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './class-detail.component.html'
})
export class ClassDetailComponent {
    @Input() cls!: ClassEntity;
    @Output() closed = new EventEmitter<void>();
    @Output() edit = new EventEmitter<ClassEntity>();

    assignUserEmail = '';
    isAssigning = false;
    assignSuccess: string | null = null;
    assignError: string | null = null;

    constructor(
        private classService: ClassService,
        private learnerUserLookupService: LearnerUserLookupService
    ) {}

    onAssign(): void {
        const email = this.assignUserEmail.trim();
        if (!email || !this.cls.classId) return;

        this.isAssigning = true;
        this.assignSuccess = null;
        this.assignError = null;

        this.learnerUserLookupService.getByEmail(email).subscribe({
            next: (user) => {
                const userId = Number(user?.userId);
                if (!userId) {
                    this.assignError = 'Student email not found.';
                    this.isAssigning = false;
                    return;
                }

                this.classService.assignClass(userId, this.cls.classId!).subscribe({
                    next: () => {
                        this.assignSuccess = `Student ${email} assigned to Class ${this.cls.name}.`;
                        this.assignUserEmail = '';
                        this.isAssigning = false;
                    },
                    error: () => {
                        this.assignError = 'Failed to assign student. Please check the email and try again.';
                        this.isAssigning = false;
                    }
                });
            },
            error: () => {
                this.assignError = 'Student email not found.';
                this.isAssigning = false;
            }
        });
    }
}