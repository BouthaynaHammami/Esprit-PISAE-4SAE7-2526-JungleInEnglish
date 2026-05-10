import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subject, forkJoin, of, Observable } from 'rxjs';
import { takeUntil, finalize, catchError } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';

import { Club, ClubRole, ClubStatus } from '../../../../core/models/club.model';
import { Excursion, Training, ActivityStatus } from '../../../../core/models/activity.model';

import { ClubService } from '../../../../core/services/books_clubs/club.service';
import { MembershipService, ParticipationClub } from '../../../../core/services/books_clubs/membership.service';
import { RequestService, MembershipRequest } from '../../../../core/services/books_clubs/request.service';
import { ActivityService } from '../../../../core/services/books_clubs/activity.service';
import { environment } from '../../../../../environments/environment';

export interface AppUser {
  userId: number;
  firstName: string;
  lastName: string;
  email: string;
}

type Tab = 'clubs' | 'requests' | 'activities' | 'members';

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
  showDeleteModal = false;
  showParticipantsModal = false;
  showTrainingAdminModal = false;

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
  clubToDelete: Club | null = null;

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

  // ── Selected ───────────────────────────────────────────────────────────────
  memberId: number | null = null;
  clubId: number | null = null;
  selectedTrainingId: number | null = null;
  selectedTrainingTitle = '';

  get selectedClubName(): string {
    return this.clubs.find(c => c.clubId === this.clubId)?.name ?? '—';
  }

  // ── Memberships ────────────────────────────────────────────────────────────
  membersOfClub: ParticipationClub[] = [];

  // ── Requests ───────────────────────────────────────────────────────────────
  requests: MembershipRequest[] = [];

  get pendingRequestsCount(): number {
    return this.requests.filter(r => r.status === 'PENDING').length;
  }

  get approvedRequestsCount(): number {
    return this.requests.filter(r => r.status === 'ACCEPTED').length;
  }

  // ── Activities ─────────────────────────────────────────────────────────────
  excursions: Excursion[] = [];
  trainings: Training[] = [];
  excursionForm: Excursion = this.emptyExcursion();
  trainingForm: Training = this.emptyTraining();
  excursionReservations: Record<number, number> = {};
  trainingReservations: Record<number, number> = {};

  // ── Participants ───────────────────────────────────────────────────────────
  participants: any[] = [];
  trainingParticipantsList: any[] = [];

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
      price: 0,
      rewardEnabled: false,
      rewardMax: 0,
      club: { clubId: this.clubId ?? 0 }
    };
  }

  // ── Tab ────────────────────────────────────────────────────────────────────
  setTab(t: Tab): void {
    this.tab = t;
    this.clearMessages();
  }

  // ── Modal helpers ──────────────────────────────────────────────────────────
  openClubModal(): void {
    this.clubForm = this.emptyClub();
    this.showClubModal = true;
  }

  openEditClubModal(c: Club): void {
    this.clubForm = { ...c };
    this.showClubModal = true;
  }

  confirmDeleteClub(c: Club): void {
    this.clubToDelete = c;
    this.showDeleteModal = true;
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

  openActivitiesForClub(c: Club): void {
    this.clubId = c.clubId ?? null;
    this.setTab('activities');
    this.loadExcursions();
    this.loadTrainings();
  }

  openTrainingAdminModal(t: Training): void {
    if (!t.trainingId) return;

    this.selectedTrainingId = t.trainingId;
    this.selectedTrainingTitle = t.title;
    this.trainingParticipantsList = [];
    this.showTrainingAdminModal = true;

    this.loadTrainingParticipantsList(t.trainingId);
  }

  closeTrainingAdminModal(): void {
    this.showTrainingAdminModal = false;
    this.selectedTrainingId = null;
    this.selectedTrainingTitle = '';
    this.trainingParticipantsList = [];
  }

  // ── Style helpers ──────────────────────────────────────────────────────────
  typeIcon(type?: string): string {
    const map: Record<string, string> = {
      SPORT: '⚽',
      CULTURAL: '🎭',
      SCIENTIFIC: '🔬',
      TECHNOLOGICAL: '💻',
      ARTISTIC: '🎨',
      SOCIAL: '🤝'
    };
    return map[type ?? ''] ?? '🏛';
  }

  roleIcon(role?: string): string {
    const map: Record<string, string> = {
      PRESIDENT: '👑',
      SECRETARY: '📝',
      TREASURER: '💰',
      MEMBER: '👤'
    };
    return map[role ?? ''] ?? '👤';
  }

  getSpineStyle(type?: string): string {
    const map: Record<string, string> = {
      SPORT: 'background:linear-gradient(180deg,#059669,#34d399)',
      CULTURAL: 'background:linear-gradient(180deg,#8b5cf6,#c4b5fd)',
      SCIENTIFIC: 'background:linear-gradient(180deg,#0ea5e9,#7dd3fc)',
      TECHNOLOGICAL: 'background:linear-gradient(180deg,#f59e0b,#fcd34d)',
      ARTISTIC: 'background:linear-gradient(180deg,#ec4899,#f9a8d4)',
      SOCIAL: 'background:linear-gradient(180deg,#006D77,#83C5BE)'
    };
    return map[type ?? ''] ?? 'background:linear-gradient(180deg,#83C5BE,#EDF6F9)';
  }

  getIconBg(type?: string): string {
    const map: Record<string, string> = {
      SPORT: 'background:#d1fae5',
      CULTURAL: 'background:#ede9fe',
      SCIENTIFIC: 'background:#e0f2fe',
      TECHNOLOGICAL: 'background:#fef3c7',
      ARTISTIC: 'background:#fce7f3',
      SOCIAL: 'background:#EDF6F9'
    };
    return map[type ?? ''] ?? 'background:#EDF6F9';
  }

  getActivityStatusStyle(status?: string): string {
    const map: Record<string, string> = {
      PLANNED: 'background:#EDF6F9;color:#006D77',
      ONGOING: 'background:#d4f0eb;color:#006D77',
      FINISHED: 'background:#f3f4f6;color:#6b7280',
      CANCELLED: 'background:#FEE2E2;color:#DC2626'
    };
    return map[status ?? ''] ?? 'background:#EDF6F9;color:#006D77';
  }

  getRoleStyle(role?: string): string {
    const map: Record<string, string> = {
      PRESIDENT: 'background:#FEF3C7;color:#D97706',
      SECRETARY: 'background:#EDF6F9;color:#006D77',
      TREASURER: 'background:#FFDDD2;color:#c9785f',
      MEMBER: 'background:#f3f4f6;color:#6b7280'
    };
    return map[role ?? ''] ?? 'background:#f3f4f6;color:#6b7280';
  }

  getCapacityBarStyle(excursionId: number, capacity?: number | null): string {
    if (!capacity || capacity <= 0) return 'width:0%;background:#83C5BE';
    const reserved = this.excursionReservations[excursionId] ?? 0;
    const pct = Math.min(100, Math.round((reserved / capacity) * 100));
    const color = pct >= 90 ? '#E29578' : pct >= 60 ? '#f59e0b' : '#006D77';
    return `width:${pct}%;background:${color}`;
  }

  getTrainingCapacityBarStyle(trainingId: number, capacity?: number | null): string {
    if (!capacity || capacity <= 0) return 'width:0%;background:#E29578';
    const reserved = this.trainingReservations[trainingId] ?? 0;
    const pct = Math.min(100, Math.round((reserved / capacity) * 100));
    const color = pct >= 90 ? '#dc2626' : pct >= 60 ? '#f59e0b' : '#E29578';
    return `width:${pct}%;background:${color}`;
  }

  

  clubName(id?: number): string {
    if (!id) return '—';
    return this.clubs.find(c => c.clubId === id)?.name ?? `Club #${id}`;
  }

  getClubMembersCount(clubId: number): number {
    return this.membersOfClub.filter(m => (m as any).club?.clubId === clubId).length;
  }

  extractRole(motivation?: string): string {
    const match = motivation?.match(/\[Role:\s*([^\]]+)\]/);
    if (!match) return '';
    return `${this.roleIcon(match[1].trim())} ${match[1].trim()}`;
  }

  cleanMotivation(motivation?: string): string {
    return (motivation ?? '')
      .replace(/\[(Role|Phone|Address):[^\]]*\]/g, '')
      .replace(/\s+/g, ' ')
      .trim();
  }

  remainingExcursionPlaces(excursionId: number, nbrDeplace?: number | null): number {
    const cap = nbrDeplace ?? 0;
    const reserved = this.excursionReservations[excursionId] ?? 0;
    return Math.max(0, cap - reserved);
  }

  remainingTrainingPlaces(trainingId: number, nbrDeplace?: number | null): number {
    const cap = nbrDeplace ?? 0;
    const reserved = this.trainingReservations[trainingId] ?? 0;
    return Math.max(0, cap - reserved);
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

  private require(...ids: (number | null | undefined)[]): boolean {
    if (ids.some(id => !id)) {
      this.showError(null, 'Please select all required fields');
      return false;
    }
    return true;
  }

  resolveRequestId(r: MembershipRequest): number | null {
    const anyRequest = r as any;
    return anyRequest.membershipRequestId ?? anyRequest.requestId ?? anyRequest.id ?? null;
  }

  private resolveClubId(r: any): number | null {
    return r?.club?.clubId ?? r?.club?.id ?? r?.clubId ?? null;
  }

  // ═══════════════════════════════════════════════ CLUBS ═════════════════════
  loadClubs(): void {
    this.loading = true;
    this.clubService.getAll()
      .pipe(takeUntil(this.destroy$), finalize(() => (this.loading = false)))
      .subscribe({
        next: (d: Club[]) => {
          this.clubs = d ?? [];
          this.loadAllRequests();
        },
        error: (e: any) => this.showError(e, 'Failed to load clubs')
      });
  }

  loadUsers(): void {
    const token = localStorage.getItem('jwt_token') ?? '';
    const headers = { Authorization: token ? `Bearer ${token}` : '' };
    this.http.get<any[]>(`${environment.devUnityUrl}/learners/api/users`, { headers })
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: d => {
          this.users = (d ?? []).map((u: any) => ({
            userId: u.userId ?? u.id ?? u.user_id,
            firstName: u.firstName ?? u.first_name ?? '',
            lastName: u.lastName ?? u.last_name ?? '',
            email: u.email ?? ''
          }));
        },
        error: () => {}
      });
  }
memberName(id: number): string {
  if (!id) return '—';

  const u = this.users.find(user => user.userId === id);
  if (!u) return `User #${id}`;

  return u.firstName?.trim() || `User #${id}`;
}

userEmail(id: number): string {
  return this.users.find(user => user.userId === id)?.email ?? '—';
}

userName(id: number): string {
  return this.memberName(id);
}

  saveClub(): void {
    if (!this.clubForm.name?.trim()) {
      this.showError(null, 'Club name is required');
      return;
    }

    this.loading = true;
    const req$ = this.clubForm.clubId
      ? this.clubService.update(this.clubForm)
      : this.clubService.create(this.clubForm);

    req$
      .pipe(takeUntil(this.destroy$), finalize(() => (this.loading = false)))
      .subscribe({
        next: () => {
          this.showSuccess(this.clubForm.clubId ? 'Club updated!' : 'Club created!');
          this.showClubModal = false;
          this.loadClubs();
        },
        error: (e: any) => this.showError(e, 'Save failed')
      });
  }

  deleteClub(id?: number): void {
    const clubId = id ?? this.clubToDelete?.clubId;
    if (!clubId) return;

    this.loading = true;
    this.clubService.delete(clubId)
      .pipe(takeUntil(this.destroy$), finalize(() => (this.loading = false)))
      .subscribe({
        next: () => {
          this.showSuccess('Club deleted');
          this.showDeleteModal = false;
          this.clubToDelete = null;
          this.loadClubs();
        },
        error: (e: any) => this.showError(e, 'Delete failed')
      });
  }

  // ═══════════════════════════════════════════════ MEMBERS ═══════════════════
  loadMembersOfClub(clubId: number | null): void {
    if (!clubId) return;

    this.clubId = clubId;
    this.loading = true;
    this.membershipService.membersOfClub(clubId)
      .pipe(takeUntil(this.destroy$), finalize(() => (this.loading = false)))
      .subscribe({
        next: (d: ParticipationClub[]) => (this.membersOfClub = d ?? []),
        error: (e: any) => this.showError(e, 'Failed to load members')
      });
  }

  removeMember(m: ParticipationClub): void {
    if (!confirm('Remove this member from the club?')) return;

    const id = (m as any).participationId ?? (m as any).id;
    if (!id) {
      this.showError(null, 'Cannot identify membership record');
      return;
    }

    this.loading = true;
    const token = localStorage.getItem('jwt_token') ?? '';
    const headers = { Authorization: token ? `Bearer ${token}` : '' };
    this.http.delete(`${environment.devUnityUrl}/learners/api/api/memberships/${id}`, { headers })
      .pipe(takeUntil(this.destroy$), finalize(() => (this.loading = false)))
      .subscribe({
        next: () => {
          this.showSuccess('Member removed');
          this.loadMembersOfClub(this.clubId);
        },
        error: (e: any) => this.showError(e, 'Remove failed')
      });
  }

  // ═══════════════════════════════════════════════ REQUESTS ══════════════════
  loadRequestsByClub(): void {
    if (!this.clubId) {
      this.loadAllRequests();
      return;
    }

    this.loading = true;
    this.requestService.byClub(this.clubId)
      .pipe(takeUntil(this.destroy$), finalize(() => (this.loading = false)))
      .subscribe({
        next: (d: MembershipRequest[]) => (this.requests = d ?? []),
        error: (e: any) => this.showError(e, 'Failed to load requests')
      });
  }

  loadAllRequests(): void {
    if (this.clubs.length === 0) return;

    this.loading = true;
    this.requests = [];
    let completed = 0;
    const total = this.clubs.length;

    this.clubs.forEach(club => {
      if (!club.clubId) {
        if (++completed >= total) this.loading = false;
        return;
      }

      this.requestService.byClub(club.clubId)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: (d: MembershipRequest[]) => {
            this.requests = [...this.requests, ...(d ?? [])];
            if (++completed >= total) this.loading = false;
          },
          error: () => {
            if (++completed >= total) this.loading = false;
          }
        });
    });
  }

  acceptRequest(r: any): void {
    const id = this.resolveRequestId(r);
    const clubId = this.resolveClubId(r);

    if (!id || !clubId) {
      this.showError(null, 'Identifiants manquants (id ou clubId introuvable)');
      return;
    }

    this.loading = true;
    this.requestService.accept(id, clubId)
      .pipe(takeUntil(this.destroy$), finalize(() => (this.loading = false)))
      .subscribe({
        next: () => {
          this.showSuccess('Demande acceptée ✅');
          this.loadAllRequests();
        },
        error: (e: any) => this.showError(e, 'Accept failed')
      });
  }

  rejectRequest(r: any): void {
    const id = this.resolveRequestId(r);
    const clubId = this.resolveClubId(r);

    if (!id || !clubId) {
      this.showError(null, 'Identifiants manquants (id ou clubId introuvable)');
      return;
    }

    this.loading = true;
    this.requestService.reject(id, clubId)
      .pipe(takeUntil(this.destroy$), finalize(() => (this.loading = false)))
      .subscribe({
        next: () => {
          this.showSuccess('Demande rejetée ✕');
          this.loadAllRequests();
        },
        error: (e: any) => this.showError(e, 'Reject failed')
      });
  }

  // ═══════════════════════════════════════════════ ACTIVITIES ════════════════
  onClubSelect(clubId: number | null): void {
    this.clubId = clubId;
    if (clubId) {
      this.loadExcursions();
      this.loadTrainings();
    } else {
      this.excursions = [];
      this.trainings = [];
    }
  }

  loadExcursions(): void {
    if (!this.require(this.clubId)) return;

    this.loading = true;
    this.activityService.getExcursionsByClub(this.clubId!)
      .pipe(takeUntil(this.destroy$), finalize(() => (this.loading = false)))
      .subscribe({
        next: (d: Excursion[]) => {
          this.excursions = d ?? [];
          this.loadExcursionReservations();
        },
        error: (e: any) => this.showError(e, 'Load excursions failed')
      });
  }

  private loadExcursionReservations(): void {
    if (!this.excursions.length) {
      this.excursionReservations = {};
      return;
    }

    const filtered = this.excursions.filter(e => !!e.excursionId);
    if (!filtered.length) return;

    const calls: Observable<any[]>[] = filtered.map(e =>
      (this.activityService.excursionParticipants(e.excursionId!) as Observable<any[]>)
        .pipe(catchError(() => of([])))
    );

    forkJoin(calls)
      .pipe(takeUntil(this.destroy$))
      .subscribe((results: any[][]) => {
        const map: Record<number, number> = {};
        filtered.forEach((e, i) => {
          if (e.excursionId) map[e.excursionId] = Array.isArray(results[i]) ? results[i].length : 0;
        });
        this.excursionReservations = map;
      });
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
          this.showSuccess('Excursion saved!');
          this.showExcursionModal = false;
          this.loadExcursions();
        },
        error: (e: any) => this.showError(e, 'Save excursion failed')
      });
  }

  deleteExcursion(id?: number): void {
    if (!id) return;
    if (!confirm('Delete this excursion?')) return;

    this.loading = true;
    this.activityService.deleteExcursion(id)
      .pipe(takeUntil(this.destroy$), finalize(() => (this.loading = false)))
      .subscribe({
        next: () => {
          this.showSuccess('Excursion deleted');
          this.loadExcursions();
        },
        error: (e: any) => this.showError(e, 'Delete failed')
      });
  }

  loadTrainings(): void {
    if (!this.require(this.clubId)) return;

    this.loading = true;
    this.activityService.getTrainingsByClub(this.clubId!)
      .pipe(takeUntil(this.destroy$), finalize(() => (this.loading = false)))
      .subscribe({
        next: (d: Training[]) => {
          this.trainings = d ?? [];
          this.loadTrainingReservations();
        },
        error: (e: any) => this.showError(e, 'Load trainings failed')
      });
  }

  private loadTrainingReservations(): void {
    if (!this.trainings.length) {
      this.trainingReservations = {};
      return;
    }

    const filtered = this.trainings.filter(t => !!t.trainingId);
    if (!filtered.length) return;

    const calls: Observable<any[]>[] = filtered.map(t =>
      (this.activityService.trainingParticipants(t.trainingId!) as Observable<any[]>)
        .pipe(catchError(() => of([])))
    );

    forkJoin(calls)
      .pipe(takeUntil(this.destroy$))
      .subscribe((results: any[][]) => {
        const map: Record<number, number> = {};
        filtered.forEach((t, i) => {
          if (t.trainingId) map[t.trainingId] = Array.isArray(results[i]) ? results[i].length : 0;
        });
        this.trainingReservations = map;
      });
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
          this.showSuccess('Training saved!');
          this.showTrainingModal = false;
          this.loadTrainings();
        },
        error: (e: any) => this.showError(e, 'Save training failed')
      });
  }

  deleteTraining(id?: number): void {
    if (!id) return;
    if (!confirm('Delete this training?')) return;

    this.loading = true;
    this.activityService.deleteTraining(id)
      .pipe(takeUntil(this.destroy$), finalize(() => (this.loading = false)))
      .subscribe({
        next: () => {
          this.showSuccess('Training deleted');
          this.loadTrainings();
        },
        error: (e: any) => this.showError(e, 'Delete failed')
      });
  }

  // ═══════════════════════════════════════════════ PARTICIPANTS ══════════════
  viewParticipants(type: 'excursion' | 'training', id: number): void {
    this.participants = [];
    this.showParticipantsModal = true;
    this.loading = true;

    const req$: Observable<any[]> =
      type === 'excursion'
        ? (this.activityService.excursionParticipants(id) as Observable<any[]>)
        : (this.activityService.trainingParticipants(id) as Observable<any[]>);

    req$
      .pipe(takeUntil(this.destroy$), finalize(() => (this.loading = false)))
      .subscribe({
        next: (d: any[]) => (this.participants = d ?? []),
        error: (e: any) => this.showError(e, 'Failed to load participants')
      });
  }

  // ═══════════════════════════════════════════════ TRAINING ADMIN ════════════
  loadTrainingParticipantsList(trainingId: number): void {
    this.activityService.getTrainingParticipants(trainingId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (d: any[]) => {
          this.trainingParticipantsList = (d ?? []).map(p => ({
            ...p,
            score: p.score ?? 0
          }));
        },
        error: () => {
          this.trainingParticipantsList = [];
          this.showError(null, 'Erreur chargement participants');
        }
      });
  }

  saveParticipantScore(p: any): void {
    const participationId = p?.participationId ?? p?.id;
    const score = p?.score;

    if (participationId == null) {
      this.showError(null, 'Participation introuvable');
      return;
    }

    if (score == null || score < 0 || score > 100) {
      this.showError(null, 'Score must be between 0 and 100');
      return;
    }

    this.loading = true;
    this.activityService.completeTraining(participationId, score)
      .pipe(takeUntil(this.destroy$), finalize(() => (this.loading = false)))
      .subscribe({
        next: (res: any) => {
          p.status = res.status;
          p.completed = res.completed;
          p.rewardAmount = res.rewardAmount;
          p.rewardTransferred = res.rewardTransferred;
          p.score = res.score;
          this.showSuccess('Score saved successfully');
        },
        error: (e: any) => this.showError(e, 'Unable to save score')
      });
  }

validateCashPayment(p: any): void {

  // ✅ FIX récupération paymentId
  const paymentId =
    p?.paymentId ||
    p?.payment?.id ||
    p?.payment?.paymentId;

  if (!paymentId) {
    console.error("Participant:", p);
    this.showError(null, 'Payment ID introuvable pour ce participant');
    return;
  }

  this.loading = true;

  this.activityService.confirmCashPayment(paymentId)
    .pipe(
      takeUntil(this.destroy$),
      finalize(() => (this.loading = false))
    )
    .subscribe({
      next: () => {
        // ✅ Mise à jour UI
        p.paymentStatus = 'PAID';

        if (p.payment) {
          p.payment.status = 'PAID';
        }

        this.showSuccess('Paiement CASH validé avec succès');
      },
      error: (e: any) => {
        console.error("Erreur validation paiement:", e);
        this.showError(e, 'Validation paiement échouée');
      }
    });
}


  hasPendingPayments(): boolean {
    return this.participants.some(p => p.paymentMethod === 'CASH' && p.paymentStatus === 'PENDING');
  }

  pendingPayments(): any[] {
    return this.participants.filter(p => p.paymentMethod === 'CASH' && p.paymentStatus === 'PENDING');
  }

  displayMemberName(item: any, memberId?: number): string {
    const first =
      item?.member?.firstName ??
      item?.member?.prenom ??
      item?.user?.firstName ??
      item?.user?.prenom ??
      '';

    const last =
      item?.member?.lastName ??
      item?.member?.nom ??
      item?.user?.lastName ??
      item?.user?.nom ??
      '';

    const fullName = `${first} ${last}`.trim();
    if (fullName) return fullName;

    return this.memberName(memberId ?? item?.memberId);
  }

  displayMemberInitial(item: any, memberId?: number): string {
    const name = this.displayMemberName(item, memberId);
    return name?.charAt(0)?.toUpperCase() || 'U';
  }

  displayMemberEmail(item: any, memberId?: number): string {
    return (
      item?.member?.email ??
      item?.user?.email ??
      this.userEmail(memberId ?? item?.memberId)
    );
  }
  
 displayRequestUserName(r: any): string {
  const firstName =
    r?.member?.firstName ??
    r?.member?.prenom ??
    r?.user?.firstName ??
    r?.user?.prenom ??
    r?.student?.firstName ??
    r?.student?.prenom ??
    r?.appUser?.firstName ??
    r?.appUser?.prenom ??
    '';

  if (firstName?.trim()) return firstName.trim();

  const email =
    r?.member?.email ??
    r?.user?.email ??
    r?.student?.email ??
    r?.appUser?.email ??
    '';

  if (email) {
    const uByEmail = this.users.find(
      u => (u.email || '').toLowerCase() === email.toLowerCase()
    );
    if (uByEmail?.firstName?.trim()) return uByEmail.firstName.trim();
  }

  const requestMemberId =
    r?.memberId ??
    r?.userId ??
    r?.studentId ??
    r?.member?.userId ??
    r?.member?.id ??
    r?.user?.userId ??
    r?.user?.id ??
    r?.student?.userId ??
    r?.student?.id ??
    r?.appUser?.userId ??
    r?.appUser?.id;

  const uById = this.users.find(u => u.userId === requestMemberId);
  if (uById?.firstName?.trim()) return uById.firstName.trim();

  return 'Utilisateur inconnu';
}
  

  // ── Training participant stats (computed — arrow fns not allowed in templates) ──
get paidParticipantsCount(): number {
  return this.trainingParticipantsList.filter(p => p.paymentStatus === 'PAID').length;
}

get pendingParticipantsCount(): number {
  return this.trainingParticipantsList.filter(p => p.paymentStatus === 'PENDING').length;
}

get completedParticipantsCount(): number {
  return this.trainingParticipantsList.filter(p => p.completed).length;
}
}