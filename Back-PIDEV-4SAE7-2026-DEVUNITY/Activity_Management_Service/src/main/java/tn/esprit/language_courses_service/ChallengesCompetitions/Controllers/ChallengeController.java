package tn.esprit.language_courses_service.ChallengesCompetitions.Controllers;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;
import tn.esprit.language_courses_service.ChallengesCompetitions.DTO.ChallengeSubmissionDTO;
import tn.esprit.language_courses_service.ChallengesCompetitions.DTO.ChallengeResultDTO;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.*;
import tn.esprit.language_courses_service.ChallengesCompetitions.Producer.ChallengeProducer;
import tn.esprit.language_courses_service.ChallengesCompetitions.Repositories.ChallengeRepository;
import tn.esprit.language_courses_service.ChallengesCompetitions.Repositories.ChallengeAttemptRepository;
import tn.esprit.language_courses_service.ChallengesCompetitions.Services.IServices.ChallengeService;
import tn.esprit.language_courses_service.ChallengesCompetitions.Services.IServices.IWordBattleGameService;
import tn.esprit.language_courses_service.ChallengesCompetitions.Services.ImplServices.WordBattleSessionManager;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/challenges")
@RequiredArgsConstructor
public class ChallengeController {
    private final ChallengeService challengeService;
    private final ChallengeRepository challengeRepository;
    private final ChallengeAttemptRepository attemptRepository;
    private final ChallengeProducer challengeProducer;
    private final WordBattleSessionManager wordBattleSessionManager;
    private final IWordBattleGameService wordBattleGameService;

    // --- REST Endpoints ---

    @PostMapping()
    public Challenge addChallenge(@RequestBody Challenge challenge) {
        return challengeService.addChallenge(challenge);
    }

    @PutMapping("/{id}")
    public Challenge updateChallenge(@PathVariable Long id, @RequestBody Challenge challenge) {
        return challengeService.updateChallenge(id, challenge);
    }

    @GetMapping()
    public List<Challenge> getAllChallenges() {
        return challengeService.getAllChallenges();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getChallengeById(@PathVariable long id) {
        try {
            if (id <= 0) {
                return ResponseEntity.badRequest().body("Invalid challenge ID");
            }
            Challenge challenge = challengeService.getChallengeById(id);
            return ResponseEntity.ok(challenge);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteChallenge(@PathVariable long id) {
        try {
            if (id <= 0) {
                return ResponseEntity.badRequest().body("Invalid challenge ID");
            }
            challengeService.deleteChallenge(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting challenge: " + e.getMessage());
        }
    }

    /**
     * Get available challenges with optional filters
     * Query parameters are ALL optional - if not provided, returns all active challenges
     * 
     * Examples:
     * - GET /challenges/available → all active challenges
     * - GET /challenges/available?type=MYSTERY_WORD → all active MYSTERY_WORD challenges
     * - GET /challenges/available?type=MYSTERY_WORD&level=A1 → A1 level MYSTERY_WORD challenges
     */
    @GetMapping("/available")
    public ResponseEntity<?> available(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,
            @RequestParam(required = false) Level level,
            @RequestParam(required = false) ChallengeType type
    ) {
        try {
            LocalDate searchDate = (date == null) ? LocalDate.now() : date;
            List<Challenge> challenges = challengeService.available(searchDate, level, type);
            
            if (challenges.isEmpty()) {
                return ResponseEntity.ok(challenges);  // Return empty list instead of 404
            }
            
            return ResponseEntity.ok(challenges);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching challenges: " + e.getMessage());
        }
    }

    @PostMapping("/sync")
    public ResponseEntity<String> syncChallenges() {
        challengeProducer.syncAllChallenges();
        return ResponseEntity.ok("Synchronization triggered for all challenges.");
    }

    /**
     * Submit challenge score - endpoint for timed challenge completion
     * POST /challenges/{id}/submit-score
     * 
     * This endpoint receives the final score, attempt data, and performance metrics
     * from the frontend challenge component and processes them.
     *
     * @param id Challenge ID
     * @param idUser User/Student ID (from auth context)
     * @param submission Challenge submission data with score, answers, time, etc.
     * @return Challenge result with points awarded and badges earned
     */
    @PostMapping("/{id}/submit-score")
    public ResponseEntity<?> submitChallengeScore(
            @PathVariable Long id,
            @RequestParam(required = false) Long idUser,
            @RequestBody ChallengeSubmissionDTO submission) {
        
        try {
            if (id <= 0 || idUser == null || idUser <= 0) {
                return ResponseEntity.badRequest().body("Invalid challenge ID or user ID");
            }

            if (submission == null) {
                return ResponseEntity.badRequest().body("Invalid submission data");
            }

            // Get user ID from security context in real implementation
            // Long userId = getCurrentUserId(); 
            
            ChallengeResultDTO result = challengeService.submitChallengeScore(idUser, id, submission);
            
            if (result.getSuccess()) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(400).body(result);
            }
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid request: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error submitting challenge score: " + e.getMessage());
        }
    }

    /**
     * Get student's attempt history for a challenge
     * GET /challenges/{id}/attempts/{idUser}
     */
    @GetMapping("/{id}/attempts/{idUser}")
    public ResponseEntity<?> getAttemptHistory(
            @PathVariable Long id,
            @PathVariable Long idUser) {
        
        try {
            if (id <= 0 || idUser <= 0) {
                return ResponseEntity.badRequest().body("Invalid challenge ID or user ID");
            }

            List<ChallengeAttempt> attempts = attemptRepository.findByIdUserAndChallengeId(idUser, id);
            return ResponseEntity.ok(attempts);
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching attempt history: " + e.getMessage());
        }
    }

    /**
     * Get leaderboard for a challenge
     * GET /challenges/{id}/leaderboard?limit=10
     */
    @GetMapping("/{id}/leaderboard")
    public ResponseEntity<?> getLeaderboard(
            @PathVariable Long id,
            @RequestParam(defaultValue = "10") Integer limit) {
        
        try {
            if (id <= 0 || limit <= 0) {
                return ResponseEntity.badRequest().body("Invalid parameters");
            }

            // Get top scores for this challenge
            // Implement based on your database schema
            return ResponseEntity.ok("Leaderboard for challenge " + id);
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching leaderboard: " + e.getMessage());
        }
    }

    // --- WebSocket DTOs ---

    @Data
    public static class StoryChainJoinRequest {
        private String username;
        private Long userId;
        private Long challengeId;
    }

    @Data
    public static class StoryChainSentenceSubmission {
        private String username;
        private String sentence;
    }

    @Data
    public static class WordBattleJoinRequest {
        private String username;
        private Long userId;
        private Long challengeId;
    }

    @Data
    public static class WordBattleWordSubmission {
        private String username;
        private String word;
    }

    @Data
    public static class TranslationRaceJoinRequest {
        private String username;
        private Long userId;
        private Long challengeId;
    }

    @Data
    public static class TranslationSubmission {
        private String username;
        private String translation;
    }

    // --- Story Chain WebSocket Handlers ---

    @MessageMapping("/story-chain/{roomId}/join")
    public void joinStoryRoom(@DestinationVariable String roomId, @Payload StoryChainJoinRequest request) {
        StoryChainGameSession session = challengeService.getStorySession(roomId);

        if (session == null) {
            Challenge challenge = challengeRepository.findById(request.getChallengeId()).orElse(null);
            if (challenge != null && challenge.getType() == ChallengeType.STORY_CHAIN) {
                session = challengeService.createStoryChainSession(challenge, roomId);
                challengeService.createStorySession(session);
            } else {
                return;
            }
        }

        challengeService.addPlayerToStory(session, request.getUsername(), request.getUserId());
        challengeService.broadcastStoryUpdate(session);
    }

    @MessageMapping("/story-chain/{roomId}/start")
    public void startStoryRoom(@DestinationVariable String roomId) {
        StoryChainGameSession session = challengeService.getStorySession(roomId);
        if (session != null && !session.isGameStarted() && session.getPlayers().size() >= 2) {
            challengeService.startStoryGame(session);
            challengeService.broadcastStoryUpdate(session);
        }
    }

    @MessageMapping("/story-chain/{roomId}/submit")
    public void submitStorySentence(@DestinationVariable String roomId, @Payload StoryChainSentenceSubmission submission) {
        StoryChainGameSession session = challengeService.getStorySession(roomId);
        if (session != null && session.isGameStarted() && !session.isGameEnded()) {
            challengeService.submitStorySentence(session, submission.getUsername(), submission.getSentence());
            challengeService.broadcastStoryUpdate(session);
        }
    }

    // --- Word Battle WebSocket Handlers ---

    @MessageMapping("/word-battle/{roomId}/join")
    public void joinWordBattleRoom(@DestinationVariable String roomId, @Payload WordBattleJoinRequest request) {
        WordBattleGameSession session = wordBattleSessionManager.getSession(roomId);

        if (session == null) {
            Challenge challenge = challengeRepository.findById(request.getChallengeId()).orElse(null);
            if (challenge != null && challenge.getType() == ChallengeType.WORD_BATTLE_ROYALE) {
                session = challengeService.createWordBattleSession(challenge, roomId);
                wordBattleSessionManager.createSession(session);
            } else {
                return;
            }
        }

        wordBattleGameService.addPlayer(session, request.getUsername(), request.getUserId());
        
        if (session.getPlayers().size() >= 2 && !session.isGameStarted()) {
            if (session.getPlayers().size() == session.getMaxParticipants()) {
                wordBattleGameService.startGame(session);
            }
        }
        
        wordBattleSessionManager.broadcastSessionUpdate(session);
    }

    @MessageMapping("/word-battle/{roomId}/start")
    public void startWordBattleRoom(@DestinationVariable String roomId) {
        WordBattleGameSession session = wordBattleSessionManager.getSession(roomId);
        if (session != null && !session.isGameStarted() && session.getPlayers().size() >= 2) {
            wordBattleGameService.startGame(session);
            wordBattleSessionManager.broadcastSessionUpdate(session);
        }
    }

    @MessageMapping("/word-battle/{roomId}/submit")
    public void submitWordBattleWord(@DestinationVariable String roomId, @Payload WordBattleWordSubmission submission) {
        WordBattleGameSession session = wordBattleSessionManager.getSession(roomId);

        if (session != null && session.isGameStarted() && !session.isGameEnded()) {
            Player currentInfo = wordBattleGameService.getCurrentPlayer(session);
            if (currentInfo != null && currentInfo.getUsername().equals(submission.getUsername())) {
                wordBattleGameService.validateWord(session, submission.getWord());
                wordBattleSessionManager.broadcastSessionUpdate(session);
            }
        }
    }

    // --- Speed Translation WebSocket Handlers ---

    @MessageMapping("/speed-translation/{roomId}/join")
    public void joinTranslationRoom(@DestinationVariable String roomId, @Payload TranslationRaceJoinRequest request) {
        TranslationRaceSession session = challengeService.getTranslationSession(roomId);

        if (session == null) {
            Challenge challenge = challengeRepository.findById(request.getChallengeId()).orElse(null);
            if (challenge != null && challenge.getType() == ChallengeType.SPEED_TRANSLATION_RACE) {
                session = challengeService.createTranslationRaceSession(challenge, roomId);
                challengeService.createTranslationSession(session);
            } else {
                return;
            }
        }

        challengeService.addPlayerToTranslation(session, request.getUsername(), request.getUserId());
        challengeService.broadcastTranslationUpdate(session);
    }

    @MessageMapping("/speed-translation/{roomId}/start")
    public void startTranslationRoom(@DestinationVariable String roomId) {
        TranslationRaceSession session = challengeService.getTranslationSession(roomId);
        if (session != null && !session.isGameStarted() && session.getPlayers().size() >= 1) {
            challengeService.startTranslationGame(session);
            challengeService.broadcastTranslationUpdate(session);
        }
    }

    @MessageMapping("/speed-translation/{roomId}/submit")
    public void submitTranslation(@DestinationVariable String roomId, @Payload TranslationSubmission submission) {
        TranslationRaceSession session = challengeService.getTranslationSession(roomId);
        if (session != null && session.isGameStarted() && !session.isGameEnded()) {
            challengeService.submitTranslation(session, submission.getUsername(), submission.getTranslation());
            challengeService.broadcastTranslationUpdate(session);
        }
    }
}