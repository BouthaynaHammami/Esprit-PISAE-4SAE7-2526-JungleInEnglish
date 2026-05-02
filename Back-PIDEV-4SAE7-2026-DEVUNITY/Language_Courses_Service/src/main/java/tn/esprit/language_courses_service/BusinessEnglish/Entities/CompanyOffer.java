package tn.esprit.language_courses_service.BusinessEnglish.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "company_offer_id")
    private Long idCompanyOffer;
    private LocalDate paidDate;
    private LocalDate requestedAt;
    private LocalDate decidedAt;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;

    @Column(name = "company_id")
    private Long companyId;

    @ElementCollection
    private List<Long> students = new ArrayList<>();

    @ManyToOne
    private Offer offer;
}
