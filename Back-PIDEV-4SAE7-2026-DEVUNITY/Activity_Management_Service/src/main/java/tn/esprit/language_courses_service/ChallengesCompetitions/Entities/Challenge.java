package tn.esprit.language_courses_service.ChallengesCompetitions.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "challenge_type", nullable = false, length = 50)
    private ChallengeType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "challenge_level", nullable = true, length = 20)
    private Level level;

    private Integer timeLimitSeconds;

    private Integer maxAttempts;

    // MysteryWord
    private String hints;

    // Shared by types
    private String correctAnswer;

    // SentenceBuilder
    private String scrambledSentence;

    // EmojiWord
    private String emojiPrompt;

    // Word Battle Royale fields
    private String wordTheme;

    private String allowedLetters;

    private String forbiddenWords; // comma-separated strings

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer maxParticipants;

    // Story Chain fields
    private String initialSentence;

    private Integer maxSentences;

    private Integer minWordsPerSentence;

    private Integer maxWordsPerSentence;

    @Builder.Default
    private Boolean allowVoting = false;

    // Speed Translation Race fields
    private String sourceLanguage;

    private String targetLanguage;

    private String sentencesList; // "Source|Target;Source2|Target2"

    private Integer maxQuestions;

    private Integer pointsPerCorrectAnswer;

    @Builder.Default
    private Boolean speedBonusEnabled = false;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ChallengeAttempt> attempts;
}