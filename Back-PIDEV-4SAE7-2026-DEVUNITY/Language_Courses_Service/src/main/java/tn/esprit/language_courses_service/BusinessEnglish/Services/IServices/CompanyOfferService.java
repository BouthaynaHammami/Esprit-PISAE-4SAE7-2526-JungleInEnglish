package tn.esprit.language_courses_service.BusinessEnglish.Services.IServices;

import tn.esprit.language_courses_service.BusinessEnglish.Entities.ApprovalStatus;
import tn.esprit.language_courses_service.BusinessEnglish.Entities.CompanyOffer;
import tn.esprit.language_courses_service.BusinessEnglish.Entities.EmployeeInvitation;
import tn.esprit.language_courses_service.BusinessEnglish.Entities.PaymentStatus;
import tn.esprit.language_courses_service.DTO.CompanyOfferDTO;
import tn.esprit.language_courses_service.DTO.CompanyOfferRequestDTO;

import java.util.List;

public interface CompanyOfferService {
    CompanyOffer addCompanyOffer(CompanyOffer companyOffer);
    CompanyOffer updateCompanyOffer(Long id, CompanyOffer companyOffer);
    List<CompanyOffer> getAllCompanyOffers();
    CompanyOffer getCompanyOfferById(Long id);
    void deleteCompanyOffer(Long id);

    CompanyOffer updateApprovalStatus(Long companyOfferId, ApprovalStatus status);
    CompanyOffer updatePaymentStatus(Long companyOfferId, PaymentStatus status);
    List<EmployeeInvitation> requestOffer(Long companyId, Long offerId, List<String> emails);
    List<CompanyOfferRequestDTO> getAllRequestsForAdmin();
    List<CompanyOfferDTO> getStudentOffers(Integer studentId);
}
