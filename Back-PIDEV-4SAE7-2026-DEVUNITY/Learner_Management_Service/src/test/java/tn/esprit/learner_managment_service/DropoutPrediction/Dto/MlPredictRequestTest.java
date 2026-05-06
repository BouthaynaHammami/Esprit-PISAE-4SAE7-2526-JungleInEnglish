package tn.esprit.learner_managment_service.DropoutPrediction.Dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class MlPredictRequestTest {

    @Test
    void builder_setsBaseFields() {
        MlPredictRequest request = MlPredictRequest.builder()
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
            .build();

        assertEquals(4, request.getMotivationLevel());
        assertEquals(6.5, request.getWeeklyStudyHours());
        assertEquals("online", request.getPreferredLearningMode());
    }

    @Test
    void noArgsConstructor_allowsSetters() {
        MlPredictRequest request = new MlPredictRequest();
        request.setMotivationLevel(2);
        request.setEnglishLevelSelf("A2");

        assertEquals(2, request.getMotivationLevel());
        assertEquals("A2", request.getEnglishLevelSelf());
        assertNotNull(request.toString());
    }
}
