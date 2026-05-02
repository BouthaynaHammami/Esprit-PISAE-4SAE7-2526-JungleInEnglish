package tn.esprit.language_courses_service.ChallengesCompetitions.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a 3-minute challenge session for a student
 * Stores pre-shuffled challenges and tracks score
 */
@Entity
@Table(name = "student_challenge_sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentChallengeSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Session metadata
    private Long idUser;
    
    @Enumerated(EnumType.STRING)
    private ChallengeType sessionType;
    
    @Enumerated(EnumType.STRING)
    private Level sessionLevel;
    
    private LocalDateTime sessionStartTime;
    private LocalDateTime sessionEndTime;
    
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private SessionStatus status = SessionStatus.IN_PROGRESS; // IN_PROGRESS, COMPLETED, EXPIRED

    // Challenges (stored as JSON or separate relationship)
    @ElementCollection
    @CollectionTable(name = "session_challenge_ids")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private List<Long> challengeIds = new ArrayList<>();

    private Integer currentChallengeIndex; // Index in challengeIds array

    // Score tracking
    private Integer totalScore;
    private Integer correctAnswers;
    private Integer wrongAnswers;
    private Integer totalChallengesPlayed;
    
    // Utility fields
    private Long sessionDurationSeconds; // 180 seconds = 3 minutes

    @PrePersist
    protected void onCreate() {
        if (sessionStartTime == null) {
            sessionStartTime = LocalDateTime.now();
        }
        if (sessionDurationSeconds == null) {
            sessionDurationSeconds = 180L; // 3 minutes
        }
        if (currentChallengeIndex == null) {
            currentChallengeIndex = 0;
        }
        if (totalScore == null) {
            totalScore = 0;
        }
        if (correctAnswers == null) {
            correctAnswers = 0;
        }
        if (wrongAnswers == null) {
            wrongAnswers = 0;
        }
        if (totalChallengesPlayed == null) {
            totalChallengesPlayed = 0;
        }
    }
}
