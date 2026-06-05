package tn.esprit.language_courses_service.ChallengesCompetitions.Services.ImplServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.*;
import tn.esprit.language_courses_service.ChallengesCompetitions.Repositories.ChallengeRepository;
import tn.esprit.language_courses_service.ChallengesCompetitions.Repositories.StudentChallengeSessionRepository;
import tn.esprit.language_courses_service.ChallengesCompetitions.Services.IServices.IStudentChallengeSessionService;
import tn.esprit.language_courses_service.ChallengesCompetitions.Services.IServices.IBadgeEvaluationService;
import tn.esprit.language_courses_service.DTO.StudentChallengeSessionDTO;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class StudentChallengeSessionServiceImpl implements IStudentChallengeSessionService {

    private final StudentChallengeSessionRepository sessionRepository;
    private final ChallengeRepository challengeRepository;
    private final IBadgeEvaluationService badgeEvaluationService; // Inject badge service

    @Override
    public StudentChallengeSessionDTO startSession(Long userId, ChallengeType type, Level level) {
        log.info("Starting session for user {} with type={}, level={}", userId, type, level);

        // Check if user already has active session
        Optional<StudentChallengeSession> existingSession = sessionRepository.findActiveSessionByUser(userId);
        if (existingSession.isPresent()) {
            StudentChallengeSession oldSession = existingSession.get();
            log.warn("User {} already has active session. Expiring old session {}", userId, oldSession.getId());
            oldSession.setStatus(SessionStatus.EXPIRED);
            oldSession.setSessionEndTime(LocalDateTime.now());
            sessionRepository.save(oldSession);
        }

        // ===== STEP 1: Load & Shuffle Challenges =====
        List<Challenge> availableChallenges = challengeRepository.findAvailable(
            LocalDate.now(),
            level,
            type
        );

        if (availableChallenges.isEmpty()) {
            throw new IllegalArgumentException(
                "No challenges available for type=" + type + ", level=" + level
            );
        }

        log.debug("Found {} challenges. Shuffling...", availableChallenges.size());

        // Shuffle the challenges (Fisher-Yates algorithm)
        List<Long> challengeIds = availableChallenges.stream()
            .map(Challenge::getId)
            .collect(Collectors.toList());
        Collections.shuffle(challengeIds); // Shuffle on backend

        // ===== STEP 2: Create Session Entity =====
        StudentChallengeSession session = StudentChallengeSession.builder()
            .idUser(userId)
            .sessionType(type)
            .sessionLevel(level)
            .sessionStartTime(LocalDateTime.now())
            .sessionDurationSeconds(180L) // 3 minutes
            .status(SessionStatus.IN_PROGRESS)
            .challengeIds(challengeIds)
            .currentChallengeIndex(0)
            .totalScore(0)
            .correctAnswers(0)
            .wrongAnswers(0)
            .totalChallengesPlayed(0)
            .build();

        StudentChallengeSession savedSession = sessionRepository.save(session);
        log.info("Session started with ID={}, challenges={}", savedSession.getId(), challengeIds.size());

        return toDTO(savedSession);
    }

    @Override
    public Optional<StudentChallengeSessionDTO> getActiveSession(Long userId) {
        return sessionRepository.findActiveSessionByUser(userId)
            .map(this::toDTO);
    }

    @Override
    public Optional<StudentChallengeSessionDTO> getSessionById(Long sessionId) {
        return sessionRepository.findById(sessionId)
            .map(this::toDTO);
    }

    @Override
    public List<StudentChallengeSessionDTO> getUserSessions(Long userId) {
        return sessionRepository.findByIdUserOrderBySessionStartTimeDesc(userId)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Long> getCurrentChallengeId(Long sessionId) {
        Optional<StudentChallengeSession> session = sessionRepository.findById(sessionId);

        if (session.isEmpty()) {
            return Optional.empty();
        }

        StudentChallengeSession s = session.get();

        // Check if session is expired
        if (isSessionExpired(s)) {
            return Optional.empty();
        }

        // Check if we have any challenges
        if (s.getChallengeIds().isEmpty()) {
            return Optional.empty();
        }

        // Pick a random challenge from the available pool
        int randomIndex = new java.util.Random().nextInt(s.getChallengeIds().size());
        return Optional.of(s.getChallengeIds().get(randomIndex));
    }

    @Override
    public boolean advanceChallenge(Long sessionId) {
        StudentChallengeSession session = sessionRepository.findById(sessionId)
            .orElseThrow(() -> new IllegalArgumentException("Session not found"));

        if (session.getStatus() != SessionStatus.IN_PROGRESS) {
            return false;
        }

        // Move to next challenge
        session.setCurrentChallengeIndex(session.getCurrentChallengeIndex() + 1);

        // Check if time expired
        if (isSessionExpired(session)) {
            completeSession(sessionId);
            return false;
        }

        sessionRepository.save(session);
        return true;
    }

    @Override
    public StudentChallengeSessionDTO submitAnswer(Long sessionId, Long challengeId, String answer, boolean isCorrect, int wrongAttempts) {
        log.info("Submitting answer for session={}, challenge={}, correct={}, wrongAttempts={}", sessionId, challengeId, isCorrect, wrongAttempts);

        StudentChallengeSession session = sessionRepository.findById(sessionId)
            .orElseThrow(() -> new IllegalArgumentException("Session not found"));

        if (session.getStatus() != SessionStatus.IN_PROGRESS) {
            throw new IllegalStateException("Session is not in progress");
        }

        // ===== Update Score =====
        session.setTotalChallengesPlayed(session.getTotalChallengesPlayed() + 1);

        if (isCorrect) {
            session.setCorrectAnswers(session.getCorrectAnswers() + 1);
            session.setWrongAnswers(session.getWrongAnswers() + wrongAttempts);

            Challenge challenge = challengeRepository.findById(challengeId).orElse(null);
            int basePoints = (challenge != null && challenge.getPointsPerCorrectAnswer() != null)
                             ? challenge.getPointsPerCorrectAnswer() : 20;

            // Dynamic points: deduct 1 point per wrong attempt, never below 1 if correct
            int earnedPoints = Math.max(1, basePoints - wrongAttempts);
            session.setTotalScore(session.getTotalScore() + earnedPoints);

            log.debug("Answer correct! Earned {} points. Total score: {}, Total played: {}",
                earnedPoints, session.getTotalScore(), session.getTotalChallengesPlayed());
        } else {
            session.setWrongAnswers(session.getWrongAnswers() + wrongAttempts);
            // 0 points for wrong answer
            log.debug("Answer wrong. Total played: {}", session.getTotalChallengesPlayed());
        }

        // FIX Bug #4: advance the index directly on the SAME entity instance to avoid
        // a double-save race condition (advanceChallenge() would reload and save a separate instance).
        if (session.getStatus() == SessionStatus.IN_PROGRESS && !isSessionExpired(session)) {
            session.setCurrentChallengeIndex(session.getCurrentChallengeIndex() + 1);
        } else if (isSessionExpired(session)) {
            session.setStatus(SessionStatus.EXPIRED);
            session.setSessionEndTime(LocalDateTime.now());
        }

        // Single authoritative save
        StudentChallengeSession updatedSession = sessionRepository.save(session);
        return toDTO(updatedSession);
    }

    @Override
    public StudentChallengeSessionDTO completeSession(Long sessionId) {
        StudentChallengeSession session = sessionRepository.findById(sessionId)
            .orElseThrow(() -> new IllegalArgumentException("Session not found"));

        if (session.getStatus() == SessionStatus.COMPLETED) {
            return toDTO(session);
        }

        log.info("Completing session ID={}. Correct={}, Wrong={}, AccumulatedScore={}, Total={}",
            sessionId,
            session.getCorrectAnswers(),
            session.getWrongAnswers(),
            session.getTotalScore(),
            session.getTotalChallengesPlayed()
        );

        // Determine status (COMPLETED or EXPIRED)
        if (isSessionExpired(session)) {
            session.setStatus(SessionStatus.EXPIRED);
        } else {
            session.setStatus(SessionStatus.COMPLETED);
        }

        session.setSessionEndTime(LocalDateTime.now());

        // ===== FIX Bug #3: DO NOT overwrite the accumulated score =====
        // The score was already accumulated correctly during submitAnswer().
        // We only add a small time bonus on top of it.
        long timeElapsedSeconds = java.time.temporal.ChronoUnit.SECONDS.between(
            session.getSessionStartTime(),
            session.getSessionEndTime()
        );
        long timeRemainingSeconds = Math.max(0, session.getSessionDurationSeconds() - timeElapsedSeconds);

        // Time bonus: 1 bonus point every 5 seconds remaining (capped at 30)
        int timeBonus = (int) Math.min(30, timeRemainingSeconds / 5);
        int finalScore = session.getTotalScore() + timeBonus;
        session.setTotalScore(finalScore);

        log.info("Session {} finalized: accumulatedScore + timeBonus({}) = {}",
            sessionId, timeBonus, finalScore);

        StudentChallengeSession completedSession = sessionRepository.save(session);

        // ===== AUTO-EVALUATE BADGES AFTER SESSION COMPLETION =====
        try {
            log.info("Evaluating badges for user {} after session completion", session.getIdUser());
            badgeEvaluationService.evaluateBadgesForUser(session.getIdUser(), sessionId);
        } catch (Exception e) {
            log.error("Error evaluating badges for user {} after session {}", session.getIdUser(), sessionId, e);
        }

        return toDTO(completedSession);
    }

    @Override
    @Scheduled(fixedRate = 60000) // Check every minute
    public void checkAndExpireTimedOutSessions() {
        log.debug("Checking for timed-out sessions...");

        List<StudentChallengeSession> activeSessions = sessionRepository.findByStatus(SessionStatus.IN_PROGRESS);
        LocalDateTime now = LocalDateTime.now();

        for (StudentChallengeSession session : activeSessions) {
            if (isSessionExpired(session)) {
                log.info("Expiring session ID={} due to timeout. Final score preserved: {}",
                    session.getId(), session.getTotalScore());
                session.setStatus(SessionStatus.EXPIRED);
                session.setSessionEndTime(now);
                sessionRepository.save(session);
            }
        }
    }

    // ============ HELPER METHODS ============

    /**
     * Convert entity to DTO with calculated fields
     */
    private StudentChallengeSessionDTO toDTO(StudentChallengeSession session) {
        long timeElapsedSeconds = java.time.temporal.ChronoUnit.SECONDS.between(
            session.getSessionStartTime(),
            LocalDateTime.now()
        );

        long timeRemainingSeconds = Math.max(0, session.getSessionDurationSeconds() - timeElapsedSeconds);

        return StudentChallengeSessionDTO.builder()
            .id(session.getId())
            .idUser(session.getIdUser())
            .sessionType(session.getSessionType().name())
            .sessionLevel(session.getSessionLevel().name())
            .sessionStartTime(session.getSessionStartTime())
            .sessionEndTime(session.getSessionEndTime())
            .sessionDurationSeconds(session.getSessionDurationSeconds())
            .status(session.getStatus())
            .challengeIds(session.getChallengeIds())
            .currentChallengeIndex(session.getCurrentChallengeIndex())
            .totalScore(session.getTotalScore())
            .correctAnswers(session.getCorrectAnswers())
            .wrongAnswers(session.getWrongAnswers())
            .totalChallengesPlayed(session.getTotalChallengesPlayed())
            .timeRemainingSeconds(timeRemainingSeconds)
            .build();
    }

    /**
     * Check if session has exceeded 3 minutes
     */
    private boolean isSessionExpired(StudentChallengeSession session) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime deadline = session.getSessionStartTime().plusSeconds(session.getSessionDurationSeconds());
        return now.isAfter(deadline);
    }
}
