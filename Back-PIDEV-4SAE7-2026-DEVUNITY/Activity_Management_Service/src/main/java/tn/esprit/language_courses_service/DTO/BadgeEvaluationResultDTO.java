package tn.esprit.language_courses_service.DTO;

import lombok.*;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.Badge;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.StudentBadge;

import java.util.List;

/**
 * DTO for badge evaluation result
 * Contains newly earned badges and already owned badges
 * Returned after session completion for UI display
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BadgeEvaluationResultDTO {

    /**
     * Newly earned badges in this session
     */
    private List<Badge> newlyEarnedBadges;

    /**
     * All badges already owned by student
     */
    private List<StudentBadge> allOwnedBadges;

    /**
     * Total badges owned
     */
    private int totalBadgesOwned;

    /**
     * Total badges available in system
     */
    private int totalBadgesAvailable;

    /**
     * Student's current total score
     */
    private int totalScore;



    /**
     * Number of completed challenges
     */
    private long completedChallenges;
}
