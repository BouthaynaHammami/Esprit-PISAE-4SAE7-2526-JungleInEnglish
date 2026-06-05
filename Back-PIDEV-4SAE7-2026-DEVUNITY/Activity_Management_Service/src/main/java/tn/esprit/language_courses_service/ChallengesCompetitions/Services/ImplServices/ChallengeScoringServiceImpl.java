package tn.esprit.language_courses_service.ChallengesCompetitions.Services.ImplServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.language_courses_service.ChallengesCompetitions.DTO.ChallengeResultDTO;
import tn.esprit.language_courses_service.ChallengesCompetitions.DTO.ChallengeSubmissionDTO;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.AttemptStatus;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.Challenge;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.ChallengeAttempt;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.StudentBadge;
import tn.esprit.language_courses_service.ChallengesCompetitions.Repositories.ChallengeAttemptRepository;
import tn.esprit.language_courses_service.ChallengesCompetitions.Repositories.ChallengeRepository;
import tn.esprit.language_courses_service.ChallengesCompetitions.Services.IServices.IChallengeScoringService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service implementation for challenge scoring and result processing
 * Handles score calculation, persistence, and badge awarding
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ChallengeScoringServiceImpl implements IChallengeScoringService {

    private final ChallengeAttemptRepository attemptRepository;
    private final ChallengeRepository challengeRepository;
    private final BadgeServiceImpl badgeService;

    /**
     * Submit challenge score and create/update attempt record
     */
    @Override
    public ChallengeResultDTO submitChallengeScore(
            Long idUser,
            Long challengeId,
            ChallengeSubmissionDTO submission) {

        log.info("Processing challenge submission for user {} on challenge {}", idUser, challengeId);

        try {
            // Fetch challenge
            Challenge challenge = challengeRepository.findById(challengeId)
                    .orElseThrow(() -> new IllegalArgumentException("Challenge not found"));

            // Create or update attempt record
            ChallengeAttempt attempt = new ChallengeAttempt();
            attempt.setIdUser(idUser);
            attempt.setChallenge(challenge);
            attempt.setStartTime(LocalDateTime.now().minusSeconds(
                    challenge.getTimeLimitSeconds() != null ? challenge.getTimeLimitSeconds() : 180));
            attempt.setEndTime(LocalDateTime.now());
            attempt.setStatus(AttemptStatus.COMPLETED);
            attempt.setScore(submission.getScore());
            attempt.setProgress(calculateProgress(submission.getCorrectAnswers(), submission.getWrongAnswers()));
            attempt.setAttemptsUsed(submission.getAttemptsUsed());

            // Save attempt
            ChallengeAttempt savedAttempt = attemptRepository.save(attempt);
            log.info("Attempt saved with ID: {}", savedAttempt.getId());

            // Calculate points awarded
            Integer pointsAwarded = calculatePointsAwarded(
                    submission.getScore(),
                    submission.getCorrectAnswers(),
                    submission.getWrongAnswers(),
                    submission.getBonus());

            // Check and award badges
            String[] badgesEarned = checkAndAwardBadges(idUser, submission.getScore());

            // Process attempt completion (update user profile, points, etc.)
            processCompletedAttempt(savedAttempt, pointsAwarded);

            return ChallengeResultDTO.builder()
                    .sessionId(savedAttempt.getId())
                    .message("Challenge completed successfully")
                    .success(true)
                    .score(submission.getScore())
                    .pointsAwarded(pointsAwarded)
                    .badgesEarned(badgesEarned)
                    .build();

        } catch (Exception e) {
            log.error("Error submitting challenge score", e);
            return ChallengeResultDTO.builder()
                    .success(false)
                    .message("Error processing challenge submission: " + e.getMessage())
                    .score(0)
                    .build();
        }
    }

    /**
     * Calculate points awarded based on performance
     * Formula: Base score * multiplier + bonus
     */
    @Override
    public Integer calculatePointsAwarded(
            Integer score,
            Integer correctAnswers,
            Integer wrongAnswers,
            Integer bonus) {

        // Base points per correct answer: 10 points
        int basePoints = correctAnswers * 10;

        // Penalty for wrong answers: 2 points each
        int penalty = wrongAnswers * 2;

        // Net points
        int netPoints = Math.max(0, basePoints - penalty);

        // Time bonus (1 point per 2 seconds, already calculated on frontend)
        int totalPoints = netPoints + (bonus != null ? bonus : 0);

        return totalPoints;
    }

    /**
     * Check and award badges based on performance
     */
    @Override
    public String[] checkAndAwardBadges(Long idUser, Integer score) {
        List<String> earnedBadges = new ArrayList<>();

        try {
            // Perfect Score Badge (score >= 100)
            if (score >= 100) {
                earnedBadges.add("Perfect Score");
            }

            // Speed Demon Badge (completed in less than 1 minute = high points in short time)
            if (score >= 50) {
                earnedBadges.add("Speed Demon");
            }

            // For now, just log badge awarding
            // In a full implementation, you would call badgeService.awardBadge(idUser, badgeName)
            log.info("Badges earned for user {}: {}", idUser, earnedBadges);

        } catch (Exception e) {
            log.error("Error checking badges", e);
        }

        return earnedBadges.toArray(new String[0]);
    }

    /**
     * Process completed attempt and update student progress.
     * FIX Bug #2: persist the recalculated pointsAwarded into the attempt record.
     * The global score for leaderboard / badges is derived from StudentChallengeSession
     * via BadgeEvaluationServiceImpl.calculateTotalScore() — no separate Player table needed.
     */
    @Override
    public void processCompletedAttempt(ChallengeAttempt attempt, Integer pointsAwarded) {
        try {
            log.info("Processing completed attempt {} with {} points awarded", attempt.getId(), pointsAwarded);

            // Persist the real calculated score (not the raw frontend value)
            attempt.setScore(pointsAwarded);
            attemptRepository.save(attempt);

            log.info("Attempt {} updated: score set to {}", attempt.getId(), pointsAwarded);
        } catch (Exception e) {
            log.error("Error processing completed attempt {}: {}", attempt.getId(), e.getMessage(), e);
        }
    }

    /**
     * Calculate progress (accuracy) as a real percentage of correct / total answered.
     * FIX Bug #5: replaced arbitrary total*10 formula with actual accuracy ratio.
     */
    private Integer calculateProgress(Integer correctAnswers, Integer wrongAnswers) {
        int total = correctAnswers + wrongAnswers;
        if (total == 0) {
            return 0;
        }
        return (int) Math.round((correctAnswers * 100.0) / total);
    }
}
