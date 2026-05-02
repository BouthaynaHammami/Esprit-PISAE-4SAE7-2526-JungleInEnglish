// ==========================
// ENUMS / TYPES
// Aligned with Backend Spring Boot
// ==========================
export type ChallengeType =
  | 'MYSTERY_WORD'
  | 'SENTENCE_BUILDER'
  | 'EMOJI_WORD'
  | 'WORD_BATTLE_ROYALE'
  | 'STORY_CHAIN'
  | 'SPEED_TRANSLATION_RACE';

/**
 * ChallengeAttempt Status - Source de vérité: Backend
 * @see Activity_Management_Service/ChallengesCompetitions/Entities/AttemptStatus.java
 */
export type AttemptStatus =
  | 'PENDING'
  | 'IN_PROGRESS'
  | 'COMPLETED'
  | 'EXPIRED';

export type Level = 'A1' | 'A2' | 'B1' | 'B2' | 'C1' | 'C2';

// ==========================
// CHALLENGE - Source de vérité: Backend Entity
// @see Activity_Management_Service/ChallengesCompetitions/Entities/Challenge.java
// ==========================
export interface Challenge {
  id: number;
  title: string;
  description?: string | null;

  type: ChallengeType;
  level?: Level | null;

  // Common fields
  timeLimitSeconds?: number | null;
  maxAttempts?: number | null;
  startDate?: string | null; // ISO 8601: YYYY-MM-DD
  endDate?: string | null;   // ISO 8601: YYYY-MM-DD
  maxParticipants?: number | null;

  // MYSTERY_WORD specific
  hints?: string | null;
  correctAnswer?: string | null;

  // SENTENCE_BUILDER specific
  scrambledSentence?: string | null;

  // EMOJI_WORD specific
  emojiPrompt?: string | null;

  // WORD_BATTLE_ROYALE specific
  wordTheme?: string | null;
  allowedLetters?: string | null;
  forbiddenWords?: string | null; // comma-separated

  // STORY_CHAIN specific
  initialSentence?: string | null;
  maxSentences?: number | null;
  minWordsPerSentence?: number | null;
  maxWordsPerSentence?: number | null;
  allowVoting?: boolean;

  // SPEED_TRANSLATION_RACE specific
  sourceLanguage?: string | null;
  targetLanguage?: string | null;
  sentencesList?: string | null; // Format: "Source|Target;Source2|Target2"
  maxQuestions?: number | null;
  pointsPerCorrectAnswer?: number | null;
  speedBonusEnabled?: boolean;

  // Relations (readonly from API)
  attempts?: ChallengeAttempt[] | null;
}


// ==========================
// CHALLENGE ATTEMPT
// Source de vérité: Backend Entity
// @see Activity_Management_Service/ChallengesCompetitions/Entities/ChallengeAttempt.java
// ==========================
export interface ChallengeAttempt {
  id: number;
  idUser: number;
  challenge?: Challenge | null;
  startTime?: string | null;  // ISO 8601 DateTime
  endTime?: string | null;    // ISO 8601 DateTime
  deadlineTime?: string | null; // ISO 8601 DateTime
  status: AttemptStatus;
  score?: number | null;
  progress?: number | null;
  attemptsUsed?: number | null;
}

// ==========================
// BADGE - Source de vérité: Backend Entity
// @see Activity_Management_Service/ChallengesCompetitions/Entities/Badge.java
// ==========================
export interface Badge {
  id?: number;
  idBadge?: number; // Legacy field (prefer id)
  name: string;
  description?: string | null;
  pointsRequired?: number;
  imageUrl?: string | null;
}

// ==========================
// STUDENT CHALLENGE (Legacy)
// Note: Le backend ne gérait pas directement le concept de StudentChallenge.
// Les données de progression sont dans ChallengeAttempt.
// ==========================
export interface StudentChallenge {
  idStudentChallenge?: number;
  idUser: number;
  progress?: number | null;
  attemptsUsed?: number | null;
  startDate?: string | null;
  endDate?: string | null;
  status?: AttemptStatus;
  challenge?: Challenge | null;
}

// ==========================
// STUDENT BADGE
// ==========================
export interface StudentBadge {
  idStudentBadge: number;
  badge: Badge;
  idUser: number;
  dateAcquisition: string;
}

// ==========================
// STUDENT STATS
// ==========================
export interface StudentStats {
  idUser: number;
  scoreTotal: number;
}

// ==========================
// WORD BATTLE ROYALE
// ==========================
export interface Player {
  username: string;
  idUser: number;
  active: boolean;
  eliminated: boolean;
  eliminationReason?: string;
  score: number;
  hasAnsweredCurrentQuestion: boolean;
}

export interface WordBattleGameSession {
  roomId: string;
  challengeId: number;
  wordTheme?: string;
  allowedLetters?: string;
  forbiddenWords?: string;
  maxParticipants: number;
  timeLimitSeconds: number;
  players: Player[];
  usedWords: string[];
  currentPlayerIndex: number;
  turnEndTime?: string;
  gameStarted: boolean;
  gameEnded: boolean;
}

export interface StoryChainGameSession {
  roomId: string;
  challengeId: number;
  
  initialSentence?: string;
  maxSentences: number;
  minWordsPerSentence: number;
  maxWordsPerSentence: number;
  timeLimitSeconds: number;
  allowVoting: boolean;

  players: Player[];
  currentStory: string[];
  
  currentPlayerIndex: number;
  turnEndTime?: string;
  gameStarted: boolean;
  gameEnded: boolean;
}


// ==========================
// DTO REQUESTS
// ==========================
export interface StartOrGetRequest {
  userId: number;
  challengeId: number;
}

// ==========================
// SESSION STATUS (3-MINUTE SESSION)
// ==========================
export type SessionStatus = 'IN_PROGRESS' | 'COMPLETED' | 'EXPIRED';

/**
 * DTO for StudentChallengeSession
 * Used for 3-minute session management and progress tracking
 * Synchronized with Backend DTO: StudentChallengeSessionDTO
 */
export interface StudentChallengeSessionDTO {
  id: number;
  idUser: number;

  // Session setup
  sessionType: ChallengeType;
  sessionLevel: Level;

  // Timing
  sessionStartTime: string;      // ISO DateTime
  sessionEndTime?: string | null; // ISO DateTime
  sessionDurationSeconds: number; // 180 = 3 min

  // Status
  status: SessionStatus;

  // Challenges
  challengeIds: number[];
  currentChallengeIndex: number;

  // Scores
  totalScore: number;
  correctAnswers: number;
  wrongAnswers: number;
  totalChallengesPlayed: number;

  // Calculated fields (frontend convenience)
  accuracyPercentage?: number;
  timeRemainingSeconds?: number;
}

/**
 * Request DTO for submitting an answer in a session
 */
export interface SubmitAnswerRequest {
  challengeId: number;
  answer: string;
  isCorrect: boolean;
  wrongAttempts?: number;
}

export interface SubmitRequest {
  input: string;
}

// ==========================
// RESPONSE DTO (aligné backend)
// ==========================
export interface ChallengeResponseDTO {
  challengeAttempt: StudentChallenge;
  idUser?: number;
  totalScore: number;
  newBadges: Badge[];
  allBadges: Badge[];
  attemptStatus?: AttemptStatus;
}

// ==========================
// SPEED TRANSLATION RACE
// ==========================
export interface TranslationQuestion {
  sourceText: string;
  expectedTranslation: string;
}

export interface TranslationRaceSession {
  roomId: string;
  challengeId: number;

  sourceLanguage: string;
  targetLanguage: string;
  questions: TranslationQuestion[];
  maxQuestions: number;
  pointsPerCorrectAnswer: number;
  speedBonusEnabled: boolean;
  timeLimitSeconds: number;

  players: Player[];
  currentQuestionIndex: number;
  questionEndTime?: string;
  gameStarted: boolean;
  gameEnded: boolean;

  correctAnswersThisRound: number;
}

export interface BadgeEvaluationResultDTO {
  newlyEarnedBadges: StudentBadge[];
  allOwnedBadges: StudentBadge[];
  totalBadgesOwned: number;
  totalScore: number;
  completedChallenges: number;
}