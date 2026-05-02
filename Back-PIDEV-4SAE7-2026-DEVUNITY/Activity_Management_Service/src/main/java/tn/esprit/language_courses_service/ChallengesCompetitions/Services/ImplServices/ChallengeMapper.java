package tn.esprit.language_courses_service.ChallengesCompetitions.Services.ImplServices;

import org.springframework.stereotype.Component;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.Challenge;
import tn.esprit.language_courses_service.DTO.ChallengeDetailDTO;
import java.time.LocalDate;

/**
 * Mapper to convert Challenge Entity to DTOs
 * Handles null fields and provides type-safe conversion
 */
@Component
public class ChallengeMapper {

    /**
     * Convert Challenge entity to ChallengeDetailDTO
     * Filters out unnecessary nulls based on challenge type
     */
    public ChallengeDetailDTO toDetailDTO(Challenge challenge) {
        if (challenge == null) {
            return null;
        }

        LocalDate today = LocalDate.now();
        boolean isActive = (challenge.getStartDate() == null || challenge.getStartDate().compareTo(today) <= 0)
                && (challenge.getEndDate() == null || challenge.getEndDate().compareTo(today) >= 0);

        return ChallengeDetailDTO.builder()
                .id(challenge.getId())
                .title(challenge.getTitle())
                .description(challenge.getDescription())
                .type(challenge.getType() != null ? challenge.getType().name() : null)
                .level(challenge.getLevel() != null ? challenge.getLevel().name() : null)
                .startDate(challenge.getStartDate())
                .endDate(challenge.getEndDate())
                .timeLimitSeconds(challenge.getTimeLimitSeconds())
                .isActive(isActive)
                
                // MYSTERY_WORD
                .hints(challenge.getHints())
                .maxAttempts(challenge.getMaxAttempts())
                .correctAnswer(challenge.getCorrectAnswer())
                
                // SENTENCE_BUILDER
                .scrambledSentence(challenge.getScrambledSentence())
                
                // EMOJI_WORD
                .emojiPrompt(challenge.getEmojiPrompt())
                
                // WORD_BATTLE_ROYALE
                .wordTheme(challenge.getWordTheme())
                .allowedLetters(challenge.getAllowedLetters())
                .forbiddenWords(challenge.getForbiddenWords())
                .maxParticipants(challenge.getMaxParticipants())
                
                // STORY_CHAIN
                .initialSentence(challenge.getInitialSentence())
                .maxSentences(challenge.getMaxSentences())
                .minWordsPerSentence(challenge.getMinWordsPerSentence())
                .maxWordsPerSentence(challenge.getMaxWordsPerSentence())
                .allowVoting(challenge.getAllowVoting())
                
                // SPEED_TRANSLATION_RACE
                .sourceLanguage(challenge.getSourceLanguage())
                .targetLanguage(challenge.getTargetLanguage())
                .sentencesList(challenge.getSentencesList())
                .maxQuestions(challenge.getMaxQuestions())
                .pointsPerCorrectAnswer(challenge.getPointsPerCorrectAnswer())
                .speedBonusEnabled(challenge.getSpeedBonusEnabled())
                
                .build();
    }

    /**
     * Check if a challenge is currently active based on its date range
     */
    public boolean isChallengeActive(Challenge challenge) {
        LocalDate today = LocalDate.now();
        boolean startOK = challenge.getStartDate() == null || challenge.getStartDate().compareTo(today) <= 0;
        boolean endOK = challenge.getEndDate() == null || challenge.getEndDate().compareTo(today) >= 0;
        return startOK && endOK;
    }
}
