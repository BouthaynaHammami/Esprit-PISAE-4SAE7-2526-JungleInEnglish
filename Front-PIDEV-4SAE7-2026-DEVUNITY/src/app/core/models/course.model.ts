import { Lesson } from './lesson.model';
import { Quiz } from './quiz.model';
import { Enrollment } from './enrollment.model';

export interface Course {
  courseId?: number;
  title: string;
  description: string;
  level: 'A1' | 'A2' | 'B1' | 'B2' | 'C1' | 'C2';
  type: 'Business_English' | 'General_English';
  price: number;
  lessonsNumber?: number;
  isHidden: boolean;
  imageUrl?: string;
  lessons?: Lesson[];
  quiz?: Quiz;
  enrollments?: Enrollment[];
}