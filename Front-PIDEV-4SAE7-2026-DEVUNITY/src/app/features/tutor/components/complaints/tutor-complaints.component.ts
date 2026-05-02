import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../../core/services/auth.service';
import { RoomScheduleComplaint } from '../../../../core/models/room-complaint.model';
import { RoomScheduleComplaintService } from '../../../../core/services/room-complaint.service';

@Component({
    selector: 'app-tutor-complaints',
    templateUrl: './tutor-complaints.component.html'
})
export class TutorComplaintsComponent implements OnInit {
    pageTitle: string = 'Planning Complaints';
    complaints: RoomScheduleComplaint[] = [];
    isLoading = true;
    error: string | null = null;
    successMessage: string | null = null;

    constructor(
        private authService: AuthService,
        private complaintService: RoomScheduleComplaintService
    ) { }

    ngOnInit(): void {
        this.loadMine();
    }

    loadMine(): void {
        const email = this.authService.getUserEmail();
        if (!email) {
            this.error = 'Unable to resolve connected tutor email.';
            this.isLoading = false;
            return;
        }

        this.isLoading = true;
        this.error = null;

        this.complaintService.getByComplainantEmail(email, 'TUTOR').subscribe({
            next: (data) => {
                this.complaints = (data || []).sort((a, b) => (b.complaintId || 0) - (a.complaintId || 0));
                this.isLoading = false;
            },
            error: () => {
                this.error = 'Failed to load your complaints.';
                this.isLoading = false;
            }
        });
    }

    deleteComplaint(complaint: RoomScheduleComplaint): void {
        if (!complaint.complaintId) {
            return;
        }

        if (!confirm(`Delete complaint #${complaint.complaintId}?`)) {
            return;
        }

        this.complaintService.delete(complaint.complaintId).subscribe({
            next: () => {
                this.complaints = this.complaints.filter((c) => c.complaintId !== complaint.complaintId);
                this.flash('Complaint deleted.');
            },
            error: () => {
                this.error = 'Failed to delete complaint.';
            }
        });
    }

    private flash(message: string): void {
        this.successMessage = message;
        setTimeout(() => this.successMessage = null, 3000);
    }
}
