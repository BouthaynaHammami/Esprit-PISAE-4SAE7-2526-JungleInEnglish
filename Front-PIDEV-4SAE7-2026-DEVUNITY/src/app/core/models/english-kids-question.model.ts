export interface QuizOption {
    optionId?: number;
    optionText: string;
    imageUrl?: string;
    orderIndex: number;
}

export interface Question {
    questionId?: number;
    questionText: string;
    imageUrl?: string;
    correctAnswerIndex: number;
    orderIndex: number;
    options: QuizOption[];
}

export interface ActivityWithQuestions {
    activityId?: number;
    title: string;
    type: string;
    contentUrl?: string;
    points: number;
    orderIndex: number;
    questions: Question[];
}
