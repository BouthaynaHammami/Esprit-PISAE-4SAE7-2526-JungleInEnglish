export interface Course {
  id: string;
  title: string;
  level: 'A1' | 'A2' | 'B1' | 'B2' | 'C1' | 'C2';
  description: string;
  duration: string;
  lessons: number;
  enrolledStudents: number;
  progress?: number;
  color: string;
}
