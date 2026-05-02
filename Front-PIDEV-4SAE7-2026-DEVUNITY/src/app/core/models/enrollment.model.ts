import { Course } from './course.model';

export interface Enrollment {
  enrollmentId?: number;
  userId?: number;
  course?: Course;
  enrollmentDate?: string;
  duration?: number;
  progress?: number;
  isCompleted?: boolean;
  score?: number;
}