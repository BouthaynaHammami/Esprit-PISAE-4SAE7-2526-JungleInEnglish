package tn.esprit.language_courses_service.BusinessEnglish.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeInvitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String activationCode;
    private LocalDate sentDate;
    private LocalDateTime expirationDate;

    @Enumerated(EnumType.STRING)
    private InvitationStatus status;

    @ManyToOne
    @JsonIgnore
    private CompanyOffer companyOffer;

    @ManyToOne
    @JsonIgnore
    private Offer offer;
}

