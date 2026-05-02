import { Component, OnInit } from '@angular/core';
import { EventService } from '../../../../../core/services/certif-event/event.service';
import { RegistrationService } from '../../../../../core/services/certif-event/registration.service';
import { EventStatus } from '../../../../../core/models/event.model';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

interface EventFormModel {
  title: string;
  description: string;
  startDate: string;
  endDate: string;
  location: string;
  capacity: number;
  status: EventStatus | '';
}

@Component({
  selector: 'app-add-event',
  templateUrl: './add-event.component.html',
  styleUrls: ['./add-event.component.scss']
})
export class AddEventComponent implements OnInit {

  selectedFile: File | null = null;
  imagePreview: string | null = null;

  event: EventFormModel = {
    title: '',
    description: '',
    startDate: '',
    endDate: '',
    location: '',
    capacity: 1,
    status: ''
  };

  events: any[] = [];
  registrations: any[] = [];

  userNames: Record<number, string> = {};

  successMessage = '';
  errorMessage = '';
  isLoading = false;
  editingId: number | null = null;

  activeRegFilter = 'ALL';

  // Pagination
  currentPage = 1;
  pageSize = 5;

  regFilters = [
    { label: '🌐 All',       value: 'ALL'        },
    { label: '⏳ Pending',   value: 'PENDING'    },
    { label: '✅ Confirmed', value: 'CONFIRMED'  },
    { label: '🟠 Waitlist',  value: 'WAITLISTED' },
    { label: '❌ Rejected',  value: 'REJECTED'   },
    { label: '🚫 Cancelled', value: 'CANCELLED'  }
  ];

  constructor(
    private eventService: EventService,
    private registrationService: RegistrationService
  ) {}

  ngOnInit(): void {
    this.loadEvents();
    this.loadRegistrations();
  }

  onFileSelected(event: any): void {
    const file = event.target.files?.[0];
    if (!file) return;
    this.selectedFile = file;
    const reader = new FileReader();
    reader.onload = () => { this.imagePreview = reader.result as string; };
    reader.readAsDataURL(file);
  }

  loadEvents(): void {
    this.eventService.getAll().subscribe({
      next: (data: any[]) => {
        this.events = data ?? [];
        // Ensure current page is valid after reload
        if (this.currentPage > this.totalPages) {
          this.currentPage = this.totalPages;
        }
      },
      error: (err: any) => {
        console.error(err);
        this.errorMessage = 'Failed to load events ❌';
      }
    });
  }

  loadRegistrations(): void {
    this.registrationService.getAllRegistrations().subscribe({
      next: (data: any[]) => {
        this.registrations = data ?? [];

        const uniqueIds = [
          ...new Set(
            data
              .map((r: any) => Number(r.user))
              .filter((id: number) => !isNaN(id) && id > 0)
          )
        ] as number[];

        if (uniqueIds.length === 0) return;

        const requests = uniqueIds.map(id =>
          this.registrationService.getUserById(id).pipe(
            catchError(() => of({ id: id, firstName: '?', lastName: '' }))
          )
        );

        // ✅ FIX : utilise uniqueIds[index] au lieu de u.userId
        forkJoin(requests).subscribe((users: any[]) => {
          users.forEach((u, index) => {
            const uid = uniqueIds[index];
            this.userNames[uid] =
              `${u.firstName ?? ''} ${u.lastName ?? ''}`.trim() || '—';
          });
        });
      },
      error: (err: any) => {
        console.error(err);
        this.errorMessage = 'Failed to load registrations ❌';
      }
    });
  }

  getUserName(userId: number): string {
    return this.userNames[userId] ?? '—';
  }

  onSubmit(): void {
    this.successMessage = '';
    this.errorMessage = '';

    if (!this.event.status) {
      this.errorMessage = 'Please select a status ❌';
      return;
    }

    if (!this.selectedFile && this.editingId === null) {
      this.errorMessage = 'Please select an image ❌';
      return;
    }

    this.isLoading = true;

    const formData = new FormData();
    formData.append('title', this.event.title);
    formData.append('description', this.event.description);
    formData.append('startDate', this.event.startDate);
    formData.append('endDate', this.event.endDate);
    formData.append('location', this.event.location);
    formData.append('capacity', String(this.event.capacity));
    formData.append('status', this.event.status as string);

    if (this.selectedFile) {
      formData.append('image', this.selectedFile);
    }

    const request = this.editingId
      ? this.eventService.updateEventWithImage(this.editingId, formData)
      : this.eventService.addEventWithImage(formData);

    request.subscribe({
      next: () => {
        this.isLoading = false;
        this.successMessage = 'Saved successfully ✅';
        this.resetForm();
        this.loadEvents();
      },
      error: (err: any) => {
        this.isLoading = false;
        console.error(err);
        this.errorMessage = 'Request failed ❌';
      }
    });
  }

  onCancel(): void {
    this.resetForm();
  }

  resetForm(): void {
    this.event = {
      title: '',
      description: '',
      startDate: '',
      endDate: '',
      location: '',
      capacity: 1,
      status: ''
    };
    this.selectedFile = null;
    this.imagePreview = null;
    this.editingId = null;
    this.isLoading = false;
  }

  editEvent(e: any): void {
    this.editingId = e.eventId || e.id;
    this.event = {
      title: e.title,
      description: e.description,
      startDate: e.startDate?.substring(0, 16),
      endDate: e.endDate?.substring(0, 16),
      location: e.location,
      capacity: e.capacity,
      status: e.status
    };
    this.imagePreview = e.imageUrl || null;
  }

  confirmDelete(e: any): void {
    const id = e.eventId || e.id;
    if (!confirm('Are you sure?')) return;

    this.eventService.deleteEvent(id).subscribe({
      next: () => this.loadEvents(),
      error: (err: any) => {
        console.error(err);
        this.errorMessage = 'Delete failed ❌';
      }
    });
  }

  filteredRegistrations(): any[] {
    if (this.activeRegFilter === 'ALL') return this.registrations;
    return this.registrations.filter(r => r.status === this.activeRegFilter);
  }

  getPendingCount(): number {
    return this.registrations.filter(r => r.status === 'PENDING').length;
  }

  updateRegistrationStatus(registration: any, newStatus: string): void {
    const id = registration.id || registration.registrationId;
    this.registrationService.updateStatus(id, newStatus).subscribe({
      next: () => this.loadRegistrations(),
      error: (err: any) => {
        console.error(err);
        this.errorMessage = 'Status update failed ❌';
      }
    });
  }

  // ── Pagination helpers ────────────────────────────────────────
  get totalPages(): number {
    return Math.ceil(this.events.length / this.pageSize) || 1;
  }

  get paginatedEvents(): any[] {
    const start = (this.currentPage - 1) * this.pageSize;
    return this.events.slice(start, start + this.pageSize);
  }

  get pageNumbers(): number[] {
    const pages: number[] = [];
    for (let i = 1; i <= this.totalPages; i++) { pages.push(i); }
    return pages;
  }

  goToPage(page: number): void {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
    }
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages) { this.currentPage++; }
  }

  prevPage(): void {
    if (this.currentPage > 1) { this.currentPage--; }
  }
}