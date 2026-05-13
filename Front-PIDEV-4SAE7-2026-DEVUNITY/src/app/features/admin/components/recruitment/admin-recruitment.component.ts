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
  public analyzingApplicantId: number | null = null;
  public analysisResults: Map<number, any> = new Map();

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
    this.el<HTMLInputElement>('rc-f-skills').value = '';
    this.el<HTMLInputElement>('rc-f-exp').value = '0';
    this.el<HTMLInputElement>('rc-f-location').value = '';
    (this.el('rc-f-contract') as HTMLSelectElement).value = 'CDI';
    (this.el('rc-f-desc') as HTMLTextAreaElement).value = '';
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
    this.el<HTMLInputElement>('rc-f-skills').value = rec.requiredSkills || '';
    this.el<HTMLInputElement>('rc-f-exp').value = String(rec.experienceYears || 0);
    this.el<HTMLInputElement>('rc-f-location').value = rec.location || '';
    (this.el('rc-f-contract') as HTMLSelectElement).value = rec.contractType || 'CDI';
    (this.el('rc-f-desc') as HTMLTextAreaElement).value = rec.description || '';
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
    const requiredSkills = (this.el('rc-f-skills')     as HTMLInputElement).value.trim();
    const experienceYears = Number((this.el('rc-f-exp') as HTMLInputElement).value);
    const location      = (this.el('rc-f-location')   as HTMLInputElement).value.trim();
    const contractType  = (this.el('rc-f-contract')   as HTMLSelectElement).value;
    const description   = (this.el('rc-f-desc')       as HTMLTextAreaElement).value.trim();
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
      requiredSkills,
      experienceYears,
      location,
      contractType: contractType as any,
      description,
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

  analyzeApplicantCV(recId: number, appId: number): void {
    this.analyzingApplicantId = appId;
    
    // Get the applicant to access CV URL
    this.recruitmentService.getApplicantById(appId).subscribe({
      next: (applicant) => {
        if (!applicant || !applicant.cv) {
          this.analyzingApplicantId = null;
          alert('No CV found for this applicant.');
          return;
        }

        // Convert Google Docs URL to PDF download URL if needed
        const cvUrl = this.convertToPdfUrl(applicant.cv);
        
        // Send analysis request with converted URL
        this.recruitmentService.analyzeCVWithUrl(appId, recId, cvUrl).subscribe({
          next: (result) => {
            this.analyzingApplicantId = null;
            this.analysisResults.set(appId, result);
            this.renderDetail(recId);
            
            // Show notification with result
            const decision = result.decision || 'PENDING';
            const message = result.summary || `Analysis completed with decision: ${decision}`;
            alert(`🤖 AI Analysis Result\n\nDecision: ${decision}\n\n${message}`);
          },
          error: (err) => {
            this.analyzingApplicantId = null;
            console.error('Error analyzing CV:', err);
            alert('Failed to analyze CV. Please try again.');
          }
        });
      },
      error: (err) => {
        this.analyzingApplicantId = null;
        console.error('Error fetching applicant:', err);
        alert('Failed to fetch applicant data.');
      }
    });
  }

  /**
   * Convert Google Docs/Drive URLs to direct PDF download URLs
   */
  private convertToPdfUrl(url: string): string {
    if (!url) return url;

    // 1. Google Docs → PDF export
    if (url.includes('docs.google.com/document/')) {
      const docId = url.match(/\/d\/([a-zA-Z0-9-_]+)/)?.[1];
      if (docId) {
        return `https://docs.google.com/document/d/${docId}/export?format=pdf`;
      }
    }

    // 2. Google Drive file → Direct download
    if (url.includes('drive.google.com/file/')) {
      const fileId = url.match(/\/d\/([a-zA-Z0-9-_]+)/)?.[1];
      if (fileId) {
        return `https://drive.google.com/uc?export=download&id=${fileId}`;
      }
    }

    // 3. Google Drive open link → Direct download
    if (url.includes('drive.google.com/open?id=')) {
      const fileId = url.match(/id=([a-zA-Z0-9-_]+)/)?.[1];
      if (fileId) {
        return `https://drive.google.com/uc?export=download&id=${fileId}`;
      }
    }

    // 4. Already a direct PDF or other format → return as is
    return url;
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
      OPEN: 'bg-green-50 text-green-600 border border-green-100', 
      CLOSED: 'bg-[#FFDDD2] text-[#E29578] border border-[#E29578]/20', 
      CANCELLED: 'bg-red-50 text-red-600 border border-red-100',
      PENDING: 'bg-[#FFDDD2] text-[#E29578] border border-[#E29578]/20', 
      ACCEPTED: 'bg-green-50 text-green-600 border border-green-100', 
      REJECTED: 'bg-red-50 text-red-600 border border-red-100',
    };
    const cls = map[status] || map['CLOSED'];
    const label = status.charAt(0) + status.slice(1).toLowerCase();
    return `<span class="px-3 py-1 rounded-full text-[10px] font-black uppercase tracking-widest ${cls}">${label}</span>`;
  }

  private deptTag(dept: string): string {
    return `<span class="px-3 py-1 bg-[#EDF6F9] text-[#006D77] rounded-full text-xs font-bold">${this.escHtml(dept)}</span>`;
  }

  private interviewCount(rec: Recruitment): number {
    return (rec.interviews || []).length;
  }

  // ── Render ─────────────────────────────────────────────────────

  private renderList(): void {
    const tbody = this.el('rc-recruitment-tbody');
    if (this.recruitments.length === 0) {
      tbody.innerHTML = `<tr><td colspan="8" class="px-8 py-10 text-center text-gray-500 font-medium">No recruitments found. Click "New Recruitment" to create one.</td></tr>`;
      this.updateStats();
      return;
    }

    tbody.innerHTML = this.recruitments.map((rec, idx) => `
      <tr class="hover:bg-[#EDF6F9]/30 transition-all duration-200 group">
        <td class="px-8 py-5 text-sm font-bold text-gray-500">${idx + 1}</td>
        <td class="px-8 py-5 font-bold text-[#006D77] text-sm">${this.escHtml(rec.positionTitle)}</td>
        <td class="px-8 py-5">${this.deptTag(rec.department || 'N/A')}</td>
        <td class="px-8 py-5 text-sm text-gray-500 font-medium">${this.fmtDate(rec.openedAt)}</td>
        <td class="px-8 py-5 text-center"><span class="inline-flex items-center justify-center px-3 py-1 bg-purple-50 text-purple-600 rounded-lg text-sm font-bold border border-purple-100 shadow-sm">${this.interviewCount(rec)}</span></td>
        <td class="px-8 py-5 text-center"><span class="inline-flex items-center justify-center px-3 py-1 bg-[#EDF6F9] text-[#006D77] rounded-lg text-sm font-bold border border-[#83C5BE]/30 shadow-sm">${(rec.applicants || []).length}</span></td>
        <td class="px-8 py-5 text-center">${this.statusBadge(rec.status || 'OPEN')}</td>
        <td class="px-8 py-5 text-right">
          <div class="flex justify-end gap-1 opacity-0 group-hover:opacity-100 transition-all transform translate-x-2 group-hover:translate-x-0">
            <button class="p-2.5 hover:bg-[#EDF6F9] text-[#006D77] rounded-xl transition-all" title="View applicants"
              onclick="window._rcComp.showDetailView(${rec.id})"><svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M22 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/></svg></button>
            <button class="p-2.5 hover:bg-[#EDF6F9] text-[#006D77] rounded-xl transition-all" title="Edit"
              onclick="window._rcComp.openEditModal(${rec.id})"><svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M17 3a2.85 2.83 0 1 1 4 4L7.5 20.5 2 22l1.5-5.5Z"/><path d="m15 5 4 4"/></svg></button>
            <button class="p-2.5 hover:bg-[#FFDDD2] text-[#E29578] rounded-xl transition-all" title="Delete"
              onclick="window._rcComp.deleteRecruitment(${rec.id})"><svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M3 6h18"/><path d="M19 6v14c0 1-1 2-2 2H7c-1 0-2-1-2-2V6"/><path d="M8 6V4c0-1 1-2 2-2h4c1 0 2 1 2 2v2"/></svg></button>
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

      this.el('rc-detail-status-badge').outerHTML = `<span id="rc-detail-status-badge">${this.statusBadge(rec.status || 'OPEN')}</span>`;

      this.el('rc-detail-opened-date').textContent = rec.openedAt ? `Opened ${this.fmtDate(rec.openedAt)}` : '';

      const tbody = this.el('rc-applicants-tbody');
      const applicants = rec.applicants || [];

      if (applicants.length === 0) {
        tbody.innerHTML = `<tr><td colspan="6" class="px-8 py-10 text-center text-gray-500 font-medium">No applicants yet.</td></tr>`;
        return;
      }

      tbody.innerHTML = applicants.map((app, idx) => {
        const isAnalyzing = this.analyzingApplicantId === app.id;
        const analysisResult = this.analysisResults.get(app.id!);
        
        const actions = app.status === 'PENDING'
          ? `<div class="flex gap-1 justify-end">
               <button class="px-3 py-1.5 bg-[#006D77] text-white rounded-xl text-xs font-bold hover:bg-[#83C5BE] shadow-sm transition-all disabled:opacity-50 disabled:bg-gray-400 flex items-center gap-1.5" 
                 onclick="window._rcComp.analyzeApplicantCV(${rec.id},${app.id})"
                 ${isAnalyzing ? 'disabled' : ''}>
                 ${isAnalyzing ? '<svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="animate-spin"><path d="M21 12a9 9 0 1 1-6.219-8.56"/></svg> Analyzing' : '<svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M12 8V4H8"/><rect width="16" height="12" x="4" y="8" rx="2"/><path d="M2 14h2"/><path d="M20 14h2"/><path d="M15 13v2"/><path d="M9 13v2"/></svg> Analyze'}
               </button>
               <button class="p-1.5 hover:bg-green-50 text-green-600 rounded-xl transition-all font-bold" title="Accept"
                 onclick="window._rcComp.updateApplicantStatus(${rec.id},${app.id},'ACCEPTED')"><svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><polyline points="20 6 9 17 4 12"/></svg></button>
               <button class="p-1.5 hover:bg-red-50 text-[#E29578] rounded-xl transition-all font-bold" title="Reject"
                 onclick="window._rcComp.updateApplicantStatus(${rec.id},${app.id},'REJECTED')"><svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg></button>
             </div>`
          : `<span class="text-xs text-gray-300 font-bold">—</span>`;

        const interviewInfo = app.interview && app.interview.title 
          ? `<div class="text-[10px] font-bold text-[#006D77] mt-1 bg-[#EDF6F9] px-2 py-0.5 rounded-md inline-flex items-center"><svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="mr-1"><rect width="18" height="18" x="3" y="4" rx="2" ry="2"/><line x1="16" y1="2" x2="16" y2="6"/><line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/></svg> ${this.escHtml(app.interview.title)}</div>`
          : '';

        const aiResult = app.aiResult;
        const aiAnalysisInfo = aiResult 
          ? `<div class="mt-3 p-3 rounded-2xl bg-white border-2 border-[#83C5BE]/20 shadow-sm relative overflow-hidden">
               <div class="absolute top-0 right-0 px-2 py-0.5 bg-[#006D77] text-white text-[8px] font-black uppercase tracking-tighter rounded-bl-lg">AI Engine</div>
               <div class="text-[10px] font-black uppercase tracking-widest flex items-center gap-1.5 ${aiResult.decision === 'ACCEPTED' ? 'text-green-600' : 'text-[#006D77]'}">
                 <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="M12 8V4H8"/><rect width="16" height="12" x="4" y="8" rx="2"/><path d="M2 14h2"/><path d="M20 14h2"/><path d="M15 13v2"/><path d="M9 13v2"/></svg> 
                 Cluster: ${aiResult.cluster || 'N/A'}
               </div>
               ${aiResult.score ? `
                 <div class="flex items-center gap-2 mt-1.5">
                   <div class="flex-1 h-1.5 bg-gray-100 rounded-full overflow-hidden">
                     <div class="h-full rounded-full transition-all duration-1000 ${aiResult.score > 70 ? 'bg-green-500 shadow-[0_0_8px_rgba(34,197,94,0.4)]' : aiResult.score > 40 ? 'bg-yellow-500' : 'bg-red-500'}" style="width: ${aiResult.score}%"></div>
                   </div>
                   <span class="text-[10px] font-black text-gray-800">${aiResult.score}%</span>
                 </div>` : ''}
               ${aiResult.raison ? `<div class="text-[11px] text-gray-600 mt-2 leading-relaxed font-medium bg-[#EDF6F9]/30 p-2 rounded-lg border border-[#83C5BE]/10">${this.escHtml(aiResult.raison)}</div>` : ''}
             </div>`
          : '';

        return `
          <tr class="hover:bg-[#EDF6F9]/30 transition-all duration-200 group">
            <td class="px-8 py-5 text-sm font-bold text-gray-500">${idx + 1}</td>
            <td class="px-8 py-5 font-bold text-[#006D77] text-sm">
              ${(app.firstName || app.lastName) 
                ? this.escHtml(app.firstName + ' ' + app.lastName) 
                : 'USR-' + this.escHtml(app.userId)}
            </td>
            <td class="px-8 py-5 text-xs text-gray-500"><div class="truncate max-w-xs" title="${this.escHtml(app.reponse)}">${this.escHtml(app.reponse || 'No data')}</div></td>
            <td class="px-8 py-5">
              <div class="flex items-center gap-2">
                <a class="text-[#006D77] font-bold text-xs hover:underline flex items-center gap-1" href="${this.escHtml(app.cv)}" target="_blank"><svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/><polyline points="7 10 12 15 17 10"/><line x1="12" y1="15" x2="12" y2="3"/></svg> Link</a>
                <button class="p-1 text-gray-400 hover:text-[#006D77] transition-all" title="Quick View"
                  onclick="window._rcComp.openViewer('${this.escHtml(app.cv)}')"><svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M2 12s3-7 10-7 10 7 10 7-3 7-10 7-10-7-10-7Z"/><circle cx="12" cy="12" r="3"/></svg></button>
              </div>
            </td>
            <td class="px-8 py-5">
              ${this.statusBadge(app.status || 'PENDING')}
              ${interviewInfo}
              ${aiAnalysisInfo}
            </td>
            <td class="px-8 py-5 text-right opacity-0 group-hover:opacity-100 transition-opacity">
              ${actions}
            </td>
          </tr>`;
      }).join('');
    });
  }
}
