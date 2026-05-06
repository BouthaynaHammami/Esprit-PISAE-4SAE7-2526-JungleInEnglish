package tn.esprit.LevelTest.Dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;

class LevelTestResultTest {

    @Test
    void noArgsConstructor_hasNullFields() {
        LevelTestResult result = new LevelTestResult();

        assertNull(result.getLevel());
        assertNull(result.getScore());
        assertNull(result.getFeedback());
        assertNull(result.getDetails());
    }

    @Test
    void allArgsConstructor_setsFields() {
        LevelTestResult result = new LevelTestResult("B2", 85, "Good", Map.of("detail", "value"));

        assertEquals("B2", result.getLevel());
        assertEquals(85, result.getScore());
        assertEquals("Good", result.getFeedback());
        assertEquals("value", result.getDetails().get("detail"));
    }

    @Test
    void gettersAndSetters_work() {
        LevelTestResult result = new LevelTestResult();
        result.setLevel("A2");
        result.setScore(60);
        result.setFeedback("Ok");
        result.setDetails(Map.of("key", "value"));

        assertEquals("A2", result.getLevel());
        assertEquals(60, result.getScore());
        assertEquals("Ok", result.getFeedback());
        assertEquals("value", result.getDetails().get("key"));
    }

    @Test
    void equalsAndHashCode_identicalObjects() {
        LevelTestResult left = new LevelTestResult("B2", 85, "Good", Map.of("detail", "value"));
        LevelTestResult right = new LevelTestResult("B2", 85, "Good", Map.of("detail", "value"));

        assertTrue(left.equals(right));
        assertEquals(left.hashCode(), right.hashCode());
    }

    @Test
    void equals_returnsFalseWhenLevelDiffers() {
        LevelTestResult base = buildLevelTestResult();
        LevelTestResult other = buildLevelTestResult();
        other.setLevel("C1");

        assertFalse(base.equals(other));
    }

    @Test
    void equals_returnsFalseWhenScoreDiffers() {
        LevelTestResult base = buildLevelTestResult();
        LevelTestResult other = buildLevelTestResult();
        other.setScore(95);

        assertFalse(base.equals(other));
    }

    @Test
    void equals_returnsFalseWhenFeedbackDiffers() {
        LevelTestResult base = buildLevelTestResult();
        LevelTestResult other = buildLevelTestResult();
        other.setFeedback("Great");

        assertFalse(base.equals(other));
    }

    @Test
    void equals_returnsFalseWhenDetailsDiffers() {
        LevelTestResult base = buildLevelTestResult();
        LevelTestResult other = buildLevelTestResult();
        other.setDetails(Map.of("detail", "other"));

        assertFalse(base.equals(other));
    }

    @Test
    void equals_returnsFalseForNullOrDifferentType() {
        LevelTestResult result = buildLevelTestResult();

        assertFalse(result.equals(null));
        assertFalse(result.equals("not-a-result"));
    }

    @Test
    void hashCode_differsWhenFieldDiffers() {
        LevelTestResult base = buildLevelTestResult();
        LevelTestResult other = buildLevelTestResult();
        other.setFeedback("Great");

        assertFalse(base.hashCode() == other.hashCode());
    }

    @Test
    void toString_containsFieldValues() {
        LevelTestResult result = buildLevelTestResult();
        String output = result.toString();

        assertTrue(output.contains("level=B2"));
        assertTrue(output.contains("score=85"));
        assertTrue(output.contains("feedback=Good"));
    }

    @Test
    void setters_acceptNulls() {
        LevelTestResult result = buildLevelTestResult();
        result.setLevel(null);
        result.setScore(null);
        result.setFeedback(null);
        result.setDetails(null);

        assertNull(result.getLevel());
        assertNull(result.getScore());
        assertNull(result.getFeedback());
        assertNull(result.getDetails());
        assertNotNull(result.toString());
    }

    private LevelTestResult buildLevelTestResult() {
        return new LevelTestResult("B2", 85, "Good", Map.of("detail", "value"));
    }
}
