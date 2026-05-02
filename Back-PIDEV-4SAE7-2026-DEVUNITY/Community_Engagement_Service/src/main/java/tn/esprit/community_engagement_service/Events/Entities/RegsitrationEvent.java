package tn.esprit.community_engagement_service.Events.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class RegsitrationEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime registrationDate;

    @Enumerated(EnumType.STRING)
    private RegistrationStatus status;

    private String comment;

    // ✅ Ticket ID unique, généré une seule fois
    @Column(unique = true, updatable = false)
    private String ticketId;

    @ManyToOne
    private Events events;

    private Integer user;
}