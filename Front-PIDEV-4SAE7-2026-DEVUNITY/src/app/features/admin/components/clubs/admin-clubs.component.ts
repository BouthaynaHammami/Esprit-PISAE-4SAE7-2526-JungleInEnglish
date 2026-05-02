import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subject, forkJoin, of } from 'rxjs';
import { takeUntil, finalize, catchError } from 'rxjs/operators';

import { Club, ClubRole, ClubStatus } from '../../../../core/models/club.model';
import { Excursion, Training, ActivityStatus } from '../../../../core/models/activity.model';

import { ClubService } from '../../../../core/services/books_clubs/club.service';
import { MembershipService, ParticipationClub } from '../../../../core/services/books_clubs/membership.service';
import { RequestService, MembershipRequest } from '../../../../core/services/books_clubs/request.service';
import { ActivityService } from '../../../../core/services/books_clubs/activity.service';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../../environments/environment';

export interface AppUser {
  userId: number;
  firstName: string;
  lastName: string;
  email: string;
}

type Tab = 'clubs' | 'requests' | 'activities';

@Component({
  selector: 'app-admin-clubs',
  templateUrl: './admin-clubs.component.html'
})
export class AdminClubsComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();

  constructor(
    private clubService: ClubService,
    private membershipService: MembershipService,
    private requestService: RequestService,
    private activityService: ActivityService,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    this.loadClubs();
    this.loadUsers();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  // ── UI State ───────────────────────────────────────────────────────────────
  tab: Tab = 'clubs';
  loading = false;
  errorMsg = '';
  successMsg = '';

  // ── Modals ─────────────────────────────────────────────────────────────────
  showClubModal = false;
  showExcursionModal = false;
  showTrainingModal = false;

  // ── Search / Filter ────────────────────────────────────────────────────────
  searchQuery = '';
  filterType = '';

  // ── Enums ──────────────────────────────────────────────────────────────────
  readonly roles: ClubRole[] = ['SPORT', 'CULTURAL', 'SCIENTIFIC', 'TECHNOLOGICAL', 'ARTISTIC', 'SOCIAL'];
  readonly clubStatuses: ClubStatus[] = ['ACTIVE', 'INACTIVE', 'ARCHIVED'];
  readonly activityStatuses: ActivityStatus[] = ['PLANNED', 'ONGOING', 'FINISHED', 'CANCELLED'];

  // ── Clubs ──────────────────────────────────────────────────────────────────
  clubs: Club[] = [];
  clubForm: Club = this.emptyClub();

  get filteredClubs(): Club[] {
    return this.clubs.filter(c => {
      const matchType = !this.filterType || c.type === this.filterType;
      const matchSearch =
        !this.searchQuery ||
        c.name.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
        (c.description || '').toLowerCase().includes(this.searchQuery.toLowerCase());
      return matchType && matchSearch;
    });
  }

  get activeClubsCount(): number {
    return this.clubs.filter(c => c.status === 'ACTIVE').length;
  }

  // ── Users ──────────────────────────────────────────────────────────────────
  users: AppUser[] = [];

  // ── Selected (dropdowns) ───────────────────────────────────────────────────
  memberId: number | null = null;
  clubId: number | null = null;

  get selectedClubName(): string {
    return this.clubs.find(c => c.clubId === this.clubId)?.name ?? '—';
  }

  // ── Memberships ────────────────────────────────────────────────────────────
  membersOfClub: ParticipationClub[] = [];
  clubsOfMember: ParticipationClub[] = [];

  // ── Requests ───────────────────────────────────────────────────────────────
  motivation = '';
  requests: MembershipRequest[] = [];

  // ── Activities ─────────────────────────────────────────────────────────────
  excursions: Excursion[] = [];
  trainings: Training[] = [];
  excursionForm: Excursion = this.emptyExcursion();
  trainingForm: Training = this.emptyTraining();

  // ✅ NEW: maps pour afficher nb de réservations par activité
  excursionReservations: Record<number, number> = {};
  trainingReservations: Record<number, number> = {};

  // ── Factories ──────────────────────────────────────────────────────────────
  private emptyClub(): Club {
    return {
      name: '',
      description: '',
      creationDate: new Date().toISOString().slice(0, 10),
      type: 'CULTURAL',
      status: 'ACTIVE'
    };
  }

  private emptyExcursion(): Excursion {
    return {
      title: '',
      description: '',
      location: '',
      startDate: '',
      endDate: '',
      status: 'PLANNED',
      nbrDeplace: 0,
      club: { clubId: this.clubId ?? 0 }
    };
  }

  private emptyTraining(): Training {
    return {
      title: '',
      description: '',
      trainer: '',
      startDate: '',
      endDate: '',
      status: 'PLANNED',
      nbrDeplace: 0,
      club: { clubId: this.clubId ?? 0 }
    };
  }

  // ── Tab ────────────────────────────────────────────────────────────────────
  setTab(t: Tab): void {
    this.tab = t;
    this.clearMessages();
  }

  // ── Modals ─────────────────────────────────────────────────────────────────
  openClubModal(): void {
    this.clubForm = this.emptyClub();
    this.showClubModal = true;
  }
  openEditClubModal(c: Club): void {
    this.clubForm = { ...c };
    this.showClubModal = true;
  }

  openExcursionModal(): void {
    this.excursionForm = this.emptyExcursion();
    this.showExcursionModal = true;
  }
  openEditExcursionModal(e: Excursion): void {
    this.excursionForm = { ...e };
    this.showExcursionModal = true;
  }

  openTrainingModal(): void {
    this.trainingForm = this.emptyTraining();
    this.showTrainingModal = true;
  }
  openEditTrainingModal(t: Training): void {
    this.trainingForm = { ...t };
    this.showTrainingModal = true;
  }

  // ── Icon helper ────────────────────────────────────────────────────────────
  typeIcon(type: string): string {
    const map: Record<string, string> = {
      SPORT: '⚽',
      CULTURAL: '🎭',
      SCIENTIFIC: '🔬',
      TECHNOLOGICAL: '💻',
      ARTISTIC: '🎨',
      SOCIAL: '🤝'
    };
    return map[type] ?? '🏛';
  }

  // ── Helpers ────────────────────────────────────────────────────────────────
  userName(id: number): string {
    const u = this.users.find(u => u.userId === id);
    return u ? `${u.firstName} ${u.lastName}` : `#${id}`;
  }

  clubName(id: number): string {
    return this.clubs.find(c => c.clubId === id)?.name ?? `#${id}`;
  }

  // ── Messaging ──────────────────────────────────────────────────────────────
  private clearMessages(): void {
    this.errorMsg = '';
    this.successMsg = '';
  }

  private showSuccess(msg: string): void {
    this.successMsg = msg;
    this.errorMsg = '';
    setTimeout(() => (this.successMsg = ''), 4000);
  }

  private showError(err: any, fallback = 'An error occurred'): void {
    this.errorMsg = err?.error?.message ?? err?.message ?? fallback;
    this.successMsg = '';
  }

  private require(...ids: (number | null)[]): boolean {
    if (ids.some(id => !id)) {
      this.showError(null, 'Please select all required fields');
      return false;
    }
    return true;
  }

  // ============================================================ CLUBS ========
  loadClubs(): void {
    this.loading = true;
    this.clubService
      .getAll()
      .pipe(takeUntil(this.destroy$), finalize(() => (this.loading = false)))
      .subscribe({
        next: (d: Club[]) => (this.clubs = d ?? []),
        error: e => this.showError(e, 'Failed to load clubs')
      });
  }

  loadUsers(): void {
    this.http
      .get<AppUser[]>(`${environment.apiUrl}/learners/api/users`)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: d => (this.users = d ?? []),
        error: () => {}
      });
  }

  saveClub(): void {
    if (!this.clubForm.name?.trim()) {
      this.showError(null, 'Club name is required');
      return;
    }

    this.loading = true;
    const req$ = this.clubForm.clubId ? this.clubService.update(this.clubForm) : this.clubService.create(this.clubForm);

    req$
      .pipe(takeUntil(this.destroy$), finalize(() => (this.loading = false)))
      .subscribe({
        next: () => {
          this.showSuccess('Club saved');
          this.showClubModal = false;
          this.loadClubs();
        },
        error: e => this.showError(e, 'Failed to save club')
      });
  }

  deleteClub(id?: number): void {
    if (!id || !confirm('Delete this club?')) return;

    this.loading = true;
    this.clubService
      .delete(id)
      .pipe(takeUntil(this.destroy$), finalize(() => (this.loading = false)))
      .subscribe({
        next: () => {
          this.showSuccess('Club deleted');
          this.loadClubs();
        },
        error: e => this.showError(e, 'Delete failed')
      });
  }

  // =========================================================== REQUESTS ======
  loadRequestsByClub(): void {
    if (!this.require(this.clubId)) return;

    this.loading = true;
    this.requestService
      .byClub(this.clubId!)
      .pipe(takeUntil(this.destroy$), finalize(() => (this.loading = false)))
      .subscribe({
        next: (d: MembershipRequest[]) => (this.requests = d ?? []),
        error: e => this.showError(e, 'Load failed')
      });
  }

  loadAllRequests(): void {
    if (this.clubs.length === 0) {
      this.showError(null, 'No clubs available');
      return;
    }

    this.loading = true;
    this.requests = [];

    let completed = 0;
    const total = this.clubs.length;

    this.clubs.forEach(club => {
      this.requestService
        .byClub(club.clubId!)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: (d: MembershipRequest[]) => {
            this.requests = [...this.requests, ...(d ?? [])];
            completed++;
            if (completed === total) this.loading = false;
          },
          error: () => {
            completed++;
            if (completed === total) this.loading = false;
          }
        });
    });
  }

  acceptRequest(id?: number): void {
    if (!id) return;

    this.loading = true;
    this.requestService
      .accept(id, 0)
      .pipe(takeUntil(this.destroy$), finalize(() => (this.loading = false)))
      .subscribe({
        next: () => {
          this.showSuccess('Request accepted');
          this.reloadRequests();
        },
        error: e => this.showError(e, 'Accept failed')
      });
  }

  rejectRequest(id?: number): void {
    if (!id) return;

    this.loading = true;
    this.requestService
      .reject(id, 0)
      .pipe(takeUntil(this.destroy$), finalize(() => (this.loading = false)))
      .subscribe({
        next: () => {
          this.showSuccess('Request rejected');
          this.reloadRequests();
        },
        error: e => this.showError(e, 'Reject failed')
      });
  }

  private reloadRequests(): void {
    if (this.clubId) this.loadRequestsByClub();
  }

  // ======================================================== EXCURSIONS =======
  loadExcursions(): void {
    if (!this.require(this.clubId)) return;

    this.loading = true;
    this.activityService
      .getExcursionsByClub(this.clubId!)
      .pipe(takeUntil(this.destroy$), finalize(() => (this.loading = false)))
      .subscribe({
        next: (d: Excursion[]) => {
          this.excursions = d ?? [];
          // ✅ NEW: après chargement, charger les compteurs de réservations
          this.loadExcursionReservations();
        },
        error: e => this.showError(e, 'Load excursions failed')
      });
  }

  private loadExcursionReservations(): void {
    if (!this.excursions || this.excursions.length === 0) {
      this.excursionReservations = {};
      return;
    }

    const calls = this.excursions
      .filter(e => !!e.excursionId)
      .map(e =>
        this.activityService.excursionParticipants(e.excursionId!)
          .pipe(
            catchError(() => of([])) // si erreur sur une activité, on n'arrête pas tout
          )
      );

    forkJoin(calls)
      .pipe(takeUntil(this.destroy$))
      .subscribe((results) => {
        const map: Record<number, number> = {};
        let idx = 0;

        this.excursions.forEach(e => {
          if (!e.excursionId) return;
          const list = results[idx] || [];
          map[e.excursionId] = Array.isArray(list) ? list.length : 0;
          idx++;
        });

        this.excursionReservations = map;
      });
  }

  remainingExcursionPlaces(excursionId: number, nbrDeplace?: number | null): number {
    const cap = (nbrDeplace !== null && nbrDeplace !== undefined) ? nbrDeplace : 0;
    const reserved = this.excursionReservations[excursionId] || 0;
    const remaining = cap - reserved;
    return remaining > 0 ? remaining : 0;
  }

  saveExcursion(): void {
    if (!this.require(this.clubId)) return;

    this.excursionForm.club = { clubId: this.clubId! };
    this.loading = true;

    const req$ = this.excursionForm.excursionId
      ? this.activityService.updateExcursion(this.excursionForm)
      : this.activityService.addExcursion(this.excursionForm);

    req$
      .pipe(takeUntil(this.destroy$), finalize(() => (this.loading = false)))
      .subscribe({
        next: () => {
          this.showSuccess('Excursion saved');
          this.showExcursionModal = false;
          this.loadExcursions(); // ✅ recharge excursions + compteurs
        },
        error: e => this.showError(e, 'Save excursion failed')
      });
  }

  deleteExcursion(id?: number): void {
    if (!id || !confirm('Delete this excursion?')) return;

    this.loading = true;
    this.activityService
      .deleteExcursion(id)
      .pipe(takeUntil(this.destroy$), finalize(() => (this.loading = false)))
      .subscribe({
        next: () => {
          this.showSuccess('Excursion deleted');
          this.loadExcursions(); // ✅ recharge excursions + compteurs
        },
        error: e => this.showError(e, 'Delete failed')
      });
  }

  // ========================================================== TRAININGS ======
  loadTrainings(): void {
    if (!this.require(this.clubId)) return;

    this.loading = true;
    this.activityService
      .getTrainingsByClub(this.clubId!)
      .pipe(takeUntil(this.destroy$), finalize(() => (this.loading = false)))
      .subscribe({
        next: (d: Training[]) => {
          this.trainings = d ?? [];
          // ✅ NEW: après chargement, charger les compteurs de réservations
          this.loadTrainingReservations();
        },
        error: e => this.showError(e, 'Load trainings failed')
      });
  }

  private loadTrainingReservations(): void {
    if (!this.trainings || this.trainings.length === 0) {
      this.trainingReservations = {};
      return;
    }

    const calls = this.trainings
      .filter(t => !!t.trainingId)
      .map(t =>
        this.activityService.trainingParticipants(t.trainingId!)
          .pipe(
            catchError(() => of([]))
          )
      );

    forkJoin(calls)
      .pipe(takeUntil(this.destroy$))
      .subscribe((results) => {
        const map: Record<number, number> = {};
        let idx = 0;

        this.trainings.forEach(t => {
          if (!t.trainingId) return;
          const list = results[idx] || [];
          map[t.trainingId] = Array.isArray(list) ? list.length : 0;
          idx++;
        });

        this.trainingReservations = map;
      });
  }

  remainingTrainingPlaces(trainingId: number, nbrDeplace?: number | null): number {
    const cap = (nbrDeplace !== null && nbrDeplace !== undefined) ? nbrDeplace : 0;
    const reserved = this.trainingReservations[trainingId] || 0;
    const remaining = cap - reserved;
    return remaining > 0 ? remaining : 0;
  }

  saveTraining(): void {
    if (!this.require(this.clubId)) return;

    this.trainingForm.club = { clubId: this.clubId! };
    this.loading = true;

    const req$ = this.trainingForm.trainingId
      ? this.activityService.updateTraining(this.trainingForm)
      : this.activityService.addTraining(this.trainingForm);

    req$
      .pipe(takeUntil(this.destroy$), finalize(() => (this.loading = false)))
      .subscribe({
        next: () => {
          this.showSuccess('Training saved');
          this.showTrainingModal = false;
          this.loadTrainings(); // ✅ recharge trainings + compteurs
        },
        error: e => this.showError(e, 'Save training failed')
      });
  }

  deleteTraining(id?: number): void {
    if (!id || !confirm('Delete this training?')) return;

    this.loading = true;
    this.activityService
      .deleteTraining(id)
      .pipe(takeUntil(this.destroy$), finalize(() => (this.loading = false)))
      .subscribe({
        next: () => {
          this.showSuccess('Training deleted');
          this.loadTrainings(); // ✅ recharge trainings + compteurs
        },
        error: e => this.showError(e, 'Delete failed')
      });
  }
}