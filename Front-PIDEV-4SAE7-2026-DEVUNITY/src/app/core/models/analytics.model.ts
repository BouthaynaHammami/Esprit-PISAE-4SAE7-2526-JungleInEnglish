export interface AnalyticsData {
  overviewStats: OverviewStats;
  topPerformers: ChildPerformance[];
  activityStats: ActivityStats[];
  progressTrends: ProgressTrend[];
  levelDistribution: { [key: string]: number };
}

export interface OverviewStats {
  totalChildren: number;
  totalActivitiesCompleted: number;
  averageScore: number;
  totalXpEarned: number;
  activeLearners: number;
}

export interface ChildPerformance {
  childId: number;
  childName: string;
  currentLevel: number;
  totalXp: number;
  averageScore: number;
  activitiesCompleted: number;
  badgesEarned: number;
}

export interface ActivityStats {
  activityId: number;
  activityTitle: string;
  activityType: string;
  completionCount: number;
  averageScore: number;
  completionRate: number;
}

export interface ProgressTrend {
  date: string;
  activitiesCompleted: number;
  averageScore: number;
  xpEarned: number;
}
