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
@Table(name = "participation_clubs")
@Entity
public class ParticipationClub {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long participationId;

    @ManyToOne
    private User member;

    private LocalDate joinDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private MembershipRole role;

    @Enumerated(EnumType.STRING)
    private MembershipStatus status;

    @ManyToOne
    private Club club;
}