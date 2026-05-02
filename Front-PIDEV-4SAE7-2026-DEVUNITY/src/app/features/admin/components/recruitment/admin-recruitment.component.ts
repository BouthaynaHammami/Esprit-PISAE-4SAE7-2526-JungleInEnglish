import { Component, AfterViewInit, ViewEncapsulation } from '@angular/core';
import { RecruitmentService } from '../../../../core/services/recruitment.service';
import { EnhancedNotificationService } from '../../../../core/services/enhanced-notification.service';
import { Recruitment, Applicant, RecruitmentStatus, ApplicantStatus, Interview } from '../../../../core/models/recruitment.model';

// Stable global reference so innerHTML onclick strings can reach the component.
declare global { interface Window { _rcComp: AdminRecruitmentComponent; } }

@Component({
  selector: 'app-admin-recruitment',
  templateUrl: './admin-recruitment.component.html',
  styleUrls: ['./admin-recruitment.component.css'],
  encapsulation: ViewEncapsulation.None,
})
export class AdminRecruitmentComponent implements AfterViewInit {

  // ── State ──────────────────────────────────────────────────────
  public recruitments: Recruitment[] = [];
  public activeRecruitmentId: number | null = null;
  public isLoading = false;
  public isScheduling = false;
  public targetAppId: number | null = null;
  public targetRecId: number | null = null;

  constructor(
    private recruitmentService: RecruitmentService,
    private notificationService: EnhancedNotificationService
  ) {}

  // ── Angular Lifecycle ──────────────────────────────────────────
  ngAfterViewInit(): void {
    window._rcComp = this;   // register globally for innerHTML onclick callbacks
    this.refreshData();
  }

  // ── Data Fetching ──────────────────────────────────────────────
  refreshData(): void {
    this.isLoading = true;
    this.recruitmentService.getAllRecruitments().subscribe({
      next: (data) => {
        this.recruitments = data || [];
        this.renderList();
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error fetching recruitments', err);
        this.isLoading = false;
      }
    });
  }

  // ── Public handlers (called from Angular (click) bindings) ─────

  openCreateModal(): void {
    this.el<HTMLInputElement>('rc-edit-id').value = '';
    this.el('rc-modal-title').textContent = 'New recruitment';
    this.el<HTMLInputElement>('rc-f-position').value = '';
    this.el<HTMLInputElement>('rc-f-department').value = '';
    (this.el('rc-f-status') as HTMLSelectElement).value = 'OPEN';
    this.el<HTMLInputElement>('rc-f-opened').value = new Date().toISOString().slice(0, 10);
    this.clearErrors();
    this.el('rc-modal-overlay').classList.add('open');
  }

  openEditModal(recId: number): void {
    const rec = this.findRec(recId);
    if (!rec) return;
    this.el<HTMLInputElement>('rc-edit-id').value = String(rec.id);
    this.el('rc-modal-title').textContent = 'Edit recruitment';
    this.el<HTMLInputElement>('rc-f-position').value = rec.positionTitle || '';
    this.el<HTMLInputElement>('rc-f-department').value = rec.department || '';
    (this.el('rc-f-status') as HTMLSelectElement).value = rec.status || 'OPEN';
    this.el<HTMLInputElement>('rc-f-opened').value = this.getRawDate(rec.openedAt);
    this.clearErrors();
    this.el('rc-modal-overlay').classList.add('open');
  }

  closeModal(): void {
    this.el('rc-modal-overlay').classList.remove('open');
  }

  openViewer(cvUrl: string): void {
    if (!cvUrl) return;
    const viewerUrl = this.getViewerUrl(cvUrl);
    this.el<HTMLIFrameElement>('rc-viewer-frame').src = viewerUrl;
    this.el('rc-viewer-overlay').classList.add('open');
  }

  closeViewer(): void {
    this.el('rc-viewer-overlay').classList.remove('open');
    this.el<HTMLIFrameElement>('rc-viewer-frame').src = '';
  }

  onOverlayClick(event: MouseEvent): void {
    if (event.target === this.el('rc-modal-overlay')) this.closeModal();
  }

  saveRecruitment(): void {
    const positionTitle = (this.el('rc-f-position')   as HTMLInputElement).value.trim();
    const department    = (this.el('rc-f-department') as HTMLInputElement).value.trim();
    const status        = (this.el('rc-f-status')     as HTMLSelectElement).value as RecruitmentStatus;
    const openedAt      = (this.el('rc-f-opened')     as HTMLInputElement).value;

    this.clearErrors();
    let valid = true;
    if (!positionTitle)  { this.el('rc-field-position').classList.add('has-error');   valid = false; }
    if (!department)     { this.el('rc-field-department').classList.add('has-error'); valid = false; }
    if (!valid) return;

    const editId = (this.el('rc-edit-id') as HTMLInputElement).value;

    const payload: Recruitment = {
      positionTitle,
      department,
      status,
      openedAt: openedAt ? new Date(openedAt) : new Date()
    };

    if (editId) {
      this.recruitmentService.updateRecruitment(Number(editId), payload).subscribe(() => {
        this.closeModal();
        this.refreshData();
      });
    } else {
      this.recruitmentService.createRecruitment(payload).subscribe(() => {
        this.closeModal();
        this.refreshData();
      });
    }
  }

  deleteRecruitment(recId: number): void {
    if (!confirm('Delete this recruitment? This action cannot be undone.')) return;
    this.recruitmentService.deleteRecruitment(recId).subscribe(() => {
      if (this.activeRecruitmentId === recId) this.showListView();
      this.refreshData();
    });
  }

  showDetailView(recId: number): void {
    this.activeRecruitmentId = recId;
    this.el('rc-list-view').style.display = 'none';
    this.el('rc-detail-view').style.display = 'block';
    this.renderDetail(recId);
  }

  showListView(): void {
    this.activeRecruitmentId = null;
    this.el('rc-detail-view').style.display = 'none';
    this.el('rc-list-view').style.display = 'block';
    this.refreshData();
  }

  updateApplicantStatus(recId: number, appId: number, newStatus: ApplicantStatus): void {
    if (newStatus === 'ACCEPTED') {
      this.openScheduleModal(recId, appId);
      return;
    }

    this.recruitmentService.getApplicantById(appId).subscribe(app => {
      if (app) {
        app.status = newStatus;
        this.recruitmentService.updateApplicant(appId, app).subscribe(() => {
          this.renderDetail(recId);
        });
      }
    });
  }

  openScheduleModal(recId: number, appId: number): void {
    this.targetAppId = appId;
    this.targetRecId = recId;
    this.isScheduling = true;
    
    // Clear form
    this.el<HTMLInputElement>('in-f-title').value = 'Interview for ' + this.findRec(recId)?.positionTitle;
    this.el<HTMLInputElement>('in-f-date').value = '';
    this.el<HTMLInputElement>('in-f-duration').value = '30';
    this.el<HTMLInputElement>('in-f-link').value = '';
    (this.el('in-f-type') as HTMLSelectElement).value = 'ENLIGNE';
    
    this.el('rc-schedule-overlay').classList.add('open');
  }

  closeScheduleModal(): void {
    this.isScheduling = false;
    this.el('rc-schedule-overlay').classList.remove('open');
  }

  submitInterview(): void {
    if (!this.targetAppId || !this.targetRecId) return;

    const title = this.el<HTMLInputElement>('in-f-title').value.trim();
    const startStr = this.el<HTMLInputElement>('in-f-date').value;
    const duration = Number(this.el<HTMLInputElement>('in-f-duration').value);
    const link = this.el<HTMLInputElement>('in-f-link').value.trim();
    const type = (this.el('in-f-type') as HTMLSelectElement).value;

    if (!title || !startStr) {
      alert('Please fill in required fields');
      return;
    }

    // 1. Fetch Applicant first to get their userId
    this.recruitmentService.getApplicantById(this.targetAppId).subscribe(app => {
      if (!app) return;

      const interviewPayload: Interview = {
        title,
        startDateTime: new Date(startStr),
        durationMinutes: duration,
        meetingLink: link,
        meetingStatus: type as any, 
        userId: app.userId, // Synchronize userId to interview
        recruitment: { id: this.targetRecId! }
      };

      // 2. Create Interview
      this.recruitmentService.createInterview(interviewPayload).subscribe(newInt => {
        // 3. Update Applicant status and link
        if (newInt.id) {
          app.status = 'ACCEPTED';
          app.interview = { id: newInt.id };
          this.recruitmentService.updateApplicant(this.targetAppId!, app).subscribe(() => {
            
            // 4. Send notification to applicant about acceptance and interview
            if (app.userId) {
              this.recruitmentService.getUserOfApplicant(this.targetAppId!).subscribe(user => {
                const recruitment = this.findRec(this.targetRecId!);
                this.notificationService.sendNotificationToUser(
                  user.email,
                  'Application Accepted - Interview Scheduled',
                  `Congratulations! Your application for ${recruitment?.positionTitle || 'the position'} has been accepted. An interview has been scheduled for ${new Date(startStr).toLocaleDateString()} at ${new Date(startStr).toLocaleTimeString()}.`,
                  'APPLICANT_ACCEPTED',
                  this.targetAppId!,
                  'APPLICANT'
                ).subscribe({
                  next: () => console.log('Notification sent to applicant'),
                  error: (err) => console.error('Failed to send notification:', err)
                });
              });
            }

            this.closeScheduleModal();
            this.renderDetail(this.targetRecId!);
          });
        }
      });
    });
  }

  // ── Private helpers ────────────────────────────────────────────

  private el<T extends HTMLElement = HTMLElement>(id: string): T {
    return document.getElementById(id) as T;
  }

  private findRec(id: number): Recruitment | undefined {
    return this.recruitments.find(r => r.id === id);
  }

  private clearErrors(): void {
    this.el('rc-field-position').classList.remove('has-error');
    this.el('rc-field-department').classList.remove('has-error');
  }

  private getViewerUrl(url: string): string {
    if (!url) return '';
    
    // 1. Handle Google Drive Files (PDF, etc.)
    if (url.includes('drive.google.com')) {
      return url.replace(/\/view(\?.*)?$/, '/preview');
    }
    
    // 2. Handle Google-native Docs/Slides/Sheets
    if (url.includes('docs.google.com') && (url.includes('/document/') || url.includes('/presentation/') || url.includes('/spreadsheets/'))) {
      // Just ensure it ends with /preview
      return url.replace(/\/(edit|view|copy)(\?.*)?$/, '/preview');
    }

    // 3. Fallback for external direct file links (PDF, DOCX)
    return `https://docs.google.com/viewer?url=${encodeURIComponent(url)}&embedded=true`;
  }

  private updateStats(): void {
    this.el('rc-stat-total').textContent  = String(this.recruitments.length);
    this.el('rc-stat-open').textContent   = String(this.recruitments.filter(r => r.status === 'OPEN').length);
    this.el('rc-stat-closed').textContent = String(this.recruitments.filter(r => r.status === 'CLOSED').length);
  }

  private escHtml(str: any): string {
    const d = document.createElement('div');
    d.textContent = String(str || '');
    return d.innerHTML;
  }

  private fmtDate(iso: any): string {
    if (!iso) return '–';
    const date = new Date(iso);
    if (isNaN(date.getTime())) return '–';
    const d = String(date.getDate()).padStart(2, '0');
    const m = String(date.getMonth() + 1).padStart(2, '0');
    const y = date.getFullYear();
    return `${d}/${m}/${y}`;
  }

  private getRawDate(iso: any): string {
    if (!iso) return '';
    const date = new Date(iso);
    if (isNaN(date.getTime())) return '';
    return date.toISOString().slice(0, 10);
  }

  private statusBadge(status: string): string {
    const map: Record<string, string> = {
      OPEN: 'open', CLOSED: 'closed', CANCELLED: 'canceled',
      PENDING: 'pending', ACCEPTED: 'accepted', REJECTED: 'rejected',
    };
    const cls = map[status] || 'closed';
    const label = status.charAt(0) + status.slice(1).toLowerCase();
    return `<span class="rc-pill rc-pill--${cls}">${label}</span>`;
  }

  private deptTag(dept: string): string {
    return `<span class="rc-dept-tag">${this.escHtml(dept)}</span>`;
  }

  private interviewCount(rec: Recruitment): number {
    return (rec.interviews || []).length;
  }

  // ── Render ─────────────────────────────────────────────────────

  private renderList(): void {
    const tbody = this.el('rc-recruitment-tbody');
    if (this.recruitments.length === 0) {
      tbody.innerHTML = `<tr class="rc-empty"><td colspan="8">No recruitments found. Click "＋ New recruitment" to create one.</td></tr>`;
      this.updateStats();
      return;
    }

    tbody.innerHTML = this.recruitments.map((rec, idx) => `
      <tr>
        <td class="rc-td-num">${idx + 1}</td>
        <td style="font-weight:700">${this.escHtml(rec.positionTitle)}</td>
        <td>${this.deptTag(rec.department || 'N/A')}</td>
        <td style="font-size:12px;opacity:.65">${this.fmtDate(rec.openedAt)}</td>
        <td><span class="rc-count rc-count--purple">${this.interviewCount(rec)}</span></td>
        <td><span class="rc-count rc-count--teal">${(rec.applicants || []).length}</span></td>
        <td>${this.statusBadge(rec.status || 'OPEN')}</td>
        <td>
          <div class="rc-td-actions">
            <button class="rc-btn rc-btn--ghost rc-btn--icon" title="View applicants"
              onclick="window._rcComp.showDetailView(${rec.id})">👥</button>
            <button class="rc-btn rc-btn--secondary rc-btn--icon" title="Edit"
              onclick="window._rcComp.openEditModal(${rec.id})">✏️</button>
            <button class="rc-btn rc-btn--soft rc-btn--icon" title="Delete"
              onclick="window._rcComp.deleteRecruitment(${rec.id})">🗑️</button>
          </div>
        </td>
      </tr>
    `).join('');
    this.updateStats();
  }

  private renderDetail(recId: number): void {
    this.recruitmentService.getRecruitmentById(recId).subscribe(rec => {
      if (!rec) return;

      this.el('rc-detail-title-text').textContent = rec.positionTitle || 'Untitled';
      this.el('rc-detail-dept-tag').textContent   = rec.department || 'N/A';

      const map: Record<string, string> = { OPEN: 'open', CLOSED: 'closed', CANCELLED: 'canceled' };
      const badgeEl = this.el('rc-detail-status-badge');
      const statusStr = rec.status || 'CLOSED';
      badgeEl.className = `rc-pill rc-pill--${map[statusStr] || 'closed'}`;
      badgeEl.textContent = statusStr.charAt(0) + statusStr.slice(1).toLowerCase();

      this.el('rc-detail-opened-date').textContent = rec.openedAt ? `Opened ${this.fmtDate(rec.openedAt)}` : '';

      const tbody = this.el('rc-applicants-tbody');
      const applicants = rec.applicants || [];

      if (applicants.length === 0) {
        tbody.innerHTML = `<tr class="rc-empty"><td colspan="6">No applicants yet.</td></tr>`;
        return;
      }

      tbody.innerHTML = applicants.map((app, idx) => {
        const actions = app.status === 'PENDING'
          ? `<div class="rc-td-actions">
               <button class="rc-btn rc-btn--primary rc-btn--sm"
                 onclick="window._rcComp.updateApplicantStatus(${rec.id},${app.id},'ACCEPTED')">Accept</button>
               <button class="rc-btn rc-btn--accent rc-btn--sm"
                 onclick="window._rcComp.updateApplicantStatus(${rec.id},${app.id},'REJECTED')">Reject</button>
             </div>`
          : `<span style="font-size:12px;opacity:.38">—</span>`;

        const interviewInfo = app.interview && app.interview.title 
          ? `<div style="font-size:11px; color:#006D77; margin-top:4px">📅 ${this.escHtml(app.interview.title)}</div>`
          : '';

        return `
          <tr>
            <td class="rc-td-num">${idx + 1}</td>
            <td style="font-weight:700">
              ${(app.firstName || app.lastName) 
                ? this.escHtml(app.firstName + ' ' + app.lastName) 
                : 'USR-' + this.escHtml(app.userId)}
            </td>
            <td><div class="rc-cover-cell" title="${this.escHtml(app.reponse)}">${this.escHtml(app.reponse || 'No data')}</div></td>
            <td>
              <div style="display:flex; align-items:center; gap:8px">
                <a class="rc-cv-link" href="${this.escHtml(app.cv)}" target="_blank">Link</a>
                <button class="rc-btn rc-btn--ghost rc-btn--icon" style="height:24px; width:24px; font-size:12px" title="Quick View"
                  onclick="window._rcComp.openViewer('${this.escHtml(app.cv)}')">👁️</button>
              </div>
            </td>
            <td>
              ${this.statusBadge(app.status || 'PENDING')}
              ${interviewInfo}
            </td>
            <td>${actions}</td>
          </tr>`;
      }).join('');
    });
  }
}
