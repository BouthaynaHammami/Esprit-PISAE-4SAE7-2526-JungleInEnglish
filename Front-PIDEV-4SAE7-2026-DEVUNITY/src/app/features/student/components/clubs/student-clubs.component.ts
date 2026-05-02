// src/app/features/student/components/clubs/student-clubs.component.ts
import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Subject, forkJoin, of } from 'rxjs';
import { takeUntil, finalize, catchError } from 'rxjs/operators';

import { Club } from '../../../../core/models/club.model';
import { Excursion, Training } from '../../../../core/models/activity.model';

import { ClubService } from '../../../../core/services/books_clubs/club.service';
import { MembershipService, ParticipationClub } from '../../../../core/services/books_clubs/membership.service';
import { RequestService, MembershipRequest } from '../../../../core/services/books_clubs/request.service';
import { ActivityService } from '../../../../core/services/books_clubs/activity.service';
import { AuthService } from '../../../../core/services/auth.service';
import { environment } from '../../../../../environments/environment';

export type MemberRole = 'MEMBER' | 'SECRETARY' | 'TREASURER' | 'PRESIDENT';
type Tab = 'clubs' | 'my-clubs' | 'activities' | 'requests';
type ActivityTab = 'excursions' | 'trainings';

export interface ClubActivities {
  excursions: Excursion[];
  trainings:  Training[];
  loading:    boolean;
  loaded:     boolean;
}

@Component({
  selector: 'app-student-clubs',
  templateUrl: './student-clubs.component.html',
  styleUrls: ['./student-clubs.component.scss']
})
export class StudentClubsComponent implements OnInit, OnDestroy {

  private destroy$ = new Subject<void>();
  private readonly api = `${environment.apiUrl}/learners/api`;

  tab: Tab = 'clubs';
  loading  = false;
  errorMsg   = '';
  successMsg = '';

  currentUserId:   number | null = null;
  currentUserName: string        = '';

  searchQuery = '';
  filterType  = '';
  readonly roles: string[]       = ['SPORT', 'CULTURAL', 'SCIENTIFIC', 'TECHNOLOGICAL', 'ARTISTIC', 'SOCIAL'];
  readonly clubRoles: MemberRole[] = ['MEMBER', 'SECRETARY', 'TREASURER', 'PRESIDENT'];

  clubs: Club[] = [];

  get filteredClubs(): Club[] {
    return this.clubs.filter(c => {
      const matchType   = !this.filterType   || c.type === this.filterType;
      const matchSearch = !this.searchQuery  ||
        c.name.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
        (c.description || '').toLowerCase().includes(this.searchQuery.toLowerCase());
      return matchType && matchSearch;
    });
  }

  myClubs:    ParticipationClub[]  = [];
  myRequests: MembershipRequest[]  = [];

  // ── Activities ─────────────────────────────────────────────────────────────
  selectedClubId: number | null = null;
  activityTab: ActivityTab      = 'excursions';
  filterStatus = '';

  allActivities:    Map<number, ClubActivities> = new Map();
  participantCounts: Map<string, number>        = new Map();
  myRegistrations:   Set<string>                = new Set();
  reservingKeys:     Set<string>                = new Set();

  get visibleClubs(): ParticipationClub[] {
    if (this.selectedClubId === null) return this.myClubs;
    return this.myClubs.filter(m => m.club.clubId === this.selectedClubId);
  }

  get totalActivitiesCount(): number {
    let count = 0;
    this.allActivities.forEach(a => count += a.excursions.length + a.trainings.length);
    return count;
  }

  get anyActivityLoading(): boolean {
    for (const [, v] of this.allActivities) { if (v.loading) return true; }
    return false;
  }

  getClubActivities(clubId: number): ClubActivities {
    return this.allActivities.get(clubId) ?? { excursions: [], trainings: [], loading: false, loaded: false };
  }

  getFilteredExcursions(clubId: number): Excursion[] {
    return this.getClubActivities(clubId).excursions.filter(e =>
      !this.filterStatus || e.status === this.filterStatus
    );
  }

  getFilteredTrainings(clubId: number): Training[] {
    return this.getClubActivities(clubId).trainings.filter(t =>
      !this.filterStatus || t.status === this.filterStatus
    );
  }

  getParticipantCount(type: 'exc' | 'tr', id: number): number {
    return this.participantCounts.get(`${type}-${id}`) ?? 0;
  }

  isRegistered(type: 'exc' | 'tr', id: number): boolean {
    return this.myRegistrations.has(`${type}-${id}`);
  }

  isReserving(type: 'exc' | 'tr', id: number): boolean {
    return this.reservingKeys.has(`${type}-${id}`);
  }

  // ── Modals ─────────────────────────────────────────────────────────────────
  showReserveModal  = false;
  reserveType: 'exc' | 'tr' = 'exc';
  reserveActivity: Excursion | Training | null = null;

  showJoinModal  = false;
  selectedClub: Club | null = null;
  motivation   = '';
  joinRole: MemberRole = 'MEMBER';
  joinPhone    = '';
  joinAddress  = '';

  constructor(
    private http:              HttpClient,
    private clubService:       ClubService,
    private membershipService: MembershipService,
    private requestService:    RequestService,
    private activityService:   ActivityService,
    private authService:       AuthService
  ) {}

  ngOnInit(): void {
    this.currentUserId   = this.authService.getUserId();
    this.currentUserName = this.authService.getUserFullName();

    // ✅ Debug — à retirer en production
    console.log('[Clubs] currentUserId:', this.currentUserId);
    console.log('[Clubs] currentUserName:', this.currentUserName);
    console.log('[Clubs] JWT decoded:', this.authService.decodeToken());

    if (!this.currentUserId) {
      this.showError(null, 'User not identified — déconnecte-toi et reconnecte-toi.');
    }
    this.loadClubs();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  setTab(t: Tab): void {
    this.tab = t;
    this.clearMessages();
    if (t === 'my-clubs')   this.loadMyClubs();
    if (t === 'requests')   this.loadMyRequests();
    if (t === 'activities') {
      this.loadMyClubs(true, () => this.loadAllActivities());
      this.selectedClubId = null;
      this.activityTab    = 'excursions';
      this.filterStatus   = '';
    }
  }

  onClubSelect(clubId: number | null): void {
    this.selectedClubId = clubId;
    if (clubId !== null && !this.getClubActivities(clubId).loaded) {
      this.loadActivitiesForClub(clubId);
    }
  }

  loadAllActivities(): void {
    for (const m of this.myClubs) {
      const id = m.club.clubId;
      if (id && !this.getClubActivities(id).loaded) {
        this.loadActivitiesForClub(id);
      }
    }
  }

  loadActivitiesForClub(clubId: number): void {
    this.allActivities.set(clubId, { excursions: [], trainings: [], loading: true, loaded: false });

    forkJoin({
      excursions: this.activityService.getExcursionsByClub(clubId).pipe(catchError(() => of([] as Excursion[]))),
      trainings:  this.activityService.getTrainingsByClub(clubId).pipe(catchError(() => of([] as Training[])))
    })
    .pipe(takeUntil(this.destroy$))
    .subscribe(({ excursions, trainings }) => {
      this.allActivities.set(clubId, {
        excursions: excursions ?? [],
        trainings:  trainings  ?? [],
        loading: false,
        loaded:  true
      });
      this.loadParticipantCounts(excursions ?? [], trainings ?? []);
    });
  }

  loadParticipantCounts(excursions: Excursion[], trainings: Training[]): void {
    for (const e of excursions) {
      if (!e.excursionId) continue;
      this.http.get<any[]>(`${this.api}/api/activities/excursions/${e.excursionId}/participants`)
        .pipe(takeUntil(this.destroy$), catchError(() => of([])))
        .subscribe(list => {
          this.participantCounts.set(`exc-${e.excursionId}`, list.length);
          const already = list.some((p: any) =>
            p.member?.userId === this.currentUserId || p.memberId === this.currentUserId
          );
          if (already) this.myRegistrations.add(`exc-${e.excursionId}`);
        });
    }
    for (const t of trainings) {
      if (!t.trainingId) continue;
      this.http.get<any[]>(`${this.api}/api/activities/trainings/${t.trainingId}/participants`)
        .pipe(takeUntil(this.destroy$), catchError(() => of([])))
        .subscribe(list => {
          this.participantCounts.set(`tr-${t.trainingId}`, list.length);
          const already = list.some((p: any) =>
            p.member?.userId === this.currentUserId || p.memberId === this.currentUserId
          );
          if (already) this.myRegistrations.add(`tr-${t.trainingId}`);
        });
    }
  }

  // ── Réservation ────────────────────────────────────────────────────────────

  openReserveModal(type: 'exc' | 'tr', activity: Excursion | Training): void {
    this.reserveType     = type;
    this.reserveActivity = activity;
    this.showReserveModal = true;
  }

  confirmReserve(): void {
    if (!this.currentUserId || !this.reserveActivity) return;

    const type = this.reserveType;
    const id   = type === 'exc'
      ? (this.reserveActivity as Excursion).excursionId!
      : (this.reserveActivity as Training).trainingId!;
    const key  = `${type}-${id}`;

    this.reservingKeys.add(key);
    this.showReserveModal = false;

    const url = type === 'exc'
      ? `${this.api}/api/activities/excursions/register?memberId=${this.currentUserId}&excursionId=${id}`
      : `${this.api}/api/activities/trainings/register?memberId=${this.currentUserId}&trainingId=${id}`;

    this.http.post<any>(url, {})
      .pipe(takeUntil(this.destroy$), finalize(() => this.reservingKeys.delete(key)))
      .subscribe({
        next: () => {
          this.myRegistrations.add(key);
          this.participantCounts.set(key, (this.participantCounts.get(key) ?? 0) + 1);
          this.showSuccess(`Réservation confirmée — "${this.reserveActivity!.title}" !`);
          this.reserveActivity = null;
        },
        error: e => {
          const msg = e?.status === 409
            ? 'Vous êtes déjà inscrit à cette activité.'
            : (e?.error?.message ?? 'Échec de la réservation');
          this.showError(null, msg);
        }
      });
  }

  // ── Join club ──────────────────────────────────────────────────────────────

  openJoinModal(club: Club): void {
    this.selectedClub = club;
    this.motivation   = '';
    this.joinRole     = 'MEMBER';
    this.joinPhone    = '';
    this.joinAddress  = '';
    this.showJoinModal = true;
  }

  sendRequest(): void {
    // ✅ Debug — à retirer en production
    console.log('[sendRequest] userId:', this.currentUserId);
    console.log('[sendRequest] clubId:', this.selectedClub?.clubId);
    console.log('[sendRequest] role:', this.joinRole);

    if (!this.currentUserId) {
      this.showError(null, 'User not identified — veuillez vous reconnecter.');
      return;
    }
    if (!this.selectedClub?.clubId) {
      this.showError(null, 'No club selected.');
      return;
    }

    this.loading = true;

    const fullMotivation = [
      this.motivation || '',
      this.joinRole !== 'MEMBER' ? `[Role: ${this.joinRole}]` : '',
      this.joinPhone    ? `[Phone: ${this.joinPhone}]`       : '',
      this.joinAddress  ? `[Address: ${this.joinAddress}]`   : ''
    ].filter(Boolean).join(' ') || '—';

    console.log('[sendRequest] motivation:', fullMotivation);

    this.requestService.create(this.currentUserId, this.selectedClub.clubId, fullMotivation)
      .pipe(takeUntil(this.destroy$), finalize(() => this.loading = false))
      .subscribe({
        next: () => {
          this.showSuccess(`Request sent to "${this.selectedClub!.name}" as ${this.joinRole}!`);
          this.showJoinModal = false;
          this.motivation    = '';
          this.loadMyRequests(true);
        },
        error: e => {
          console.error('[sendRequest] error:', e);
          const msg = e?.status === 409
            ? 'You already have a pending request for this club.'
            : (e?.error?.message ?? `Failed to send request (${e?.status ?? 'unknown error'})`);
          this.showError(null, msg);
        }
      });
  }

  // ── Utilitaires ────────────────────────────────────────────────────────────

  typeIcon(type: string): string {
    const map: Record<string, string> = {
      SPORT: '⚽', CULTURAL: '🎭', SCIENTIFIC: '🔬',
      TECHNOLOGICAL: '💻', ARTISTIC: '🎨', SOCIAL: '🤝'
    };
    return map[type] ?? '🏛';
  }

  roleIcon(role: string): string {
    const map: Record<string, string> = {
      PRESIDENT: '👑', SECRETARY: '📝', TREASURER: '💰', MEMBER: '👤'
    };
    return map[role] ?? '👤';
  }

  statusColor(status: string): string {
    const map: Record<string, string> = {
      ONGOING:   'background:#D1FAE5;color:#059669',
      PLANNED:   'background:#EDF6F9;color:#006D77',
      FINISHED:  'background:#F3F4F6;color:#6B7280',
      COMPLETED: 'background:#F3F4F6;color:#6B7280',
      CANCELLED: 'background:#FEE2E2;color:#DC2626'
    };
    return map[status] ?? 'background:#EDF6F9;color:#006D77';
  }

  getReserveDetail(activity: Excursion | Training): string {
    if (!activity) {
      return 'Unknown location';
    }
    if (this.reserveType === 'exc') {
      return (activity as Excursion).location || 'Unknown location';
    }
    return (activity as Training).trainer || 'Instructor';
  }

  isMyClub(clubId: number): boolean {
    return this.myClubs.some(m => m.club.clubId === clubId);
  }

  hasPendingRequest(clubId: number): boolean {
    return this.myRequests.some(r => r.club?.clubId === clubId && r.status === 'PENDING');
  }

  clubName(clubId: number): string {
    return this.clubs.find(c => c.clubId === clubId)?.name ?? `#${clubId}`;
  }

  extractRole(motivation: string): string {
    const match = motivation?.match(/\[Role:\s*([^\]]+)\]/);
    if (!match) return '';
    return `${this.roleIcon(match[1].trim())} ${match[1].trim()}`;
  }

  cleanMotivation(motivation: string): string {
    return (motivation ?? '')
      .replace(/\[(Role|Phone|Address):[^\]]*\]/g, '')
      .replace(/\s+/g, ' ')
      .trim();
  }

  // ── Loaders ────────────────────────────────────────────────────────────────

  loadClubs(): void {
    this.loading = true;
    this.clubService.getAll()
      .pipe(takeUntil(this.destroy$), finalize(() => this.loading = false))
      .subscribe({
        next: (d: Club[]) => {
          this.clubs = (d ?? []).filter(c => c.status === 'ACTIVE');
          if (this.currentUserId) {
            this.loadMyClubs(true);
            this.loadMyRequests(true);
          }
        },
        error: e => this.showError(e, 'Failed to load clubs')
      });
  }

  loadMyClubs(silent = false, callback?: () => void): void {
    if (!this.currentUserId) {
      if (!silent) this.showError(null, 'User not identified');
      return;
    }
    if (!silent) this.loading = true;
    this.membershipService.clubsOfMember(this.currentUserId)
      .pipe(takeUntil(this.destroy$), finalize(() => { if (!silent) this.loading = false; }))
      .subscribe({
        next: (d: ParticipationClub[]) => {
          this.myClubs = d ?? [];
          if (callback) callback();
        },
        error: e => { if (!silent) this.showError(e, 'Failed to load your clubs'); }
      });
  }

  loadMyRequests(silent = false): void {
    if (!this.currentUserId) {
      if (!silent) this.showError(null, 'User not identified');
      return;
    }
    if (!silent) this.loading = true;
    this.requestService.byMember(this.currentUserId)
      .pipe(takeUntil(this.destroy$), finalize(() => { if (!silent) this.loading = false; }))
      .subscribe({
        next: (d: MembershipRequest[]) => this.myRequests = d ?? [],
        error: e => { if (silent) return; this.showError(e, 'Failed to load requests'); }
      });
  }

  // ── Feedback ───────────────────────────────────────────────────────────────

  private clearMessages(): void {
    this.errorMsg   = '';
    this.successMsg = '';
  }

  private showSuccess(msg: string): void {
    this.successMsg = msg;
    this.errorMsg   = '';
    setTimeout(() => this.successMsg = '', 4000);
  }

  private showError(err: any, fallback = 'An error occurred'): void {
    this.errorMsg   = err?.error?.message ?? err?.message ?? fallback;
    this.successMsg = '';
  }
}