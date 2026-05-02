import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ClassEntity } from '../../../../../core/models/class.model';
import { ClassService } from '../../../../../core/services/class.service';

@Component({
    selector: 'app-class-form',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule],
    templateUrl: './class-form.component.html'
})
export class ClassFormComponent implements OnInit {
    @Input() cls: ClassEntity | null = null;
    @Output() saved = new EventEmitter<void>();
    @Output() cancelled = new EventEmitter<void>();

    form!: FormGroup;
    isSaving = false;
    error: string | null = null;

    constructor(private fb: FormBuilder, private classService: ClassService) { }

    ngOnInit(): void {
        this.form = this.fb.group({
            name: [this.cls?.name ?? '', [Validators.required, Validators.minLength(2)]],
            numberStudents: [this.cls?.numberStudents ?? 1, [Validators.required, Validators.min(1)]],
            level: [this.cls?.level ?? '', [Validators.required, Validators.minLength(1)]]
        });
    }

    get isEditing(): boolean { return !!this.cls?.classId; }

    save(): void {
        if (this.form.invalid) { this.form.markAllAsTouched(); return; }
        this.isSaving = true;
        this.error = null;
        const payload: ClassEntity = this.form.value;
        const obs = this.isEditing
            ? this.classService.update(this.cls!.classId!, payload)
            : this.classService.create(payload);
        obs.subscribe({
            next: () => { this.isSaving = false; this.saved.emit(); },
            error: () => { this.error = 'Failed to save class.'; this.isSaving = false; }
        });
    }
}
