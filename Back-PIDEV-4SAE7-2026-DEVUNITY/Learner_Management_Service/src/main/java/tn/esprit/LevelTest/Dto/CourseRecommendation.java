package tn.esprit.LevelTest.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseRecommendation {
    private String currentLevel;
    private String levelName;
    private Integer score;
    private List<RecommendedCourse> recommendedCourses;
    private List<String> skillsToImprove;
    private NextLevelSuggestion nextLevelSuggestion;
    
    private LearningPath learningPath;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecommendedCourse {
        private String title;
        private String description;
        private List<String> topics;
        private String duration;
        private String difficulty;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NextLevelSuggestion {
        private String level;
        private String levelName;
        private String message;
        private List<RecommendedCourse> previewCourses;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LearningPath {
        private String currentFocus;
        private String estimatedTime;
        private List<String> studyTips;
    }
}
