package tn.esprit.LevelTest.Dto;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

class CourseRecommendationTest {

    @Test
    void equals_returnsTrue_whenCurrentLevelNullOnBoth() {
        CourseRecommendation left = baseCourseRecommendation();
        CourseRecommendation right = baseCourseRecommendation();
        left.setCurrentLevel(null);
        right.setCurrentLevel(null);

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenCurrentLevelNullOnLeft() {
        CourseRecommendation left = baseCourseRecommendation();
        CourseRecommendation right = baseCourseRecommendation();
        left.setCurrentLevel(null);

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsTrue_whenCurrentLevelSameValue() {
        CourseRecommendation left = baseCourseRecommendation();
        CourseRecommendation right = baseCourseRecommendation();
        left.setCurrentLevel("B1");
        right.setCurrentLevel("B1");

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenCurrentLevelDiffers() {
        CourseRecommendation left = baseCourseRecommendation();
        CourseRecommendation right = baseCourseRecommendation();
        right.setCurrentLevel("B2");

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsTrue_whenLevelNameNullOnBoth() {
        CourseRecommendation left = baseCourseRecommendation();
        CourseRecommendation right = baseCourseRecommendation();
        left.setLevelName(null);
        right.setLevelName(null);

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenLevelNameNullOnLeft() {
        CourseRecommendation left = baseCourseRecommendation();
        CourseRecommendation right = baseCourseRecommendation();
        left.setLevelName(null);

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsTrue_whenLevelNameSameValue() {
        CourseRecommendation left = baseCourseRecommendation();
        CourseRecommendation right = baseCourseRecommendation();
        left.setLevelName("Starter");
        right.setLevelName("Starter");

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenLevelNameDiffers() {
        CourseRecommendation left = baseCourseRecommendation();
        CourseRecommendation right = baseCourseRecommendation();
        right.setLevelName("Advanced");

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsTrue_whenScoreNullOnBoth() {
        CourseRecommendation left = baseCourseRecommendation();
        CourseRecommendation right = baseCourseRecommendation();
        left.setScore(null);
        right.setScore(null);

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenScoreNullOnLeft() {
        CourseRecommendation left = baseCourseRecommendation();
        CourseRecommendation right = baseCourseRecommendation();
        left.setScore(null);

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsTrue_whenScoreSameValue() {
        CourseRecommendation left = baseCourseRecommendation();
        CourseRecommendation right = baseCourseRecommendation();
        left.setScore(80);
        right.setScore(80);

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenScoreDiffers() {
        CourseRecommendation left = baseCourseRecommendation();
        CourseRecommendation right = baseCourseRecommendation();
        right.setScore(95);

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsTrue_whenRecommendedCoursesNullOnBoth() {
        CourseRecommendation left = baseCourseRecommendation();
        CourseRecommendation right = baseCourseRecommendation();
        left.setRecommendedCourses(null);
        right.setRecommendedCourses(null);

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenRecommendedCoursesNullOnLeft() {
        CourseRecommendation left = baseCourseRecommendation();
        CourseRecommendation right = baseCourseRecommendation();
        left.setRecommendedCourses(null);

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsTrue_whenRecommendedCoursesSameValue() {
        CourseRecommendation left = baseCourseRecommendation();
        CourseRecommendation right = baseCourseRecommendation();
        List<CourseRecommendation.RecommendedCourse> courses = List.of(recommendedCourse("Title"));
        left.setRecommendedCourses(courses);
        right.setRecommendedCourses(courses);

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenRecommendedCoursesDiffers() {
        CourseRecommendation left = baseCourseRecommendation();
        CourseRecommendation right = baseCourseRecommendation();
        right.setRecommendedCourses(List.of(recommendedCourse("Other")));

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsTrue_whenSkillsToImproveNullOnBoth() {
        CourseRecommendation left = baseCourseRecommendation();
        CourseRecommendation right = baseCourseRecommendation();
        left.setSkillsToImprove(null);
        right.setSkillsToImprove(null);

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenSkillsToImproveNullOnLeft() {
        CourseRecommendation left = baseCourseRecommendation();
        CourseRecommendation right = baseCourseRecommendation();
        left.setSkillsToImprove(null);

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsTrue_whenSkillsToImproveSameValue() {
        CourseRecommendation left = baseCourseRecommendation();
        CourseRecommendation right = baseCourseRecommendation();
        List<String> skills = List.of("Vocabulary");
        left.setSkillsToImprove(skills);
        right.setSkillsToImprove(skills);

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenSkillsToImproveDiffers() {
        CourseRecommendation left = baseCourseRecommendation();
        CourseRecommendation right = baseCourseRecommendation();
        right.setSkillsToImprove(List.of("Listening"));

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsTrue_whenNextLevelSuggestionNullOnBoth() {
        CourseRecommendation left = baseCourseRecommendation();
        CourseRecommendation right = baseCourseRecommendation();
        left.setNextLevelSuggestion(null);
        right.setNextLevelSuggestion(null);

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenNextLevelSuggestionNullOnLeft() {
        CourseRecommendation left = baseCourseRecommendation();
        CourseRecommendation right = baseCourseRecommendation();
        left.setNextLevelSuggestion(null);

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsTrue_whenNextLevelSuggestionSameValue() {
        CourseRecommendation left = baseCourseRecommendation();
        CourseRecommendation right = baseCourseRecommendation();
        CourseRecommendation.NextLevelSuggestion suggestion = nextLevelSuggestion("B1");
        left.setNextLevelSuggestion(suggestion);
        right.setNextLevelSuggestion(suggestion);

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenNextLevelSuggestionDiffers() {
        CourseRecommendation left = baseCourseRecommendation();
        CourseRecommendation right = baseCourseRecommendation();
        right.setNextLevelSuggestion(nextLevelSuggestion("B2"));

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsTrue_whenLearningPathNullOnBoth() {
        CourseRecommendation left = baseCourseRecommendation();
        CourseRecommendation right = baseCourseRecommendation();
        left.setLearningPath(null);
        right.setLearningPath(null);

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenLearningPathNullOnLeft() {
        CourseRecommendation left = baseCourseRecommendation();
        CourseRecommendation right = baseCourseRecommendation();
        left.setLearningPath(null);

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsTrue_whenLearningPathSameValue() {
        CourseRecommendation left = baseCourseRecommendation();
        CourseRecommendation right = baseCourseRecommendation();
        CourseRecommendation.LearningPath path = learningPath("Grammar");
        left.setLearningPath(path);
        right.setLearningPath(path);

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenLearningPathDiffers() {
        CourseRecommendation left = baseCourseRecommendation();
        CourseRecommendation right = baseCourseRecommendation();
        right.setLearningPath(learningPath("Speaking"));

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenComparedToNullOrDifferentType() {
        CourseRecommendation value = baseCourseRecommendation();

        assertFalse(value.equals(null));
        assertFalse(value.equals("not-a-course"));
    }

    @Test
    void hashCode_returnsSame_whenFieldsSame() {
        CourseRecommendation left = baseCourseRecommendation();
        CourseRecommendation right = baseCourseRecommendation();

        assertEquals(left.hashCode(), right.hashCode());
    }

    @Test
    void hashCode_doesNotThrow_whenFieldNull() {
        CourseRecommendation value = baseCourseRecommendation();
        value.setCurrentLevel(null);

        assertDoesNotThrow(value::hashCode);
    }

    @Test
    void hashCode_differs_whenCurrentLevelDiffers() {
        CourseRecommendation left = baseCourseRecommendation();
        CourseRecommendation right = baseCourseRecommendation();
        right.setCurrentLevel("B2");

        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    void hashCode_differs_whenLevelNameDiffers() {
        CourseRecommendation left = baseCourseRecommendation();
        CourseRecommendation right = baseCourseRecommendation();
        right.setLevelName("Advanced");

        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    void hashCode_differs_whenScoreDiffers() {
        CourseRecommendation left = baseCourseRecommendation();
        CourseRecommendation right = baseCourseRecommendation();
        right.setScore(95);

        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    void hashCode_differs_whenRecommendedCoursesDiffers() {
        CourseRecommendation left = baseCourseRecommendation();
        CourseRecommendation right = baseCourseRecommendation();
        right.setRecommendedCourses(List.of(recommendedCourse("Other")));

        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    void hashCode_differs_whenSkillsToImproveDiffers() {
        CourseRecommendation left = baseCourseRecommendation();
        CourseRecommendation right = baseCourseRecommendation();
        right.setSkillsToImprove(List.of("Listening"));

        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    void hashCode_differs_whenNextLevelSuggestionDiffers() {
        CourseRecommendation left = baseCourseRecommendation();
        CourseRecommendation right = baseCourseRecommendation();
        right.setNextLevelSuggestion(nextLevelSuggestion("B2"));

        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    void hashCode_differs_whenLearningPathDiffers() {
        CourseRecommendation left = baseCourseRecommendation();
        CourseRecommendation right = baseCourseRecommendation();
        right.setLearningPath(learningPath("Speaking"));

        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    void equals_returnsTrue_whenRecommendedCourseTitleNullOnBoth() {
        CourseRecommendation.RecommendedCourse left = baseRecommendedCourse();
        CourseRecommendation.RecommendedCourse right = baseRecommendedCourse();
        left.setTitle(null);
        right.setTitle(null);

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenRecommendedCourseTitleNullOnLeft() {
        CourseRecommendation.RecommendedCourse left = baseRecommendedCourse();
        CourseRecommendation.RecommendedCourse right = baseRecommendedCourse();
        left.setTitle(null);

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsTrue_whenRecommendedCourseTitleSameValue() {
        CourseRecommendation.RecommendedCourse left = baseRecommendedCourse();
        CourseRecommendation.RecommendedCourse right = baseRecommendedCourse();
        left.setTitle("Title");
        right.setTitle("Title");

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenRecommendedCourseTitleDiffers() {
        CourseRecommendation.RecommendedCourse left = baseRecommendedCourse();
        CourseRecommendation.RecommendedCourse right = baseRecommendedCourse();
        right.setTitle("Other");

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsTrue_whenRecommendedCourseDescriptionNullOnBoth() {
        CourseRecommendation.RecommendedCourse left = baseRecommendedCourse();
        CourseRecommendation.RecommendedCourse right = baseRecommendedCourse();
        left.setDescription(null);
        right.setDescription(null);

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenRecommendedCourseDescriptionNullOnLeft() {
        CourseRecommendation.RecommendedCourse left = baseRecommendedCourse();
        CourseRecommendation.RecommendedCourse right = baseRecommendedCourse();
        left.setDescription(null);

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsTrue_whenRecommendedCourseDescriptionSameValue() {
        CourseRecommendation.RecommendedCourse left = baseRecommendedCourse();
        CourseRecommendation.RecommendedCourse right = baseRecommendedCourse();
        left.setDescription("Desc");
        right.setDescription("Desc");

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenRecommendedCourseDescriptionDiffers() {
        CourseRecommendation.RecommendedCourse left = baseRecommendedCourse();
        CourseRecommendation.RecommendedCourse right = baseRecommendedCourse();
        right.setDescription("Other");

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsTrue_whenRecommendedCourseTopicsNullOnBoth() {
        CourseRecommendation.RecommendedCourse left = baseRecommendedCourse();
        CourseRecommendation.RecommendedCourse right = baseRecommendedCourse();
        left.setTopics(null);
        right.setTopics(null);

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenRecommendedCourseTopicsNullOnLeft() {
        CourseRecommendation.RecommendedCourse left = baseRecommendedCourse();
        CourseRecommendation.RecommendedCourse right = baseRecommendedCourse();
        left.setTopics(null);

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsTrue_whenRecommendedCourseTopicsSameValue() {
        CourseRecommendation.RecommendedCourse left = baseRecommendedCourse();
        CourseRecommendation.RecommendedCourse right = baseRecommendedCourse();
        left.setTopics(List.of("topic1"));
        right.setTopics(List.of("topic1"));

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenRecommendedCourseTopicsDiffers() {
        CourseRecommendation.RecommendedCourse left = baseRecommendedCourse();
        CourseRecommendation.RecommendedCourse right = baseRecommendedCourse();
        right.setTopics(List.of("topic2"));

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsTrue_whenRecommendedCourseDurationNullOnBoth() {
        CourseRecommendation.RecommendedCourse left = baseRecommendedCourse();
        CourseRecommendation.RecommendedCourse right = baseRecommendedCourse();
        left.setDuration(null);
        right.setDuration(null);

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenRecommendedCourseDurationNullOnLeft() {
        CourseRecommendation.RecommendedCourse left = baseRecommendedCourse();
        CourseRecommendation.RecommendedCourse right = baseRecommendedCourse();
        left.setDuration(null);

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsTrue_whenRecommendedCourseDurationSameValue() {
        CourseRecommendation.RecommendedCourse left = baseRecommendedCourse();
        CourseRecommendation.RecommendedCourse right = baseRecommendedCourse();
        left.setDuration("2h");
        right.setDuration("2h");

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenRecommendedCourseDurationDiffers() {
        CourseRecommendation.RecommendedCourse left = baseRecommendedCourse();
        CourseRecommendation.RecommendedCourse right = baseRecommendedCourse();
        right.setDuration("3h");

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsTrue_whenRecommendedCourseDifficultyNullOnBoth() {
        CourseRecommendation.RecommendedCourse left = baseRecommendedCourse();
        CourseRecommendation.RecommendedCourse right = baseRecommendedCourse();
        left.setDifficulty(null);
        right.setDifficulty(null);

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenRecommendedCourseDifficultyNullOnLeft() {
        CourseRecommendation.RecommendedCourse left = baseRecommendedCourse();
        CourseRecommendation.RecommendedCourse right = baseRecommendedCourse();
        left.setDifficulty(null);

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsTrue_whenRecommendedCourseDifficultySameValue() {
        CourseRecommendation.RecommendedCourse left = baseRecommendedCourse();
        CourseRecommendation.RecommendedCourse right = baseRecommendedCourse();
        left.setDifficulty("A1");
        right.setDifficulty("A1");

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenRecommendedCourseDifficultyDiffers() {
        CourseRecommendation.RecommendedCourse left = baseRecommendedCourse();
        CourseRecommendation.RecommendedCourse right = baseRecommendedCourse();
        right.setDifficulty("B1");

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenRecommendedCourseComparedToNullOrDifferentType() {
        CourseRecommendation.RecommendedCourse value = baseRecommendedCourse();

        assertFalse(value.equals(null));
        assertFalse(value.equals("not-a-course"));
    }

    @Test
    void hashCode_returnsSame_whenRecommendedCourseFieldsSame() {
        CourseRecommendation.RecommendedCourse left = baseRecommendedCourse();
        CourseRecommendation.RecommendedCourse right = baseRecommendedCourse();

        assertEquals(left.hashCode(), right.hashCode());
    }

    @Test
    void hashCode_doesNotThrow_whenRecommendedCourseFieldNull() {
        CourseRecommendation.RecommendedCourse value = baseRecommendedCourse();
        value.setTitle(null);

        assertDoesNotThrow(value::hashCode);
    }

    @Test
    void hashCode_differs_whenRecommendedCourseTitleDiffers() {
        CourseRecommendation.RecommendedCourse left = baseRecommendedCourse();
        CourseRecommendation.RecommendedCourse right = baseRecommendedCourse();
        right.setTitle("Other");

        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    void hashCode_differs_whenRecommendedCourseDescriptionDiffers() {
        CourseRecommendation.RecommendedCourse left = baseRecommendedCourse();
        CourseRecommendation.RecommendedCourse right = baseRecommendedCourse();
        right.setDescription("Other");

        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    void hashCode_differs_whenRecommendedCourseTopicsDiffers() {
        CourseRecommendation.RecommendedCourse left = baseRecommendedCourse();
        CourseRecommendation.RecommendedCourse right = baseRecommendedCourse();
        right.setTopics(List.of("topic2"));

        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    void hashCode_differs_whenRecommendedCourseDurationDiffers() {
        CourseRecommendation.RecommendedCourse left = baseRecommendedCourse();
        CourseRecommendation.RecommendedCourse right = baseRecommendedCourse();
        right.setDuration("3h");

        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    void hashCode_differs_whenRecommendedCourseDifficultyDiffers() {
        CourseRecommendation.RecommendedCourse left = baseRecommendedCourse();
        CourseRecommendation.RecommendedCourse right = baseRecommendedCourse();
        right.setDifficulty("B1");

        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    void equals_returnsTrue_whenNextLevelSuggestionLevelNullOnBoth() {
        CourseRecommendation.NextLevelSuggestion left = baseNextLevelSuggestion();
        CourseRecommendation.NextLevelSuggestion right = baseNextLevelSuggestion();
        left.setLevel(null);
        right.setLevel(null);

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenNextLevelSuggestionLevelNullOnLeft() {
        CourseRecommendation.NextLevelSuggestion left = baseNextLevelSuggestion();
        CourseRecommendation.NextLevelSuggestion right = baseNextLevelSuggestion();
        left.setLevel(null);

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsTrue_whenNextLevelSuggestionLevelSameValue() {
        CourseRecommendation.NextLevelSuggestion left = baseNextLevelSuggestion();
        CourseRecommendation.NextLevelSuggestion right = baseNextLevelSuggestion();
        left.setLevel("B1");
        right.setLevel("B1");

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenNextLevelSuggestionLevelDiffers() {
        CourseRecommendation.NextLevelSuggestion left = baseNextLevelSuggestion();
        CourseRecommendation.NextLevelSuggestion right = baseNextLevelSuggestion();
        right.setLevel("B2");

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsTrue_whenNextLevelSuggestionLevelNameNullOnBoth() {
        CourseRecommendation.NextLevelSuggestion left = baseNextLevelSuggestion();
        CourseRecommendation.NextLevelSuggestion right = baseNextLevelSuggestion();
        left.setLevelName(null);
        right.setLevelName(null);

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenNextLevelSuggestionLevelNameNullOnLeft() {
        CourseRecommendation.NextLevelSuggestion left = baseNextLevelSuggestion();
        CourseRecommendation.NextLevelSuggestion right = baseNextLevelSuggestion();
        left.setLevelName(null);

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsTrue_whenNextLevelSuggestionLevelNameSameValue() {
        CourseRecommendation.NextLevelSuggestion left = baseNextLevelSuggestion();
        CourseRecommendation.NextLevelSuggestion right = baseNextLevelSuggestion();
        left.setLevelName("Intermediate");
        right.setLevelName("Intermediate");

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenNextLevelSuggestionLevelNameDiffers() {
        CourseRecommendation.NextLevelSuggestion left = baseNextLevelSuggestion();
        CourseRecommendation.NextLevelSuggestion right = baseNextLevelSuggestion();
        right.setLevelName("Advanced");

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsTrue_whenNextLevelSuggestionMessageNullOnBoth() {
        CourseRecommendation.NextLevelSuggestion left = baseNextLevelSuggestion();
        CourseRecommendation.NextLevelSuggestion right = baseNextLevelSuggestion();
        left.setMessage(null);
        right.setMessage(null);

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenNextLevelSuggestionMessageNullOnLeft() {
        CourseRecommendation.NextLevelSuggestion left = baseNextLevelSuggestion();
        CourseRecommendation.NextLevelSuggestion right = baseNextLevelSuggestion();
        left.setMessage(null);

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsTrue_whenNextLevelSuggestionMessageSameValue() {
        CourseRecommendation.NextLevelSuggestion left = baseNextLevelSuggestion();
        CourseRecommendation.NextLevelSuggestion right = baseNextLevelSuggestion();
        left.setMessage("Nice");
        right.setMessage("Nice");

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenNextLevelSuggestionMessageDiffers() {
        CourseRecommendation.NextLevelSuggestion left = baseNextLevelSuggestion();
        CourseRecommendation.NextLevelSuggestion right = baseNextLevelSuggestion();
        right.setMessage("Other");

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsTrue_whenNextLevelSuggestionPreviewCoursesNullOnBoth() {
        CourseRecommendation.NextLevelSuggestion left = baseNextLevelSuggestion();
        CourseRecommendation.NextLevelSuggestion right = baseNextLevelSuggestion();
        left.setPreviewCourses(null);
        right.setPreviewCourses(null);

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenNextLevelSuggestionPreviewCoursesNullOnLeft() {
        CourseRecommendation.NextLevelSuggestion left = baseNextLevelSuggestion();
        CourseRecommendation.NextLevelSuggestion right = baseNextLevelSuggestion();
        left.setPreviewCourses(null);

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsTrue_whenNextLevelSuggestionPreviewCoursesSameValue() {
        CourseRecommendation.NextLevelSuggestion left = baseNextLevelSuggestion();
        CourseRecommendation.NextLevelSuggestion right = baseNextLevelSuggestion();
        List<CourseRecommendation.RecommendedCourse> preview = List.of(recommendedCourse("Title"));
        left.setPreviewCourses(preview);
        right.setPreviewCourses(preview);

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenNextLevelSuggestionPreviewCoursesDiffers() {
        CourseRecommendation.NextLevelSuggestion left = baseNextLevelSuggestion();
        CourseRecommendation.NextLevelSuggestion right = baseNextLevelSuggestion();
        right.setPreviewCourses(List.of(recommendedCourse("Other")));

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenNextLevelSuggestionComparedToNullOrDifferentType() {
        CourseRecommendation.NextLevelSuggestion value = baseNextLevelSuggestion();

        assertFalse(value.equals(null));
        assertFalse(value.equals("not-a-suggestion"));
    }

    @Test
    void hashCode_returnsSame_whenNextLevelSuggestionFieldsSame() {
        CourseRecommendation.NextLevelSuggestion left = baseNextLevelSuggestion();
        CourseRecommendation.NextLevelSuggestion right = baseNextLevelSuggestion();

        assertEquals(left.hashCode(), right.hashCode());
    }

    @Test
    void hashCode_doesNotThrow_whenNextLevelSuggestionFieldNull() {
        CourseRecommendation.NextLevelSuggestion value = baseNextLevelSuggestion();
        value.setLevel(null);

        assertDoesNotThrow(value::hashCode);
    }

    @Test
    void hashCode_differs_whenNextLevelSuggestionLevelDiffers() {
        CourseRecommendation.NextLevelSuggestion left = baseNextLevelSuggestion();
        CourseRecommendation.NextLevelSuggestion right = baseNextLevelSuggestion();
        right.setLevel("B2");

        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    void hashCode_differs_whenNextLevelSuggestionLevelNameDiffers() {
        CourseRecommendation.NextLevelSuggestion left = baseNextLevelSuggestion();
        CourseRecommendation.NextLevelSuggestion right = baseNextLevelSuggestion();
        right.setLevelName("Advanced");

        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    void hashCode_differs_whenNextLevelSuggestionMessageDiffers() {
        CourseRecommendation.NextLevelSuggestion left = baseNextLevelSuggestion();
        CourseRecommendation.NextLevelSuggestion right = baseNextLevelSuggestion();
        right.setMessage("Other");

        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    void hashCode_differs_whenNextLevelSuggestionPreviewCoursesDiffers() {
        CourseRecommendation.NextLevelSuggestion left = baseNextLevelSuggestion();
        CourseRecommendation.NextLevelSuggestion right = baseNextLevelSuggestion();
        right.setPreviewCourses(List.of(recommendedCourse("Other")));

        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    void equals_returnsTrue_whenLearningPathCurrentFocusNullOnBoth() {
        CourseRecommendation.LearningPath left = baseLearningPath();
        CourseRecommendation.LearningPath right = baseLearningPath();
        left.setCurrentFocus(null);
        right.setCurrentFocus(null);

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenLearningPathCurrentFocusNullOnLeft() {
        CourseRecommendation.LearningPath left = baseLearningPath();
        CourseRecommendation.LearningPath right = baseLearningPath();
        left.setCurrentFocus(null);

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsTrue_whenLearningPathCurrentFocusSameValue() {
        CourseRecommendation.LearningPath left = baseLearningPath();
        CourseRecommendation.LearningPath right = baseLearningPath();
        left.setCurrentFocus("Grammar");
        right.setCurrentFocus("Grammar");

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenLearningPathCurrentFocusDiffers() {
        CourseRecommendation.LearningPath left = baseLearningPath();
        CourseRecommendation.LearningPath right = baseLearningPath();
        right.setCurrentFocus("Speaking");

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsTrue_whenLearningPathEstimatedTimeNullOnBoth() {
        CourseRecommendation.LearningPath left = baseLearningPath();
        CourseRecommendation.LearningPath right = baseLearningPath();
        left.setEstimatedTime(null);
        right.setEstimatedTime(null);

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenLearningPathEstimatedTimeNullOnLeft() {
        CourseRecommendation.LearningPath left = baseLearningPath();
        CourseRecommendation.LearningPath right = baseLearningPath();
        left.setEstimatedTime(null);

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsTrue_whenLearningPathEstimatedTimeSameValue() {
        CourseRecommendation.LearningPath left = baseLearningPath();
        CourseRecommendation.LearningPath right = baseLearningPath();
        left.setEstimatedTime("4 weeks");
        right.setEstimatedTime("4 weeks");

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenLearningPathEstimatedTimeDiffers() {
        CourseRecommendation.LearningPath left = baseLearningPath();
        CourseRecommendation.LearningPath right = baseLearningPath();
        right.setEstimatedTime("2 weeks");

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsTrue_whenLearningPathStudyTipsNullOnBoth() {
        CourseRecommendation.LearningPath left = baseLearningPath();
        CourseRecommendation.LearningPath right = baseLearningPath();
        left.setStudyTips(null);
        right.setStudyTips(null);

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenLearningPathStudyTipsNullOnLeft() {
        CourseRecommendation.LearningPath left = baseLearningPath();
        CourseRecommendation.LearningPath right = baseLearningPath();
        left.setStudyTips(null);

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsTrue_whenLearningPathStudyTipsSameValue() {
        CourseRecommendation.LearningPath left = baseLearningPath();
        CourseRecommendation.LearningPath right = baseLearningPath();
        List<String> tips = List.of("Tip 1");
        left.setStudyTips(tips);
        right.setStudyTips(tips);

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenLearningPathStudyTipsDiffers() {
        CourseRecommendation.LearningPath left = baseLearningPath();
        CourseRecommendation.LearningPath right = baseLearningPath();
        right.setStudyTips(List.of("Tip 2"));

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenLearningPathComparedToNullOrDifferentType() {
        CourseRecommendation.LearningPath value = baseLearningPath();

        assertFalse(value.equals(null));
        assertFalse(value.equals("not-a-path"));
    }

    @Test
    void hashCode_returnsSame_whenLearningPathFieldsSame() {
        CourseRecommendation.LearningPath left = baseLearningPath();
        CourseRecommendation.LearningPath right = baseLearningPath();

        assertEquals(left.hashCode(), right.hashCode());
    }

    @Test
    void hashCode_doesNotThrow_whenLearningPathFieldNull() {
        CourseRecommendation.LearningPath value = baseLearningPath();
        value.setCurrentFocus(null);

        assertDoesNotThrow(value::hashCode);
    }

    @Test
    void hashCode_differs_whenLearningPathCurrentFocusDiffers() {
        CourseRecommendation.LearningPath left = baseLearningPath();
        CourseRecommendation.LearningPath right = baseLearningPath();
        right.setCurrentFocus("Speaking");

        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    void hashCode_differs_whenLearningPathEstimatedTimeDiffers() {
        CourseRecommendation.LearningPath left = baseLearningPath();
        CourseRecommendation.LearningPath right = baseLearningPath();
        right.setEstimatedTime("2 weeks");

        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    void hashCode_differs_whenLearningPathStudyTipsDiffers() {
        CourseRecommendation.LearningPath left = baseLearningPath();
        CourseRecommendation.LearningPath right = baseLearningPath();
        right.setStudyTips(List.of("Tip 2"));

        assertNotEquals(left.hashCode(), right.hashCode());
    }

    private CourseRecommendation baseCourseRecommendation() {
        CourseRecommendation.RecommendedCourse course = recommendedCourse("Title");
        CourseRecommendation.NextLevelSuggestion suggestion = nextLevelSuggestion("B1");
        CourseRecommendation.LearningPath path = learningPath("Grammar");

        return new CourseRecommendation(
            "A1",
            "Starter",
            80,
            List.of(course),
            List.of("Vocabulary"),
            suggestion,
            path
        );
    }

    private CourseRecommendation.RecommendedCourse recommendedCourse(String title) {
        return new CourseRecommendation.RecommendedCourse(
            title,
            "Desc",
            List.of("topic1"),
            "2h",
            "A1"
        );
    }

    private CourseRecommendation.NextLevelSuggestion nextLevelSuggestion(String level) {
        return new CourseRecommendation.NextLevelSuggestion(
            level,
            "Intermediate",
            "Nice",
            List.of(recommendedCourse("Title"))
        );
    }

    private CourseRecommendation.LearningPath learningPath(String currentFocus) {
        return new CourseRecommendation.LearningPath(
            currentFocus,
            "4 weeks",
            List.of("Tip 1")
        );
    }

    private CourseRecommendation.RecommendedCourse baseRecommendedCourse() {
        return recommendedCourse("Title");
    }

    private CourseRecommendation.NextLevelSuggestion baseNextLevelSuggestion() {
        return nextLevelSuggestion("B1");
    }

    private CourseRecommendation.LearningPath baseLearningPath() {
        return learningPath("Grammar");
    }
}