package tn.esprit.language_courses_service.BusinessEnglish.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.language_courses_service.BusinessEnglish.Entities.ApprovalStatus;
import tn.esprit.language_courses_service.BusinessEnglish.Entities.CompanyOffer;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyOfferRepository extends JpaRepository<CompanyOffer, Long> {
    Optional<CompanyOffer> findByCompanyIdAndOffer_IdAndApprovalStatus(
            Long companyId,
            Long offerId,
            ApprovalStatus approvalStatus
    );

    Optional<CompanyOffer> findByCompanyIdAndOffer_Id(
            Long companyId,
            Long offerId
    );

    List<CompanyOffer> findByApprovalStatus(ApprovalStatus status);

    List<CompanyOffer> findByStudentsContaining(Long studentId);
}
