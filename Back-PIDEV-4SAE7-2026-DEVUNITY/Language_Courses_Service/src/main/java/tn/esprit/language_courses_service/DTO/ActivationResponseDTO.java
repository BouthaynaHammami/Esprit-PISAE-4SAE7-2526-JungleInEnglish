package tn.esprit.language_courses_service.DTO;

import lombok.*;
import tn.esprit.language_courses_service.BusinessEnglish.Entities.EmployeeInvitation;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivationResponseDTO {
    private EmployeeInvitation invitation;
    private List<CourseDTO> courses;
    private List<CompanyOfferDTO> enrolledOffers;
}
