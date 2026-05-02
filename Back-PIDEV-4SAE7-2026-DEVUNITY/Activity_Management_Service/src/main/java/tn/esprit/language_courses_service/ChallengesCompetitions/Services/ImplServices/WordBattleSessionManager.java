package tn.esprit.language_courses_service.ChallengesCompetitions.Services.ImplServices;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.Player;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.WordBattleGameSession;
import tn.esprit.language_courses_service.ChallengesCompetitions.Services.IServices.IWordBattleGameService;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WordBattleSessionManager {

    private final Map<String, WordBattleGameSession> activeSessions = new ConcurrentHashMap<>();
    private final SimpMessagingTemplate messagingTemplate;
    private final IWordBattleGameService gameService;

    public WordBattleSessionManager(SimpMessagingTemplate messagingTemplate, IWordBattleGameService gameService) {
        this.messagingTemplate = messagingTemplate;
        this.gameService = gameService;
    }

    public WordBattleGameSession getSession(String roomId) {
        return activeSessions.get(roomId);
    }

    public void createSession(WordBattleGameSession session) {
        activeSessions.put(session.getRoomId(), session);
    }

    public void removeSession(String roomId) {
        activeSessions.remove(roomId);
    }

    // Runs every second to check for timeouts
    @Scheduled(fixedRate = 1000)
    public void checkTimeouts() {
        LocalDateTime now = LocalDateTime.now();
        for (WordBattleGameSession session : activeSessions.values()) {
            if (session.isGameStarted() && !session.isGameEnded()) {
                if (session.getTurnEndTime() != null && now.isAfter(session.getTurnEndTime())) {
                    // Current player timed out
                    Player cp = gameService.getCurrentPlayer(session);
                    if (cp != null) {
                        gameService.eliminateCurrentPlayer(session, "Time out");
                        broadcastSessionUpdate(session);
                    }
                }
            }
        }
    }

    public void broadcastSessionUpdate(WordBattleGameSession session) {
        messagingTemplate.convertAndSend("/topic/word-battle/" + session.getRoomId(), session);
    }
}
