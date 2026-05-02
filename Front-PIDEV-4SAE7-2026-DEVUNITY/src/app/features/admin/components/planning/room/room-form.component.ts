import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Room } from '../../../../../core/models/room.model';
import { RoomService } from '../../../../../core/services/room.service';

@Component({
    selector: 'app-room-form',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule],
    templateUrl: './room-form.component.html'
})
export class RoomFormComponent implements OnInit {
    @Input() room: Room | null = null;
    @Output() saved = new EventEmitter<void>();
    @Output() cancelled = new EventEmitter<void>();

    form!: FormGroup;
    isSaving = false;
    error: string | null = null;

    constructor(private fb: FormBuilder, private roomService: RoomService) { }

    ngOnInit(): void {
        this.form = this.fb.group({
            name: [this.room?.name ?? '', [Validators.required, Validators.minLength(2)]],
            capacity: [this.room?.capacity ?? 1, [Validators.required, Validators.min(1)]],
            level: [this.room?.level ?? 1, [Validators.required, Validators.min(1)]],
            available: [this.room?.available ?? true]
        });
    }

    get isEditing(): boolean { return !!this.room?.roomId; }

    save(): void {
        if (this.form.invalid) { this.form.markAllAsTouched(); return; }
        this.isSaving = true;
        this.error = null;
        const payload: Room = this.form.value;
        const obs = this.isEditing
            ? this.roomService.update(this.room!.roomId!, payload)
            : this.roomService.create(payload);
        obs.subscribe({
            next: () => { this.isSaving = false; this.saved.emit(); },
            error: () => { this.error = 'Failed to save room.'; this.isSaving = false; }
        });
    }
}
