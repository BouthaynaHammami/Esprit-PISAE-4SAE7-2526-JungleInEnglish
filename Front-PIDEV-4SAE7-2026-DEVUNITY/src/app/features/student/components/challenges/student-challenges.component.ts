import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { trigger, state, style, transition, animate, keyframes } from '@angular/animations';

import { Challenge, StudentChallenge, Level, ChallengeType, Badge, StudentBadge, ChallengeResponseDTO, AttemptStatus } from '../../../../core/models/challenges-competitions.model';
import { AuthService } from '../../../../core/services/auth.service';
import { ChallengeService } from '../../../../core/services/activity/challenge.service';
import { StudentChallengeService } from '../../../../core/services/activity/student-challenge.service';
import { StudentChallengeSessionService, StudentChallengeSessionDTO, SubmitAnswerRequest } from '../../../../core/services/activity/student-challenge-session.service';
import { UserService } from '../../../../core/services/activity/user.service';
import { BadgeService } from '../../../../core/services/activity/badge.service';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Component({
  selector: 'app-student-challenges',
  templateUrl: './student-challenges.component.html',
  styleUrls: ['./student-challenges.component.scss'],
  animations: [
    trigger('fadeInAnimation', [
      transition(':enter', [
        style({ opacity: 0 }),
        animate('400ms 200ms ease-out', style({ opacity: 1 }))
      ])
    ]),
    trigger('slideInAnimation', [
      transition(':enter', [
        style({ opacity: 0, transform: 'translateY(20px)' }),
        animate('500ms 150ms cubic-bezier(0.165, 0.84, 0.44, 1)', style({ opacity: 1, transform: 'translateY(0)' }))
      ])
    ])
  ]
})
export class StudentChallengesComponent implements OnInit, OnDestroy {

  userId!: number;
  totalScore: number = 0;
  loading: boolean = false;

  // LEGACY: old flow (individual challenges)
  step: 'TYPE' | 'CHALLENGES' | 'PLAY' | 'SESSION_PLAY' = 'TYPE';

  level: Level = 'A1';
  type!: ChallengeType;
  
  // === NEW: 3-MINUTE SESSION STATE ===
  sessionMode: boolean = false; // true = session 3 min, false = individual  // NEW SESSION VARIABLES
  remainingHearts: number = 3;

  // Session state
  activeSession: StudentChallengeSessionDTO | null = null;
  sessionTimeLeft: number = 180; // 3 minutes = 180 seconds
  sessionTimerInterval: any;
  sessionSelectedType: ChallengeType | null = null;
  sessionSelectedLevel: Level = 'A1';
  sessionCurrentChallenge: Challenge | null = null;
  sessionAnswer: string = '';
  sessionGameStatus: 'PLAYING' | 'WON' | 'LOST' | 'TIMEOUT' | 'COMPLETED' = 'PLAYING';
  sessionScore: number = 0;
  sessionCorrectCount: number = 0;
  sessionWrongCount: number = 0;
  sessionTotalPlayed: number = 0;
  
  // === FEEDBACK SYSTEM ===
  sessionAnswerFeedback: 'CORRECT' | 'INCORRECT' | null = null;
  showAnswerFeedback: boolean = false;
  feedbackMessage: string = '';

  // === ATTEMPTS MANAGEMENT ===
  currentAttempt: number = 1;
  totalAttempts: number = 3;
  isFinished: boolean = false;

  availableChallenges: Challenge[] = [];
  activeStudentChallenge: StudentChallenge | null = null;

  answer = '';
  errorMsg = '';

  types: ChallengeType[] = ['MYSTERY_WORD', 'SENTENCE_BUILDER', 'EMOJI_WORD', 'WORD_BATTLE_ROYALE', 'STORY_CHAIN', 'SPEED_TRANSLATION_RACE'];
  sessionTypes: ChallengeType[] = ['MYSTERY_WORD', 'SENTENCE_BUILDER', 'EMOJI_WORD']; // Session supports only these 3 types
  levels: Level[] = ['A1', 'A2', 'B1', 'B2', 'C1', 'C2'];

  // --- GAME STATE (LEGACY) ---
  remainingAttempts: number = 0;
  hintsList: string[] = [];
  currentHintIndex: number = 0;
  timeLeft: number = 0;
  timerInterval: any;
  gameStatus: 'PLAYING' | 'WON' | 'LOST' | 'TIMEOUT' = 'PLAYING';
  gainedScore: number = 0;
  scrambledWords: string[] = [];
  availableWords: string[] = [];
  selectedWords: string[] = [];
  draggedWordIndex: number | null = null;
  draggedOrigin: 'BANK' | 'BUILDER' | null = null;
  showIncorrectAlert: boolean = false;
  newlyEarnedBadges: Badge[] = [];
  allBadges: Badge[] = [];

  // --- BADGE POPUP ---
  selectedBadge: Badge | null = null;
  showBadgePopup: boolean = false;
  // --- CLEANUP ---
  private destroy$ = new Subject<void>();

  formatType(type: string): string {
    if (!type) return '';
    return type.split('_')
      .map(word => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase())
      .join(' ');
  }

  constructor(
    private authService: AuthService,
    private userService: UserService,
    private challengeService: ChallengeService,
    private studentChallengeService: StudentChallengeService,
    private sessionService: StudentChallengeSessionService,
    private badgeService: BadgeService,
    private router: Router
  ) { }

  ngOnInit(): void {
    const email = this.authService.getUserEmail();

    if (!email) {
      this.errorMsg = "User not logged in";
      return;
    }

    this.userService.getByEmail(email).subscribe({
      next: user => {
        this.userId = user.userId!;
        this.bootstrap();
      },
      error: () => {
        this.errorMsg = "User not found";
      }
    });
  }

  private bootstrap() {
    // Load student stats (score + badges)
    this.badgeService.getStudentStats(this.userId).subscribe({
      next: (stats) => {
        // We will explicitly fetch totalScore below, just load badges here
        // Map StudentBadge[] to Badge[]
        if (stats.allOwnedBadges) {
          this.allBadges = stats.allOwnedBadges.map((sb: any) => ({
            ...sb.badge,
            dateAcquisition: sb.dateAcquisition // Keep for tooltip if needed
          }));
        }
      },
      error: () => console.error('Failed to load stats for challenges')
    });

    // Explicitly load the total score using the dedicated endpoint
    this.refreshTotalScore();

    // Load challenges
    this.studentChallengeService.getByUser(this.userId).subscribe({
      next: (challenges) => {
        this.step = 'TYPE';
      },
      error: () => {
        this.step = 'TYPE';
      }
    });
  }

  // ============ NEW: 3-MINUTE SESSION FLOW ============

  /**
   * Select and validate level
   * Ensures level is always a valid Level string, never contains artifacts
   */
  selectLevel(selectedLevel: Level): void {
    // Extract just the level string (handle any edge cases)
    if (selectedLevel && typeof selectedLevel === 'string') {
      const cleanLevel = selectedLevel.split(':')[0].trim().toUpperCase() as Level;
      // Validate it's a known level
      if (['A1', 'A2', 'B1', 'B2', 'C1', 'C2'].includes(cleanLevel)) {
        this.level = cleanLevel;
        this.sessionSelectedLevel = cleanLevel; // Also update session level
      }
    }
  }

  /**
   * Quick Play: Start session immediately with selected type (skips TYPE selection)
   */
  quickPlaySession(type: ChallengeType) {
    this.sessionSelectedType = type;
    
    // Clean and validate the level
    let cleanLevel: Level = 'A1';
    if (this.level && typeof this.level === 'string') {
      const extracted = this.level.split(':')[0].trim().toUpperCase();
      if (['A1', 'A2', 'B1', 'B2', 'C1', 'C2'].includes(extracted)) {
        cleanLevel = extracted as Level;
      }
    }
    
    this.sessionSelectedLevel = cleanLevel;
    this.sessionMode = true;
    this.startSession();
  }

  /**
   * Start the 3-minute session
   */
  startSession() {
    if (!this.sessionSelectedType) {
      this.errorMsg = "Please select a type first";
      return;
    }

    // Ensure level is valid before making API call
    if (!this.sessionSelectedLevel || !['A1', 'A2', 'B1', 'B2', 'C1', 'C2'].includes(this.sessionSelectedLevel)) {
      this.sessionSelectedLevel = 'A1';
    }

    this.loading = true;
    this.sessionService.startSession(
      this.userId,
      this.sessionSelectedType,
      this.sessionSelectedLevel
    ).pipe(takeUntil(this.destroy$)).subscribe({
      next: (session) => {
        this.loading = false;
        this.activeSession = session;
        this.sessionService.setActiveSession(session);
        this.sessionGameStatus = 'PLAYING';
        this.sessionScore = 0;
        this.sessionCorrectCount = 0;
        this.sessionWrongCount = 0;
        this.sessionTotalPlayed = 0;
        this.sessionAnswer = '';
        this.errorMsg = '';
        
        // Initialize hearts
        this.remainingHearts = 3;
        this.isFinished = false;
        
        // Load first challenge
        this.loadNextSessionChallenge();
        
        // Start 3-minute timer
        this.startSessionTimer();
        
        this.step = 'SESSION_PLAY';
      },
      error: (err) => {
        this.loading = false;
        this.errorMsg = err.error?.message || "Failed to start session";
      }
    });
  }

  /**
   * Load the next challenge from session
   * Initialize hearts based on number of hints for MYSTERY_WORD challenges
   */
  loadNextSessionChallenge() {
    if (!this.activeSession) return;

    const currentIndex = this.activeSession.currentChallengeIndex;
    const challengeIds = this.activeSession.challengeIds;

    // The backend already shuffled the challengeIds list when the session started.
    // We just iterate sequentially using the currentIndex to avoid showing the same challenge twice in a row.
    // We use modulo so that if they play more challenges than exist, it loops back.
    const safeIndex = currentIndex % challengeIds.length;
    const challengeId = challengeIds[safeIndex];
    
    // Load challenge by ID
    this.challengeService.getById(challengeId).pipe(takeUntil(this.destroy$)).subscribe({
      next: (challenge) => {
        this.sessionCurrentChallenge = challenge;
        this.sessionAnswer = '';
        this.errorMsg = '';
        this.showAnswerFeedback = false;
        this.sessionAnswerFeedback = null;
        
        // Initialize hints for MYSTERY_WORD challenges
        if (challenge.type === 'MYSTERY_WORD') {
          this.hintsList = challenge.hints ? challenge.hints.split(';') : [];
          this.currentHintIndex = 0;
          this.remainingHearts = this.hintsList.length;
        } else {
          this.hintsList = [];
          this.currentHintIndex = 0;
          this.remainingHearts = 3;
        }
      },
      error: () => {
        this.errorMsg = "Failed to load challenge";
      }
    });
  }

  /**
   * Start 3-minute session timer
   */
  startSessionTimer() {
    if (this.sessionTimerInterval) {
      clearInterval(this.sessionTimerInterval);
    }

    // Calculate remaining time based on session start time
    if (this.activeSession) {
      const sessionStartMs = new Date(this.activeSession.sessionStartTime || new Date()).getTime();
      const sessionDurationMs = (this.activeSession.sessionDurationSeconds || 180) * 1000;
      const elapsed = Math.floor((new Date().getTime() - sessionStartMs) / 1000);
      this.sessionTimeLeft = Math.max(0, (this.activeSession.sessionDurationSeconds || 180) - elapsed);
    }

    this.sessionTimerInterval = setInterval(() => {
      if (this.sessionTimeLeft > 0) {
        this.sessionTimeLeft--;
      }
      if (this.sessionTimeLeft <= 0) {
        this.stopSessionTimer();
        this.sessionTimeoutHandler();
      }
    }, 1000);
  }

  /**
   * Stop session timer
   */
  stopSessionTimer() {
    if (this.sessionTimerInterval) {
      clearInterval(this.sessionTimerInterval);
    }
  }

  /**
   * Handle session timeout
   */
  sessionTimeoutHandler() {
    if (this.sessionGameStatus !== 'PLAYING') return;

    this.sessionGameStatus = 'TIMEOUT';
    this.completeSessionGame();
  }

  /**
   * Submit answer for current session challenge
   * - Correct answer: Advance to next challenge
   * - Wrong answer: Lose a heart, show next hint if MYSTERY_WORD
   *   - If no hearts left: Challenge lost, advance to next
   *   - Otherwise: Stay on same challenge
   * - Session ends only when time expires or all challenges completed
   */
  submitSessionAnswer() {
    if (!this.activeSession || !this.sessionCurrentChallenge || this.sessionGameStatus !== 'PLAYING' || this.isFinished) {
      return;
    }

    // Validate answer
    const userInput = this.normalize(this.sessionAnswer);
    let isCorrect = false;
    
    if (this.sessionCurrentChallenge.correctAnswer) {
      const correctAnswers = this.sessionCurrentChallenge.correctAnswer.split(';').map(a => this.normalize(a));
      isCorrect = correctAnswers.includes(userInput);
    }

    // Show feedback visually
    this.sessionAnswerFeedback = isCorrect ? 'CORRECT' : 'INCORRECT';
    this.feedbackMessage = isCorrect ? 'Excellent! 🎉' : 'Try again! 💪';
    this.showAnswerFeedback = true;

    // For MYSTERY_WORD challenges, handle hint/heart logic on wrong answer
    const isMysteryWord = this.sessionCurrentChallenge.type === 'MYSTERY_WORD';
    
    if (!isCorrect) {
      // Wrong answer: Lose a heart
      if (isMysteryWord) {
        // Show next hint if available
        if (this.currentHintIndex < this.hintsList.length - 1) {
          this.currentHintIndex++;
        }
      }
      // Lose a heart globally
      this.remainingHearts--;
    }

    const isQuestionFinished = isCorrect || (!isCorrect && this.remainingHearts <= 0);

    if (isQuestionFinished) {
      // Calculate total wrong attempts made for this challenge
      const maxHearts = isMysteryWord ? this.hintsList.length : 3;
      const wrongAttempts = maxHearts - this.remainingHearts;

      // Submit to backend only when question is finished (either correct or out of local attempts)
      const request: SubmitAnswerRequest = {
        challengeId: this.sessionCurrentChallenge.id!,
        answer: this.sessionAnswer,
        isCorrect: isCorrect,
        wrongAttempts: wrongAttempts
      };

      this.sessionService.submitAnswer(
        this.activeSession.id!,
        request
      ).pipe(takeUntil(this.destroy$)).subscribe({
        next: (updatedSession) => {
          this.activeSession = updatedSession;
          this.sessionService.setActiveSession(updatedSession);
          
          // SYNC SCORE FROM BACKEND (source of truth)
          this.sessionScore = updatedSession.totalScore;
          this.sessionCorrectCount = updatedSession.correctAnswers;
          this.sessionWrongCount = updatedSession.wrongAnswers;
          this.sessionTotalPlayed = updatedSession.totalChallengesPlayed;
          
          this.sessionAnswer = '';

          // Hide feedback after animation
          setTimeout(() => {
            this.showAnswerFeedback = false;
            this.sessionAnswerFeedback = null;
          }, 1200);

          // Check if session is complete
          if (updatedSession.status === 'COMPLETED' || updatedSession.status === 'EXPIRED') {
            // Wait for feedback to fade before showing results
            setTimeout(() => {
              this.completeSessionGame();
            }, 800);
          } else {
            // Load next challenge after feedback (Sprint Model: automatically move to next)
            setTimeout(() => {
              this.loadNextSessionChallenge();
            }, 900);
          }
        },
        error: () => {
          this.showAnswerFeedback = false;
          this.errorMsg = "Failed to submit answer";
        }
      });
    } else {
      // Question not finished (wrong answer, but local hearts remain)
      // Do not submit to backend yet. Let the player try again.
      this.sessionAnswer = '';
      setTimeout(() => {
        this.showAnswerFeedback = false;
        this.sessionAnswerFeedback = null;
      }, 1200);
    }
  }

  /**
   * Complete session game and show results
   * Also evaluates and retrieves badges
   */
  completeSessionGame() {
    if (!this.activeSession) return;

    this.stopSessionTimer();

    // Call complete endpoint to finalize session
    this.sessionService.completeSession(this.activeSession.id!).pipe(takeUntil(this.destroy$)).subscribe({
      next: (finalSession) => {
        this.activeSession = finalSession;
        this.sessionService.setActiveSession(finalSession);
        
        // SYNC FINAL SCORES FROM BACKEND
        this.sessionScore = finalSession.totalScore;
        this.sessionCorrectCount = finalSession.correctAnswers;
        this.sessionWrongCount = finalSession.wrongAnswers;
        this.sessionTotalPlayed = finalSession.totalChallengesPlayed;
        
        // SCORE UPDATE DELAYED TO BADGE FETCH (to get accurate global total)
        // this.totalScore = finalSession.totalScore || this.sessionScore;
        
        this.sessionGameStatus = 'COMPLETED';
        
        // FETCH BADGES AFTER SESSION COMPLETION
        // Backend auto-evaluates badges, now we retrieve them for UI display
        this.sessionService.evaluateAndAssignBadges(finalSession.id!, this.userId).pipe(takeUntil(this.destroy$)).subscribe({
          next: (badgeResult) => {
            // Map StudentBadge[] to Badge[] to avoid 'undefined' property issues
            this.newlyEarnedBadges = (badgeResult.newlyEarnedBadges || []).map((sb: any) => sb.badge);
            this.allBadges = (badgeResult.allOwnedBadges || []).map((sb: any) => ({
              ...sb.badge,
              dateAcquisition: sb.dateAcquisition
            }));
            
            // UPDATE TOTAL SCORE IN SIDEBAR WITH TRUE GLOBAL SCORE
            if (badgeResult.totalScore !== undefined) {
              this.totalScore = badgeResult.totalScore;
            }
            this.refreshTotalScore(); // Force sync with absolute truth
            
            if (this.newlyEarnedBadges.length > 0) {
              console.log(`🎉 User earned ${this.newlyEarnedBadges.length} new badges!`);
            }
            // Do NOT auto close - wait for user to click continue
          },
          error: (err) => {
            console.warn("Failed to fetch badge results, continuing...", err);
            // Do NOT auto close - wait for user to click continue
          }
        });
      },
      error: () => {
        // Even if complete fails, redirect back
        this.sessionGameStatus = 'COMPLETED';
        this.goBackSession();
      }
    });
  }


  /**
   * Go back from session
   */
  goBackSession() {
    if (this.step === 'SESSION_PLAY') {
      this.stopSessionTimer();
      this.sessionMode = false;
      this.step = 'TYPE';
      this.activeSession = null;
      this.sessionCurrentChallenge = null;
    }
    this.errorMsg = '';
  }

  /**
   * Increment attempt counter (tracks hearts lost)
   * Simplified: just increments for tracking
   */
  incrementAttempt(): void {
    this.currentAttempt++;
  }

  /**
   * Calculate SVG stroke-dashoffset for circular timer animation
   * Circle circumference = 2πr = 2π(32) ≈ 201 pixels
   */
  calculateTimerDashoffset(): number {
    const totalDuration = 180; // 3 minutes in seconds
    const circumference = 201; // 2 * π * 32
    const progress = this.sessionTimeLeft / totalDuration;
    return circumference - (progress * circumference);
  }

  /**
   * Normalize text for comparison: trim, lowercase, remove extra spaces
   */
  normalize(s: string): string {
    if (!s) return "";
    return s.trim().replace(/\s+/g, ' ').toLowerCase();
  }

  openBadgeDetails(badge: Badge) {
    this.selectedBadge = badge;
    this.showBadgePopup = true;
  }

  closeBadgePopup() {
    this.showBadgePopup = false;
    this.selectedBadge = null;
  }

  getBadgeTier(badge: Badge): string {
    const pts = badge.pointsRequired ?? 0;
    if (pts >= 80) {
      return 'Légendaire';
    }
    if (pts >= 50) {
      return 'Élite';
    }
    if (pts >= 30) {
      return 'Avancé';
    }
    return 'Découverte';
  }

  isBadgeNew(badge: Badge): boolean {
    return this.newlyEarnedBadges.some(nb => nb.idBadge === badge.idBadge);
  }

  getBadgeDelay(index: number): string {
    return `${index * 0.06}s`;
  }

  showTypes() {
    this.step = 'TYPE';
  }

  goBack(): void {
    if (this.step === 'CHALLENGES') {
      this.step = 'TYPE';
    } else if (this.step === 'PLAY') {
      this.step = 'CHALLENGES';
      this.activeStudentChallenge = null;
      this.answer = '';
      this.stopTimer();
    }
    this.errorMsg = '';
  }

  backToProgress(): void {
    this.step = 'TYPE';
    this.activeStudentChallenge = null;
    this.answer = '';
    this.stopTimer();
  }

  playAnotherChallenge(): void {
    this.step = 'TYPE';
    this.activeStudentChallenge = null;
    this.answer = '';
    this.stopTimer();
  }

  chooseType(type: ChallengeType) {
    this.type = type;
    this.challengeService.getAvailable({ type: this.type, level: this.level }).pipe(
      takeUntil(this.destroy$)
    ).subscribe({
      next: list => {
        // Shuffle the challenges list for a random display
        for (let i = list.length - 1; i > 0; i--) {
          const j = Math.floor(Math.random() * (i + 1));
          [list[i], list[j]] = [list[j], list[i]];
        }
        this.availableChallenges = list;
        this.step = 'CHALLENGES';
      },
      error: () => {
        this.errorMsg = "Could not load missions";
      }
    });
  }

  startChallenge(challenge: Challenge) {
    if (challenge.type === 'WORD_BATTLE_ROYALE') {
      console.log('Navigating to Word Battle Arena for challenge ID:', challenge.id);
      this.router.navigate(['/student/challenges/arena', challenge.id]);
      return;
    }

    if (challenge.type === 'STORY_CHAIN') {
      console.log('Navigating to Story Chain Arena for challenge ID:', challenge.id);
      this.router.navigate(['/student/challenges/story-chain', challenge.id]);
      return;
    }

    if (challenge.type === 'SPEED_TRANSLATION_RACE') {
      console.log('Navigating to Speed Translation Arena for challenge ID:', challenge.id);
      this.router.navigate(['/student/challenges/translation-race', challenge.id]);
      return;
    }

    this.studentChallengeService.startOrGet(
      this.userId,
      challenge.id
    ).pipe(
      takeUntil(this.destroy$)
    ).subscribe({
      next: res => {
        this.activeStudentChallenge = res.challengeAttempt;
        this.totalScore = res.totalScore;
        this.newlyEarnedBadges = res.newBadges;
        this.allBadges = res.allBadges;
        this.step = 'PLAY';
        this.initGameMode(this.activeStudentChallenge);
      },
      error: () => {
        this.errorMsg = "Failed to start mission";
      }
    });
  }

  // --- GAME LOGIC ---

  initGameMode(sc: StudentChallenge) {
    const ch = sc.challenge!;
    this.gameStatus = 'PLAYING';
    this.hintsList = ch.hints ? ch.hints.split(';') : [];
    this.currentHintIndex = 0;
    this.answer = '';
    this.gainedScore = 0;
    this.showIncorrectAlert = false;

    if (ch.type === 'SENTENCE_BUILDER') {
      let baseScore = 1;
      if (ch.level === 'B1' || ch.level === 'B2') baseScore = 2;
      if (ch.level === 'C1' || ch.level === 'C2') baseScore = 3;
      this.remainingAttempts = baseScore;
      this.scrambledWords = ch.scrambledSentence ? ch.scrambledSentence.split(/\s*\/\s*/).filter(w => w.length > 0) : [];
      this.availableWords = [...this.scrambledWords];
      this.selectedWords = [];
    } else if (ch.type === 'MYSTERY_WORD') {
      // For MYSTERY_WORD: hearts = number of hints available + 1
      this.remainingAttempts = this.hintsList.length + 1 || 1;
      this.scrambledWords = [];
      this.availableWords = [];
      this.selectedWords = [];
    } else {
      // Default for other types
      this.remainingAttempts = ch.maxAttempts || 5;
      this.scrambledWords = [];
      this.availableWords = [];
      this.selectedWords = [];
    }

    // Check if attempt is already completed (status = COMPLETED or EXPIRED)
    if (sc.status === 'COMPLETED' || sc.status === 'EXPIRED') {
      this.gameStatus = sc.progress && sc.progress > 0 ? 'WON' : 'LOST';
      this.gainedScore = sc.progress || 0;
      this.stopTimer();
      return;
    }

    if (sc.attemptsUsed && ch.type !== 'SENTENCE_BUILDER') {
      this.currentHintIndex = sc.attemptsUsed;
      this.remainingAttempts = Math.max(0, this.remainingAttempts - sc.attemptsUsed);
    }

    if (ch.timeLimitSeconds && ch.timeLimitSeconds > 0) {
      const startMs = sc.startDate ? new Date(sc.startDate).getTime() : new Date().getTime();
      const elapsed = Math.floor((new Date().getTime() - startMs) / 1000);
      this.timeLeft = Math.max(0, ch.timeLimitSeconds - elapsed);
      this.startTimer();
    } else {
      this.timeLeft = 0;
    }
  }

  startTimer() {
    this.stopTimer();
    this.timerInterval = setInterval(() => {
      if (this.timeLeft > 0) {
        this.timeLeft--;
      }
      if (this.timeLeft <= 0) {
        this.stopTimer();
        this.handleTimeout();
      }
    }, 1000);
  }

  stopTimer() {
    if (this.timerInterval) {
      clearInterval(this.timerInterval);
    }
  }

  handleTimeout() {
    this.gameStatus = 'TIMEOUT';
    this.remainingAttempts = 0;
    this.submitAnswer(true);
  }

  /**
   * Calculate SVG stroke-dashoffset for circular timer animation
   * Circle circumference = 2πr = 2π(35) ≈ 220 pixels
   */
  getTimerDashoffset(): number {
    const totalDuration = 300; // Default timer duration in seconds
    const circumference = 220; // 2 * π * 35
    const progress = Math.max(0, this.timeLeft / totalDuration);
    return circumference - (progress * circumference);
  }

  retryChallenge() {
    if (!this.activeStudentChallenge || !this.activeStudentChallenge.idStudentChallenge) return;
    this.studentChallengeService.submit(
      this.activeStudentChallenge.idStudentChallenge,
      "___retry___"
    ).pipe(
      takeUntil(this.destroy$)
    ).subscribe({
      next: (res) => {
        // res is ChallengeResponseDTO with { challengeAttempt, totalScore, newBadges, allBadges }
        this.activeStudentChallenge = res.challengeAttempt;
        this.newlyEarnedBadges = res.newBadges;
        this.allBadges = res.allBadges;
        this.totalScore = res.totalScore;
        this.initGameMode(res.challengeAttempt);
      },
      error: () => {
        this.errorMsg = "Failed to restart mission";
      }
    });
  }

  // --- DRAG AND DROP ---
  onDragStart(event: DragEvent, index: number, origin: 'BANK' | 'BUILDER') {
    this.draggedWordIndex = index;
    this.draggedOrigin = origin;
    if (event.dataTransfer) {
      event.dataTransfer.effectAllowed = 'move';
      event.dataTransfer.setData('text/plain', index.toString());
    }
  }

  allowDrop(event: DragEvent) {
    event.preventDefault();
  }

  onDrop(event: DragEvent, targetOrigin: 'BANK' | 'BUILDER') {
    event.preventDefault();
    if (this.draggedWordIndex === null || this.draggedOrigin === null) return;
    this.moveWord(this.draggedWordIndex, this.draggedOrigin, targetOrigin);
    this.draggedWordIndex = null;
    this.draggedOrigin = null;
  }

  moveWord(index: number, from: 'BANK' | 'BUILDER', to: 'BANK' | 'BUILDER') {
    if (from === to) return;
    if (from === 'BANK' && to === 'BUILDER') {
      const word = this.availableWords.splice(index, 1)[0];
      this.selectedWords.push(word);
    } else if (from === 'BUILDER' && to === 'BANK') {
      const word = this.selectedWords.splice(index, 1)[0];
      this.availableWords.push(word);
    }
  }

  toggleWord(index: number, origin: 'BANK' | 'BUILDER') {
    if (origin === 'BANK') {
      this.moveWord(index, 'BANK', 'BUILDER');
    } else {
      this.moveWord(index, 'BUILDER', 'BANK');
    }
  }

  resetSentence() {
    this.availableWords.push(...this.selectedWords);
    this.selectedWords = [];
  }

  checkSentenceAnswer() {
    this.answer = this.selectedWords.join(' ');
    this.submitAnswer();
  }

  submitAnswer(isTimeout = false) {
    if (!this.activeStudentChallenge || (this.gameStatus !== 'PLAYING' && !isTimeout)) return;

    if (!isTimeout) {
      const ch = this.activeStudentChallenge.challenge!;
      const userInput = this.normalize(this.answer);
      let isCorrect = false;
      if (ch.correctAnswer) {
        const correctAnswers = ch.correctAnswer.split(';').map(a => this.normalize(a));
        isCorrect = correctAnswers.includes(userInput);
      }

      if (isCorrect) {
        this.showIncorrectAlert = false;
        this.stopTimer();
      } else {
        this.showIncorrectAlert = true;
        setTimeout(() => this.showIncorrectAlert = false, 3000);
        
        if (ch.type !== 'SENTENCE_BUILDER') {
          // Lose a heart on wrong answer
          this.remainingAttempts = Math.max(0, this.remainingAttempts - 1);
          
          if (ch.type === 'MYSTERY_WORD') {
            // For MYSTERY_WORD: Show next hint if available
            if (this.currentHintIndex < this.hintsList.length) {
              this.currentHintIndex++;
            }
            if (this.currentHintIndex >= this.hintsList.length - 1 && this.remainingAttempts === 0) {
              this.stopTimer();
            }
          } else if (ch.type === 'EMOJI_WORD') {
            // For EMOJI_WORD: Stop timer if no hearts left
            if (this.remainingAttempts === 0) {
              this.stopTimer();
            }
          }
        }
      }
    }

    this.studentChallengeService.submit(
      this.activeStudentChallenge!.idStudentChallenge!,
      isTimeout ? "___timeout___" : this.answer
    ).pipe(
      takeUntil(this.destroy$)
    ).subscribe({
      next: res => {
        // res is ChallengeResponseDTO with { challengeAttempt, totalScore, newBadges, allBadges }
        this.activeStudentChallenge = res.challengeAttempt;
        this.answer = '';
        if (res.totalScore !== undefined) {
          this.totalScore = res.totalScore;
        }
        this.refreshTotalScore(); // Force sync with absolute truth

        this.newlyEarnedBadges = res.newBadges || [];
        this.allBadges = res.allBadges || [];

        // Check if attempt is now completed
        if (this.activeStudentChallenge!.status === 'COMPLETED' || this.activeStudentChallenge!.status === 'EXPIRED') {
          this.stopTimer();
          if (this.activeStudentChallenge!.progress && this.activeStudentChallenge!.progress > 0) {
            this.gameStatus = 'WON';
            this.gainedScore = this.activeStudentChallenge!.progress;
          } else {
            this.gameStatus = isTimeout ? 'TIMEOUT' : 'LOST';
            this.remainingAttempts = 0;
          }
        }
      },
      error: () => {
        this.errorMsg = "Submission error";
      }
    });
  }

  ngOnDestroy(): void {
    this.stopTimer();
    this.stopSessionTimer();
    this.destroy$.next();
    this.destroy$.complete();
  }

  private refreshTotalScore() {
    this.sessionService.getUserTotalScore(this.userId).pipe(takeUntil(this.destroy$)).subscribe({
      next: (score) => {
        if (score !== undefined && score !== null) {
          this.totalScore = score;
        }
      },
      error: () => console.error('Failed to refresh absolute total score')
    });
  }

}