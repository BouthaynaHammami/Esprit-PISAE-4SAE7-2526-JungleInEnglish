package tn.esprit.language_courses_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.SessionStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for StudentChallengeSession
 * Used for session management and progress tracking
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentChallengeSessionDTO {
    
    private Long id;
    private Long idUser;
    
    // Session setup
    private String sessionType;
    private String sessionLevel;
    
    // Timing
    private LocalDateTime sessionStartTime;
    private LocalDateTime sessionEndTime;
    private Long sessionDurationSeconds; // 180 = 3 min
    
    // Status
    private SessionStatus status;
    
    // Challenges
    private List<Long> challengeIds;
    private Integer currentChallengeIndex;
    
    // Scores
    private Integer totalScore;
    private Integer correctAnswers;
    private Integer wrongAnswers;
    private Integer totalChallengesPlayed;
    
    // Calculated fields (frontend convenience)
    private Long timeRemainingSeconds;
}
