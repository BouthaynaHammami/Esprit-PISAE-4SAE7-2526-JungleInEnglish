package tn.esprit.LevelTest.Dto;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;

class LevelTestResultTest {

    @Test
    void equals_returnsTrue_whenLevelNullOnBoth() {
        LevelTestResult left = baseLevelTestResult();
        LevelTestResult right = baseLevelTestResult();
        left.setLevel(null);
        right.setLevel(null);

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenLevelNullOnLeft() {
        LevelTestResult left = baseLevelTestResult();
        LevelTestResult right = baseLevelTestResult();
        left.setLevel(null);

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsTrue_whenLevelSameValue() {
        LevelTestResult left = baseLevelTestResult();
        LevelTestResult right = baseLevelTestResult();
        left.setLevel("B2");
        right.setLevel("B2");

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenLevelDiffers() {
        LevelTestResult left = baseLevelTestResult();
        LevelTestResult right = baseLevelTestResult();
        right.setLevel("C1");

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsTrue_whenScoreNullOnBoth() {
        LevelTestResult left = baseLevelTestResult();
        LevelTestResult right = baseLevelTestResult();
        left.setScore(null);
        right.setScore(null);

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenScoreNullOnLeft() {
        LevelTestResult left = baseLevelTestResult();
        LevelTestResult right = baseLevelTestResult();
        left.setScore(null);

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsTrue_whenScoreSameValue() {
        LevelTestResult left = baseLevelTestResult();
        LevelTestResult right = baseLevelTestResult();
        left.setScore(85);
        right.setScore(85);

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenScoreDiffers() {
        LevelTestResult left = baseLevelTestResult();
        LevelTestResult right = baseLevelTestResult();
        right.setScore(95);

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsTrue_whenFeedbackNullOnBoth() {
        LevelTestResult left = baseLevelTestResult();
        LevelTestResult right = baseLevelTestResult();
        left.setFeedback(null);
        right.setFeedback(null);

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenFeedbackNullOnLeft() {
        LevelTestResult left = baseLevelTestResult();
        LevelTestResult right = baseLevelTestResult();
        left.setFeedback(null);

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsTrue_whenFeedbackSameValue() {
        LevelTestResult left = baseLevelTestResult();
        LevelTestResult right = baseLevelTestResult();
        left.setFeedback("Good");
        right.setFeedback("Good");

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenFeedbackDiffers() {
        LevelTestResult left = baseLevelTestResult();
        LevelTestResult right = baseLevelTestResult();
        right.setFeedback("Great");

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsTrue_whenDetailsNullOnBoth() {
        LevelTestResult left = baseLevelTestResult();
        LevelTestResult right = baseLevelTestResult();
        left.setDetails(null);
        right.setDetails(null);

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenDetailsNullOnLeft() {
        LevelTestResult left = baseLevelTestResult();
        LevelTestResult right = baseLevelTestResult();
        left.setDetails(null);

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsTrue_whenDetailsSameValue() {
        LevelTestResult left = baseLevelTestResult();
        LevelTestResult right = baseLevelTestResult();
        Map<String, Object> details = Map.of("detail", "value");
        left.setDetails(details);
        right.setDetails(details);

        assertTrue(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenDetailsDiffers() {
        LevelTestResult left = baseLevelTestResult();
        LevelTestResult right = baseLevelTestResult();
        right.setDetails(Map.of("detail", "other"));

        assertFalse(left.equals(right));
    }

    @Test
    void equals_returnsFalse_whenComparedToNullOrDifferentType() {
        LevelTestResult value = baseLevelTestResult();

        assertFalse(value.equals(null));
        assertFalse(value.equals("not-a-result"));
    }

    @Test
    void hashCode_returnsSame_whenFieldsSame() {
        LevelTestResult left = baseLevelTestResult();
        LevelTestResult right = baseLevelTestResult();

        assertEquals(left.hashCode(), right.hashCode());
    }

    @Test
    void hashCode_doesNotThrow_whenFieldNull() {
        LevelTestResult value = baseLevelTestResult();
        value.setLevel(null);

        assertDoesNotThrow(value::hashCode);
    }

    @Test
    void hashCode_differs_whenLevelDiffers() {
        LevelTestResult left = baseLevelTestResult();
        LevelTestResult right = baseLevelTestResult();
        right.setLevel("C1");

        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    void hashCode_differs_whenScoreDiffers() {
        LevelTestResult left = baseLevelTestResult();
        LevelTestResult right = baseLevelTestResult();
        right.setScore(95);

        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    void hashCode_differs_whenFeedbackDiffers() {
        LevelTestResult left = baseLevelTestResult();
        LevelTestResult right = baseLevelTestResult();
        right.setFeedback("Great");

        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    void hashCode_differs_whenDetailsDiffers() {
        LevelTestResult left = baseLevelTestResult();
        LevelTestResult right = baseLevelTestResult();
        right.setDetails(Map.of("detail", "other"));

        assertNotEquals(left.hashCode(), right.hashCode());
    }

    private LevelTestResult baseLevelTestResult() {
        return new LevelTestResult("B2", 85, "Good", Map.of("detail", "value"));
    }
}