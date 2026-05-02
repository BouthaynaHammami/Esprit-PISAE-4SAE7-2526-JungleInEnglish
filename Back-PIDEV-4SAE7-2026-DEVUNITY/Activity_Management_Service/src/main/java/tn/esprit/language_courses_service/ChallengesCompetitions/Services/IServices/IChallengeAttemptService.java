package tn.esprit.language_courses_service.ChallengesCompetitions.Services.IServices;

import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.ChallengeAttempt;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.ChallengeType;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.Level;

import java.util.List;
import java.util.Optional;

public interface IChallengeAttemptService {

    ChallengeAttempt startAttempt(Long idUser, Long challengeId);

    ChallengeAttempt startAttemptByType(Long idUser, ChallengeType type, Level level);

    Optional<ChallengeAttempt> getAttemptById(Long attemptId);

    /**
     * Optimized: Get in-progress attempt for user + challenge using single DB query
     * Avoids N+1 loading all attempts in memory
     */
    Optional<ChallengeAttempt> getInProgressAttempt(Long userId, Long challengeId);

    List<ChallengeAttempt> getAttemptsByUser(Long idUser);

    List<ChallengeAttempt> getAttemptsByChallenge(Long challengeId);

    ChallengeAttempt updateAttempt(ChallengeAttempt attempt);

    void expireAttempt(Long attemptId);

    void completeAttempt(Long attemptId, Integer score);

    void checkAndExpireTimedOutAttempts();
}