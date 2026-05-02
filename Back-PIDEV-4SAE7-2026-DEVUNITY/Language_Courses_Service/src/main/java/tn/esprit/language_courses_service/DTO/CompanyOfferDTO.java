package tn.esprit.language_courses_service.DTO;

import lombok.*;
import tn.esprit.language_courses_service.BusinessEnglish.Entities.ApprovalStatus;
import tn.esprit.language_courses_service.BusinessEnglish.Entities.Offer;
import tn.esprit.language_courses_service.BusinessEnglish.Entities.PaymentStatus;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyOfferDTO {
    private Long idCompanyOffer;
    private LocalDate paidDate;
    private LocalDate requestedAt;
    private LocalDate decidedAt;
    private PaymentStatus paymentStatus;
    private ApprovalStatus approvalStatus;
    private Long companyId;
    private Offer offer;
    private List<CourseDTO> courses;
}
