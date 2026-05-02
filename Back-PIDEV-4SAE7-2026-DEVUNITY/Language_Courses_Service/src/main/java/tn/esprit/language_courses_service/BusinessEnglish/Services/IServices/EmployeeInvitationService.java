package tn.esprit.language_courses_service.BusinessEnglish.Services.IServices;

import tn.esprit.language_courses_service.BusinessEnglish.Entities.EmployeeInvitation;
import tn.esprit.language_courses_service.BusinessEnglish.Entities.InvitationStatus;
import tn.esprit.language_courses_service.DTO.ActivationResponseDTO;

import java.util.List;

public interface EmployeeInvitationService {
    EmployeeInvitation addEmployeeInvitation(EmployeeInvitation employeeInvitation);
    EmployeeInvitation updateEmployeeInvitation(Long id, EmployeeInvitation employeeInvitation);
    List<EmployeeInvitation> getAllEmployeeInvitations();
    EmployeeInvitation getEmployeeInvitationById(Long id);
    void deleteEmployeeInvitation(Long id);

    List<EmployeeInvitation> updateEmailsForCompanyOffer(Long companyOfferId, List<String> emails);
    // récupérer invitations par statut
    List<EmployeeInvitation> getAllByStatus(InvitationStatus status);
    // récupérer invitations d'une demande (CompanyOffer)
    List<EmployeeInvitation> getInvitationsByCompanyOfferAndStatus(Long companyOfferId, InvitationStatus status);
    // ADMIN approve → génère codes + envoi
    List<EmployeeInvitation> approveAndSendCodes(Long companyOfferId);
    // ADMIN reject
    List<EmployeeInvitation> rejectCompanyOffer(Long companyOfferId);
    // STUDENT activation
    ActivationResponseDTO activate(Integer studentId, String email, String code);
}
