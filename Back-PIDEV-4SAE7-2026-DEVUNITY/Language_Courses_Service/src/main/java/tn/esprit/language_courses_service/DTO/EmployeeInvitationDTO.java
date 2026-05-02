package tn.esprit.language_courses_service.DTO;

import lombok.*;
import tn.esprit.language_courses_service.BusinessEnglish.Entities.InvitationStatus;

import java.time.LocalDateTime;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeInvitationDTO {
    private Long id;
    private String email;
    private InvitationStatus status;
    private LocalDate sentDate;
    private LocalDateTime expirationDate;
    private String activationCode;
}
