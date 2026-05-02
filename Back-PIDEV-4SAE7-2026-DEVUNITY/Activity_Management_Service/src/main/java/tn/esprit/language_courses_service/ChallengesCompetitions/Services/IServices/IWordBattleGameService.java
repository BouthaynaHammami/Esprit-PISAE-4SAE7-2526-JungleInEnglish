package tn.esprit.language_courses_service.ChallengesCompetitions.Services.IServices;

import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.Player;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.WordBattleGameSession;

import java.util.List;

public interface IWordBattleGameService {
    void addPlayer(WordBattleGameSession session, String username, Long idUser);
    void startGame(WordBattleGameSession session);
    Player getCurrentPlayer(WordBattleGameSession session);
    void resetTimer(WordBattleGameSession session);
    void nextTurn(WordBattleGameSession session);
    void eliminateCurrentPlayer(WordBattleGameSession session, String reason);
    boolean validateWord(WordBattleGameSession session, String word);
    List<Player> getActivePlayers(WordBattleGameSession session);
}
