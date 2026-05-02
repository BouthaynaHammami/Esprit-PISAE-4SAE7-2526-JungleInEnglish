import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RoomScheduleComplaint } from '../../../../../core/models/room-complaint.model';
import { RoomScheduleComplaintService } from '../../../../../core/services/room-complaint.service';

@Component({
    selector: 'app-complaint-detail',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule],
    templateUrl: './complaint-detail.component.html'
})
export class ComplaintDetailComponent implements OnInit {
    @Input() complaint!: RoomScheduleComplaint;
    @Output() answered = new EventEmitter<RoomScheduleComplaint>();
    @Output() closed = new EventEmitter<void>();

    answerForm!: FormGroup;
    isSubmitting = false;
    error: string | null = null;
    showAnswerForm = false;

    constructor(private fb: FormBuilder, private complaintService: RoomScheduleComplaintService) { }

    ngOnInit(): void {
        this.answerForm = this.fb.group({
            answer: [this.complaint.answer ?? '', [Validators.required, Validators.minLength(5)]]
        });
        this.showAnswerForm = !this.complaint.status;
    }

    submitAnswer(): void {
        if (this.answerForm.invalid) { this.answerForm.markAllAsTouched(); return; }
        this.isSubmitting = true;
        this.error = null;
        const answer: string = this.answerForm.get('answer')!.value;
        this.complaintService.answer(this.complaint.complaintId!, answer).subscribe({
            next: updated => {
                this.isSubmitting = false;
                this.showAnswerForm = false;
                this.answered.emit(updated);
            },
            error: () => { this.error = 'Failed to submit answer.'; this.isSubmitting = false; }
        });
    }
}
