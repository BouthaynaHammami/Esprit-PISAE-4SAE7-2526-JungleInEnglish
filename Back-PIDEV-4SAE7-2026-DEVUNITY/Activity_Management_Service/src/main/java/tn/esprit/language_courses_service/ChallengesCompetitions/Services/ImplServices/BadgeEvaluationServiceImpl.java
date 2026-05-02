package tn.esprit.language_courses_service.ChallengesCompetitions.Services.ImplServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.*;
import tn.esprit.language_courses_service.ChallengesCompetitions.Repositories.*;
import tn.esprit.language_courses_service.ChallengesCompetitions.Services.IServices.IBadgeEvaluationService;
import tn.esprit.language_courses_service.DTO.BadgeEvaluationResultDTO;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for automatic badge evaluation and assignment
 * 
 * Badge Attribution Rules:
 * 1. Total Score Rule: Award badges when student reaches score thresholds
 * 2. Challenge Completion: Award badge after N completed challenges
 * 3. Type Mastery: Award badge for proficiency in challenge type
 * 4. Streak: Award badge for consecutive correct answers
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BadgeEvaluationServiceImpl implements IBadgeEvaluationService {

    private final StudentBadgeRepository studentBadgeRepository;
    private final BadgeRepository badgeRepository;
    private final StudentChallengeSessionRepository sessionRepository;
    private final ChallengeAttemptRepository attemptRepository;

    /**
     * MAIN: Evaluate all badges for a student after session completion
     */
    @Override
    public BadgeEvaluationResultDTO evaluateBadgesForUser(Long userId, Long sessionId) {
        log.info("Evaluating badges for user {} after session {}", userId, sessionId);

        // Get all available badges
        List<Badge> allBadges = badgeRepository.findAll();
        List<Badge> newlyEarned = new ArrayList<>();

        // Check each badge
        for (Badge badge : allBadges) {
            if (isEligibleForBadge(userId, badge.getIdBadge())) {
                // Try to assign
                StudentBadge assigned = assignBadgeToStudent(userId, badge.getIdBadge());
                if (assigned != null) {
                    newlyEarned.add(badge);
                    log.info("Badge {} awarded to user {}", badge.getIdBadge(), userId);
                }
            }
        }

        // Get student stats
        int totalScore = calculateTotalScore(userId);
        long completedChallenges = countCompletedChallenges(userId);
        List<StudentBadge> allOwned = getStudentBadges(userId);

        return BadgeEvaluationResultDTO.builder()
            .newlyEarnedBadges(newlyEarned)
            .allOwnedBadges(allOwned)
            .totalBadgesOwned(allOwned.size())
            .totalBadgesAvailable(allBadges.size())
            .totalScore(totalScore)
            .completedChallenges(completedChallenges)
            .build();
    }

    /**
     * Check if student is eligible for a badge
     * Implements all badge criteria rules
     */
    @Override
    public boolean isEligibleForBadge(Long userId, Long badgeId) {
        // Check if already owned
        Optional<StudentBadge> existing = studentBadgeRepository.findByIdUserAndBadge_IdBadge(userId, badgeId);
        if (existing.isPresent()) {
            log.debug("User {} already owns badge {}", userId, badgeId);
            return false;
        }

        Optional<Badge> badgeOpt = badgeRepository.findById(badgeId);
        if (badgeOpt.isEmpty()) {
            return false;
        }

        Badge badge = badgeOpt.get();
        int pointsRequired = badge.getPointsRequired();

        log.debug("Checking badge {} (pointsRequired={}) for user {}", badgeId, pointsRequired, userId);

        // ===== RULE 1: TOTAL SCORE =====
        // Badge threshold based on pointsRequired
        // Examples:
        // - 30 points = "Starter" badge (first badge)
        // - 50 points = "Advanced" badge
        // - 80 points = "Legend" badge
        int totalScore = calculateTotalScore(userId);
        if (totalScore >= pointsRequired) {
            log.debug("User {} eligible for badge {} via SCORE RULE (score={}, required={})", 
                userId, badgeId, totalScore, pointsRequired);
            return true;
        }

        // ===== RULE 2: CHALLENGE COMPLETION =====
        // Award badge after completing N challenges
        // Mapping: pointsRequired as proxy for required completions
        // E.g., badge.pointsRequired=5 -> need 5 completions
        long completedChallenges = countCompletedChallenges(userId);
        if (completedChallenges >= pointsRequired / 10.0) { // E.g., 50 points = 5 challenges
            log.debug("User {} eligible for badge {} via COMPLETION RULE (completed={})", 
                userId, badgeId, completedChallenges);
            return true;
        }



        // ===== RULE 4: CHALLENGE TYPE MASTERY =====
        // Check for MYSTERY_WORD mastery
        if (hasTypeMastery(userId, "MYSTERY_WORD")) {
            log.debug("User {} eligible for badge {} via MYSTERY_WORD MASTERY", userId, badgeId);
            return true;
        }

        // Check for SENTENCE_BUILDER mastery
        if (hasTypeMastery(userId, "SENTENCE_BUILDER")) {
            log.debug("User {} eligible for badge {} via SENTENCE_BUILDER MASTERY", userId, badgeId);
            return true;
        }

        // Check for EMOJI_WORD mastery
        if (hasTypeMastery(userId, "EMOJI_WORD")) {
            log.debug("User {} eligible for badge {} via EMOJI_WORD MASTERY", userId, badgeId);
            return true;
        }

        log.debug("User {} not eligible for badge {}", userId, badgeId);
        return false;
    }

    /**
     * Assign a badge to a student (create StudentBadge record)
     * Avoids duplicates via unique constraint
     */
    @Override
    public StudentBadge assignBadgeToStudent(Long userId, Long badgeId) {
        // Check if already owned
        Optional<StudentBadge> existing = studentBadgeRepository.findByIdUserAndBadge_IdBadge(userId, badgeId);
        if (existing.isPresent()) {
            log.debug("Badge {} already assigned to user {}", badgeId, userId);
            return existing.get();
        }

        // Find badge
        Badge badge = badgeRepository.findById(badgeId)
            .orElseThrow(() -> new IllegalArgumentException("Badge not found: " + badgeId));

        // Create new StudentBadge
        StudentBadge studentBadge = StudentBadge.builder()
            .idUser(userId)
            .badge(badge)
            .dateAcquisition(LocalDateTime.now())
            .scoreAtAcquisition(calculateTotalScore(userId))
            .build();

        StudentBadge saved = studentBadgeRepository.save(studentBadge);
        log.info("Badge {} assigned to user {} on {}", badgeId, userId, saved.getDateAcquisition());
        return saved;
    }

    /**
     * Get all badges earned by student
     */
    @Override
    public List<StudentBadge> getStudentBadges(Long userId) {
        return studentBadgeRepository.findByIdUserOrderByDateAcquisitionDesc(userId);
    }

    /**
     * Get newly earned badges (placeholder for now)
     * In production, track sessionId that triggered badge
     */
    @Override
    public List<StudentBadge> getNewlyEarnedBadges(Long userId, Long sessionId) {
        if (sessionId == null) {
            return List.of();
        }
        return studentBadgeRepository.findByIdUserOrderByDateAcquisitionDesc(userId)
            .stream()
            .filter(sb -> sb.getSessionIdThatTriggeredBadge() != null && 
                         sb.getSessionIdThatTriggeredBadge().equals(sessionId))
            .collect(Collectors.toList());
    }

    /**
     * Calculate total score from all completed sessions
     * Score = sum of totalScore from all COMPLETED sessions
     */
    @Override
    public Integer calculateTotalScore(Long userId) {
        // Only use StudentChallengeSession for global score, ignore legacy ChallengeAttempts
        List<StudentChallengeSession> sessions = sessionRepository.findByIdUserOrderBySessionStartTimeDesc(userId);
        return sessions.stream()
            .filter(s -> s.getStatus() == SessionStatus.COMPLETED || s.getStatus() == SessionStatus.EXPIRED)
            .mapToInt(StudentChallengeSession::getTotalScore)
            .sum();
    }



    /**
     * Count completed challenges by student
     */
    @Override
    public long countCompletedChallenges(Long userId) {
        return sessionRepository.findByIdUserOrderBySessionStartTimeDesc(userId)
            .stream()
            .filter(s -> s.getStatus() == SessionStatus.COMPLETED)
            .mapToLong(StudentChallengeSession::getTotalChallengesPlayed)
            .sum();
    }

    /**
     * Check if student has mastery in a challenge type
     * Criteria: completed 5+ of type
     */
    @Override
    public boolean hasTypeMastery(Long userId, String challengeType) {
        List<StudentChallengeSession> sessions = sessionRepository.findByIdUserOrderBySessionStartTimeDesc(userId);
        
        // Filter sessions by type
        List<StudentChallengeSession> typeSessions = sessions.stream()
            .filter(s -> s.getSessionType().name().equals(challengeType))
            .filter(s -> s.getStatus() == SessionStatus.COMPLETED)
            .collect(Collectors.toList());

        if (typeSessions.size() < 5) {
            log.debug("User {} needs {} more {} sessions for mastery", 
                userId, 5 - typeSessions.size(), challengeType);
            return false;
        }

        boolean hasMastery = true;
        log.debug("User {} {} mastery for {} (sessions={})", 
            userId, hasMastery ? "HAS" : "NOT HAS", challengeType, typeSessions.size());
        
        return hasMastery;
    }
}
