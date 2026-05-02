package tn.esprit.language_courses_service.ChallengesCompetitions.Services.IServices;

import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.StudentBadge;
import tn.esprit.language_courses_service.DTO.BadgeEvaluationResultDTO;

import java.util.List;

/**
 * Service interface for badge evaluation and assignment
 * Handles automatic badge awarding based on student achievements
 */
public interface IBadgeEvaluationService {

    /**
     * Evaluate all badges for a student after completing a challenge session
     * Checks all badge criteria and assigns newly earned badges
     * 
     * @param userId - Student ID
     * @param sessionId - Completed session ID (for context)
     * @return BadgeEvaluationResultDTO with newly earned + already owned badges
     */
    BadgeEvaluationResultDTO evaluateBadgesForUser(Long userId, Long sessionId);

    /**
     * Check if a student meets criteria for a specific badge
     * 
     * Rules examples:
     * - Total score >= X
     * - Completed N challenges
     * - Accuracy >= Y%
     * - Challenge streak
     * - Type mastery (MYSTERY_WORD / SENTENCE_BUILDER / EMOJI_WORD)
     * 
     * @param userId - Student ID
     * @param badgeId - Badge ID to check
     * @return true if criteria met and not already owned
     */
    boolean isEligibleForBadge(Long userId, Long badgeId);

    /**
     * Manually assign a badge to a student (admin operation)
     * Avoids duplicates
     * 
     * @param userId - Student ID
     * @param badgeId - Badge ID
     * @return The created StudentBadge or existing if already owned
     */
    StudentBadge assignBadgeToStudent(Long userId, Long badgeId);

    /**
     * Get all badges earned by a student
     * 
     * @param userId - Student ID
     * @return List of StudentBadges
     */
    List<StudentBadge> getStudentBadges(Long userId);

    /**
     * Get newly earned badges for a student in a session
     * Used for UI display of newly earned achievements
     * 
     * @param userId - Student ID
     * @param sessionId - Session ID (optional, for context)
     * @return List of newly earned badges
     */
    List<StudentBadge> getNewlyEarnedBadges(Long userId, Long sessionId);

    /**
     * Calculate student's total score (aggregated from all sessions)
     * 
     * @param userId - Student ID
     * @return Total accumulated score
     */
    Integer calculateTotalScore(Long userId);

    /**
     * Count completed challenges by student
     * 
     * @param userId - Student ID
     * @return Number of completed challenges
     */
    long countCompletedChallenges(Long userId);

    /**
     * Check if student has mastery in a challenge type
     * Criteria: completed N challenges of type + accuracy >= 80%
     * 
     * @param userId - Student ID
     * @param challengeType - Challenge type (MYSTERY_WORD, SENTENCE_BUILDER, EMOJI_WORD)
     * @return true if criteria met
     */
    boolean hasTypeMastery(Long userId, String challengeType);
}
