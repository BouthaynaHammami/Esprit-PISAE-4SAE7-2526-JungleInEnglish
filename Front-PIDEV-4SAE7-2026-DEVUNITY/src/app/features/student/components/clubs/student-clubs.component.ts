import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subject, forkJoin, of } from 'rxjs';
import { takeUntil, finalize, catchError } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';

import { Club } from '../../../../core/models/club.model';
import { Excursion, Training } from '../../../../core/models/activity.model';

import { ClubService } from '../../../../core/services/books_clubs/club.service';
import { MembershipService, ParticipationClub } from '../../../../core/services/books_clubs/membership.service';
import { RequestService, MembershipRequest } from '../../../../core/services/books_clubs/request.service';
import { ActivityService } from '../../../../core/services/books_clubs/activity.service';
import { AuthService } from '../../../../core/services/auth.service';
import { WalletService, Wallet } from '../../../../core/services/books_clubs/wallet.service';
import { environment } from '../../../../../environments/environment';

export type MemberRole = 'MEMBER' | 'SECRETARY' | 'TREASURER' | 'PRESIDENT';
type Tab = 'clubs' | 'my-clubs' | 'activities' | 'requests';
type ActivityTab = 'excursions' | 'trainings';

export interface ClubActivities {
  excursions: Excursion[];
  trainings: Training[];
  loading: boolean;
  loaded: boolean;
}

@Component({
  selector: 'app-student-clubs',
  templateUrl: './student-clubs.component.html'
})
export class StudentClubsComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();
  private readonly api = environment.apiUrl;

  tab: Tab = 'clubs';
  loading = false;
  errorMsg = '';
  successMsg = '';

  currentUserId: number | null = null;
  currentUserName = '';

  searchQuery = '';
  filterType = '';
  filterStatus = '';

  readonly roles: string[] = ['SPORT', 'CULTURAL', 'SCIENTIFIC', 'TECHNOLOGICAL', 'ARTISTIC', 'SOCIAL'];
  readonly clubRoles: MemberRole[] = ['MEMBER', 'SECRETARY', 'TREASURER', 'PRESIDENT'];

  clubs: Club[] = [];
  myClubs: ParticipationClub[] = [];
  myRequests: MembershipRequest[] = [];

  selectedClubId: number | null = null;
  activityTab: ActivityTab = 'excursions';

  allActivities: Map<number, ClubActivities> = new Map();
  participantCounts: Map<string, number> = new Map();
  myRegistrations: Set<string> = new Set();
  reservingKeys: Set<string> = new Set();

  showReserveModal = false;
  reserveType: 'exc' | 'tr' = 'exc';
  reserveActivity: Excursion | Training | null = null;

  showJoinModal = false;
  selectedClub: Club | null = null;
  motivation = '';
  joinRole: MemberRole = 'MEMBER';
  joinPhone = '';
  joinAddress = '';

  // PAYMENT + SAME WALLET AS LIBRARY
  showPaymentModal = false;
  paymentTraining: Training | null = null;
  paymentLoading = false;
  paymentSuccess: Record<number, boolean> = {};

  wallet: Wallet | null = null;
  walletLoading = false;
  insufficientFunds = false;

  constructor(
    private http: HttpClient,
    private clubService: ClubService,
    private membershipService: MembershipService,
    private requestService: RequestService,
    private activityService: ActivityService,
    private authService: AuthService,
    private walletSvc: WalletService
  ) {}

  ngOnInit(): void {
    this.currentUserId = this.authService.getUserId();
    this.currentUserName = this.authService.getUserName() ?? '';

    if (!this.currentUserId) {
      this.showError(null, 'User not identified — please log out and log back in.');
      return;
    }

    this.loadClubs();
    this.loadWallet();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  get filteredClubs(): Club[] {
    return this.clubs.filter(c => {
      const matchType = !this.filterType || c.type === this.filterType;
      const q = this.searchQuery.trim().toLowerCase();
      const matchSearch =
        !q ||
        c.name.toLowerCase().includes(q) ||
        (c.description || '').toLowerCase().includes(q);

      return matchType && matchSearch;
    });
  }

  get visibleClubs(): ParticipationClub[] {
    if (this.selectedClubId === null) return this.myClubs;
    return this.myClubs.filter(m => m.club.clubId === this.selectedClubId);
  }

  get totalActivitiesCount(): number {
    let count = 0;
    this.allActivities.forEach(a => {
      count += a.excursions.length + a.trainings.length;
    });
    return count;
  }

  get anyActivityLoading(): boolean {
    for (const [, value] of this.allActivities) {
      if (value.loading) return true;
    }
    return false;
  }

  get walletBalance(): number {
    return Number(this.wallet?.balance) || 0;
  }

  get paymentAmount(): number {
    return Number(this.paymentTraining?.price) || 0;
  }

  getClubActivities(clubId: number): ClubActivities {
    return this.allActivities.get(clubId) ?? {
      excursions: [],
      trainings: [],
      loading: false,
      loaded: false
    };
  }

  getFilteredExcursions(clubId: number): Excursion[] {
    return this.getClubActivities(clubId).excursions.filter(
      e => !this.filterStatus || e.status === this.filterStatus
    );
  }

  getFilteredTrainings(clubId: number): Training[] {
    return this.getClubActivities(clubId).trainings.filter(
      t => !this.filterStatus || t.status === this.filterStatus
    );
  }

  getParticipantCount(type: 'exc' | 'tr', id: number | undefined): number {
    if (id === undefined) return 0;
    return this.participantCounts.get(`${type}-${id}`) ?? 0;
  }

  isRegistered(type: 'exc' | 'tr', id: number | undefined): boolean {
    if (id === undefined) return false;
    return this.myRegistrations.has(`${type}-${id}`);
  }

  isReserving(type: 'exc' | 'tr', id: number | undefined): boolean {
    if (id === undefined) return false;
    return this.reservingKeys.has(`${type}-${id}`);
  }

  hasTrainingPaymentSuccess(trainingId: number | undefined): boolean {
    if (trainingId === undefined) return false;
    return !!this.paymentSuccess[trainingId];
  }

  setTab(t: Tab): void {
    this.tab = t;
    this.clearMessages();

    if (t === 'my-clubs') {
      this.loadMyClubs();
    }

    if (t === 'requests') {
      this.loadMyRequests();
    }

    if (t === 'activities') {
      this.loadMyClubs(true, () => this.loadAllActivities());
      this.selectedClubId = null;
      this.activityTab = 'excursions';
      this.filterStatus = '';
    }
  }

  onClubSelect(clubId: number | null): void {
    this.selectedClubId = clubId;

    if (clubId !== null && !this.getClubActivities(clubId).loaded) {
      this.loadActivitiesForClub(clubId);
    }
  }

  loadWallet(): void {
    if (!this.currentUserId) return;

    this.walletLoading = true;

    this.walletSvc.getWallet(this.currentUserId)
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => (this.walletLoading = false))
      )
      .subscribe({
        next: (wallet: Wallet) => {
          this.wallet = wallet;
        },
        error: () => {
          this.walletSvc.createWallet(this.currentUserId!)
            .pipe(takeUntil(this.destroy$))
            .subscribe({
              next: (wallet: Wallet) => {
                this.wallet = wallet;
              },
              error: (e: any) => this.showError(e, 'Unable to load wallet')
            });
        }
      });
  }

  loadClubs(): void {
    this.loading = true;

    this.clubService.getAll()
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => (this.loading = false))
      )
      .subscribe({
        next: (data: Club[]) => {
          this.clubs = (data ?? []).filter(c => c.status === 'ACTIVE');

          if (this.currentUserId) {
            this.loadMyClubs(true);
            this.loadMyRequests(true);
          }
        },
        error: (e: any) => this.showError(e, 'Failed to load clubs')
      });
  }

  loadMyClubs(silent = false, callback?: () => void): void {
    if (!this.currentUserId) {
      if (!silent) this.showError(null, 'User not identified');
      return;
    }

    if (!silent) this.loading = true;

    this.membershipService.clubsOfMember(this.currentUserId)
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => {
          if (!silent) this.loading = false;
        })
      )
      .subscribe({
        next: (data: ParticipationClub[]) => {
          this.myClubs = data ?? [];
          if (callback) callback();
        },
        error: (e: any) => {
          if (!silent) this.showError(e, 'Failed to load your clubs');
        }
      });
  }

  loadMyRequests(silent = false): void {
    if (!this.currentUserId) {
      if (!silent) this.showError(null, 'User not identified');
      return;
    }

    if (!silent) this.loading = true;

    this.requestService.byMember(this.currentUserId)
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => {
          if (!silent) this.loading = false;
        })
      )
      .subscribe({
        next: (data: MembershipRequest[]) => {
          this.myRequests = data ?? [];
        },
        error: (e: any) => {
          if (!silent) this.showError(e, 'Failed to load requests');
        }
      });
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
    this.allActivities.set(clubId, {
      excursions: [],
      trainings: [],
      loading: true,
      loaded: false
    });

    forkJoin({
      excursions: this.activityService.getExcursionsByClub(clubId).pipe(
        catchError(() => of([] as Excursion[]))
      ),
      trainings: this.activityService.getTrainingsByClub(clubId).pipe(
        catchError(() => of([] as Training[]))
      )
    })
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: ({ excursions, trainings }) => {
          const safeExcursions = excursions ?? [];
          const safeTrainings = trainings ?? [];

          this.allActivities.set(clubId, {
            excursions: safeExcursions,
            trainings: safeTrainings,
            loading: false,
            loaded: true
          });

          this.loadParticipantCounts(safeExcursions, safeTrainings);
        },
        error: (e: any) => {
          this.allActivities.set(clubId, {
            excursions: [],
            trainings: [],
            loading: false,
            loaded: false
          });
          this.showError(e, 'Failed to load activities');
        }
      });
  }

  loadParticipantCounts(excursions: Excursion[], trainings: Training[]): void {
    for (const e of excursions) {
      if (!e.excursionId) continue;

      this.http.get<any[]>(`${this.api}/activities/excursions/${e.excursionId}/participants`)
        .pipe(
          takeUntil(this.destroy$),
          catchError(() => of([]))
        )
        .subscribe(list => {
          this.participantCounts.set(`exc-${e.excursionId}`, list.length);

          const already = list.some((p: any) =>
            p.member?.userId === this.currentUserId || p.memberId === this.currentUserId
          );

          if (already) {
            this.myRegistrations.add(`exc-${e.excursionId}`);
          }
        });
    }

    for (const t of trainings) {
      if (!t.trainingId) continue;

      this.http.get<any[]>(`${this.api}/activities/trainings/${t.trainingId}/participants`)
        .pipe(
          takeUntil(this.destroy$),
          catchError(() => of([]))
        )
        .subscribe(list => {
          this.participantCounts.set(`tr-${t.trainingId}`, list.length);

          const already = list.some((p: any) =>
            p.member?.userId === this.currentUserId || p.memberId === this.currentUserId
          );

          if (already) {
            this.myRegistrations.add(`tr-${t.trainingId}`);
          }
        });
    }
  }

  openReserveModal(type: 'exc' | 'tr', activity: Excursion | Training): void {
    this.reserveType = type;
    this.reserveActivity = activity;
    this.showReserveModal = true;
  }

  confirmReserve(): void {
    if (!this.currentUserId || !this.reserveActivity) return;

    const type = this.reserveType;
    const id =
      type === 'exc'
        ? (this.reserveActivity as Excursion).excursionId
        : (this.reserveActivity as Training).trainingId;

    if (id === undefined) return;

    const key = `${type}-${id}`;
    this.reservingKeys.add(key);
    this.showReserveModal = false;

    if (type === 'exc') {
      this.activityService.registerExcursion(this.currentUserId, id)
        .pipe(
          takeUntil(this.destroy$),
          finalize(() => this.reservingKeys.delete(key))
        )
        .subscribe({
          next: () => {
            this.myRegistrations.add(key);
            this.participantCounts.set(key, (this.participantCounts.get(key) ?? 0) + 1);
            this.showSuccess(`Reservation confirmed — "${this.reserveActivity?.title}"!`);
            this.reserveActivity = null;
          },
          error: (e: any) => {
            const msg =
              e?.status === 409
                ? 'You are already registered for this activity.'
                : (e?.error?.message ?? 'Reservation failed');

            this.showError(null, msg);
            this.reserveActivity = null;
          }
        });

      return;
    }

    this.activityService.registerTraining(this.currentUserId, id)
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => this.reservingKeys.delete(key))
      )
      .subscribe({
        next: () => {
          this.myRegistrations.add(key);
          this.participantCounts.set(key, (this.participantCounts.get(key) ?? 0) + 1);

          const training = this.reserveActivity as Training;
          const price = Number(training.price ?? 0);

          if (price > 0) {
            this.paymentTraining = training;
            this.insufficientFunds = this.walletBalance < price;
            this.showPaymentModal = true;
            this.showSuccess(`Training reserved — choose payment for "${training.title}".`);
          } else {
            this.showSuccess(`Reservation confirmed — "${training.title}"!`);
          }

          this.reserveActivity = null;
        },
        error: (e: any) => {
          const msg =
            e?.status === 409
              ? 'You are already registered for this activity.'
              : (e?.error?.message ?? 'Reservation failed');

          this.showError(null, msg);
          this.reserveActivity = null;
        }
      });
  }

  openPaymentModal(training: Training): void {
    if (!training?.trainingId) return;
    this.paymentTraining = training;
    this.insufficientFunds = this.walletBalance < Number(training.price ?? 0);
    this.showPaymentModal = true;
  }

  closePaymentModal(): void {
    this.showPaymentModal = false;
    this.paymentTraining = null;
    this.paymentLoading = false;
    this.insufficientFunds = false;
  }

  payTraining(method: 'cash' | 'wallet'): void {
    if (!this.currentUserId || !this.paymentTraining?.trainingId) {
      this.showError(null, 'Training or user not found');
      return;
    }

    if (method === 'wallet' && this.walletBalance < this.paymentAmount) {
      this.insufficientFunds = true;
      this.showError(null, 'Insufficient wallet balance');
      return;
    }

    this.paymentLoading = true;

    const req$ =
      method === 'cash'
        ? this.activityService.payWithCash(this.currentUserId, this.paymentTraining.trainingId)
        : this.activityService.payWithWallet(this.currentUserId, this.paymentTraining.trainingId);

    req$
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => (this.paymentLoading = false))
      )
      .subscribe({
        next: () => {
          this.paymentSuccess[this.paymentTraining!.trainingId!] = true;

          if (method === 'wallet') {
            this.loadWallet();
            this.showSuccess('Wallet payment completed successfully');
          } else {
            this.showSuccess('Cash payment recorded');
          }

          this.showPaymentModal = false;
          this.paymentTraining = null;
          this.insufficientFunds = false;
        },
        error: (e: any) => {
          this.showError(e, 'Payment failed');
        }
      });
  }

  openJoinModal(club: Club): void {
    this.selectedClub = club;
    this.motivation = '';
    this.joinRole = 'MEMBER';
    this.joinPhone = '';
    this.joinAddress = '';
    this.showJoinModal = true;
  }

  sendRequest(): void {
    if (!this.currentUserId) {
      this.showError(null, 'User not identified — please log back in.');
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
      this.joinPhone ? `[Phone: ${this.joinPhone}]` : '',
      this.joinAddress ? `[Address: ${this.joinAddress}]` : ''
    ]
      .filter(Boolean)
      .join(' ') || '—';

    this.requestService.create(this.currentUserId, this.selectedClub.clubId, fullMotivation)
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => (this.loading = false))
      )
      .subscribe({
        next: () => {
          this.showSuccess(`Request sent to "${this.selectedClub?.name}" as ${this.joinRole}!`);
          this.showJoinModal = false;
          this.motivation = '';
          this.loadMyRequests(true);
        },
        error: (e: any) => {
          const msg =
            e?.status === 409
              ? 'You already have a pending request for this club.'
              : (e?.error?.message ?? `Failed to send request (${e?.status ?? 'unknown error'})`);

          this.showError(null, msg);
        }
      });
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

  getRoleStyle(role?: string): string {
    const map: Record<string, string> = {
      PRESIDENT: 'background:#FEF3C7;color:#D97706',
      SECRETARY: 'background:#EDF6F9;color:#006D77',
      TREASURER: 'background:#FFDDD2;color:#c9785f',
      MEMBER: 'background:#f3f4f6;color:#6b7280'
    };

    return map[role ?? ''] ?? 'background:#f3f4f6;color:#6b7280';
  }

  getCapacityBarStyle(capacity: number, reserved: number): string {
    if (!capacity || capacity <= 0) return 'width:0%;background:#83C5BE';

    const pct = Math.min(100, Math.round((reserved / capacity) * 100));
    const color = pct >= 90 ? '#E29578' : pct >= 60 ? '#f59e0b' : '#006D77';

    return `width:${pct}%;background:${color}`;
  }

  getTrainingCapacityBarStyle(capacity: number, reserved: number): string {
    if (!capacity || capacity <= 0) return 'width:0%;background:#E29578';

    const pct = Math.min(100, Math.round((reserved / capacity) * 100));
    const color = pct >= 90 ? '#dc2626' : pct >= 60 ? '#f59e0b' : '#E29578';

    return `width:${pct}%;background:${color}`;
  }

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

  statusColor(status?: string): string {
    const map: Record<string, string> = {
      ONGOING: 'background:#D1FAE5;color:#059669',
      PLANNED: 'background:#EDF6F9;color:#006D77',
      FINISHED: 'background:#F3F4F6;color:#6B7280',
      COMPLETED: 'background:#F3F4F6;color:#6B7280',
      CANCELLED: 'background:#FEE2E2;color:#DC2626'
    };

    return map[status ?? ''] ?? 'background:#EDF6F9;color:#006D77';
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
}