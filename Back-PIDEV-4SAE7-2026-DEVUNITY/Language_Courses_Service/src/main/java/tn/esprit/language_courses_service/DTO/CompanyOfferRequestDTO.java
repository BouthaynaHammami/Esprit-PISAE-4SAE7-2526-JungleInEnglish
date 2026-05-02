package tn.esprit.language_courses_service.DTO;

import lombok.*;
import tn.esprit.language_courses_service.BusinessEnglish.Entities.ApprovalStatus;
import tn.esprit.language_courses_service.BusinessEnglish.Entities.PaymentStatus;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyOfferRequestDTO {
    private Long companyOfferId;

    private Long companyId;
    private UserDTO company;

    private Long offerId;
    private String offerName;

    private ApprovalStatus status;
    private PaymentStatus paymentStatus;

    private LocalDate requestedAt;
    private LocalDate decidedAt;
    private LocalDate paidDate;

    private List<UserDTO> students;
    private List<EmployeeInvitationDTO> invitations;
}
