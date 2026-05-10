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
@Table(name = "clubs")
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clubId;

    private String name;
    private String description;
    private LocalDate creationDate;

    @Enumerated(EnumType.STRING)
    private ClubRole type;

    @Enumerated(EnumType.STRING)
    private ClubStatus status;

    @JsonIgnore
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    private List<ParticipationClub> participations;

    @JsonIgnore
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    private List<MembershipRequest> requests;

    @JsonIgnore
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    private List<Excursion> excursions;

    @JsonIgnore
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    private List<Training> trainings;
}