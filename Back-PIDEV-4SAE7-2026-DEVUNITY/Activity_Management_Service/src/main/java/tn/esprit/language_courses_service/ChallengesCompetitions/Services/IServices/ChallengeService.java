package tn.esprit.language_courses_service.ChallengesCompetitions.Services.IServices;


import tn.esprit.language_courses_service.ChallengesCompetitions.DTO.ChallengeSubmissionDTO;
import tn.esprit.language_courses_service.ChallengesCompetitions.DTO.ChallengeResultDTO;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.*;

import java.time.LocalDate;
import java.util.List;

public interface ChallengeService {
    public Challenge addChallenge(Challenge challenge);
    public Challenge updateChallenge(Long id, Challenge challenge);
    public List<Challenge> getAllChallenges();
    public Challenge getChallengeById(Long id);
    public void deleteChallenge(Long id);

    List<Challenge> available(LocalDate date, Level level, ChallengeType type);

    StoryChainGameSession createStoryChainSession(Challenge challenge, String roomId);
    WordBattleGameSession createWordBattleSession(Challenge challenge, String roomId);
    TranslationRaceSession createTranslationRaceSession(Challenge challenge, String roomId);

    // --- Story Chain Game & Session Methods ---
    void createStorySession(StoryChainGameSession session);
    StoryChainGameSession getStorySession(String roomId);
    void broadcastStoryUpdate(StoryChainGameSession session);
    
    void addPlayerToStory(StoryChainGameSession session, String username, Long userId);
    void startStoryGame(StoryChainGameSession session);
    boolean submitStorySentence(StoryChainGameSession session, String username, String sentence);
    Player getCurrentPlayer(StoryChainGameSession session);
    void eliminateCurrentPlayer(StoryChainGameSession session, String reason);

    // --- Speed Translation Race Methods ---
    void createTranslationSession(TranslationRaceSession session);
    TranslationRaceSession getTranslationSession(String roomId);
    void broadcastTranslationUpdate(TranslationRaceSession session);
    void addPlayerToTranslation(TranslationRaceSession session, String username, Long userId);
    void startTranslationGame(TranslationRaceSession session);
    boolean submitTranslation(TranslationRaceSession session, String username, String translation);

    // --- Timed Challenge Methods ---
    /**
     * Submit challenge score and process completion
     * @param idUser User/Student ID
     * @param challengeId Challenge ID
     * @param submission Challenge submission data
     * @return Challenge result with score and badges
     */
    ChallengeResultDTO submitChallengeScore(Long idUser, Long challengeId, ChallengeSubmissionDTO submission);
}
