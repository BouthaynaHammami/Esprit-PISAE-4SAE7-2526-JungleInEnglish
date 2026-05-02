package tn.esprit.language_courses_service.ChallengesCompetitions.DTO;

import lombok.*;

/**
 * DTO for challenge submission response
 * Sent back to the frontend after score submission
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChallengeResultDTO {
    
    private Long sessionId;
    private String message;
    private Boolean success;
    private Integer score;
    private Integer pointsAwarded;
    private String[] badgesEarned;
}
