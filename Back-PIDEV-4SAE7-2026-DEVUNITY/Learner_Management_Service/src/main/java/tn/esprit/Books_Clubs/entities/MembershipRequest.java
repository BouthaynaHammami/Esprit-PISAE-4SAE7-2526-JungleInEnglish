package tn.esprit.Books_Clubs.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "membership_requests")
public class MembershipRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    private Long memberId;

    private String motivation;

    private LocalDate requestDate;
    private LocalDate decisionDate;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    private Long decidedByMemberId;

    @ManyToOne
    private Club club;
}