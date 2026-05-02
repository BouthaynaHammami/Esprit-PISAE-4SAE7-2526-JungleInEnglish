export interface Progress {
  progressId?: number;
  completionRate: number;
  lastAccess: Date;
  child?: any;
  course?: any;
}
