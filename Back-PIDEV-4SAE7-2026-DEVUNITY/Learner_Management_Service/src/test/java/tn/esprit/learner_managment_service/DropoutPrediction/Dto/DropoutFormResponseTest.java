package tn.esprit.learner_managment_service.DropoutPrediction.Dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class DropoutFormResponseTest {

    @Test
    void builder_setsFields() {
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 10, 0);
        DropoutFormResponse response = DropoutFormResponse.builder()
            .id(1L)
            .userId(10)
            .userEmail("user@example.com")
            .motivationLevel(4)
            .weeklyStudyHours(6.5)
            .freeTimeHoursPerWeek(5.0)
            .satisfactionLevel(3)
            .preferredLearningMode("online")
            .attendanceCommitment(4)
            .homeworkCompletionSelf(5)
            .financialStressLevel(2)
            .interactionWithTeacher(3)
            .englishLevelSelf("B1")
            .goalClarityLevel(4)
            .classDifficultyLevel(3)
            .peerInteractionLevel(2)
            .technicalIssuesFrequency(1)
            .predictedDropout("no")
            .predictedProbability(0.2)
            .modelName("model-v1")
            .createdAt(createdAt)
            .build();

        assertEquals(1L, response.getId());
        assertEquals(10, response.getUserId());
        assertEquals("user@example.com", response.getUserEmail());
        assertEquals("no", response.getPredictedDropout());
        assertEquals(createdAt, response.getCreatedAt());
    }

    @Test
    void settersAndGetters_work() {
        DropoutFormResponse response = new DropoutFormResponse();
        response.setMotivationLevel(3);
        response.setEnglishLevelSelf("A2");

        assertEquals(3, response.getMotivationLevel());
        assertEquals("A2", response.getEnglishLevelSelf());
        assertNotNull(response.toString());
    }
}
