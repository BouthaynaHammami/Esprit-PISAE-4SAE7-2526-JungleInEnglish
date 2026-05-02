package tn.esprit.language_courses_service.ChallengesCompetitions.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.language_courses_service.DTO.ChallengeAttemptDTO;
import tn.esprit.language_courses_service.DTO.ErrorResponse;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.ChallengeAttempt;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.ChallengeType;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.Level;
import tn.esprit.language_courses_service.ChallengesCompetitions.Services.IServices.IChallengeAttemptService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller for student challenge endpoints
 * Provides API endpoints for students to interact with challenges and attempts
 */
@RestController
@RequestMapping("/studentChallenges")
@RequiredArgsConstructor
public class StudentChallengesController {

    private final IChallengeAttemptService attemptService;

    /**
     * Get all attempts for a specific user
     * GET /activities/api/studentChallenges/byUser/{userId}
     */
    @GetMapping("/byUser/{userId}")
    public ResponseEntity<List<ChallengeAttemptDTO>> getAttemptsByUser(@PathVariable Long userId) {
        try {
            List<ChallengeAttempt> attempts = attemptService.getAttemptsByUser(userId);
            List<ChallengeAttemptDTO> dtos = attempts.stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Start a new attempt or get an existing one for a user and challenge
     * POST /activities/api/studentChallenges/startOrGet?userId=1&challengeId=1
     * 
     * Returns:
     * - 200 OK if in-progress attempt already exists
     * - 201 CREATED if new attempt created
     * - 400 BAD_REQUEST if attempt limit exceeded or invalid challenge type
     * - 404 NOT_FOUND if challenge doesn't exist or is not active
     * - 409 CONFLICT if max participants reached
     */
    @PostMapping("/startOrGet")
    public ResponseEntity<?> startOrGetAttempt(
            @RequestParam Long userId,
            @RequestParam Long challengeId) {
        try {
            // Validate inputs
            if (userId == null || userId <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("Invalid userId", "ERR_INVALID_USER_ID"));
            }
            if (challengeId == null || challengeId <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("Invalid challengeId", "ERR_INVALID_CHALLENGE_ID"));
            }

            // Try to get existing in-progress attempt using optimized query (single DB call)
            Optional<ChallengeAttempt> inProgressAttempt = attemptService.getInProgressAttempt(userId, challengeId);
            
            if (inProgressAttempt.isPresent()) {
                // Resume existing attempt
                return ResponseEntity.ok(toDto(inProgressAttempt.get()));
            }

            // If no in-progress attempt exists, start a new one
            ChallengeAttempt newAttempt = attemptService.startAttempt(userId, challengeId);
            return ResponseEntity.status(HttpStatus.CREATED).body(toDto(newAttempt));
            
        } catch (IllegalStateException e) {
            // Max attempts reached, challenge already in progress, etc.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage(), "ERR_ATTEMPT_STATE"));
        } catch (IllegalArgumentException e) {
            // Challenge not found, invalid type, etc.
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage(), "ERR_CHALLENGE_NOT_FOUND"));
        } catch (Exception e) {
            // Log unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An unexpected error occurred: " + e.getMessage(), "ERR_INTERNAL"));
        }
    }

    /**
     * Get a specific attempt by ID
     * GET /activities/api/studentChallenges/{attemptId}
     */
    @GetMapping("/{attemptId}")
    public ResponseEntity<ChallengeAttemptDTO> getAttempt(@PathVariable Long attemptId) {
        try {
            Optional<ChallengeAttempt> attempt = attemptService.getAttemptById(attemptId);
            return attempt.map(value -> ResponseEntity.ok(toDto(value)))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get attempts for a specific challenge
     * GET /activities/api/studentChallenges/byChallenge/{challengeId}
     */
    @GetMapping("/byChallenge/{challengeId}")
    public ResponseEntity<List<ChallengeAttemptDTO>> getAttemptsByChallenge(@PathVariable Long challengeId) {
        try {
            List<ChallengeAttempt> attempts = attemptService.getAttemptsByChallenge(challengeId);
            List<ChallengeAttemptDTO> dtos = attempts.stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Complete an attempt with a score
     * PUT /activities/api/studentChallenges/{attemptId}/complete?score=85
     * 
     * If time has expired, marks attempt as EXPIRED (score is ignored).
     * Otherwise marks as COMPLETED with provided score.
     */
    @PutMapping("/{attemptId}/complete")
    public ResponseEntity<?> completeAttempt(
            @PathVariable Long attemptId, 
            @RequestParam(required = false) Integer score) {
        try {
            if (attemptId == null || attemptId <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("Invalid attemptId", "ERR_INVALID_ATTEMPT_ID"));
            }

            // Score is optional but should be >= 0
            if (score != null && score < 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("Score cannot be negative", "ERR_INVALID_SCORE"));
            }

            // Use default score of 0 if not provided
            int finalScore = score != null ? score : 0;
            
            attemptService.completeAttempt(attemptId, finalScore);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Attempt not found: " + e.getMessage(), "ERR_ATTEMPT_NOT_FOUND"));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Invalid attempt state: " + e.getMessage(), "ERR_ATTEMPT_STATE"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error completing attempt: " + e.getMessage(), "ERR_INTERNAL"));
        }
    }

    /**
     * Expire an attempt (time limit exceeded)
     * PUT /activities/api/studentChallenges/{attemptId}/expire
     */
    @PutMapping("/{attemptId}/expire")
    public ResponseEntity<?> expireAttempt(@PathVariable Long attemptId) {
        try {
            if (attemptId == null || attemptId <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("Invalid attemptId", "ERR_INVALID_ATTEMPT_ID"));
            }

            attemptService.expireAttempt(attemptId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Attempt not found: " + e.getMessage(), "ERR_ATTEMPT_NOT_FOUND"));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Invalid attempt state: " + e.getMessage(), "ERR_ATTEMPT_STATE"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error expiring attempt: " + e.getMessage(), "ERR_INTERNAL"));
        }
    }

    /**
     * Start an attempt by challenge type and optional level
     * POST /activities/api/studentChallenges/startByType?userId=1&type=WORD_SCRAMBLE&level=BEGINNER
     */
    @PostMapping("/startByType")
    public ResponseEntity<ChallengeAttemptDTO> startAttemptByType(
            @RequestParam Long userId,
            @RequestParam ChallengeType type,
            @RequestParam(required = false) Level level) {
        try {
            ChallengeAttempt attempt = attemptService.startAttemptByType(userId, type, level);
            return ResponseEntity.status(HttpStatus.CREATED).body(toDto(attempt));
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Convert ChallengeAttempt entity to DTO
     */
    private ChallengeAttemptDTO toDto(ChallengeAttempt attempt) {
        return ChallengeAttemptDTO.builder()
                .id(attempt.getId())
                .idUser(attempt.getIdUser())
                .challengeId(attempt.getChallenge() != null ? attempt.getChallenge().getId() : null)
                .startTime(attempt.getStartTime())
                .endTime(attempt.getEndTime())
                .deadlineTime(attempt.getDeadlineTime())
                .status(attempt.getStatus())
                .score(attempt.getScore())
                .progress(attempt.getProgress())
                .attemptsUsed(attempt.getAttemptsUsed())
                .build();
    }
}
