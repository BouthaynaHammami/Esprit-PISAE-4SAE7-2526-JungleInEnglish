package tn.esprit.language_courses_service.ChallengesCompetitions.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.ChallengeType;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.Level;
import tn.esprit.language_courses_service.ChallengesCompetitions.Services.IServices.IStudentChallengeSessionService;
import tn.esprit.language_courses_service.ChallengesCompetitions.Services.IServices.IBadgeEvaluationService;
import tn.esprit.language_courses_service.DTO.StudentChallengeSessionDTO;
import tn.esprit.language_courses_service.DTO.SubmitAnswerRequest;
import tn.esprit.language_courses_service.DTO.BadgeEvaluationResultDTO;

import java.util.List;

/**
 * Controller for 3-minute challenge session endpoints
 * Manages session lifecycle: start → submit answers → complete → finalize
 */
@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
public class StudentChallengeSessionController {

    private final IStudentChallengeSessionService sessionService;
    private final IBadgeEvaluationService badgeEvaluationService;

    /**
     * Start a new 3-minute session
     * POST /sessions/start?userId=1&type=MYSTERY_WORD&level=A1
     * 
     * Returns:
     * - 201 CREATED: Session started successfully
     * - 400 BAD_REQUEST: Invalid userId/type/level, or user already has active session
     * - 404 NOT_FOUND: No challenges available for this type/level
     * 
     * @param userId - Student ID
     * @param typeStr - Challenge type string (MYSTERY_WORD, SENTENCE_BUILDER, EMOJI_WORD)
     * @param levelStr - Difficulty level string (A1-C2)
     * @return StudentChallengeSessionDTO with session details and first challenge
     */
    @PostMapping("/start")
    public ResponseEntity<?> startSession(
            @RequestParam Long userId,
            @RequestParam("type") String typeStr,
            @RequestParam("level") String levelStr) {
        
        try {
            // Validate userId
            if (userId == null || userId <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid userId: must be positive");
            }

            // Validate and convert type
            ChallengeType type;
            try {
                type = ChallengeType.valueOf(typeStr.toUpperCase().trim());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid type: " + typeStr + ". Valid types: MYSTERY_WORD, SENTENCE_BUILDER, EMOJI_WORD, WORD_BATTLE_ROYALE, STORY_CHAIN, SPEED_TRANSLATION_RACE");
            }

            // Validate and convert level
            Level level;
            try {
                level = Level.valueOf(levelStr.toUpperCase().trim());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid level: " + levelStr + ". Valid levels: A1, A2, B1, B2, C1, C2");
            }

            StudentChallengeSessionDTO session = sessionService.startSession(userId, type, level);
            return ResponseEntity.status(HttpStatus.CREATED).body(session);

        } catch (IllegalStateException e) {
            // User already has active session
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            // No challenges available
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error starting session: " + e.getMessage());
        }
    }

    /**
     * Get active session for a user (if any)
     * GET /sessions/active?userId=1
     * 
     * @param userId - Student ID
     * @return StudentChallengeSessionDTO or null if no active session
     */
    @GetMapping("/active")
    public ResponseEntity<?> getActiveSession(@RequestParam Long userId) {
        try {
            var activeSession = sessionService.getActiveSession(userId);
            return ResponseEntity.ok(activeSession.orElse(null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error retrieving active session: " + e.getMessage());
        }
    }

    /**
     * Get session by ID
     * GET /sessions/{sessionId}
     * 
     * @param sessionId - Session ID
     * @return StudentChallengeSessionDTO
     */
    @GetMapping("/{sessionId}")
    public ResponseEntity<?> getSessionById(@PathVariable Long sessionId) {
        try {
            var session = sessionService.getSessionById(sessionId);
            return session.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error retrieving session: " + e.getMessage());
        }
    }

    /**
     * Get all sessions for a user (history)
     * GET /sessions/user/{userId}
     * 
     * @param userId - Student ID
     * @return List of StudentChallengeSessionDTOs
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserSessions(@PathVariable Long userId) {
        try {
            List<StudentChallengeSessionDTO> sessions = sessionService.getUserSessions(userId);
            return ResponseEntity.ok(sessions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error retrieving sessions: " + e.getMessage());
        }
    }

    /**
     * Submit an answer for current challenge in session
     * POST /sessions/{sessionId}/submit
     * 
     * Request body: SubmitAnswerRequest
     * {
     *   "challengeId": 1,
     *   "answer": "example",
     *   "isCorrect": true
     * }
     * 
     * Returns:
     * - 200 OK: Updated session with score incremented
     * - 400 BAD_REQUEST: Invalid session or not in progress
     * - 404 NOT_FOUND: Session not found
     * 
     * Score system: +3 for correct, +0 for wrong
     * 
     * @param sessionId - Current session ID
     * @param request - Answer submission details
     * @return Updated StudentChallengeSessionDTO with new score
     */
    @PostMapping("/{sessionId}/submit")
    public ResponseEntity<?> submitAnswer(
            @PathVariable Long sessionId,
            @RequestBody SubmitAnswerRequest request) {
        
        try {
            // Validate request
            if (request == null || request.getChallengeId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid request: challengeId required");
            }

            StudentChallengeSessionDTO updatedSession = sessionService.submitAnswer(
                sessionId,
                request.getChallengeId(),
                request.getAnswer(),
                request.isCorrect(),
                request.getWrongAttempts()
            );

            return ResponseEntity.ok(updatedSession);

        } catch (IllegalArgumentException e) {
            // Session not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            // Session not in progress
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error submitting answer: " + e.getMessage());
        }
    }

    /**
     * Complete session (marks as COMPLETED and finalizes scores)
     * POST /sessions/{sessionId}/complete
     * 
     * Automatically:
     * - Marks session as COMPLETED or EXPIRED
     * - Evaluates badges for student
     * - Returns final scores
     * 
     * @param sessionId - Session to complete
     * @return StudentChallengeSessionDTO with final state
     */
    @PostMapping("/{sessionId}/complete")
    public ResponseEntity<?> completeSession(@PathVariable Long sessionId) {
        try {
            StudentChallengeSessionDTO finalSession = sessionService.completeSession(sessionId);
            return ResponseEntity.ok(finalSession);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error completing session: " + e.getMessage());
        }
    }

    /**
     * Get badge evaluation result for a completed session
     * GET /sessions/{sessionId}/badges?userId=1
     * 
     * Called by frontend after completeSession() to get:
     * - Newly earned badges
     * - All owned badges
     * - Student stats (total score, accuracy, completed challenges)
     * 
     * @param sessionId - Completed session ID
     * @param userId - Student ID
     * @return BadgeEvaluationResultDTO
     */
    @GetMapping("/{sessionId}/badges")
    public ResponseEntity<?> getBadgeEvaluationResult(
            @PathVariable Long sessionId,
            @RequestParam Long userId) {
        
        try {
            // Get badge evaluation results
            // Note: The backend already evaluated badges in completeSession(),
            // This endpoint just retrieves and returns the results
            BadgeEvaluationResultDTO result = badgeEvaluationService.evaluateBadgesForUser(userId, sessionId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error evaluating badges: " + e.getMessage());
        }
    }

    /**
     * Update global student score
     * POST /sessions/{sessionId}/updateGlobalScore?userId=1
     * 
     * Adds session score to student's global cumulative score
     * (Placeholder for future implementation if needed for external score tracking)
     * 
     * @param sessionId - Completed session
     * @param userId - Student ID
     * @return Updated global score
     */
    @PostMapping("/{sessionId}/updateGlobalScore")
    public ResponseEntity<?> updateGlobalScore(
            @PathVariable Long sessionId,
            @RequestParam Long userId) {
        
        try {
            // For now, this is a placeholder
            // The score is already stored in the session and aggregated in badgeEvaluationService
            // In future, this could persist to an external score table
            int totalScore = badgeEvaluationService.calculateTotalScore(userId);
            return ResponseEntity.ok(new Object() {
                public final int globalScore = totalScore;
                public final String message = "Global score updated from session";
            });
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error updating global score: " + e.getMessage());
        }
    }
}
