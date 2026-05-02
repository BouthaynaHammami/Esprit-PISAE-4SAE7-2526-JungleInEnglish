package tn.esprit.language_courses_service.ChallengesCompetitions.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.language_courses_service.ChallengesCompetitions.Services.IServices.IBadgeEvaluationService;
import tn.esprit.language_courses_service.DTO.BadgeEvaluationResultDTO;

import java.util.List;

/**
 * Controller for badge evaluation endpoints
 * Handles automatic badge assignment after challenge completions
 */
@RestController
@RequestMapping("/badges")
@RequiredArgsConstructor
public class BadgeEvaluationController {

    private final IBadgeEvaluationService badgeEvaluationService;

    /**
     * Evaluate and assign badges for a student after session completion
     * POST /badges/evaluate/{userId}?sessionId=123
     * 
     * Called by frontend after completeSession() to:
     * 1. Evaluate all badge criteria
     * 2. Assign newly earned badges
     * 3. Return newly earned + already owned badges for UI display
     * 
     * @param userId - Student ID
     * @param sessionId - Completed session ID (optional, for context)
     * @return BadgeEvaluationResultDTO with newly earned + all owned badges
     */
    @PostMapping("/evaluate/{userId}")
    public ResponseEntity<BadgeEvaluationResultDTO> evaluateAndAssignBadges(
            @PathVariable Long userId,
            @RequestParam(required = false) Long sessionId) {
        
        BadgeEvaluationResultDTO result = badgeEvaluationService.evaluateBadgesForUser(userId, sessionId);
        return ResponseEntity.ok(result);
    }

    /**
     * Get all badges owned by a student (without re-evaluation)
     * GET /badges/student/{userId}
     * 
     * @param userId - Student ID
     * @return List of owned StudentBadges with details
     */
    @GetMapping("/student/{userId}")
    public ResponseEntity<BadgeEvaluationResultDTO> getStudentBadges(@PathVariable Long userId) {
        int totalScore = badgeEvaluationService.calculateTotalScore(userId);
        long completedChallenges = badgeEvaluationService.countCompletedChallenges(userId);

        BadgeEvaluationResultDTO result = BadgeEvaluationResultDTO.builder()
            .newlyEarnedBadges(List.of())
            .allOwnedBadges(badgeEvaluationService.getStudentBadges(userId))
            .totalBadgesOwned((int) badgeEvaluationService.getStudentBadges(userId).size())
            .totalScore(totalScore)
            .completedChallenges(completedChallenges)
            .build();

        return ResponseEntity.ok(result);
    }

    /**
     * Get student's progress stats (for UI dashboard)
     * GET /badges/stats/{userId}
     * 
     * @param userId - Student ID
     * @return Student stats: total score, accuracy, completed challenges, badges earned
     */
    @GetMapping("/stats/{userId}")
    public ResponseEntity<BadgeEvaluationResultDTO> getStudentStats(@PathVariable Long userId) {
        int totalScore = badgeEvaluationService.calculateTotalScore(userId);
        long completedChallenges = badgeEvaluationService.countCompletedChallenges(userId);
        var ownedBadges = badgeEvaluationService.getStudentBadges(userId);

        BadgeEvaluationResultDTO result = BadgeEvaluationResultDTO.builder()
            .newlyEarnedBadges(List.of())
            .allOwnedBadges(ownedBadges)
            .totalBadgesOwned(ownedBadges.size())
            .totalScore(totalScore)
            .completedChallenges(completedChallenges)
            .build();

        return ResponseEntity.ok(result);
    }

    /**
     * Check if student is eligible for a specific badge
     * GET /badges/eligible/{userId}/{badgeId}
     * 
     * @param userId - Student ID
     * @param badgeId - Badge ID to check
     * @return true/false if eligible
     */
    @GetMapping("/eligible/{userId}/{badgeId}")
    public ResponseEntity<Boolean> isEligibleForBadge(
            @PathVariable Long userId,
            @PathVariable Long badgeId) {
        
        boolean eligible = badgeEvaluationService.isEligibleForBadge(userId, badgeId);
        return ResponseEntity.ok(eligible);
    }

    /**
     * Manually assign badge to student (admin operation)
     * POST /badges/assign/{userId}/{badgeId}
     * 
     * @param userId - Student ID
     * @param badgeId - Badge ID to assign
     * @return Success message
     */
    @PostMapping("/assign/{userId}/{badgeId}")
    public ResponseEntity<String> assignBadge(
            @PathVariable Long userId,
            @PathVariable Long badgeId) {
        
        try {
            badgeEvaluationService.assignBadgeToStudent(userId, badgeId);
            return ResponseEntity.ok("Badge assigned successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
