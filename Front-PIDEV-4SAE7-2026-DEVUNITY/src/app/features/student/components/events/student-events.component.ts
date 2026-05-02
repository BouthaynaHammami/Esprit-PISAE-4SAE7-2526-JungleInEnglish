import { Component, OnInit } from '@angular/core';
import { EventService } from '../../../../core/services/certif-event/event.service';
import { RegistrationService } from '../../../../core/services/certif-event/registration.service';

@Component({
  selector: 'app-student-events',
  templateUrl: './student-events.component.html',
  styleUrls: ['./student-events.component.scss']
})
export class StudentEventsComponent implements OnInit {

  events: any[] = [];
  myRegistrations: any[] = [];

  // ───────── SEARCH & FILTERS ─────────
  searchQuery = '';
  locationQuery = '';
  activeView: 'list' | 'grid' = 'list';
  activeTab: 'all' | 'registrations' = 'all';

  get filteredEvents(): any[] {
    return this.events.filter(e => {
      const q = this.searchQuery.toLowerCase();
      const l = this.locationQuery.toLowerCase();
      const matchName = !q || e.title?.toLowerCase().includes(q);
      const matchLoc = !l || e.location?.toLowerCase().includes(l);
      return matchName && matchLoc;
    });
  }

  // ───────── ALERT ─────────
  showAlert = false;
  alertMessage = '';
  alertType: 'success' | 'info' | 'error' = 'success';

  // ───────── REGISTER MODAL ─────────
  showRegisterModal = false;
  selectedEvent: any = null;
  comment = '';
  isRegistering = false;

  // ───────── TICKET MODAL ─────────
  showQRModal = false;
  qrCodeImage = '';
  registrationId: number | null = null;
  currentTicket: any = null;

  // ───────── CANCEL MODAL ─────────
  showCancelModal = false;
  registrationToCancel: any = null;
  cancellingId: number | null = null;

  constructor(
    private eventService: EventService,
    private registrationService: RegistrationService
  ) {}

  ngOnInit(): void {
    this.loadEvents();
    this.loadMyRegistrations();
  }

  // ───────── TOAST ─────────
  showToast(message: string, type: 'success' | 'info' | 'error') {
    this.alertMessage = message;
    this.alertType = type;
    this.showAlert = true;

    setTimeout(() => {
      this.showAlert = false;
    }, 4000);
  }

  // ───────── LOAD EVENTS ─────────
  loadEvents(): void {
    this.eventService.getAll().subscribe({
      next: (data) => {
        this.events = data ?? [];
      },
      error: (err) => {
        console.error('Error loading events:', err);
      }
    });
  }

  loadMyRegistrations(): void {
    this.registrationService.getMyRegistrations().subscribe({
      next: (data) => {
        this.myRegistrations = data ?? [];
      },
      error: (err) => {
        console.error('Error loading registrations:', err);
      }
    });
  }

  // ───────── REGISTER ─────────
  openRegisterModal(event: any): void {
    this.selectedEvent = event;
    this.comment = '';
    this.showRegisterModal = true;
  }

  closeRegisterModal(): void {
    this.showRegisterModal = false;
    this.selectedEvent = null;
    this.comment = '';
  }

  confirmRegister(): void {
    if (!this.selectedEvent) return;

    const eventId = this.selectedEvent.eventId ?? this.selectedEvent.id;
    if (!eventId) {
      this.showToast('Invalid event ID', 'error');
      return;
    }

    this.isRegistering = true;

    this.registrationService.register(eventId, this.comment || undefined).subscribe({
      next: (newRegistration) => {

        // ✅ Enrichit la nouvelle registration avec les données de l'événement sélectionné
        newRegistration.events = this.selectedEvent;

        this.myRegistrations.push(newRegistration);
        this.isRegistering = false;
        this.closeRegisterModal();

        const status = newRegistration.status?.toUpperCase();

        if (status === 'CONFIRMED') {
          this.showToast('Registration confirmed!', 'success');
          this.openQR(newRegistration.id);
        } else if (status === 'WAITLISTED') {
          this.showToast('You are on the waiting list.', 'info');
        } else {
          this.showToast('Registration successful.', 'success');
        }
      },
      error: (err) => {
        console.error('Registration error:', err);
        this.isRegistering = false;
        this.showToast('Registration failed. Try again.', 'error');
      }
    });
  }

  // ───────── TICKET ─────────
  openQR(registrationId: number): void {
    if (!registrationId) return;

    // ✅ Récupère la registration locale pour avoir l'image de l'événement
    const registration = this.myRegistrations.find(r => r.id === registrationId);

    // ✅ Résout l'image depuis toutes les sources possibles
    const eventImage =
      registration?.events?.imageUrl    ||
      registration?.events?.image_url   ||
      registration?.events?.coverImage  ||
      registration?.events?.cover_image ||
      registration?.events?.photo       ||
      registration?.imageUrl            ||
      registration?.image_url           ||
      null;

    this.registrationService.getTicket(registrationId).subscribe({
      next: (ticket) => {

        this.currentTicket = {
          ...ticket,
          // ✅ Injecte l'image : priorité au ticket, fallback sur la registration locale
          eventImage:
            ticket.eventImage    ||
            ticket.imageUrl      ||
            ticket.image_url     ||
            ticket.coverImage    ||
            eventImage
        };

        const raw = ticket.qrCodeBase64 ?? '';
        this.qrCodeImage = raw.startsWith('data:')
          ? raw
          : 'data:image/png;base64,' + raw;

        this.registrationId = ticket.registrationId ?? registrationId;
        this.showQRModal = true;
      },
      error: (err) => {
        console.error('Ticket error:', err);
        this.showToast('Could not load ticket.', 'error');
      }
    });
  }

  closeQRModal(): void {
    this.showQRModal = false;
    this.currentTicket = null;
    this.qrCodeImage = '';
    this.registrationId = null;
  }

  // ───────── CANCEL ─────────
  openCancelModal(registration: any): void {
    this.registrationToCancel = registration;
    this.showCancelModal = true;
  }

  closeCancelModal(): void {
    this.showCancelModal = false;
    this.registrationToCancel = null;
  }

  confirmCancel(): void {
    if (!this.registrationToCancel) return;

    const id = this.registrationToCancel.id;
    this.cancellingId = id;

    this.registrationService.cancelRegistration(id).subscribe({
      next: () => {
        const reg = this.myRegistrations.find(r => r.id === id);
        if (reg) reg.status = 'CANCELLED';

        this.showToast('Registration cancelled.', 'info');
        this.cancellingId = null;
        this.closeCancelModal();
      },
      error: (err) => {
        console.error('Cancel error:', err);
        this.cancellingId = null;
        this.showToast('Cancellation failed. Try again.', 'error');
      }
    });
  }

  // ───────── HELPERS ─────────
  isAlreadyRegistered(eventId: number): boolean {
    return this.myRegistrations.some(r =>
      (r.events?.eventId === eventId || r.events?.id === eventId) &&
      r.status?.toUpperCase() !== 'CANCELLED'
    );
  }

  getStatusColor(status: string): string {
    const map: Record<string, string> = {
      REGISTRATION_OPEN : '#006D77',
      SCHEDULED         : '#F59E0B',
      CLOSED            : '#6B7280',
      COMPLETED         : '#10B981',
      CANCELED          : '#EF4444'
    };
    return map[status?.toUpperCase()] ?? '#9CA3AF';
  }

  getStatusEmoji(status: string): string {
    const map: Record<string, string> = {
      REGISTRATION_OPEN : '✍️',
      SCHEDULED         : '🗓️',
      CLOSED            : '🔒',
      COMPLETED         : '✅',
      CANCELED          : '❌'
    };
    return map[status?.toUpperCase()] ?? '📅';
  }

  getStatusLabel(status: string): string {
    const map: Record<string, string> = {
      REGISTRATION_OPEN : 'Open',
      SCHEDULED         : 'Scheduled',
      CLOSED            : 'Closed',
      COMPLETED         : 'Completed',
      CANCELED          : 'Canceled'
    };
    return map[status?.toUpperCase()] ?? status;
  }
}