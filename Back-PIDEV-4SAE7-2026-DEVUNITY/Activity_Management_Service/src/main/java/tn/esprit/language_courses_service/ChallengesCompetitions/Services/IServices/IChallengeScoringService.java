package tn.esprit.language_courses_service.ChallengesCompetitions.Services.IServices;

import tn.esprit.language_courses_service.ChallengesCompetitions.DTO.ChallengeResultDTO;
import tn.esprit.language_courses_service.ChallengesCompetitions.DTO.ChallengeSubmissionDTO;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.ChallengeAttempt;

/**
 * Interface for challenge scoring and result processing
 */
public interface IChallengeScoringService {

    /**
     * Submit challenge score and persist the attempt
     *
     * @param idUser - User/Student ID
     * @param challengeId - Challenge ID
     * @param submission - Challenge submission data
     * @return Challenge result with session info
     */
    ChallengeResultDTO submitChallengeScore(Long idUser, Long challengeId, ChallengeSubmissionDTO submission);

    /**
     * Calculate points awarded based on score and other factors
     *
     * @param score - Base score from calculation
     * @param correctAnswers - Number of correct answers
     * @param wrongAnswers - Number of wrong answers
     * @param bonus - Time bonus points
     * @return Points awarded
     */
    Integer calculatePointsAwarded(Integer score, Integer correctAnswers, Integer wrongAnswers, Integer bonus);

    /**
     * Check and award badges based on score
     * 
     * @param idUser - Student ID
     * @param score - Challenge score
     * @return Array of badge names earned
     */
    String[] checkAndAwardBadges(Long idUser, Integer score);

    /**
     * Process completed attempt and update student progress
     *
     * @param attempt - Completed attempt entity
     * @param pointsAwarded - Points to award
     */
    void processCompletedAttempt(ChallengeAttempt attempt, Integer pointsAwarded);
}
