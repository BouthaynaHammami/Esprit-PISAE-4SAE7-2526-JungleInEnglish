package tn.esprit.language_courses_service.ChallengesCompetitions.Entities;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class StoryChainGameSession {
    private String roomId;
    private Long challengeId;
    
    // Config from Challenge
    private String initialSentence;
    private int maxSentences;
    private int minWordsPerSentence;
    private int maxWordsPerSentence;
    private int timeLimitSeconds;
    private boolean allowVoting;

    // Game Dynamic State
    private List<Player> players = new ArrayList<>();
    private List<String> currentStory = new ArrayList<>(); // Ordered phrases
    
    private int currentPlayerIndex = -1;
    private LocalDateTime turnEndTime;
    private boolean gameStarted = false;
    private boolean gameEnded = false;
}
