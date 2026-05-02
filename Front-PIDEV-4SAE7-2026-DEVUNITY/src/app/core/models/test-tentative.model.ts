// src/app/core/models/test-tentative.model.ts
import { Subject } from './subject.model';

export type TestStatus = 'PENDING' | 'CORRECTED' | 'PASSED' | 'FAILED';

export interface TestTentative {
  id?: number;
  subject?: Subject;
  score?: number;
  userId?: number;
  paragraph?: string;
  status?: TestStatus;
  tutorFeedback?: string;
}
