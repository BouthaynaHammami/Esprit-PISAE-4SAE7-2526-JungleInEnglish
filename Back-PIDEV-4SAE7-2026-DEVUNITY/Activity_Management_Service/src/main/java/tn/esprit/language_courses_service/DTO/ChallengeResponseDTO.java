package tn.esprit.language_courses_service.DTO;

import lombok.*;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.Badge;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeResponseDTO {
    private ChallengeAttemptDTO challengeAttempt;
    private Long idUser;
    private int totalScore;
    private List<Badge> newBadges;
    private List<Badge> allBadges;
}
