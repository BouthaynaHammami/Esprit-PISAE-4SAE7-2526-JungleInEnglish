import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { StoryChainService } from '../../../../../core/services/activity/story-chain.service';
import { AuthService } from '../../../../../core/services/auth.service';
import { UserService } from '../../../../../core/services/activity/user.service';
import { StoryChainGameSession, Player } from '../../../../../core/models/challenges-competitions.model';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-story-chain-arena',
  templateUrl: './story-chain-arena.component.html',
  styleUrls: ['./story-chain-arena.component.css']
})
export class StoryChainArenaComponent implements OnInit, OnDestroy {
  challengeId!: number;
  userId!: number;
  username!: string;
  roomId!: string;

  session: StoryChainGameSession | null = null;
  private sessionSub?: Subscription;

  sentenceInput: string = '';
  errorMsg: string = '';
  
  timeLeft: number = 0;
  private timerInterval: any;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private storyChainService: StoryChainService,
    private authService: AuthService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (!idParam) {
      this.router.navigate(['/student/challenges']);
      return;
    }
    
    this.challengeId = +idParam;
    this.roomId = `story_${this.challengeId}`;

    const email = this.authService.getUserEmail();
    if (!email) {
      this.router.navigate(['/auth/login']);
      return;
    }

    this.userService.getByEmail(email).subscribe({
      next: (user) => {
        this.userId = user.userId!;
        this.username = user.firstName && user.lastName ? `${user.firstName} ${user.lastName}` : email.split('@')[0];
        this.connectToArena();
      },
      error: () => {
        this.errorMsg = "Unable to load profile.";
      }
    });
  }

  connectToArena(): void {
    this.sessionSub = this.storyChainService.connect(this.roomId).subscribe({
      next: (session) => {
        this.session = session;
        this.handleSessionUpdate();
      },
      error: () => {
        this.errorMsg = "WebSocket connection error.";
      }
    });

    setTimeout(() => {
      this.storyChainService.joinRoom(this.roomId, this.username, this.userId, this.challengeId);
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
    if (!this.session?.turnEndTime) return;

    const updateTimer = () => {
      const now = new Date().getTime();
      const end = new Date(this.session!.turnEndTime!).getTime();
      this.timeLeft = Math.max(0, Math.floor((end - now) / 1000));
      if (this.timeLeft <= 0) this.stopLocalTimer();
    };

    updateTimer();
    this.timerInterval = setInterval(updateTimer, 1000);
  }

  stopLocalTimer(): void {
    if (this.timerInterval) clearInterval(this.timerInterval);
  }

  isMyTurn(): boolean {
    if (!this.session || !this.session.gameStarted || this.session.gameEnded) return false;
    const currentPlayer = this.session.players[this.session.currentPlayerIndex];
    return currentPlayer?.username === this.username;
  }

  get wordCount(): number {
    return this.sentenceInput.trim() ? this.sentenceInput.trim().split(/\s+/).length : 0;
  }

  isValidLength(): boolean {
    if (!this.session) return false;
    const count = this.wordCount;
    return count >= this.session.minWordsPerSentence && count <= this.session.maxWordsPerSentence;
  }

  submitSentence(): void {
    if (!this.sentenceInput.trim() || !this.isMyTurn()) return;
    if (!this.isValidLength()) {
        this.errorMsg = `Your sentence must contain between ${this.session?.minWordsPerSentence} and ${this.session?.maxWordsPerSentence} words.`;
        setTimeout(() => this.errorMsg = '', 3000);
        return;
    }
    
    this.storyChainService.submitSentence(this.roomId, this.username, this.sentenceInput.trim());
    this.sentenceInput = '';
    this.errorMsg = '';
  }

  startGame(): void {
    this.storyChainService.startGame(this.roomId);
  }

  leaveArena(): void {
    this.router.navigate(['/student/challenges']);
  }

  ngOnDestroy(): void {
    this.stopLocalTimer();
    this.sessionSub?.unsubscribe();
    this.storyChainService.disconnect();
  }

  get activePlayers(): Player[] {
    return this.session?.players.filter(p => !p.eliminated) || [];
  }

  getActivePlayerName(): string {
    if (!this.session || this.session.currentPlayerIndex == null) return '';
    const player = this.session.players[this.session.currentPlayerIndex];
    return player ? player.username : '';
  }
}
