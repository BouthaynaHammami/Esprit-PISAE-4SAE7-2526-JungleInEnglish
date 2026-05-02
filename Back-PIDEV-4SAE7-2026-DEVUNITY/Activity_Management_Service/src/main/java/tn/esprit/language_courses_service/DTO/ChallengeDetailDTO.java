package tn.esprit.language_courses_service.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * Enriched DTO for Challenge - Contains all fields without nulls
 * Suitable for frontend to display challenges based on type
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChallengeDetailDTO implements Serializable {
    
    // Common fields
    private Long id;
    private String title;
    private String description;
    private String type;  // MYSTERY_WORD, SENTENCE_BUILDER, etc.
    private String level;  // A1, A2, B1, B2, etc.
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer timeLimitSeconds;
    private boolean isActive;  // startDate <= today <= endDate

    // MYSTERY_WORD specific
    private String hints;
    private Integer maxAttempts;
    private String correctAnswer;

    // SENTENCE_BUILDER specific
    private String scrambledSentence;

    // EMOJI_WORD specific
    private String emojiPrompt;

    // WORD_BATTLE_ROYALE specific
    private String wordTheme;
    private String allowedLetters;
    private String forbiddenWords;
    private Integer maxParticipants;

    // STORY_CHAIN specific
    private String initialSentence;
    private Integer maxSentences;
    private Integer minWordsPerSentence;
    private Integer maxWordsPerSentence;
    private Boolean allowVoting;

    // SPEED_TRANSLATION_RACE specific
    private String sourceLanguage;
    private String targetLanguage;
    private String sentencesList;  // "Source|Target;Source2|Target2"
    private Integer maxQuestions;
    private Integer pointsPerCorrectAnswer;
    private Boolean speedBonusEnabled;

    // Statistics
    private Integer totalAttempts;
    private Integer successRate;  // percentage
    private List<String> badgeRewards;  // Badges that can be earned
}
