import { Question } from './question.model';

export interface Quiz {
  quizId?: number;
  title?: string;
  questions?: Question[];
}