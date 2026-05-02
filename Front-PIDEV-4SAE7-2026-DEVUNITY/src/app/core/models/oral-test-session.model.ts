export interface OralTestSession {
  id: string;
  subjectId: number;
  userId: number;
  startTime: Date;
  endTime?: Date;
  questions: string[];
  responses: OralResponse[];
  status: 'IN_PROGRESS' | 'COMPLETED' | 'CANCELLED';
}

export interface OralResponse {
  questionIndex: number;
  question: string;
  response: string;
  timestamp: Date;
}