package tn.esprit.Books_Clubs.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "trainings")
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trainingId;

    private String title;
    private String description;
    private String trainer;

    private LocalDate startDate;
    private LocalDate endDate;
    private Integer nbrDeplace;
    private Integer nbrDeReservation = 0;

    @Enumerated(EnumType.STRING)
    private ActivityStatus status;

    @ManyToOne
    private Club club;

    @JsonIgnore
    @OneToMany(mappedBy = "training", cascade = CascadeType.ALL)
    private List<TrainingParticipation> participations;
}