import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { WordBattleService } from '../../../../../core/services/activity/word-battle.service';
import { AuthService } from '../../../../../core/services/auth.service';
import { UserService } from '../../../../../core/services/activity/user.service';
import { WordBattleGameSession, Player } from '../../../../../core/models/challenges-competitions.model';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-word-battle-arena',
  templateUrl: './word-battle-arena.component.html',
  styleUrls: ['./word-battle-arena.component.css']
})
export class WordBattleArenaComponent implements OnInit, OnDestroy {
  challengeId!: number;
  userId!: number;
  username!: string;
  roomId!: string;

  session: WordBattleGameSession | null = null;
  private sessionSub?: Subscription;

  wordInput: string = '';
  errorMsg: string = '';
  
  timeLeft: number = 0;
  private timerInterval: any;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private wordBattleService: WordBattleService,
    private authService: AuthService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    console.log('Word Battle Arena - Initializing...');
    this.challengeId = +this.route.snapshot.paramMap.get('id')!;
    console.log('Challenge ID extracted:', this.challengeId);
    this.roomId = `challenge_${this.challengeId}`;

    const email = this.authService.getUserEmail();
    if (!email) {
      this.router.navigate(['/auth/login']);
      return;
    }

    this.userService.getByEmail(email).subscribe({
      next: (user) => {
        this.userId = user.userId!;
        this.username = user.firstName && user.lastName ? `${user.firstName} ${user.lastName}` : email.split('@')[0];
        this.connectToBattle();
      },
      error: () => {
        this.errorMsg = "Unable to load user profile.";
      }
    });
  }

  connectToBattle(): void {
    this.sessionSub = this.wordBattleService.connect(this.roomId).subscribe({
      next: (session) => {
        this.session = session;
        this.handleSessionUpdate();
      },
      error: (err) => {
        console.error('Socket error:', err);
        this.errorMsg = "Error connecting to the arena.";
      }
    });

    // Join room once connected (delayed slightly to ensure socket is ready)
    setTimeout(() => {
      this.wordBattleService.joinRoom(this.roomId, this.username, this.userId, this.challengeId);
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

  isMyTurn(): boolean {
    if (!this.session || !this.session.gameStarted || this.session.gameEnded) return false;
    const currentPlayer = this.session.players[this.session.currentPlayerIndex];
    return currentPlayer?.username === this.username;
  }

  submitWord(): void {
    if (!this.wordInput.trim() || !this.isMyTurn()) return;
    
    this.wordBattleService.submitWord(this.roomId, this.username, this.wordInput.trim());
    this.wordInput = '';
  }

  startGame(): void {
    this.wordBattleService.startGame(this.roomId);
  }

  leaveArena(): void {
    this.router.navigate(['/student/challenges']);
  }

  ngOnDestroy(): void {
    this.stopLocalTimer();
    this.sessionSub?.unsubscribe();
    this.wordBattleService.disconnect();
  }

  get activePlayers(): Player[] {
    return this.session?.players.filter(p => !p.eliminated) || [];
  }

  get eliminatedPlayers(): Player[] {
    return this.session?.players.filter(p => p.eliminated) || [];
  }

  getActivePlayerName(): string {
    if (!this.session || this.session.currentPlayerIndex == null) return '';
    const player = this.session.players[this.session.currentPlayerIndex];
    return player ? player.username : '';
  }
}
