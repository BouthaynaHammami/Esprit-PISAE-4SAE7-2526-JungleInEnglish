import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { SpeedTranslationService } from '../../../../../core/services/activity/speed-translation.service';
import { AuthService } from '../../../../../core/services/auth.service';
import { UserService } from '../../../../../core/services/activity/user.service';
import { TranslationRaceSession, Player } from '../../../../../core/models/challenges-competitions.model';

@Component({
  selector: 'app-speed-translation-arena',
  templateUrl: './speed-translation-arena.component.html',
  styleUrls: ['./speed-translation-arena.component.css']
})
export class SpeedTranslationArenaComponent implements OnInit, OnDestroy {
  challengeId!: number;
  userId!: number;
  username!: string;
  roomId!: string;

  session: TranslationRaceSession | null = null;
  private sessionSub?: Subscription;

  translationInput: string = '';
  errorMsg: string = '';
  
  timeLeft: number = 0;
  private timerInterval: any;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private speedTranslationService: SpeedTranslationService,
    private authService: AuthService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.challengeId = +this.route.snapshot.paramMap.get('id')!;
    this.roomId = `translation_${this.challengeId}`;

    const email = this.authService.getUserEmail();
    if (!email) {
      this.router.navigate(['/auth/login']);
      return;
    }

    this.userService.getByEmail(email).subscribe({
      next: (user) => {
        this.userId = user.userId!;
        this.username = user.firstName && user.lastName ? `${user.firstName} ${user.lastName}` : email.split('@')[0];
        this.connectToRace();
      },
      error: () => {
        this.errorMsg = "Unable to load user profile.";
      }
    });
  }

  connectToRace(): void {
    this.sessionSub = this.speedTranslationService.connect(this.roomId).subscribe({
      next: (session) => {
        const prevIndex = this.session?.currentQuestionIndex;
        this.session = session;
        
        // Reset input on new question
        if (prevIndex !== undefined && session.currentQuestionIndex !== prevIndex) {
          this.translationInput = '';
          this.errorMsg = '';
        }

        this.handleSessionUpdate();
      },
      error: (err) => {
        console.error('Socket error:', err);
        this.errorMsg = "Error connecting to the arena.";
      }
    });

    setTimeout(() => {
      this.speedTranslationService.joinRoom(this.roomId, this.username, this.userId, this.challengeId);
    }, 1000);
  }

  handleSessionUpdate(): void {
    if (!this.session) return;

    if (this.session.gameStarted && !this.session.gameEnded) {
      this.startLocalTimer();
    } else {
      this.stopLocalTimer();
    }
  }

  startLocalTimer(): void {
    this.stopLocalTimer();
    if (!this.session?.questionEndTime) return;

    const updateTimer = () => {
      const now = new Date().getTime();
      const end = new Date(this.session!.questionEndTime!).getTime();
      this.timeLeft = Math.max(0, Math.floor((end - now) / 1000));
      
      if (this.timeLeft <= 0) {
        this.stopLocalTimer();
      }
    };

    updateTimer();
    this.timerInterval = setInterval(updateTimer, 1000);
  }

  stopLocalTimer(): void {
    if (this.timerInterval) {
      clearInterval(this.timerInterval);
    }
  }

  submitTranslation(): void {
    if (!this.translationInput.trim() || !this.session?.gameStarted || this.session.gameEnded) return;
    
    // Check if already answered this round
    const me = this.session.players.find(p => p.idUser === this.userId);
    if (me?.hasAnsweredCurrentQuestion) return;

    this.speedTranslationService.submitTranslation(this.roomId, this.username, this.translationInput.trim());
    this.translationInput = '';
  }

  startGame(): void {
    this.speedTranslationService.startGame(this.roomId);
  }

  leaveArena(): void {
    this.router.navigate(['/student/challenges']);
  }

  ngOnDestroy(): void {
    this.stopLocalTimer();
    this.sessionSub?.unsubscribe();
    this.speedTranslationService.disconnect();
  }

  get sortedPlayers(): Player[] {
    return [...(this.session?.players || [])].sort((a, b) => b.score - a.score);
  }

  get currentQuestion() {
    if (!this.session || this.session.currentQuestionIndex < 0 || this.session.currentQuestionIndex >= this.session.questions.length) {
      return null;
    }
    return this.session.questions[this.session.currentQuestionIndex];
  }

  get myStatus(): Player | undefined {
    return this.session?.players.find(p => p.idUser === this.userId);
  }

  get activePlayers(): Player[] {
    return this.session?.players || [];
  }

  getActivePlayerName(): string {
    return '';
  }

  get winner(): Player | null {
    if (!this.session?.gameEnded || !this.session.players.length) return null;
    return [...this.session.players].sort((a, b) => b.score - a.score)[0];
  }
}
