package tn.esprit.LevelTest.Dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;

class CourseRecommendationTest {

    @Test
    void allArgsConstructor_setsFields() {
        CourseRecommendation.RecommendedCourse course = new CourseRecommendation.RecommendedCourse(
            "Course 1",
            "Desc",
            List.of("topic1", "topic2"),
            "2h",
            "A1"
        );
        CourseRecommendation.NextLevelSuggestion nextLevel = new CourseRecommendation.NextLevelSuggestion(
            "A2",
            "Beginner",
            "Keep going",
            List.of(course)
        );
        CourseRecommendation.LearningPath learningPath = new CourseRecommendation.LearningPath(
            "Grammar",
            "4 weeks",
            List.of("Tip 1", "Tip 2")
        );

        CourseRecommendation recommendation = new CourseRecommendation(
            "A1",
            "Starter",
            80,
            List.of(course),
            List.of("Vocabulary"),
            nextLevel,
            learningPath
        );

        assertEquals("A1", recommendation.getCurrentLevel());
        assertEquals("Starter", recommendation.getLevelName());
        assertEquals(80, recommendation.getScore());
        assertEquals("Course 1", recommendation.getRecommendedCourses().get(0).getTitle());
        assertEquals("A2", recommendation.getNextLevelSuggestion().getLevel());
        assertEquals("Grammar", recommendation.getLearningPath().getCurrentFocus());
    }

    @Test
    void settersAndGetters_work() {
        CourseRecommendation recommendation = new CourseRecommendation();
        recommendation.setCurrentLevel("B1");
        recommendation.setLevelName("Intermediate");
        recommendation.setScore(70);

        assertEquals("B1", recommendation.getCurrentLevel());
        assertEquals("Intermediate", recommendation.getLevelName());
        assertEquals(70, recommendation.getScore());
        assertNotNull(recommendation.toString());
    }
}
