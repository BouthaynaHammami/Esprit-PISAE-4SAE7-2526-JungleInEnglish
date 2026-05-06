package tn.esprit.learner_managment_service.DropoutPrediction.Entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import tn.esprit.learner_managment_service.UserManagement.Entities.User;

class DropoutFormTest {

    @Test
    void onCreate_setsCreatedAt() {
        DropoutForm form = new DropoutForm();
        form.onCreate();
        assertNotNull(form.getCreatedAt());
    }

    @Test
    void builder_setsFields() {
        User user = new User();
        user.setUserId(1);
        user.setEmail("user@example.com");

        DropoutForm form = DropoutForm.builder()
            .id(10L)
            .user(user)
            .motivationLevel(4)
            .weeklyStudyHours(6.0)
            .freeTimeHoursPerWeek(4.0)
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
            .createdAt(LocalDateTime.of(2024, 1, 1, 10, 0))
            .build();

        assertEquals(10L, form.getId());
        assertEquals(4, form.getMotivationLevel());
        assertEquals("no", form.getPredictedDropout());
    }
}
