export interface CourseRecommendation {
  currentLevel: string;
  levelName: string;
  score: number;
  recommendedCourses: RecommendedCourse[];
  skillsToImprove: string[];
  nextLevelSuggestion: NextLevelSuggestion | null;
  learningPath: LearningPath;
}

export interface RecommendedCourse {
  title: string;
  description: string;
  topics: string[];
  duration: string;
  difficulty: string;
}

export interface NextLevelSuggestion {
  level: string;
  levelName: string;
  message: string;
  previewCourses: RecommendedCourse[];
}

export interface LearningPath {
  currentFocus: string;
  estimatedTime: string;
  studyTips: string[];
}
