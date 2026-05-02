package tn.esprit.language_courses_service.ChallengesCompetitions.Entities;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class WordBattleGameSession {
    private String roomId;
    private Long challengeId;
    private String wordTheme;
    private String allowedLetters;
    private String forbiddenWords;
    
    private int maxParticipants;
    private int timeLimitSeconds;

    private List<Player> players = new ArrayList<>();
    private List<String> usedWords = new ArrayList<>();
    
    private int currentPlayerIndex = -1;
    private LocalDateTime turnEndTime;
    private boolean gameStarted = false;
    private boolean gameEnded = false;
}
