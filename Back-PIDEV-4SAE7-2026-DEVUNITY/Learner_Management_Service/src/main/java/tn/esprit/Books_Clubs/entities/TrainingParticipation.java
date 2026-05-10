package tn.esprit.Books_Clubs.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "training_participations")
public class TrainingParticipation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private User member;

    private LocalDate registrationDate;

    @Enumerated(EnumType.STRING)
    private ParticipationStatus status;

    private Integer score;

    private Boolean completed = false;
    private Boolean rewardTransferred = false;

    private BigDecimal rewardAmount;

    @ManyToOne
    @JoinColumn(name = "training_id")
    private Training training;
}