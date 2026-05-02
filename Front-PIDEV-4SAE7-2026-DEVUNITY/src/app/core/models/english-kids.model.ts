// English Kids Models

export interface Child {
  childId?: number;
  name: string;
  age: number;
  avatar?: string;
  parentId: number;
  xp: number;
  level: number;
  createdAt?: Date;
  updatedAt?: Date;
}

export interface Activity {
  activityId: number;
  title: string;
  description: string;
  category: ActivityCategory;
  difficulty: DifficultyLevel;
  xpReward: number;
  imageUrl?: string;
  iconClass?: string;
  estimatedTime: number; // in minutes
  isCompleted?: boolean;
  completionRate?: number;
}

export enum ActivityCategory {
  VOCABULARY = 'VOCABULARY',
  GRAMMAR = 'GRAMMAR',
  LISTENING = 'LISTENING',
  READING = 'READING',
  SPEAKING = 'SPEAKING',
  WRITING = 'WRITING'
}

export enum DifficultyLevel {
  BEGINNER = 'BEGINNER',
  INTERMEDIATE = 'INTERMEDIATE',
  ADVANCED = 'ADVANCED'
}

export interface Question {
  questionId: number;
  activityId: number;
  questionText: string;
  questionType: QuestionType;
  options: string[];
  correctAnswer: string;
  imageUrl?: string;
  audioUrl?: string;
  explanation?: string;
  points: number;
}

export enum QuestionType {
  MULTIPLE_CHOICE = 'MULTIPLE_CHOICE',
  TRUE_FALSE = 'TRUE_FALSE',
  FILL_BLANK = 'FILL_BLANK',
  MATCHING = 'MATCHING'
}

export interface QuizAttempt {
  attemptId?: number;
  childId: number;
  activityId: number;
  answers: Answer[];
  score: number;
  totalQuestions: number;
  xpEarned: number;
  completedAt?: Date;
  timeSpent: number; // in seconds
}

export interface Answer {
  questionId: number;
  selectedAnswer: string;
  isCorrect: boolean;
  timeSpent: number;
}

export interface Badge {
  badgeId: number;
  name: string;
  description: string;
  iconUrl: string;
  badgeType: BadgeType;
  requirement: string;
  xpRequired?: number;
}

export enum BadgeType {
  STREAK = 'STREAK',
  PERFECT_SCORE = 'PERFECT_SCORE',
  LEVEL_UP = 'LEVEL_UP',
  ACTIVITY_MASTER = 'ACTIVITY_MASTER',
  SPEED_DEMON = 'SPEED_DEMON',
  PERSISTENT = 'PERSISTENT'
}

export interface ChildBadge {
  childBadgeId?: number;
  childId: number;
  badgeId: number;
  badge?: Badge;
  earnedAt: Date;
}

export interface Progress {
  childId: number;
  totalXp: number;
  currentLevel: number;
  xpToNextLevel: number;
  totalActivitiesCompleted: number;
  accuracyPercentage: number;
  currentStreak: number;
  longestStreak: number;
  badges: ChildBadge[];
  recentActivities: ActivityHistory[];
}

export interface ActivityHistory {
  activityId: number;
  activityTitle: string;
  completedAt: Date;
  score: number;
  xpEarned: number;
}

export interface LeaderboardEntry {
  rank: number;
  childId: number;
  childName: string;
  avatar?: string;
  totalXp: number;
  level: number;
  badgeCount: number;
}

export interface QuizSession {
  activity: Activity;
  questions: Question[];
  currentQuestionIndex: number;
  answers: Answer[];
  startTime: Date;
  timeRemaining: number;
}
