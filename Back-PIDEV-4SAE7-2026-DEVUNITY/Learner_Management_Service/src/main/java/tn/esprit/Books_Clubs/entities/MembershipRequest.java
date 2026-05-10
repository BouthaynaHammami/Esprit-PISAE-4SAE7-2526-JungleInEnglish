package tn.esprit.Books_Clubs.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "membership_requests")
public class MembershipRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    @ManyToOne
    private User member;

    private String motivation;

    private LocalDate requestDate;
    private LocalDate decisionDate;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @ManyToOne
    private User decidedByMember;

    @ManyToOne
    private Club club;
}