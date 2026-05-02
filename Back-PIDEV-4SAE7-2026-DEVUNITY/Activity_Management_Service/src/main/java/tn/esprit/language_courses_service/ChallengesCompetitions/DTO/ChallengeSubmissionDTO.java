package tn.esprit.language_courses_service.ChallengesCompetitions.DTO;

import lombok.*;

/**
 * DTO for submitting challenge results
 * Receives data from the frontend challenge component
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChallengeSubmissionDTO {
    
    private Integer score;
    private Integer correctAnswers;
    private Integer wrongAnswers;
    private Integer timeUsed;
    private Integer attemptsUsed;
    private Integer bonus;
    private String feedback;
}
