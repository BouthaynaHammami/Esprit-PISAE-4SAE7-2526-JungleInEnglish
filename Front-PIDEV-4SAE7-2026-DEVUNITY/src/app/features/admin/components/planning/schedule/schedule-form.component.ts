import { Component, EventEmitter, Output, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ScheduleService } from '../../../../../core/services/schedule.service';
import { LearnerUserLookupService } from '../../../../../core/services/learner-user-lookup.service';
import { ClassService } from '../../../../../core/services/class.service';
import { RoomService } from '../../../../../core/services/room.service';

@Component({
  selector: 'app-schedule-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './schedule-form.component.html'
})
export class ScheduleFormComponent {
  @Output() saved = new EventEmitter<void>();
  @Output() cancelled = new EventEmitter<void>();

  className = '';
  tutorEmail = '';
  courseId: number | null = null;
  roomName = '';
  startTime: string = '';

  isSubmitting = false;
  error: string | null = null;

  constructor(
    private scheduleService: ScheduleService,
    private learnerUserLookupService: LearnerUserLookupService,
    private classService: ClassService,
    private roomService: RoomService
  ) { }

  submit(): void {
    const tutorEmail = this.tutorEmail.trim();
    const className = this.className.trim();
    const roomName = this.roomName.trim();
    if (!className || !tutorEmail || !this.courseId || !roomName || !this.startTime) {
      this.error = 'Please fill in all fields.';
      return;
    }
    this.isSubmitting = true;
    this.error = null;

    // Convert datetime-local value to ISO format for the backend
    const isoStart = new Date(this.startTime).toISOString().slice(0, 19);

    this.learnerUserLookupService.getByEmail(tutorEmail).subscribe({
      next: (user) => {
        const tutorId = Number(user?.userId);
        if (!tutorId) {
          this.isSubmitting = false;
          this.error = 'Tutor email not found.';
          return;
        }

        this.classService.getByName(className).subscribe({
          next: (cls) => {
            const classId = Number(cls?.classId);
            if (!classId) {
              this.isSubmitting = false;
              this.error = 'Class name not found.';
              return;
            }

            this.roomService.getByName(roomName).subscribe({
              next: (room) => {
                const roomId = Number(room?.roomId);
                if (!roomId) {
                  this.isSubmitting = false;
                  this.error = 'Room name not found.';
                  return;
                }

                this.scheduleService.createCourseSchedule(
                  classId, tutorId, this.courseId!, roomId, isoStart
                ).subscribe({
                  next: () => { this.isSubmitting = false; this.saved.emit(); },
                  error: (err) => {
                    this.isSubmitting = false;
                    this.error = err.error || 'Failed to create schedule. Check availability.';
                  }
                });
              },
              error: () => {
                this.isSubmitting = false;
                this.error = 'Room name not found.';
              }
            });
          },
          error: () => {
            this.isSubmitting = false;
            this.error = 'Class name not found.';
          }
        });
      },
      error: () => {
        this.isSubmitting = false;
        this.error = 'Tutor email not found.';
      }
    });
  }

  cancel(): void { this.cancelled.emit(); }
}
