package tn.esprit.language_courses_service.ChallengesCompetitions.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class TranslationRaceSession {
    private String roomId;
    private Long challengeId;

    // Config from Challenge
    private String sourceLanguage;
    private String targetLanguage;
    private List<TranslationQuestion> questions = new ArrayList<>();
    private int maxQuestions;
    private int pointsPerCorrectAnswer;
    private boolean speedBonusEnabled;
    private int timeLimitSeconds;

    // Game Dynamic State
    private List<Player> players = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private LocalDateTime questionEndTime;
    private boolean gameStarted = false;
    private boolean gameEnded = false;

    // Tracking for bonus
    private int correctAnswersThisRound = 0;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TranslationQuestion {
        private String sourceText;
        private String expectedTranslation;
    }
}
