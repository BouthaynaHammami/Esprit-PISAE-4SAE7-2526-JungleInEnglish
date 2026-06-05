package tn.esprit.language_courses_service.ChallengesCompetitions.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.Badge;
import tn.esprit.language_courses_service.DTO.ChallengeAttemptDTO;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeResponseDTO {
    private ChallengeAttemptDTO challengeAttempt;
    private Integer totalScore;
    private List<Badge> newBadges;
    private List<Badge> allBadges;
}
