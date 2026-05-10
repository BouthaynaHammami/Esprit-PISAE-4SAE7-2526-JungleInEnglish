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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "excursions")
public class Excursion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long excursionId;

    private String title;
    private String description;
    private String location;

    private LocalDate startDate;
    private LocalDate endDate;
    private Integer nbrDeplace;
    private Integer nbrDeReservation = 0;

    @Enumerated(EnumType.STRING)
    private ActivityStatus status;

    @ManyToOne
    private Club club;

    @JsonIgnore
    @OneToMany(mappedBy = "excursion", cascade = CascadeType.ALL)
    private List<ExcursionParticipation> participations;
}