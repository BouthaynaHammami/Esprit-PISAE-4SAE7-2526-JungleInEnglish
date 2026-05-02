package tn.esprit.language_courses_service.ChallengesCompetitions.Services.ImplServices;

import org.springframework.stereotype.Service;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.Player;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.WordBattleGameSession;
import tn.esprit.language_courses_service.ChallengesCompetitions.Services.IServices.IWordBattleGameService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WordBattleGameServiceImpl implements IWordBattleGameService {

    @Override
    public void addPlayer(WordBattleGameSession session, String username, Long idUser) {
        if (!session.isGameStarted() && session.getPlayers().size() < session.getMaxParticipants() && session.getPlayers().stream().noneMatch(p -> p.getUsername().equals(username))) {
            Player p = new Player();
            p.setUsername(username);
            p.setIdUser(idUser);
            session.getPlayers().add(p);
        }
    }

    @Override
    public void startGame(WordBattleGameSession session) {
        if (session.getPlayers().size() >= 2) {
            session.setGameStarted(true);
            session.setCurrentPlayerIndex(0);
            resetTimer(session);
        }
    }

    @Override
    public Player getCurrentPlayer(WordBattleGameSession session) {
        if (!session.isGameStarted() || session.isGameEnded() || session.getCurrentPlayerIndex() < 0 || session.getPlayers().isEmpty()) {
            return null;
        }
        return session.getPlayers().get(session.getCurrentPlayerIndex());
    }

    @Override
    public void resetTimer(WordBattleGameSession session) {
        session.setTurnEndTime(LocalDateTime.now().plusSeconds(session.getTimeLimitSeconds()));
    }

    @Override
    public void nextTurn(WordBattleGameSession session) {
        if (session.isGameEnded()) return;

        int activePlayersCount = (int) session.getPlayers().stream().filter(Player::isActive).count();
        if (activePlayersCount <= 1) {
            session.setGameEnded(true);
            return;
        }

        do {
            session.setCurrentPlayerIndex((session.getCurrentPlayerIndex() + 1) % session.getPlayers().size());
        } while (!session.getPlayers().get(session.getCurrentPlayerIndex()).isActive());
        
        resetTimer(session);
    }

    @Override
    public void eliminateCurrentPlayer(WordBattleGameSession session, String reason) {
        Player current = getCurrentPlayer(session);
        if (current != null) {
            current.setActive(false);
            current.setEliminated(true);
            current.setEliminationReason(reason);
            nextTurn(session);
        }
    }

    @Override
    public boolean validateWord(WordBattleGameSession session, String word) {
        if (word == null || word.trim().isEmpty()) {
            eliminateCurrentPlayer(session, "Empty word");
            return false;
        }
        
        String formattedWord = word.trim().toUpperCase();

        // Check if used
        if (session.getUsedWords().contains(formattedWord)) {
            eliminateCurrentPlayer(session, "Word already used");
            return false;
        }

        // Check forbidden words
        if (session.getForbiddenWords() != null && !session.getForbiddenWords().trim().isEmpty()) {
            String[] forbidden = session.getForbiddenWords().toUpperCase().split(",");
            for (String fw : forbidden) {
                if (formattedWord.equalsIgnoreCase(fw.trim())) {
                    eliminateCurrentPlayer(session, "Forbidden word");
                    return false;
                }
            }
        }

        // Check allowed letters (if defined)
        if (session.getAllowedLetters() != null && !session.getAllowedLetters().trim().isEmpty()) {
            String[] requiredLetters = session.getAllowedLetters().toUpperCase().split(",");
            for (String letter : requiredLetters) {
                if (!formattedWord.contains(letter.trim())) {
                    eliminateCurrentPlayer(session, "Does not contain required letter: " + letter.trim());
                    return false;
                }
            }
        }

        // Check Theme (Basic implementation)
        if (session.getWordTheme() != null && !session.getWordTheme().trim().isEmpty()) {
            if (!isWordInTheme(formattedWord, session.getWordTheme())) {
                eliminateCurrentPlayer(session, "Word does not match theme: " + session.getWordTheme());
                return false;
            }
        }
        
        // Accepted
        session.getUsedWords().add(formattedWord);
        nextTurn(session);
        return true;
    }

    private boolean isWordInTheme(String word, String theme) {
        // Simplified theme validation
        // In a real scenario, this would use a dictionary API or a database of themed words.
        // For now, we accept any word if theme is not strictly defined, 
        // but we could add some basic keyword matching here if needed.
        return true; // Default to true for now as per user instruction "non" to static list
    }

    @Override
    public List<Player> getActivePlayers(WordBattleGameSession session) {
        return session.getPlayers().stream().filter(Player::isActive).collect(Collectors.toList());
    }
}
