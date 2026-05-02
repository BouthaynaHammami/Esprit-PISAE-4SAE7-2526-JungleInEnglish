export interface QuizAttempt {
  id?: number;
  userId?: number;
  quizId?: number;
  answers?: string[];
  score?: number;
  isCompleted?: boolean;
  attemptDate?: string;
}