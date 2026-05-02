package tn.esprit.language_courses_service.ChallengesCompetitions.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChallengeAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idUser;

    @ManyToOne(fetch = FetchType.LAZY)
    private Challenge challenge;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private LocalDateTime deadlineTime;

    @Enumerated(EnumType.STRING)
    private AttemptStatus status;

    private Integer score;

    private Integer progress;

    private Integer attemptsUsed;
}