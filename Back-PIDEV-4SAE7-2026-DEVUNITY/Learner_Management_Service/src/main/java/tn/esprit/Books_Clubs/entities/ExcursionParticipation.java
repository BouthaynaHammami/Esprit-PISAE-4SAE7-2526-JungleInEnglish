package tn.esprit.Books_Clubs.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "excursion_participations")
@Entity
public class ExcursionParticipation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User member;

    private LocalDate registrationDate;

    @Enumerated(EnumType.STRING)
    private ParticipationStatus status;

    @ManyToOne
    private Excursion excursion;
}