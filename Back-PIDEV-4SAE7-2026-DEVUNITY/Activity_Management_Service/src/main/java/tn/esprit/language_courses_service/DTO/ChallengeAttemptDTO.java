package tn.esprit.language_courses_service.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.AttemptStatus;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)  // Don't serialize null fields
public class ChallengeAttemptDTO {
    private Long id;
    private Long idUser;
    private Long challengeId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime deadlineTime;
    private AttemptStatus status;
    private Integer score;
    private Integer progress;
    private Integer attemptsUsed;
}