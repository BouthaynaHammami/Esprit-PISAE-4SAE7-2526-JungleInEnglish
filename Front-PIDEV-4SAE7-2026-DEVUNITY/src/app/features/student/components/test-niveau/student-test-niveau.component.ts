// src/app/features/student/components/test-niveau/student-test-niveau.component.ts
import { Component, OnInit, OnDestroy, Inject, Optional } from '@angular/core';
import { LevelTestService } from '../../../../core/services/level-test.service';
import { AuthService } from '../../../../core/services/auth.service';
import { BadWordDetectorService } from '../../../../core/services/bad-word-detector.service';
import { VapiTranscript } from '../../../../core/services/vapi.service'; // Added VapiTranscript import
import { Subject } from '../../../../core/models/subject.model';
import { TestTentative } from '../../../../core/models/test-tentative.model';
import { environment } from '../../../../../environments/environment';

@Component({
  selector: 'app-student-test-niveau',
  templateUrl: './student-test-niveau.component.html',
  styleUrls: ['./student-test-niveau.component.css']
})
export class StudentTestNiveauComponent implements OnInit, OnDestroy {
  subjects: Subject[] = [];
  myTentatives: TestTentative[] = [];

  selectedSubject: Subject | null = null;
  writtenParagraph = '';
  submitting = false;
  submitSuccess = false;
  submitError = '';
  
  // Bad word detector
  hasBadWords = false;
  detectedBadWords: string[] = [];
  badWordWarning = '';

  loadingSubjects = true;
  loadingHistory = true;

  currentUserId: number | null = null;
  
  // State
  isTestActive = false;

  // Oral test states
  isOralTestActive = false;
  isConnecting = false;
  isConnected = false;
  isSpeaking = false;
  isListening = false;
  currentQuestion = '';
  currentQuestionIndex: number = 0;
  totalQuestions = 3;
  oralResponses: string[] = [];
  showOralTestTransition = false;
  
  // Course Recommendation State
  recommendationTestId: number | undefined = undefined;
  recommendationLevel: string | null | undefined = undefined;
  recommendationScore: number | undefined = undefined;
  
  // ✅ Live transcript like reference project
  activeTranscript: { role: string, text: string } = { role: '', text: '' };
  isUserSpeaking: boolean = false;
  vapiService: any = null; // Dynamic VapiService instance

  constructor(
    private levelTestService: LevelTestService,
    private authService: AuthService,
    private badWordDetector: BadWordDetectorService
  ) {
    // Initialize VapiService dynamically to avoid injection issues
    this.initializeVapiService();
  }

  private initializeVapiService(): void {
    try {
      // Try to import and initialize VapiService dynamically
      import('../../../../core/services/vapi.service').then(({ VapiService }) => {
        this.vapiService = new VapiService();
        this.setupVapiListeners();
        console.log('✅ VAPI Service initialized successfully');
        console.log('🎤 Ready for oral testing with Assistant ID: 1920d213-2732-442e-8f1e-e50d451a98c4');
      }).catch(error => {
        console.warn('⚠️ VapiService not available:', error);
        console.warn('💡 Make sure to add your VAPI public key in vapi.service.ts');
        this.vapiService = null;
      });
    } catch (error) {
      console.warn('❌ Failed to initialize VapiService:', error);
      this.vapiService = null;
    }
  }

  ngOnInit(): void {
    this.currentUserId = this.authService.getUserId();
    this.loadSubjects();
    this.loadMyHistory();
    // VapiService will be initialized in constructor
  }

  ngOnDestroy(): void {
    if (this.isConnected && this.vapiService) {
      this.vapiService.disconnect();
    }
  }

  loadSubjects(): void {
    this.loadingSubjects = true;
    this.levelTestService.getAllSubjects().subscribe({
      next: (data) => { this.subjects = data; this.loadingSubjects = false; },
      error: () => { this.loadingSubjects = false; }
    });
  }

  loadMyHistory(): void {
    this.loadingHistory = true;
    this.levelTestService.getAllTentatives().subscribe({
      next: (data) => {
        this.myTentatives = this.currentUserId
          ? data.filter(t => t.userId === this.currentUserId)
          : data;
        this.loadingHistory = false;
      },
      error: () => { this.loadingHistory = false; }
    });
  }

  startRandomTest(): void {
    if (this.subjects.length === 0) return;
    
    // Pick random subject
    const randomIndex = Math.floor(Math.random() * this.subjects.length);
    this.selectedSubject = this.subjects[randomIndex];
    
    this.isTestActive = true;
    this.writtenParagraph = '';
    this.submitSuccess = false;
    this.submitError = '';
  }



  cancelTest(): void {
    if (confirm('Cancel this test? Progress will be lost.')) {
      this.isTestActive = false;
      this.selectedSubject = null;
    }
  }

  submitTest(): void {
    if (!this.selectedSubject || !this.writtenParagraph.trim()) return;
    
    // ✅ Check for bad words before allowing submission
    if (this.hasBadWords) {
      this.submitError = '❌ Cannot submit: Your response contains inappropriate language. Please revise it before submitting.';
      return;
    }

    this.submitting = true;
    this.submitSuccess = false;
    this.submitError = '';

    const tentative: TestTentative = {
      subject: this.selectedSubject,
      paragraph: this.writtenParagraph.trim(),
      userId: this.currentUserId ?? undefined,
      status: 'PENDING'
    };

    this.levelTestService.createTentative(tentative).subscribe({
      next: () => {
        this.isTestActive = false;
        this.submitting = false;
        this.submitSuccess = true;
        
        // Start oral test transition
        this.showOralTestTransition = true;
        this.startOralTestPhase();
      },
      error: (err) => {
        this.submitting = false;
        this.submitError = err?.error?.message ?? 'Submission failed. Please try again.';
      }
    });
  }

  getStatusClass(status?: string): string {
    switch (status) {
      case 'CORRECTED': return 'badge-success';
      case 'REJECTED':  return 'badge-danger';
      default:          return 'badge-warning';
    }
  }

  // ─── CEFR Level Display Helpers ─────────────────────────
  extractLevel(feedback?: string, score?: number): string | null {
    // First try to extract from feedback tag
    if (feedback) {
      const match = feedback.match(/\[AI\s*-\s*(A1|A2|B1|B2|C1|C2)\]/i);
      if (match) return match[1].toUpperCase();
    }
    // Derive from score using CEFR mapping
    if (score != null) {
      if (score >= 85) return 'C2';
      if (score >= 75) return 'C1';
      if (score >= 65) return 'B2';
      if (score >= 55) return 'B1';
      if (score >= 40) return 'A2';
      return 'A1';
    }
    return null;
  }

  getLevelClass(level: string | null): string {
    if (!level) return '';
    const map: Record<string, string> = {
      'C2': 'level-c2', 'C1': 'level-c1',
      'B2': 'level-b2', 'B1': 'level-b1',
      'A2': 'level-a2', 'A1': 'level-a1'
    };
    return map[level] ?? '';
  }

  getLevelLabel(level: string | null): string {
    if (!level) return '';
    const labels: Record<string, string> = {
      'C2': 'Mastery', 'C1': 'Advanced',
      'B2': 'Upper Intermediate', 'B1': 'Intermediate',
      'A2': 'Elementary', 'A1': 'Beginner'
    };
    return labels[level] ?? level;
  }

  getScoreClass(score?: number): string {
    if (score == null) return '';
    if (score >= 85) return 'score-excellent';
    if (score >= 65) return 'score-good';
    if (score >= 40) return 'score-fair';
    return 'score-low';
  }

  cleanFeedback(feedback?: string): string {
    if (!feedback) return 'Awaiting evaluation...';
    // Strip the [AI - XX] prefix for cleaner display
    return feedback.replace(/\[AI\s*-\s*(?:A1|A2|B1|B2|C1|C2)\]\s*/i, '');
  }

  // Bad Word Detector
  checkForBadWords(): void {
    const result = this.badWordDetector.checkText(this.writtenParagraph);
    this.hasBadWords = result.hasBadWords;
    this.detectedBadWords = result.badWordsList;
    this.badWordWarning = this.badWordDetector.getWarningMessage(result.badWordsList);
  }

  // Oral Test Methods
  setupVapiListeners(): void {
    if (!this.vapiService) {
      console.warn('VapiService not available');
      return;
    }
    
    this.vapiService.onConnectionChange().subscribe((connected: boolean) => {
      this.isConnected = connected;
      this.isConnecting = false;
    });

    this.vapiService.onSpeechStart().subscribe(() => {
      this.isSpeaking = true;
    });

    this.vapiService.onSpeechEnd().subscribe(() => {
      this.isSpeaking = false;
    });

    this.vapiService.onUserSpeechStart().subscribe(() => {
      this.isListening = true;
    });

    this.vapiService.onUserSpeechEnd().subscribe(() => {
      this.isListening = false;
    });

    // Unified transcript stream (partials + finals)
    this.vapiService.onTranscript().subscribe((transcript: VapiTranscript) => {
      this.activeTranscript = { role: transcript.role, text: transcript.text };
      
      if (transcript.isFinal && transcript.role === 'user' && this.isOralTestActive) {
        this.handleOralResponse(transcript.text);
      }
    });

    this.vapiService.onCallEnd().subscribe(() => {
      this.endOralTest();
    });
  }

  async startOralTestPhase(): Promise<void> {
    if (!this.selectedSubject) {
      console.warn('❌ Cannot start oral test: no subject selected');
      this.completeFullTest();
      return;
    }

    if (!this.vapiService) {
      console.warn('❌ Cannot start oral test: VAPI service not available');
      this.showOralTestUnavailable();
      return;
    }

    this.showOralTestTransition = false;
    this.isConnecting = true;
    this.submitError = '';

    try {
      // Check VAPI status
      const status = this.vapiService.getInitializationStatus();
      console.log('🔍 VAPI Status:', status);

      if (status === 'failed') {
        throw new Error('VAPI initialization failed. Please check your internet connection and try again.');
      }

      if (status === 'initializing') {
        console.log('⏳ Waiting for VAPI to initialize...');
      }

      // Connect to VAPI — service uses inline assistant config (no dashboard setup required)
      await this.vapiService.connect({
        variableValues: {
          subject: this.selectedSubject.title,
          userId: this.currentUserId?.toString() ?? ''
        }
      });

      this.isOralTestActive = true;
      this.currentQuestionIndex = 0;
      this.oralResponses = [];

      // Start with introduction
      this.startOralTestIntroduction();

    } catch (error) {
      console.error('❌ Failed to start oral test:', error);
      this.submitError = `Voice assistant connection failed: ${error}. You can skip the oral test or try again.`;
      this.isConnecting = false;
      this.showOralTestFallback();
    }
  }

  private showOralTestUnavailable(): void {
    this.submitError = 'Voice testing is currently unavailable. Your written test has been submitted successfully.';
    this.completeFullTest();
  }

  private showOralTestFallback(): void {
    // Show option to skip or retry
    this.showOralTestTransition = false;
    this.isConnecting = false;
  }

  startOralTestIntroduction(): void {
    if (!this.vapiService) return;
    
    const introMessage = `Great job on your written test! Now let's continue with the oral portion. 
      I will ask you ${this.totalQuestions} questions about ${this.selectedSubject?.title}. 
      Please speak clearly and take your time to answer each question. 
      You have 5 minutes for this oral test. Let's begin with the first question.`;
    
    this.vapiService.speak(introMessage).then(() => {
      this.askNextOralQuestion();
    });
  }

  askNextOralQuestion(): void {
    if (!this.vapiService) return;
    
    if (this.currentQuestionIndex >= this.totalQuestions) {
      this.completeOralTest();
      return;
    }

    const question = this.generateOralQuestion(this.currentQuestionIndex + 1);
    this.currentQuestion = question;

    const questionMessage = `Question ${this.currentQuestionIndex + 1}: ${question}`;
    this.vapiService.speak(questionMessage);
  }

  generateOralQuestion(questionNumber: number): string {
    const subjectName = this.selectedSubject?.title || 'this subject';
    const questions = [
      `Can you briefly explain what you wrote about in your paragraph regarding ${subjectName}?`,
      `What is the most important concept you learned about ${subjectName}?`,
      `How would you apply ${subjectName} in a real-world situation?`
    ];
    
    return questions[questionNumber - 1] || `Tell me more about your understanding of ${subjectName}`;
  }

  handleOralResponse(transcript: string): void {
    if (!this.isOralTestActive || this.isSpeaking) return;

    console.log('Oral response:', transcript);
    this.oralResponses.push(transcript);
    this.currentQuestionIndex++;
    
    // Brief acknowledgment before next question
    if (this.vapiService) {
      this.vapiService.speak("I understand. Thank you.").then(() => {
        this.askNextOralQuestion();
      });
    } else {
      this.askNextOralQuestion();
    }
  }

  completeOralTest(): void {
    if (this.vapiService) {
      this.vapiService.speak("Excellent! You have completed both the written and oral portions of your level test. Thank you for your participation!");
    }
    
    this.endOralTest();
  }

  saveCompleteTest(): void {
    if (!this.selectedSubject) return;

    // Save written + oral responses together via the existing tentative endpoint
    const tentative: TestTentative = {
      subject: this.selectedSubject,
      paragraph: this.writtenParagraph + '\n\n--- ORAL RESPONSES ---\n' +
                 this.oralResponses.map((r, i) => `Q${i + 1}: ${r}`).join('\n'),
      userId: this.currentUserId ?? undefined,
      status: 'PENDING'
    };

    this.levelTestService.createTentative(tentative).subscribe({
      next: () => console.log('✅ Complete test (written + oral) saved successfully'),
      error: (err) => console.error('❌ Failed to save complete test:', err)
    });
  }

  endOralTest(): void {
    // 1. Disconnect VAPI
    if (this.vapiService && this.isConnected) {
      this.vapiService.disconnect();
    }
    
    // 2. Save results exactly once
    this.saveCompleteTest();

    // 3. Reset states & show history
    this.isOralTestActive = false;
    this.isConnected = false;
    this.isConnecting = false;
    this.isSpeaking = false;
    this.isListening = false;
    this.currentQuestion = '';
    this.currentQuestionIndex = 0;
    
    // 4. Return to history view
    this.showOralTestTransition = true; // Show 'Assessment Complete'
    this.completeFullTest();
  }
  completeFullTest(): void {
    this.writtenParagraph = '';
    this.selectedSubject = null;
    this.showOralTestTransition = false;
    this.loadMyHistory();
  }

  skipOralTest(): void {
    if (confirm('Are you sure you want to skip the oral test? This will complete your assessment with only the written portion.')) {
      if (this.vapiService) {
        this.vapiService.speak("Test cancelled. Thank you.");
      }
      this.endOralTest();
    }
  }

  retryOralTest(): void {
    this.submitError = '';
    this.showOralTestTransition = true;
    this.startOralTestPhase();
  }

  isUsingMockVapi(): boolean {
    return this.vapiService && this.vapiService.getInitializationStatus() === 'ready' && 
           !this.vapiService.isRealVapi();
  }

  getAudioStatusClass(): string {
    if (this.vapiService && this.vapiService.isRealVapi()) {
      return 'audio-status-real';
    } else {
      return 'audio-status-mock';
    }
  }

  getAudioStatusIcon(): string {
    if (this.vapiService && this.vapiService.isRealVapi()) {
      return '🔊';
    } else {
      return '🎭';
    }
  }

  getAudioStatusTitle(): string {
    if (this.vapiService && this.vapiService.isRealVapi()) {
      return 'Real Voice Interaction';
    } else {
      return 'Demo Mode';
    }
  }

  getAudioStatusMessage(): string {
    if (this.vapiService && this.vapiService.isRealVapi()) {
      return 'You can hear the AI and speak your responses. Make sure your microphone and speakers are working.';
    } else {
      return 'This is a visual demonstration. No actual audio is produced. Questions advance automatically.';
    }
  }

  // ─── Course Recommendations ─────────────────────────────
  showRecommendations(t: TestTentative): void {
    if (t.id) {
      this.recommendationTestId = t.id;
      this.recommendationLevel = this.extractLevel(t.tutorFeedback, t.score);
      this.recommendationScore = t.score;
      
      // Scroll to recommendations panel
      setTimeout(() => {
        const el = document.querySelector('.recommendations-panel');
        if (el) el.scrollIntoView({ behavior: 'smooth', block: 'start' });
      }, 100);
    }
  }

  closeRecommendations(): void {
    this.recommendationTestId = undefined;
    this.recommendationLevel = undefined;
    this.recommendationScore = undefined;
  }
}

