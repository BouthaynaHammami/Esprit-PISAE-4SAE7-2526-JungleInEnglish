package tn.esprit.language_courses_service.ChallengesCompetitions.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.language_courses_service.DTO.ChallengeAttemptDTO;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.ChallengeAttempt;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.ChallengeType;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.Level;
import tn.esprit.language_courses_service.ChallengesCompetitions.Services.IServices.IChallengeAttemptService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/challenge-attempts")
@RequiredArgsConstructor
public class ChallengeAttemptController {

    private final IChallengeAttemptService attemptService;

    @PostMapping("/start")
    public ResponseEntity<ChallengeAttemptDTO> startAttempt(@RequestParam Long userId, @RequestParam Long challengeId) {
        try {
            ChallengeAttempt attempt = attemptService.startAttempt(userId, challengeId);
            return ResponseEntity.ok(toDto(attempt));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/start-by-type")
    public ResponseEntity<ChallengeAttemptDTO> startAttemptByType(@RequestParam Long userId,
                                                                  @RequestParam ChallengeType type,
                                                                  @RequestParam(required = false) Level level) {
        try {
            ChallengeAttempt attempt = attemptService.startAttemptByType(userId, type, level);
            return ResponseEntity.ok(toDto(attempt));
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{attemptId}")
    public ResponseEntity<ChallengeAttemptDTO> getAttemptById(@PathVariable Long attemptId) {
        Optional<ChallengeAttempt> attempt = attemptService.getAttemptById(attemptId);
        return attempt.map(value -> ResponseEntity.ok(toDto(value))).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ChallengeAttemptDTO>> getAttemptsByUser(@PathVariable Long userId) {
        List<ChallengeAttempt> attempts = attemptService.getAttemptsByUser(userId);
        return ResponseEntity.ok(attempts.stream().map(this::toDto).collect(Collectors.toList()));
    }

    @GetMapping("/challenge/{challengeId}")
    public ResponseEntity<List<ChallengeAttemptDTO>> getAttemptsByChallenge(@PathVariable Long challengeId) {
        List<ChallengeAttempt> attempts = attemptService.getAttemptsByChallenge(challengeId);
        return ResponseEntity.ok(attempts.stream().map(this::toDto).collect(Collectors.toList()));
    }

    @PutMapping("/{attemptId}/complete")
    public ResponseEntity<Void> completeAttempt(@PathVariable Long attemptId, @RequestParam Integer score) {
        try {
            attemptService.completeAttempt(attemptId, score);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{attemptId}/expire")
    public ResponseEntity<Void> expireAttempt(@PathVariable Long attemptId) {
        try {
            attemptService.expireAttempt(attemptId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

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