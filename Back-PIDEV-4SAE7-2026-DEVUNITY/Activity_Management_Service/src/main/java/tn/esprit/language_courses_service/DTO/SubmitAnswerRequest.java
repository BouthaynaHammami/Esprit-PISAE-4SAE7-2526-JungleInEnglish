package tn.esprit.language_courses_service.DTO;

import lombok.*;

/**
 * DTO for submitting an answer in a challenge session
 * Used in POST /sessions/{sessionId}/submit
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitAnswerRequest {

    /**
     * ID of the challenge being answered
     */
    private Long challengeId;

    /**
     * Student's answer (answer string to check against correct answer)
     */
    private String answer;

    /**
     * Whether the answer is correct (pre-evaluated by frontend)
     * Frontend should validate and set this based on correctAnswer field
     */
    @com.fasterxml.jackson.annotation.JsonProperty("isCorrect")
    private boolean isCorrect;

    /**
     * Number of wrong attempts made for this challenge before this submission
     */
    private int wrongAttempts;
}
