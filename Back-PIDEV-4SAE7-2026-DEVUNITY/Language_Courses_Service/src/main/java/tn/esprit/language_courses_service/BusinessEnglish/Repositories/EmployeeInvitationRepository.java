package tn.esprit.language_courses_service.BusinessEnglish.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.language_courses_service.BusinessEnglish.Entities.EmployeeInvitation;
import tn.esprit.language_courses_service.BusinessEnglish.Entities.InvitationStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeInvitationRepository extends JpaRepository<EmployeeInvitation, Long> {

    List<EmployeeInvitation> findAllByCompanyOffer_IdCompanyOffer(Long companyOfferId);

    List<EmployeeInvitation> findByCompanyOffer_IdCompanyOfferAndStatus(
            Long companyOfferId,
            InvitationStatus status
    );

    List<EmployeeInvitation> findAllByStatus(InvitationStatus status);

    boolean existsByCompanyOffer_IdCompanyOfferAndEmail(
            Long companyOfferId,
            String email
    );

    Optional<EmployeeInvitation> findByEmailAndActivationCode(
            String email,
            String activationCode
    );

    boolean existsByActivationCode(String activationCode);

    List<EmployeeInvitation> findByCompanyOffer_IdCompanyOffer(long companyOfferId);

}