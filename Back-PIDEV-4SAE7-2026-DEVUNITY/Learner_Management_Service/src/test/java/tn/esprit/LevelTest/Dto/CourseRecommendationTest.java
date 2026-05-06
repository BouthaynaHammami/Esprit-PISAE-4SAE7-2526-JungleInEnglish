package tn.esprit.LevelTest.Dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

class CourseRecommendationTest {

    @Test
    void courseRecommendation_noArgsConstructor_hasNullFields() {
        CourseRecommendation recommendation = new CourseRecommendation();

        assertNull(recommendation.getCurrentLevel());
        assertNull(recommendation.getLevelName());
        assertNull(recommendation.getScore());
        assertNull(recommendation.getRecommendedCourses());
        assertNull(recommendation.getSkillsToImprove());
        assertNull(recommendation.getNextLevelSuggestion());
        assertNull(recommendation.getLearningPath());
    }

    @Test
    void courseRecommendation_allArgsConstructor_setsAllFields() {
        CourseRecommendation.RecommendedCourse course = new CourseRecommendation.RecommendedCourse(
            "Title",
            "Desc",
            List.of("topic1"),
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
            List.of("Tip 1")
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
        assertEquals(course, recommendation.getRecommendedCourses().get(0));
        assertEquals("Vocabulary", recommendation.getSkillsToImprove().get(0));
        assertEquals(nextLevel, recommendation.getNextLevelSuggestion());
        assertEquals(learningPath, recommendation.getLearningPath());
    }

    @Test
    void courseRecommendation_gettersAndSetters_work() {
        CourseRecommendation recommendation = new CourseRecommendation();
        CourseRecommendation.RecommendedCourse course = new CourseRecommendation.RecommendedCourse();
        CourseRecommendation.NextLevelSuggestion nextLevel = new CourseRecommendation.NextLevelSuggestion();
        CourseRecommendation.LearningPath learningPath = new CourseRecommendation.LearningPath();

        recommendation.setCurrentLevel("B1");
        recommendation.setLevelName("Intermediate");
        recommendation.setScore(70);
        recommendation.setRecommendedCourses(List.of(course));
        recommendation.setSkillsToImprove(List.of("Writing"));
        recommendation.setNextLevelSuggestion(nextLevel);
        recommendation.setLearningPath(learningPath);

        assertEquals("B1", recommendation.getCurrentLevel());
        assertEquals("Intermediate", recommendation.getLevelName());
        assertEquals(70, recommendation.getScore());
        assertEquals(course, recommendation.getRecommendedCourses().get(0));
        assertEquals("Writing", recommendation.getSkillsToImprove().get(0));
        assertEquals(nextLevel, recommendation.getNextLevelSuggestion());
        assertEquals(learningPath, recommendation.getLearningPath());
    }

    @Test
    void courseRecommendation_equalsAndHashCode_identicalObjects() {
        CourseRecommendation.RecommendedCourse course = new CourseRecommendation.RecommendedCourse(
            "Title",
            "Desc",
            List.of("topic1"),
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
            List.of("Tip 1")
        );

        CourseRecommendation left = new CourseRecommendation(
            "A1",
            "Starter",
            80,
            List.of(course),
            List.of("Vocabulary"),
            nextLevel,
            learningPath
        );
        CourseRecommendation right = new CourseRecommendation(
            "A1",
            "Starter",
            80,
            List.of(course),
            List.of("Vocabulary"),
            nextLevel,
            learningPath
        );

        assertTrue(left.equals(right));
        assertEquals(left.hashCode(), right.hashCode());
    }

    @Test
    void courseRecommendation_equals_returnsFalseWhenCurrentLevelDiffers() {
        CourseRecommendation base = buildCourseRecommendation();
        CourseRecommendation other = buildCourseRecommendation();
        other.setCurrentLevel("B2");

        assertFalse(base.equals(other));
    }

    @Test
    void courseRecommendation_equals_returnsFalseWhenLevelNameDiffers() {
        CourseRecommendation base = buildCourseRecommendation();
        CourseRecommendation other = buildCourseRecommendation();
        other.setLevelName("Advanced");

        assertFalse(base.equals(other));
    }

    @Test
    void courseRecommendation_equals_returnsFalseWhenScoreDiffers() {
        CourseRecommendation base = buildCourseRecommendation();
        CourseRecommendation other = buildCourseRecommendation();
        other.setScore(95);

        assertFalse(base.equals(other));
    }

    @Test
    void courseRecommendation_equals_returnsFalseWhenRecommendedCoursesDiffers() {
        CourseRecommendation base = buildCourseRecommendation();
        CourseRecommendation other = buildCourseRecommendation();
        other.setRecommendedCourses(List.of(new CourseRecommendation.RecommendedCourse("X", "Y", List.of(), "1h", "A1")));

        assertFalse(base.equals(other));
    }

    @Test
    void courseRecommendation_equals_returnsFalseWhenSkillsToImproveDiffers() {
        CourseRecommendation base = buildCourseRecommendation();
        CourseRecommendation other = buildCourseRecommendation();
        other.setSkillsToImprove(List.of("Listening"));

        assertFalse(base.equals(other));
    }

    @Test
    void courseRecommendation_equals_returnsFalseWhenNextLevelSuggestionDiffers() {
        CourseRecommendation base = buildCourseRecommendation();
        CourseRecommendation other = buildCourseRecommendation();
        other.setNextLevelSuggestion(new CourseRecommendation.NextLevelSuggestion("B1", "Mid", "Msg", List.of()));

        assertFalse(base.equals(other));
    }

    @Test
    void courseRecommendation_equals_returnsFalseWhenLearningPathDiffers() {
        CourseRecommendation base = buildCourseRecommendation();
        CourseRecommendation other = buildCourseRecommendation();
        other.setLearningPath(new CourseRecommendation.LearningPath("Speaking", "2 weeks", List.of("Tip")));

        assertFalse(base.equals(other));
    }

    @Test
    void courseRecommendation_equals_returnsFalseForNullOrDifferentType() {
        CourseRecommendation recommendation = buildCourseRecommendation();

        assertFalse(recommendation.equals(null));
        assertFalse(recommendation.equals("not-a-course"));
    }

    @Test
    void courseRecommendation_hashCode_differsWhenFieldDiffers() {
        CourseRecommendation base = buildCourseRecommendation();
        CourseRecommendation other = buildCourseRecommendation();
        other.setLevelName("Advanced");

        assertFalse(base.hashCode() == other.hashCode());
    }

    @Test
    void courseRecommendation_toString_containsFields() {
        CourseRecommendation recommendation = buildCourseRecommendation();
        String output = recommendation.toString();

        assertTrue(output.contains("currentLevel=A1"));
        assertTrue(output.contains("levelName=Starter"));
        assertTrue(output.contains("score=80"));
    }

    @Test
    void courseRecommendation_setters_acceptNulls() {
        CourseRecommendation recommendation = buildCourseRecommendation();

        recommendation.setCurrentLevel(null);
        recommendation.setLevelName(null);
        recommendation.setScore(null);
        recommendation.setRecommendedCourses(null);
        recommendation.setSkillsToImprove(null);
        recommendation.setNextLevelSuggestion(null);
        recommendation.setLearningPath(null);

        assertNull(recommendation.getCurrentLevel());
        assertNull(recommendation.getLevelName());
        assertNull(recommendation.getScore());
        assertNull(recommendation.getRecommendedCourses());
        assertNull(recommendation.getSkillsToImprove());
        assertNull(recommendation.getNextLevelSuggestion());
        assertNull(recommendation.getLearningPath());
    }

    @Test
    void recommendedCourse_noArgsConstructor_hasNullFields() {
        CourseRecommendation.RecommendedCourse course = new CourseRecommendation.RecommendedCourse();

        assertNull(course.getTitle());
        assertNull(course.getDescription());
        assertNull(course.getTopics());
        assertNull(course.getDuration());
        assertNull(course.getDifficulty());
    }

    @Test
    void recommendedCourse_allArgsConstructor_setsAllFields() {
        CourseRecommendation.RecommendedCourse course = new CourseRecommendation.RecommendedCourse(
            "Title",
            "Desc",
            List.of("topic1"),
            "2h",
            "A1"
        );

        assertEquals("Title", course.getTitle());
        assertEquals("Desc", course.getDescription());
        assertEquals("topic1", course.getTopics().get(0));
        assertEquals("2h", course.getDuration());
        assertEquals("A1", course.getDifficulty());
    }

    @Test
    void recommendedCourse_gettersAndSetters_work() {
        CourseRecommendation.RecommendedCourse course = new CourseRecommendation.RecommendedCourse();
        course.setTitle("Title");
        course.setDescription("Desc");
        course.setTopics(List.of("topic1"));
        course.setDuration("2h");
        course.setDifficulty("A1");

        assertEquals("Title", course.getTitle());
        assertEquals("Desc", course.getDescription());
        assertEquals("topic1", course.getTopics().get(0));
        assertEquals("2h", course.getDuration());
        assertEquals("A1", course.getDifficulty());
    }

    @Test
    void recommendedCourse_equalsAndHashCode_identicalObjects() {
        CourseRecommendation.RecommendedCourse left = new CourseRecommendation.RecommendedCourse(
            "Title",
            "Desc",
            List.of("topic1"),
            "2h",
            "A1"
        );
        CourseRecommendation.RecommendedCourse right = new CourseRecommendation.RecommendedCourse(
            "Title",
            "Desc",
            List.of("topic1"),
            "2h",
            "A1"
        );

        assertTrue(left.equals(right));
        assertEquals(left.hashCode(), right.hashCode());
    }

    @Test
    void recommendedCourse_equals_returnsFalseWhenTitleDiffers() {
        CourseRecommendation.RecommendedCourse base = buildRecommendedCourse();
        CourseRecommendation.RecommendedCourse other = buildRecommendedCourse();
        other.setTitle("Other");

        assertFalse(base.equals(other));
    }

    @Test
    void recommendedCourse_equals_returnsFalseWhenDescriptionDiffers() {
        CourseRecommendation.RecommendedCourse base = buildRecommendedCourse();
        CourseRecommendation.RecommendedCourse other = buildRecommendedCourse();
        other.setDescription("Other");

        assertFalse(base.equals(other));
    }

    @Test
    void recommendedCourse_equals_returnsFalseWhenTopicsDiffers() {
        CourseRecommendation.RecommendedCourse base = buildRecommendedCourse();
        CourseRecommendation.RecommendedCourse other = buildRecommendedCourse();
        other.setTopics(List.of("topic2"));

        assertFalse(base.equals(other));
    }

    @Test
    void recommendedCourse_equals_returnsFalseWhenDurationDiffers() {
        CourseRecommendation.RecommendedCourse base = buildRecommendedCourse();
        CourseRecommendation.RecommendedCourse other = buildRecommendedCourse();
        other.setDuration("3h");

        assertFalse(base.equals(other));
    }

    @Test
    void recommendedCourse_equals_returnsFalseWhenDifficultyDiffers() {
        CourseRecommendation.RecommendedCourse base = buildRecommendedCourse();
        CourseRecommendation.RecommendedCourse other = buildRecommendedCourse();
        other.setDifficulty("B1");

        assertFalse(base.equals(other));
    }

    @Test
    void recommendedCourse_equals_returnsFalseForNullOrDifferentType() {
        CourseRecommendation.RecommendedCourse course = buildRecommendedCourse();

        assertFalse(course.equals(null));
        assertFalse(course.equals("not-a-course"));
    }

    @Test
    void recommendedCourse_hashCode_differsWhenFieldDiffers() {
        CourseRecommendation.RecommendedCourse base = buildRecommendedCourse();
        CourseRecommendation.RecommendedCourse other = buildRecommendedCourse();
        other.setDuration("3h");

        assertFalse(base.hashCode() == other.hashCode());
    }

    @Test
    void recommendedCourse_toString_containsFields() {
        CourseRecommendation.RecommendedCourse course = buildRecommendedCourse();
        String output = course.toString();

        assertTrue(output.contains("title=Title"));
        assertTrue(output.contains("description=Desc"));
        assertTrue(output.contains("duration=2h"));
        assertTrue(output.contains("difficulty=A1"));
    }

    @Test
    void recommendedCourse_setters_acceptNulls() {
        CourseRecommendation.RecommendedCourse course = buildRecommendedCourse();
        course.setTitle(null);
        course.setDescription(null);
        course.setTopics(null);
        course.setDuration(null);
        course.setDifficulty(null);

        assertNull(course.getTitle());
        assertNull(course.getDescription());
        assertNull(course.getTopics());
        assertNull(course.getDuration());
        assertNull(course.getDifficulty());
    }

    @Test
    void nextLevelSuggestion_noArgsConstructor_hasNullFields() {
        CourseRecommendation.NextLevelSuggestion suggestion = new CourseRecommendation.NextLevelSuggestion();

        assertNull(suggestion.getLevel());
        assertNull(suggestion.getLevelName());
        assertNull(suggestion.getMessage());
        assertNull(suggestion.getPreviewCourses());
    }

    @Test
    void nextLevelSuggestion_allArgsConstructor_setsAllFields() {
        CourseRecommendation.RecommendedCourse course = buildRecommendedCourse();
        CourseRecommendation.NextLevelSuggestion suggestion = new CourseRecommendation.NextLevelSuggestion(
            "B1",
            "Intermediate",
            "Nice",
            List.of(course)
        );

        assertEquals("B1", suggestion.getLevel());
        assertEquals("Intermediate", suggestion.getLevelName());
        assertEquals("Nice", suggestion.getMessage());
        assertEquals(course, suggestion.getPreviewCourses().get(0));
    }

    @Test
    void nextLevelSuggestion_gettersAndSetters_work() {
        CourseRecommendation.NextLevelSuggestion suggestion = new CourseRecommendation.NextLevelSuggestion();
        CourseRecommendation.RecommendedCourse course = buildRecommendedCourse();
        suggestion.setLevel("B1");
        suggestion.setLevelName("Intermediate");
        suggestion.setMessage("Nice");
        suggestion.setPreviewCourses(List.of(course));

        assertEquals("B1", suggestion.getLevel());
        assertEquals("Intermediate", suggestion.getLevelName());
        assertEquals("Nice", suggestion.getMessage());
        assertEquals(course, suggestion.getPreviewCourses().get(0));
    }

    @Test
    void nextLevelSuggestion_equalsAndHashCode_identicalObjects() {
        CourseRecommendation.NextLevelSuggestion left = new CourseRecommendation.NextLevelSuggestion(
            "B1",
            "Intermediate",
            "Nice",
            List.of(buildRecommendedCourse())
        );
        CourseRecommendation.NextLevelSuggestion right = new CourseRecommendation.NextLevelSuggestion(
            "B1",
            "Intermediate",
            "Nice",
            List.of(buildRecommendedCourse())
        );

        assertTrue(left.equals(right));
        assertEquals(left.hashCode(), right.hashCode());
    }

    @Test
    void nextLevelSuggestion_equals_returnsFalseWhenLevelDiffers() {
        CourseRecommendation.NextLevelSuggestion base = buildNextLevelSuggestion();
        CourseRecommendation.NextLevelSuggestion other = buildNextLevelSuggestion();
        other.setLevel("B2");

        assertFalse(base.equals(other));
    }

    @Test
    void nextLevelSuggestion_equals_returnsFalseWhenLevelNameDiffers() {
        CourseRecommendation.NextLevelSuggestion base = buildNextLevelSuggestion();
        CourseRecommendation.NextLevelSuggestion other = buildNextLevelSuggestion();
        other.setLevelName("Advanced");

        assertFalse(base.equals(other));
    }

    @Test
    void nextLevelSuggestion_equals_returnsFalseWhenMessageDiffers() {
        CourseRecommendation.NextLevelSuggestion base = buildNextLevelSuggestion();
        CourseRecommendation.NextLevelSuggestion other = buildNextLevelSuggestion();
        other.setMessage("Other");

        assertFalse(base.equals(other));
    }

    @Test
    void nextLevelSuggestion_equals_returnsFalseWhenPreviewCoursesDiffers() {
        CourseRecommendation.NextLevelSuggestion base = buildNextLevelSuggestion();
        CourseRecommendation.NextLevelSuggestion other = buildNextLevelSuggestion();
        other.setPreviewCourses(List.of(new CourseRecommendation.RecommendedCourse("X", "Y", List.of(), "1h", "A1")));

        assertFalse(base.equals(other));
    }

    @Test
    void nextLevelSuggestion_equals_returnsFalseForNullOrDifferentType() {
        CourseRecommendation.NextLevelSuggestion suggestion = buildNextLevelSuggestion();

        assertFalse(suggestion.equals(null));
        assertFalse(suggestion.equals("not-a-suggestion"));
    }

    @Test
    void nextLevelSuggestion_hashCode_differsWhenFieldDiffers() {
        CourseRecommendation.NextLevelSuggestion base = buildNextLevelSuggestion();
        CourseRecommendation.NextLevelSuggestion other = buildNextLevelSuggestion();
        other.setMessage("Other");

        assertFalse(base.hashCode() == other.hashCode());
    }

    @Test
    void nextLevelSuggestion_toString_containsFields() {
        CourseRecommendation.NextLevelSuggestion suggestion = buildNextLevelSuggestion();
        String output = suggestion.toString();

        assertTrue(output.contains("level=B1"));
        assertTrue(output.contains("levelName=Intermediate"));
        assertTrue(output.contains("message=Nice"));
    }

    @Test
    void nextLevelSuggestion_setters_acceptNulls() {
        CourseRecommendation.NextLevelSuggestion suggestion = buildNextLevelSuggestion();
        suggestion.setLevel(null);
        suggestion.setLevelName(null);
        suggestion.setMessage(null);
        suggestion.setPreviewCourses(null);

        assertNull(suggestion.getLevel());
        assertNull(suggestion.getLevelName());
        assertNull(suggestion.getMessage());
        assertNull(suggestion.getPreviewCourses());
    }

    @Test
    void learningPath_noArgsConstructor_hasNullFields() {
        CourseRecommendation.LearningPath path = new CourseRecommendation.LearningPath();

        assertNull(path.getCurrentFocus());
        assertNull(path.getEstimatedTime());
        assertNull(path.getStudyTips());
    }

    @Test
    void learningPath_allArgsConstructor_setsAllFields() {
        CourseRecommendation.LearningPath path = new CourseRecommendation.LearningPath(
            "Grammar",
            "4 weeks",
            List.of("Tip 1")
        );

        assertEquals("Grammar", path.getCurrentFocus());
        assertEquals("4 weeks", path.getEstimatedTime());
        assertEquals("Tip 1", path.getStudyTips().get(0));
    }

    @Test
    void learningPath_gettersAndSetters_work() {
        CourseRecommendation.LearningPath path = new CourseRecommendation.LearningPath();
        path.setCurrentFocus("Grammar");
        path.setEstimatedTime("4 weeks");
        path.setStudyTips(List.of("Tip 1"));

        assertEquals("Grammar", path.getCurrentFocus());
        assertEquals("4 weeks", path.getEstimatedTime());
        assertEquals("Tip 1", path.getStudyTips().get(0));
    }

    @Test
    void learningPath_equalsAndHashCode_identicalObjects() {
        CourseRecommendation.LearningPath left = new CourseRecommendation.LearningPath(
            "Grammar",
            "4 weeks",
            List.of("Tip 1")
        );
        CourseRecommendation.LearningPath right = new CourseRecommendation.LearningPath(
            "Grammar",
            "4 weeks",
            List.of("Tip 1")
        );

        assertTrue(left.equals(right));
        assertEquals(left.hashCode(), right.hashCode());
    }

    @Test
    void learningPath_equals_returnsFalseWhenCurrentFocusDiffers() {
        CourseRecommendation.LearningPath base = buildLearningPath();
        CourseRecommendation.LearningPath other = buildLearningPath();
        other.setCurrentFocus("Speaking");

        assertFalse(base.equals(other));
    }

    @Test
    void learningPath_equals_returnsFalseWhenEstimatedTimeDiffers() {
        CourseRecommendation.LearningPath base = buildLearningPath();
        CourseRecommendation.LearningPath other = buildLearningPath();
        other.setEstimatedTime("2 weeks");

        assertFalse(base.equals(other));
    }

    @Test
    void learningPath_equals_returnsFalseWhenStudyTipsDiffers() {
        CourseRecommendation.LearningPath base = buildLearningPath();
        CourseRecommendation.LearningPath other = buildLearningPath();
        other.setStudyTips(List.of("Tip 2"));

        assertFalse(base.equals(other));
    }

    @Test
    void learningPath_equals_returnsFalseForNullOrDifferentType() {
        CourseRecommendation.LearningPath path = buildLearningPath();

        assertFalse(path.equals(null));
        assertFalse(path.equals("not-a-path"));
    }

    @Test
    void learningPath_hashCode_differsWhenFieldDiffers() {
        CourseRecommendation.LearningPath base = buildLearningPath();
        CourseRecommendation.LearningPath other = buildLearningPath();
        other.setEstimatedTime("2 weeks");

        assertFalse(base.hashCode() == other.hashCode());
    }

    @Test
    void learningPath_toString_containsFields() {
        CourseRecommendation.LearningPath path = buildLearningPath();
        String output = path.toString();

        assertTrue(output.contains("currentFocus=Grammar"));
        assertTrue(output.contains("estimatedTime=4 weeks"));
    }

    @Test
    void learningPath_setters_acceptNulls() {
        CourseRecommendation.LearningPath path = buildLearningPath();
        path.setCurrentFocus(null);
        path.setEstimatedTime(null);
        path.setStudyTips(null);

        assertNull(path.getCurrentFocus());
        assertNull(path.getEstimatedTime());
        assertNull(path.getStudyTips());
    }

    private CourseRecommendation buildCourseRecommendation() {
        CourseRecommendation.RecommendedCourse course = buildRecommendedCourse();
        CourseRecommendation.NextLevelSuggestion nextLevel = new CourseRecommendation.NextLevelSuggestion(
            "A2",
            "Beginner",
            "Keep going",
            List.of(course)
        );
        CourseRecommendation.LearningPath learningPath = new CourseRecommendation.LearningPath(
            "Grammar",
            "4 weeks",
            List.of("Tip 1")
        );

        return new CourseRecommendation(
            "A1",
            "Starter",
            80,
            List.of(course),
            List.of("Vocabulary"),
            nextLevel,
            learningPath
        );
    }

    private CourseRecommendation.RecommendedCourse buildRecommendedCourse() {
        return new CourseRecommendation.RecommendedCourse(
            "Title",
            "Desc",
            List.of("topic1"),
            "2h",
            "A1"
        );
    }

    private CourseRecommendation.NextLevelSuggestion buildNextLevelSuggestion() {
        return new CourseRecommendation.NextLevelSuggestion(
            "B1",
            "Intermediate",
            "Nice",
            List.of(buildRecommendedCourse())
        );
    }

    private CourseRecommendation.LearningPath buildLearningPath() {
        return new CourseRecommendation.LearningPath(
            "Grammar",
            "4 weeks",
            List.of("Tip 1")
        );
    }
}
