package tn.esprit.LevelTest.Dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;

import org.junit.jupiter.api.Test;

class LevelTestResultTest {

    @Test
    void allArgsConstructor_setsFields() {
        LevelTestResult result = new LevelTestResult("B2", 85, "Good", Map.of("detail", "value"));

        assertEquals("B2", result.getLevel());
        assertEquals(85, result.getScore());
        assertEquals("Good", result.getFeedback());
        assertEquals("value", result.getDetails().get("detail"));
    }

    @Test
    void settersAndGetters_work() {
        LevelTestResult result = new LevelTestResult();
        result.setLevel("A2");
        result.setScore(60);
        result.setFeedback("Ok");

        assertEquals("A2", result.getLevel());
        assertEquals(60, result.getScore());
        assertEquals("Ok", result.getFeedback());
        assertNotNull(result.toString());
    }
}
