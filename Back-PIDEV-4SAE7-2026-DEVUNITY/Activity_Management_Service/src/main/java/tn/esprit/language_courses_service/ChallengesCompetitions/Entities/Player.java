package tn.esprit.language_courses_service.ChallengesCompetitions.Entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    private String username;
    private Long idUser;
    private int score;
    private int totalScore;
    private boolean active;
    private boolean eliminated;
    private String eliminationReason;
    private boolean hasAnsweredCurrentQuestion;
}