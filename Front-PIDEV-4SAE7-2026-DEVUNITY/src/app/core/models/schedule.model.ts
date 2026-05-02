import { Room } from './room.model';

export type ScheduleType = 'COURSE' | 'EXAM' | 'MEETING' | 'OTHER';

export interface ClassEntity {
  classId?: number;
  name: string;
  numberStudents: number;
  level: string;
}

export interface Schedule {
  scheduleId?: number;
  title: string;
  type: ScheduleType;
  startTime: string;
  endTime: string;
  userId: number;
  courseId: number;
  room?: Room;
  classEntity?: ClassEntity;
}
