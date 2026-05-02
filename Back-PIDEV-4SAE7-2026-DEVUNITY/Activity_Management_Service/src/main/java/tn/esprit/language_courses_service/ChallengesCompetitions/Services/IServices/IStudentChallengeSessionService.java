package tn.esprit.language_courses_service.ChallengesCompetitions.Services.IServices;

import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.StudentChallengeSession;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.ChallengeType;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.Level;
import tn.esprit.language_courses_service.DTO.StudentChallengeSessionDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing student challenge sessions (3-minute timed sessions)
 */
public interface IStudentChallengeSessionService {

    /**
     * Start a new challenge session
     * Loads available challenges of given type/level, shuffles them, and creates session
     * 
     * @param userId Student user ID
     * @param type Challenge type (MYSTERY_WORD, SENTENCE_BUILDER, EMOJI_WORD)
     * @param level Challenge level (A1, A2, B1, B2, C1, C2)
     * @return Created session DTO
     * @throws IllegalArgumentException if no challenges available
     * @throws IllegalStateException if user already has active session
     */
    StudentChallengeSessionDTO startSession(Long userId, ChallengeType type, Level level);

    /**
     * Get active session for user
     */
    Optional<StudentChallengeSessionDTO> getActiveSession(Long userId);

    /**
     * Get session by ID
     */
    Optional<StudentChallengeSessionDTO> getSessionById(Long sessionId);

    /**
     * Get all sessions for a user (paginated)
     */
    List<StudentChallengeSessionDTO> getUserSessions(Long userId);

    /**
     * Get current challenge ID from session
     */
    Optional<Long> getCurrentChallengeId(Long sessionId);

    /**
     * Advance to next challenge in session
     * Returns false if session is complete
     */
    boolean advanceChallenge(Long sessionId);

    /**
     * Submit answer for current challenge
     * Auto-advances to next challenge after validation
     * 
     * @param sessionId Session ID
     * @param challengeId Current challenge ID
     * @param answer User's answer
     * @param isCorrect Whether answer is correct
     * @return Updated session DTO
     */
    StudentChallengeSessionDTO submitAnswer(Long sessionId, Long challengeId, String answer, boolean isCorrect, int wrongAttempts);

    /**
     * Complete session and finalize score
     * 
     * @param sessionId - Session to complete
     * @return Final session state
     */
    StudentChallengeSessionDTO completeSession(Long sessionId);

    /**
     * Check and expire timed-out sessions
     * Called by @Scheduled task periodically
     */
    void checkAndExpireTimedOutSessions();
}
