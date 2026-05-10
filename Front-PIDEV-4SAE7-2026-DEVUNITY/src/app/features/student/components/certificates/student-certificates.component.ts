import { Component, OnInit, OnDestroy } from '@angular/core';
import {
  CertificationService,
  AnswerDTO,
  TestResultDTO
} from '../../../../core/services/certif-event/certification.service';
import { AuthService } from '../../../../core/services/auth.service';

type ExamStep = 'home' | 'exam' | 'result';

@Component({
  selector: 'app-student-certificates',
  templateUrl: './student-certificates.component.html',
  styleUrls: ['./student-certificates.component.scss']
})
export class StudentCertificatesComponent implements OnInit, OnDestroy {

  step: ExamStep = 'home';

  certificates: any[] = [];
  questions: any[] = [];
  answers: Record<number, string> = {};

  currentIndex = 0;
  result: TestResultDTO | null = null;

  loading = false;
  error = '';

  examBlocked = false;

  timeLeft = 30 * 60;
  private timerInterval: any = null;

  private tabSwitchCount = 0;
  private maxTabSwitch = 2;

  // ── Toast notifications (identical pattern to StudentClassComponent) ──
  showAlert = false;
  alertType: 'success' | 'error' | 'info' = 'info';
  alertMessage = '';

  constructor(
    private certService: CertificationService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const email = this.authService.getUserEmail();
    if (!email) {
      this.error = 'User not authenticated.';
      return;
    }
    this.loadCertificates();
    this.certService.getExamStatus().subscribe({
      next: (blocked: boolean) => { this.examBlocked = blocked; }
    });
    document.addEventListener('visibilitychange', this.handleTabChange);
  }

  ngOnDestroy(): void {
    this.stopTimer();
    document.removeEventListener('visibilitychange', this.handleTabChange);
  }

  handleTabChange = () => {
    if (document.visibilityState === 'hidden' && this.step === 'exam') {
      this.tabSwitchCount++;
      this.certService.reportTabViolation().subscribe();
      if (this.tabSwitchCount >= this.maxTabSwitch) {
        this.showToast('error', 'Exam blocked due to multiple tab switches.');
        this.forceBlock();
      } else {
        this.showToast('info', 'Warning: Do not switch tabs again.');
      }
    }
  };

  private forceBlock(): void {
    this.examBlocked = true;
    this.stopTimer();
    this.goHome();
  }

  get timerMinutes(): string {
    return String(Math.floor(this.timeLeft / 60)).padStart(2, '0');
  }

  get timerSeconds(): string {
    return String(this.timeLeft % 60).padStart(2, '0');
  }

  get timerWarning(): boolean {
    return this.timeLeft <= 5 * 60;
  }

  get timerDanger(): boolean {
    return this.timeLeft <= 60;
  }

  private startTimer(): void {
    this.timeLeft = 30 * 60;
    this.timerInterval = setInterval(() => {
      if (this.timeLeft > 0) {
        this.timeLeft--;
      } else {
        this.stopTimer();
        this.submitExam();
      }
    }, 1000);
  }

  private stopTimer(): void {
    if (this.timerInterval) {
      clearInterval(this.timerInterval);
      this.timerInterval = null;
    }
  }

  loadCertificates(): void {
    this.certService.getStudentCertificates().subscribe({
      next: (data) => this.certificates = data ?? [],
      error: () => {
        this.error = 'Failed to load certificates.';
        this.showToast('error', 'Failed to load certificates.');
      }
    });
  }

  startExam(): void {
    if (this.examBlocked) {
      this.showToast('error', 'You are permanently blocked from this exam.');
      return;
    }
    this.tabSwitchCount = 0;
    this.loading = true;
    this.error = '';
    this.answers = {};
    this.currentIndex = 0;
    this.result = null;

    this.certService.getStudentQuestions().subscribe({
      next: (data) => {
        this.questions = data ?? [];
        this.loading = false;
        if (!this.questions.length) {
          this.error = 'No questions available.';
          this.showToast('info', 'No questions available.');
          return;
        }
        this.step = 'exam';
        this.startTimer();
      },
      error: () => {
        this.loading = false;
        this.error = 'Failed to load questions.';
        this.showToast('error', 'Failed to load questions.');
      }
    });
  }

  prev(): void {
    if (this.currentIndex > 0 && this.isCurrentAnswered) {
      this.currentIndex--;
    }
  }

  next(): void {
    if (this.currentIndex < this.questions.length - 1 && this.isCurrentAnswered) {
      this.currentIndex++;
    }
  }

  selectAnswer(questionId: number, letter: string): void {
    if (this.examBlocked) return;
    this.answers[questionId] = letter;
  }

  isSelected(questionId: number, letter: string): boolean {
    return this.answers[questionId] === letter;
  }

  get currentQuestion(): any {
    return this.questions[this.currentIndex];
  }

  get isCurrentAnswered(): boolean {
    const q = this.currentQuestion;
    return !!(q && this.answers[q.id]);
  }

  get answeredCount(): number {
    return Object.keys(this.answers).length;
  }

  get allAnswered(): boolean {
    return this.questions.length > 0 &&
           this.answeredCount === this.questions.length;
  }

  get progress(): number {
    return this.questions.length
      ? Math.round((this.answeredCount / this.questions.length) * 100)
      : 0;
  }

  getOptions(q: any): { letter: string; text: string }[] {
    if (!q) return [];
    return [
      { letter: 'A', text: q.optionA },
      { letter: 'B', text: q.optionB },
      { letter: 'C', text: q.optionC },
      { letter: 'D', text: q.optionD }
    ].filter(opt => opt.text);
  }

  submitExam(): void {
    if (this.examBlocked) return;
    if (!this.allAnswered && this.timeLeft > 0) {
      this.error = 'Please answer all questions before submitting.';
      this.showToast('info', 'Please answer all questions before submitting.');
      return;
    }
    this.stopTimer();
    this.loading = true;
    this.error = '';

    const payload: AnswerDTO[] = Object.entries(this.answers).map(([id, ans]) => ({
      questionId: Number(id),
      selectedAnswer: ans
    }));

    this.certService.submitStudentExam(payload).subscribe({
      next: (res) => {
        this.result = res;
        this.step = 'result';
        this.loading = false;
        this.loadCertificates();
        this.showToast('success', res.passed ? 'Congratulations! You passed!' : 'Exam submitted.');
      },
      error: () => {
        this.loading = false;
        this.error = 'Submission failed.';
        this.showToast('error', 'Submission failed. Please try again.');
      }
    });
  }

  openCertificate(cert: any): void {
    if (!cert) return;

    let fullName = 'Student';
    try {
      const token = localStorage.getItem('jwt_token') ||
                    localStorage.getItem('access_token') ||
                    localStorage.getItem('token');
      if (token) {
        const payload = JSON.parse(atob(token.split('.')[1]));
        // Keycloak standard claims
        fullName = payload.name ||
                   (`${payload.given_name || ''} ${payload.family_name || ''}`).trim() ||
                   payload.preferred_username ||
                   payload.firstName && (`${payload.firstName} ${payload.lastName || ''}`).trim() ||
                   'Student';
      }
    } catch (e) {}

    const params = new URLSearchParams({
      name:  fullName,
      level: cert.level ?? '',
      score: String(cert.score ?? ''),
      qr:    cert.qrCode ?? ''
    });

    window.open('/certificate.html?' + params.toString(), '_blank');
  }

  goHome(): void {
    this.stopTimer();
    this.step = 'home';
    this.result = null;
    this.answers = {};
    this.questions = [];
    this.currentIndex = 0;
    this.error = '';
  }

  private showToast(type: 'success' | 'error' | 'info', message: string): void {
    this.alertType = type;
    this.alertMessage = message;
    this.showAlert = true;
    setTimeout(() => {
      this.showAlert = false;
    }, 3500);
  }
}