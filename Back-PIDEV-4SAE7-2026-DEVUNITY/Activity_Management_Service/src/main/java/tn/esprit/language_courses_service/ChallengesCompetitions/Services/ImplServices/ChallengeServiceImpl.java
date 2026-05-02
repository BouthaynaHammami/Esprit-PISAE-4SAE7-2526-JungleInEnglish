package tn.esprit.language_courses_service.ChallengesCompetitions.Services.ImplServices;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.language_courses_service.ChallengesCompetitions.DTO.ChallengeSubmissionDTO;
import tn.esprit.language_courses_service.ChallengesCompetitions.DTO.ChallengeResultDTO;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.*;
import tn.esprit.language_courses_service.ChallengesCompetitions.Repositories.ChallengeRepository;
import tn.esprit.language_courses_service.ChallengesCompetitions.Services.IServices.ChallengeService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class ChallengeServiceImpl implements ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final SimpMessagingTemplate messagingTemplate;
    
    // --- Story Chain Real-time State ---
    private final Map<String, StoryChainGameSession> activeStorySessions = new ConcurrentHashMap<>();

    // --- Speed Translation Real-time State ---
    private final Map<String, TranslationRaceSession> activeTranslationSessions = new ConcurrentHashMap<>();

    @Override
    public Challenge addChallenge(Challenge c) {
        sanitizeByType(c);
        return challengeRepository.save(c);
    }

    @Override
    public List<Challenge> getAllChallenges() {
        return challengeRepository.findAll();
    }

    @Override
    public Challenge getChallengeById(Long id) {
        return challengeRepository.findById(id).orElseThrow(() -> new RuntimeException("Challenge not found: " + id));
    }

    @Override
    public Challenge updateChallenge(Long id, Challenge c) {
        Challenge challenge = getChallengeById(id);

        challenge.setTitle(c.getTitle());
        challenge.setDescription(c.getDescription());
        challenge.setType(c.getType());
        challenge.setLevel(c.getLevel());

        challenge.setTimeLimitSeconds(c.getTimeLimitSeconds());
        challenge.setMaxAttempts(c.getMaxAttempts());
        challenge.setHints(c.getHints());
        challenge.setCorrectAnswer(c.getCorrectAnswer());
        challenge.setScrambledSentence(c.getScrambledSentence());
        challenge.setEmojiPrompt(c.getEmojiPrompt());

        challenge.setStartDate(c.getStartDate());
        challenge.setEndDate(c.getEndDate());
        challenge.setMaxParticipants(c.getMaxParticipants());

        // Word Battle Royale fields
        challenge.setWordTheme(c.getWordTheme());
        challenge.setAllowedLetters(c.getAllowedLetters());
        challenge.setForbiddenWords(c.getForbiddenWords());

        // Story Chain fields
        challenge.setInitialSentence(c.getInitialSentence());
        challenge.setMaxSentences(c.getMaxSentences());
        challenge.setMinWordsPerSentence(c.getMinWordsPerSentence());
        challenge.setMaxWordsPerSentence(c.getMaxWordsPerSentence());
        challenge.setAllowVoting(c.getAllowVoting());

        // Speed Translation fields
        challenge.setSourceLanguage(c.getSourceLanguage());
        challenge.setTargetLanguage(c.getTargetLanguage());
        challenge.setSentencesList(c.getSentencesList());
        challenge.setMaxQuestions(c.getMaxQuestions());
        challenge.setPointsPerCorrectAnswer(c.getPointsPerCorrectAnswer());
        challenge.setSpeedBonusEnabled(c.getSpeedBonusEnabled());

        sanitizeByType(challenge);
        return challengeRepository.save(challenge);
    }

    @Override
    public void deleteChallenge(Long id) {
        challengeRepository.deleteById(id);
    }

    @Override
    public List<Challenge> available(LocalDate date, Level level, ChallengeType type) {
        LocalDate d = (date == null) ? LocalDate.now() : date;
        return challengeRepository.findAvailable(d, level, type);
    }

    @Override
    public StoryChainGameSession createStoryChainSession(Challenge challenge, String roomId) {
        if (challenge.getType() != ChallengeType.STORY_CHAIN) {
            throw new IllegalStateException("Challenge is not a STORY_CHAIN challenge");
        }

        StoryChainGameSession session = new StoryChainGameSession();
        session.setRoomId(roomId);
        session.setChallengeId(challenge.getId());
        session.setInitialSentence(challenge.getInitialSentence());
        session.setMaxSentences(challenge.getMaxSentences() != null ? challenge.getMaxSentences() : 20);
        session.setMinWordsPerSentence(challenge.getMinWordsPerSentence() != null ? challenge.getMinWordsPerSentence() : 5);
        session.setMaxWordsPerSentence(challenge.getMaxWordsPerSentence() != null ? challenge.getMaxWordsPerSentence() : 30);
        session.setTimeLimitSeconds(challenge.getTimeLimitSeconds() != null ? challenge.getTimeLimitSeconds() : 60);
        session.setAllowVoting(Boolean.TRUE.equals(challenge.getAllowVoting()));
        return session;
    }

    @Override
    public WordBattleGameSession createWordBattleSession(Challenge challenge, String roomId) {
        if (challenge.getType() != ChallengeType.WORD_BATTLE_ROYALE) {
            throw new IllegalStateException("Challenge is not a WORD_BATTLE_ROYALE challenge");
        }

        WordBattleGameSession session = new WordBattleGameSession();
        session.setRoomId(roomId);
        session.setChallengeId(challenge.getId());
        session.setWordTheme(challenge.getWordTheme());
        session.setAllowedLetters(challenge.getAllowedLetters());
        session.setForbiddenWords(challenge.getForbiddenWords());
        session.setMaxParticipants(challenge.getMaxParticipants() != null ? challenge.getMaxParticipants() : 4);
        session.setTimeLimitSeconds(challenge.getTimeLimitSeconds() != null ? challenge.getTimeLimitSeconds() : 5);
        return session;
    }

    @Override
    public TranslationRaceSession createTranslationRaceSession(Challenge challenge, String roomId) {
        if (challenge.getType() != ChallengeType.SPEED_TRANSLATION_RACE) {
            throw new IllegalStateException("Challenge is not a SPEED_TRANSLATION_RACE challenge");
        }

        TranslationRaceSession session = new TranslationRaceSession();
        session.setRoomId(roomId);
        session.setChallengeId(challenge.getId());
        session.setSourceLanguage(challenge.getSourceLanguage());
        session.setTargetLanguage(challenge.getTargetLanguage());
        session.setMaxQuestions(challenge.getMaxQuestions() != null ? challenge.getMaxQuestions() : 10);
        session.setPointsPerCorrectAnswer(challenge.getPointsPerCorrectAnswer() != null ? challenge.getPointsPerCorrectAnswer() : 10);
        session.setSpeedBonusEnabled(Boolean.TRUE.equals(challenge.getSpeedBonusEnabled()));
        session.setTimeLimitSeconds(challenge.getTimeLimitSeconds() != null ? challenge.getTimeLimitSeconds() : 60);

        List<TranslationRaceSession.TranslationQuestion> parsedQuestions = new ArrayList<>();
        if (challenge.getSentencesList() != null && !challenge.getSentencesList().trim().isEmpty()) {
            String[] pairs = challenge.getSentencesList().split(";");
            for (String pair : pairs) {
                String[] parts = pair.split("\\|");
                if (parts.length == 2) {
                    parsedQuestions.add(new TranslationRaceSession.TranslationQuestion(parts[0], parts[1]));
                }
            }
        }
        session.setQuestions(parsedQuestions);
        return session;
    }

    // ======= Story Chain Implementation =======

    @Override
    public void createStorySession(StoryChainGameSession session) {
        activeStorySessions.put(session.getRoomId(), session);
    }

    @Override
    public StoryChainGameSession getStorySession(String roomId) {
        return activeStorySessions.get(roomId);
    }

    @Override
    public void broadcastStoryUpdate(StoryChainGameSession session) {
        messagingTemplate.convertAndSend("/topic/story-chain/" + session.getRoomId(), session);
    }

    @Override
    public void addPlayerToStory(StoryChainGameSession session, String username, Long userId) {
        if (session.isGameStarted()) return;

        boolean exists = session.getPlayers().stream().anyMatch(p -> p.getIdUser() != null && p.getIdUser().equals(userId));
        if (!exists) {
            Player player = new Player();
            player.setUsername(username);
            player.setIdUser(userId);
            player.setActive(true);
            player.setEliminated(false);
            session.getPlayers().add(player);
        }
    }

    @Override
    public void startStoryGame(StoryChainGameSession session) {
        if (session.getPlayers().size() < 2) return;
        session.setGameStarted(true);
        session.setCurrentPlayerIndex(0);
        
        if (session.getInitialSentence() != null && !session.getInitialSentence().trim().isEmpty()) {
            session.getCurrentStory().add(session.getInitialSentence());
        }
        
        resetTurnTimer(session);
    }

    @Override
    public boolean submitStorySentence(StoryChainGameSession session, String username, String sentence) {
        if (!session.isGameStarted() || session.isGameEnded()) return false;

        Player current = getCurrentPlayer(session);
        if (current == null || !current.getUsername().equals(username)) return false;

        if (isSentenceValid(session, sentence)) {
            session.getCurrentStory().add(sentence.trim());
            
            if (session.getCurrentStory().size() >= session.getMaxSentences()) {
                session.setGameEnded(true);
            } else {
                rotateTurn(session);
            }
            return true;
        } else {
            rotateTurn(session);
            return false;
        }
    }

    @Override
    public Player getCurrentPlayer(StoryChainGameSession session) {
        if (session.getCurrentPlayerIndex() < 0 || session.getCurrentPlayerIndex() >= session.getPlayers().size()) {
            return null;
        }
        return session.getPlayers().get(session.getCurrentPlayerIndex());
    }

    @Override
    public void eliminateCurrentPlayer(StoryChainGameSession session, String reason) {
        Player p = getCurrentPlayer(session);
        if (p != null) {
            p.setEliminated(true);
            p.setEliminationReason(reason);
            rotateTurn(session);
            
            long activeCount = session.getPlayers().stream().filter(pl -> !pl.isEliminated()).count();
            if (activeCount <= 1) {
                session.setGameEnded(true);
            }
        }
    }

    private void rotateTurn(StoryChainGameSession session) {
        int nextIndex = (session.getCurrentPlayerIndex() + 1) % session.getPlayers().size();
        session.setCurrentPlayerIndex(nextIndex);
        resetTurnTimer(session);
    }

    private void resetTurnTimer(StoryChainGameSession session) {
        session.setTurnEndTime(LocalDateTime.now().plusSeconds(session.getTimeLimitSeconds()));
    }

    private boolean isSentenceValid(StoryChainGameSession session, String sentence) {
        if (sentence == null || sentence.trim().isEmpty()) return false;
        String[] words = sentence.trim().split("\\s+");
        int wordCount = words.length;
        if (wordCount < session.getMinWordsPerSentence() || wordCount > session.getMaxWordsPerSentence()) return false;
        return sentence.matches("^[\\x00-\\x7F\\s]*$");
    }

    @Scheduled(fixedRate = 1000)
    public void checkStoryTimeouts() {
        LocalDateTime now = LocalDateTime.now();
        for (StoryChainGameSession session : activeStorySessions.values()) {
            if (session.isGameStarted() && !session.isGameEnded()) {
                if (session.getTurnEndTime() != null && now.isAfter(session.getTurnEndTime())) {
                    eliminateCurrentPlayer(session, "Time Out");
                    broadcastStoryUpdate(session);
                }
            }
        }
    }

    // ======= Speed Translation Race Implementation =======

    @Override
    public void createTranslationSession(TranslationRaceSession session) {
        activeTranslationSessions.put(session.getRoomId(), session);
    }

    @Override
    public TranslationRaceSession getTranslationSession(String roomId) {
        return activeTranslationSessions.get(roomId);
    }

    @Override
    public void broadcastTranslationUpdate(TranslationRaceSession session) {
        messagingTemplate.convertAndSend("/topic/speed-translation/" + session.getRoomId(), session);
    }

    @Override
    public void addPlayerToTranslation(TranslationRaceSession session, String username, Long userId) {
        if (session.isGameStarted()) return;

        boolean exists = session.getPlayers().stream().anyMatch(p -> p.getIdUser() != null && p.getIdUser().equals(userId));
        if (!exists) {
            Player player = new Player();
            player.setUsername(username);
            player.setIdUser(userId);
            player.setScore(0);
            player.setActive(true);
            player.setEliminated(false);
            session.getPlayers().add(player);
        }
    }

    @Override
    public void startTranslationGame(TranslationRaceSession session) {
        if (session.getPlayers().size() < 1) return; // For testing, 1 is enough
        session.setGameStarted(true);
        session.setCurrentQuestionIndex(0);
        session.setCorrectAnswersThisRound(0);
        
        for (Player p : session.getPlayers()) {
            p.setHasAnsweredCurrentQuestion(false);
            p.setScore(0);
        }

        resetQuestionTimer(session);
    }

    private void resetQuestionTimer(TranslationRaceSession session) {
        session.setQuestionEndTime(LocalDateTime.now().plusSeconds(session.getTimeLimitSeconds()));
    }

    @Override
    public boolean submitTranslation(TranslationRaceSession session, String username, String translation) {
        if (!session.isGameStarted() || session.isGameEnded()) return false;

        Player player = session.getPlayers().stream()
                .filter(p -> p.getUsername().equals(username))
                .findFirst()
                .orElse(null);

        if (player == null || player.isEliminated() || player.isHasAnsweredCurrentQuestion()) return false;

        TranslationRaceSession.TranslationQuestion currentQ = session.getQuestions().get(session.getCurrentQuestionIndex());
        
        if (isTranslationCorrect(currentQ, translation)) {
            int basePoints = session.getPointsPerCorrectAnswer();
            int bonus = 0;

            if (session.isSpeedBonusEnabled()) {
                if (session.getCorrectAnswersThisRound() == 0) bonus = 5;
                else if (session.getCorrectAnswersThisRound() == 1) bonus = 3;
                else if (session.getCorrectAnswersThisRound() == 2) bonus = 1;
            }

            player.setScore(player.getScore() + basePoints + bonus);
            player.setHasAnsweredCurrentQuestion(true);
            session.setCorrectAnswersThisRound(session.getCorrectAnswersThisRound() + 1);

            checkRoundComplete(session);
            return true;
        } else {
            player.setHasAnsweredCurrentQuestion(true);
            checkRoundComplete(session);
            return false;
        }
    }

    private void checkRoundComplete(TranslationRaceSession session) {
        long answeredCount = session.getPlayers().stream().filter(Player::isHasAnsweredCurrentQuestion).count();
        if (answeredCount >= session.getPlayers().size()) {
            moveToNextQuestion(session);
        }
    }

    private void moveToNextQuestion(TranslationRaceSession session) {
        if (session.getCurrentQuestionIndex() + 1 < session.getMaxQuestions() && 
            session.getCurrentQuestionIndex() + 1 < session.getQuestions().size()) {
            session.setCurrentQuestionIndex(session.getCurrentQuestionIndex() + 1);
            session.setCorrectAnswersThisRound(0);
            for (Player p : session.getPlayers()) p.setHasAnsweredCurrentQuestion(false);
            resetQuestionTimer(session);
        } else {
            session.setGameEnded(true);
        }
    }

    private boolean isTranslationCorrect(TranslationRaceSession.TranslationQuestion q, String translation) {
        if (translation == null || q.getExpectedTranslation() == null) return false;
        return translation.trim().equalsIgnoreCase(q.getExpectedTranslation().trim());
    }

    @Scheduled(fixedRate = 1000)
    public void checkTranslationTimeouts() {
        LocalDateTime now = LocalDateTime.now();
        for (TranslationRaceSession session : activeTranslationSessions.values()) {
            if (session.isGameStarted() && !session.isGameEnded()) {
                if (session.getQuestionEndTime() != null && now.isAfter(session.getQuestionEndTime())) {
                    moveToNextQuestion(session);
                    broadcastTranslationUpdate(session);
                }
            }
        }
    }

    // ======= Internal helpers =======

    private String emptyToNull(String s) {
        if (s == null || s.trim().isEmpty()) return null;
        return s.trim();
    }

    private void sanitizeByType(Challenge c) {
        if (c.getTitle() == null || c.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }
        if (c.getType() == null) {
            throw new IllegalArgumentException("Challenge Type is required");
        }

        c.setDescription(emptyToNull(c.getDescription()));
        
        // Temporarily store all possible type-specific fields
        Integer rawTime = c.getTimeLimitSeconds();
        Integer rawAttempts = c.getMaxAttempts();
        String rawHints = c.getHints();
        String rawCorrect = c.getCorrectAnswer();
        String rawScrambled = c.getScrambledSentence();
        String rawEmoji = c.getEmojiPrompt();
        
        // Word Battle Royale fields
        String rawWordTheme = c.getWordTheme();
        String rawAllowedLetters = c.getAllowedLetters();
        String rawForbiddenWords = c.getForbiddenWords();
        
        // Story Chain fields
        String rawInitialSent = c.getInitialSentence();
        Integer rawMaxSentences = c.getMaxSentences();
        Integer rawMinWords = c.getMinWordsPerSentence();
        Integer rawMaxWords = c.getMaxWordsPerSentence();
        Boolean rawAllowVoting = c.getAllowVoting();

        // Speed Translation fields
        String rawSource = c.getSourceLanguage();
        String rawTarget = c.getTargetLanguage();
        String rawList = c.getSentencesList();
        Integer rawMaxQ = c.getMaxQuestions();
        Integer rawPoints = c.getPointsPerCorrectAnswer();
        Boolean rawBonus = c.getSpeedBonusEnabled();

        // --- RESET ALL TYPE-SPECIFIC FIELDS ---
        c.setTimeLimitSeconds(null);
        c.setMaxAttempts(null);
        c.setHints(null);
        c.setCorrectAnswer(null);
        c.setScrambledSentence(null);
        c.setEmojiPrompt(null);
        
        c.setWordTheme(null);
        c.setAllowedLetters(null);
        c.setForbiddenWords(null);
        
        c.setInitialSentence(null);
        c.setMaxSentences(null);
        c.setMinWordsPerSentence(null);
        c.setMaxWordsPerSentence(null);
        c.setAllowVoting(false);
        
        c.setSourceLanguage(null);
        c.setTargetLanguage(null);
        c.setSentencesList(null);
        c.setMaxQuestions(null);
        c.setPointsPerCorrectAnswer(null);
        c.setSpeedBonusEnabled(false);

        // --- RE-ASSIGN BASED ON TYPE ---
        switch (c.getType()) {
            case MYSTERY_WORD -> {
                c.setMaxAttempts(rawAttempts != null ? rawAttempts : 5);
                c.setHints(emptyToNull(rawHints));
                c.setCorrectAnswer(emptyToNull(rawCorrect));
                c.setTimeLimitSeconds(rawTime != null ? rawTime : 60);
                int numHints = (c.getHints() != null && !c.getHints().isEmpty()) ? c.getHints().split(";").length : 0;
                c.setPointsPerCorrectAnswer(numHints);
            }
            case SENTENCE_BUILDER -> {
                c.setTimeLimitSeconds(rawTime != null ? rawTime : 180);
                c.setScrambledSentence(emptyToNull(rawScrambled));
                c.setCorrectAnswer(emptyToNull(rawCorrect));
                c.setPointsPerCorrectAnswer(3);
            }
            case EMOJI_WORD -> {
                c.setEmojiPrompt(emptyToNull(rawEmoji));
                c.setCorrectAnswer(emptyToNull(rawCorrect));
                c.setTimeLimitSeconds(rawTime != null ? rawTime : 60);
                c.setPointsPerCorrectAnswer(3);
            }
            case WORD_BATTLE_ROYALE -> {
                c.setTimeLimitSeconds(rawTime != null ? rawTime : 30);
                c.setWordTheme(emptyToNull(rawWordTheme));
                c.setAllowedLetters(emptyToNull(rawAllowedLetters));
                c.setForbiddenWords(emptyToNull(rawForbiddenWords));
            }
            case STORY_CHAIN -> {
                c.setTimeLimitSeconds(rawTime != null ? rawTime : 60);
                c.setInitialSentence(emptyToNull(rawInitialSent));
                c.setMaxSentences(rawMaxSentences != null ? rawMaxSentences : 20);
                c.setMinWordsPerSentence(rawMinWords != null ? rawMinWords : 5);
                c.setMaxWordsPerSentence(rawMaxWords != null ? rawMaxWords : 30);
                c.setAllowVoting(rawAllowVoting != null ? rawAllowVoting : false);
            }
            case SPEED_TRANSLATION_RACE -> {
                c.setTimeLimitSeconds(rawTime != null ? rawTime : 60);
                c.setSourceLanguage(emptyToNull(rawSource));
                c.setTargetLanguage(emptyToNull(rawTarget));
                c.setSentencesList(emptyToNull(rawList));
                c.setMaxQuestions(rawMaxQ != null ? rawMaxQ : 10);
                c.setPointsPerCorrectAnswer(rawPoints != null ? rawPoints : 10);
                c.setSpeedBonusEnabled(rawBonus != null ? rawBonus : false);
            }
        }
    }

    /**
     * Submit challenge score - delegates to ChallengeScoringService
     */
    @Override
    public ChallengeResultDTO submitChallengeScore(
            Long idUser,
            Long challengeId,
            ChallengeSubmissionDTO submission) {
        // This will be injected when service is ready
        // For now, return a default response
        // In a real implementation, you would inject IChallengeScoringService
        return ChallengeResultDTO.builder()
                .success(true)
                .message("Challenge score received")
                .score(submission.getScore())
                .pointsAwarded(submission.getScore())
                .build();
    }
}