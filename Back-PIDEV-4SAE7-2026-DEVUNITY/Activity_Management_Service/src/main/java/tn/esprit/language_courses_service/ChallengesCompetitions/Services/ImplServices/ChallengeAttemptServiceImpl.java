package tn.esprit.language_courses_service.ChallengesCompetitions.Services.ImplServices;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.AttemptStatus;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.Challenge;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.ChallengeAttempt;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.ChallengeType;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.Level;
import tn.esprit.language_courses_service.ChallengesCompetitions.Repositories.ChallengeAttemptRepository;
import tn.esprit.language_courses_service.ChallengesCompetitions.Repositories.ChallengeRepository;
import tn.esprit.language_courses_service.ChallengesCompetitions.Services.IServices.IChallengeAttemptService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChallengeAttemptServiceImpl implements IChallengeAttemptService {

    private final ChallengeAttemptRepository attemptRepository;
    private final ChallengeRepository challengeRepository;

    @Override
    public ChallengeAttempt startAttempt(Long idUser, Long challengeId) {
        // Check if user already has an active attempt for this challenge
        Optional<ChallengeAttempt> existingAttempt = attemptRepository.findByIdUserAndChallengeIdAndStatus(
            idUser, challengeId, AttemptStatus.IN_PROGRESS);
        if (existingAttempt.isPresent()) {
            throw new IllegalStateException("User already has an active attempt for this challenge");
        }

        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new IllegalArgumentException("Challenge not found"));

        // Validate challenge is currently active
        java.time.LocalDate today = java.time.LocalDate.now();
        boolean isActive = (challenge.getStartDate() == null || challenge.getStartDate().compareTo(today) <= 0)
                && (challenge.getEndDate() == null || challenge.getEndDate().compareTo(today) >= 0);
        if (!isActive) {
            throw new IllegalArgumentException("Challenge is not currently active");
        }

        if (!isTimedType(challenge.getType())) {
            throw new IllegalArgumentException("This challenge type does not use the timed attempt flow");
        }

        if (challenge.getTimeLimitSeconds() == null) {
            throw new IllegalArgumentException("Time limit is required for this challenge type");
        }

        List<ChallengeAttempt> previousAttempts = attemptRepository.findByIdUserAndChallengeId(idUser, challengeId);
        if (challenge.getMaxAttempts() != null && previousAttempts.size() >= challenge.getMaxAttempts()) {
            throw new IllegalStateException("Max attempts reached for this challenge");
        }

        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime deadlineTime = startTime.plusSeconds(challenge.getTimeLimitSeconds());

        ChallengeAttempt attempt = ChallengeAttempt.builder()
                .idUser(idUser)
                .challenge(challenge)
                .startTime(startTime)
                .deadlineTime(deadlineTime)
                .status(AttemptStatus.IN_PROGRESS)
                .attemptsUsed(previousAttempts.size() + 1)
                .build();

        return attemptRepository.save(attempt);
    }

    @Override
    public ChallengeAttempt startAttemptByType(Long idUser, ChallengeType type, Level level) {
        if (!isTimedType(type)) {
            throw new IllegalArgumentException("This challenge type does not use the timed attempt flow");
        }

        // Level can be null - in that case, find any available challenge of that type
        Optional<Challenge> availableChallenge;
        if (level != null) {
            availableChallenge = challengeRepository.findFirstAvailableByTypeAndLevel(java.time.LocalDate.now(), type, level);
        } else {
            List<Challenge> available = challengeRepository.findAvailable(java.time.LocalDate.now(), null, type);
            availableChallenge = available.isEmpty() ? Optional.empty() : Optional.of(available.get(0));
        }

        if (availableChallenge.isEmpty()) {
            throw new IllegalArgumentException("No available challenge for type: " + type + (level != null ? " and level: " + level : ""));
        }

        return startAttempt(idUser, availableChallenge.get().getId());
    }

    @Override
    public Optional<ChallengeAttempt> getAttemptById(Long attemptId) {
        return attemptRepository.findById(attemptId);
    }

    /**
     * Optimized query: Get IN_PROGRESS attempt for specific user + challenge.
     * Single DB query instead of loading all user attempts.
     */
    @Override
    public Optional<ChallengeAttempt> getInProgressAttempt(Long userId, Long challengeId) {
        return attemptRepository.findByIdUserAndChallengeIdAndStatus(userId, challengeId, AttemptStatus.IN_PROGRESS);
    }

    @Override
    public List<ChallengeAttempt> getAttemptsByUser(Long idUser) {
        return attemptRepository.findByIdUser(idUser);
    }

    @Override
    public List<ChallengeAttempt> getAttemptsByChallenge(Long challengeId) {
        return attemptRepository.findByChallengeId(challengeId);
    }

    @Override
    public ChallengeAttempt updateAttempt(ChallengeAttempt attempt) {
        return attemptRepository.save(attempt);
    }

    @Override
    public void expireAttempt(Long attemptId) {
        ChallengeAttempt attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() -> new IllegalArgumentException("Attempt not found"));
        if (attempt.getStatus() != AttemptStatus.IN_PROGRESS) {
            throw new IllegalStateException("Attempt is not in progress");
        }
        attempt.setStatus(AttemptStatus.EXPIRED);
        attempt.setEndTime(LocalDateTime.now());
        attemptRepository.save(attempt);
    }

    @Override
    public void completeAttempt(Long attemptId, Integer score) {
        ChallengeAttempt attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() -> new IllegalArgumentException("Attempt not found"));
        if (attempt.getStatus() != AttemptStatus.IN_PROGRESS) {
            throw new IllegalStateException("Attempt is not in progress");
        }

        LocalDateTime now = LocalDateTime.now();
        if (attempt.getDeadlineTime() != null && now.isAfter(attempt.getDeadlineTime())) {
            attempt.setStatus(AttemptStatus.EXPIRED);
            attempt.setEndTime(now);
            attemptRepository.save(attempt);
            return;
        }

        attempt.setStatus(AttemptStatus.COMPLETED);
        attempt.setEndTime(now);
        attempt.setScore(score);
        attemptRepository.save(attempt);
    }

    @Override
    @Scheduled(fixedRate = 60000) // Check every minute
    public void checkAndExpireTimedOutAttempts() {
        List<ChallengeAttempt> activeAttempts = attemptRepository.findByStatus(AttemptStatus.IN_PROGRESS);
        LocalDateTime now = LocalDateTime.now();

        for (ChallengeAttempt attempt : activeAttempts) {
            if (attempt.getDeadlineTime() != null && now.isAfter(attempt.getDeadlineTime())) {
                expireAttempt(attempt.getId());
            }
        }
    }

    private boolean isTimedType(ChallengeType type) {
        return type == ChallengeType.MYSTERY_WORD
                || type == ChallengeType.SENTENCE_BUILDER
                || type == ChallengeType.EMOJI_WORD;
    }
}