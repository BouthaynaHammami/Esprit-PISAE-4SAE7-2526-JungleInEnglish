export interface Activity {
    activityId?: number;
    title: string;
    type: ActivityType;
    contentUrl?: string;
    points: number;
    orderIndex: number;
    course?: any;
}

export enum ActivityType {
    GAME = 'GAME',
    QUIZ = 'QUIZ',
    VIDEO = 'VIDEO'
}

export interface ActivityDTO {
    activity: Activity;
    status: 'COMPLETED' | 'UNLOCKED' | 'LOCKED';
    score?: number;
    attempts?: number;
}

export interface ActivityScoreResult {
    score: number;
    passed: boolean;
    pointsEarned: number;
    nextActivityUnlocked: boolean;
    newRewards: any[];
    message: string;
}

export interface SubmitScoreRequest {
    childId: number;
    score: number;
}
