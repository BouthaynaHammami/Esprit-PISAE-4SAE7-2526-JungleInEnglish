import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RoomScheduleComplaint } from '../../../../../core/models/room-complaint.model';
import { RoomScheduleComplaintService } from '../../../../../core/services/room-complaint.service';
import { ComplaintDetailComponent } from './complaint-detail.component';


@Component({
    selector: 'app-complaint-list',
    standalone: true,
    imports: [CommonModule, ComplaintDetailComponent],
    templateUrl: './complaint-list.component.html'
})
export class ComplaintListComponent implements OnInit {
    complaints: RoomScheduleComplaint[] = [];
    isLoading = true;
    error: string | null = null;
    successMessage: string | null = null;
    viewingComplaint: RoomScheduleComplaint | null = null;
    filter: 'all' | 'pending' = 'all';

    constructor(private complaintService: RoomScheduleComplaintService) { }

    ngOnInit(): void { this.load(); }

    load(): void {
        this.isLoading = true;
        this.error = null;
        const obs = this.filter === 'pending'
            ? this.complaintService.getPending()
            : this.complaintService.getAll();
        obs.subscribe({
            next: data => { this.complaints = data; this.isLoading = false; },
            error: () => { this.error = 'Failed to load complaints.'; this.isLoading = false; }
        });
    }

    setFilter(f: 'all' | 'pending'): void {
        this.filter = f;
        this.viewingComplaint = null;
        this.load();
    }

    openDetail(c: RoomScheduleComplaint): void {
        this.viewingComplaint = c;
    }

    onAnswered(updated: RoomScheduleComplaint): void {
        const idx = this.complaints.findIndex(c => c.complaintId === updated.complaintId);
        if (idx !== -1) this.complaints[idx] = updated;
        this.viewingComplaint = updated;
        this.flash('Answer submitted successfully!');
    }

    onDetailClosed(): void {
        this.viewingComplaint = null;
    }

    delete(c: RoomScheduleComplaint): void {
        if (!confirm(`Delete complaint #${c.complaintId}?`)) return;
        this.complaintService.delete(c.complaintId!).subscribe({
            next: () => {
                this.complaints = this.complaints.filter(x => x.complaintId !== c.complaintId);
                if (this.viewingComplaint?.complaintId === c.complaintId) this.viewingComplaint = null;
                this.flash('Complaint deleted.');
            },
            error: () => { this.error = 'Failed to delete complaint.'; }
        });
    }

    private flash(msg: string): void {
        this.successMessage = msg;
        setTimeout(() => this.successMessage = null, 3000);
    }
}
