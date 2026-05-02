package tn.esprit.language_courses_service.BusinessEnglish.Services.ImplServices;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.language_courses_service.BusinessEnglish.Entities.*;
import tn.esprit.language_courses_service.BusinessEnglish.Repositories.CompanyOfferRepository;
import tn.esprit.language_courses_service.BusinessEnglish.Repositories.EmployeeInvitationRepository;
import tn.esprit.language_courses_service.BusinessEnglish.Repositories.OfferRepository;
import tn.esprit.language_courses_service.BusinessEnglish.Services.IServices.CompanyOfferService;
import tn.esprit.language_courses_service.Clients.CourseClient;
import tn.esprit.language_courses_service.Clients.UserClient;
import tn.esprit.language_courses_service.DTO.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CompanyOfferServiceImpl implements CompanyOfferService {

    private final CompanyOfferRepository companyOfferRepository;
    private final OfferRepository offerRepository;
    private final EmployeeInvitationRepository employeeInvitationRepository;
    private final UserClient userClient;
    private final CourseClient courseClient;

    @Override
    public CompanyOffer addCompanyOffer(CompanyOffer companyOffer) {
        return companyOfferRepository.save(companyOffer);
    }

    @Override
    public CompanyOffer updateCompanyOffer(Long id, CompanyOffer companyOffer) {
        CompanyOffer existing = companyOfferRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("CompanyOffer not found"));
        companyOffer.setIdCompanyOffer(existing.getIdCompanyOffer());
        return companyOfferRepository.save(companyOffer);
    }

    @Override
    public List<CompanyOffer> getAllCompanyOffers() {
        return companyOfferRepository.findAll();
    }

    @Override
    public CompanyOffer getCompanyOfferById(Long id) {
        return companyOfferRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("CompanyOffer not found"));
    }

    @Override
    public void deleteCompanyOffer(Long id) {
        companyOfferRepository.deleteById(id);
    }

    // ================= PAYMENT =================
    @Override
    public CompanyOffer updatePaymentStatus(Long id, PaymentStatus status) {

        CompanyOffer co = companyOfferRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Not found"));

        co.setPaymentStatus(status);

        if (status == PaymentStatus.PAID) {
            co.setPaidDate(LocalDate.now());
        }

        return companyOfferRepository.save(co);
    }

    // ================= ADMIN =================
    @Override
    public List<CompanyOfferRequestDTO> getAllRequestsForAdmin() {

        return companyOfferRepository.findAll().stream().map(co -> {

            List<EmployeeInvitationDTO> invDtos = employeeInvitationRepository
                    .findAllByCompanyOffer_IdCompanyOffer(co.getIdCompanyOffer())
                    .stream()
                    .map(inv -> EmployeeInvitationDTO.builder()
                            .id(inv.getId())
                            .email(inv.getEmail())
                            .status(inv.getStatus())
                            .sentDate(inv.getSentDate())
                            .expirationDate(inv.getExpirationDate())
                            .activationCode(inv.getActivationCode())
                            .build())
                    .toList();

            UserDTO company = null;
            List<UserDTO> students = List.of();

            try {
                company = userClient.getUserById(co.getCompanyId());
                students = userClient.getCompanyStudents(co.getCompanyId());
            } catch (Exception ignored) {}

            return CompanyOfferRequestDTO.builder()
                    .companyOfferId(co.getIdCompanyOffer())
                    .companyId(co.getCompanyId())
                    .company(company)
                    .students(students)
                    .offerId(co.getOffer().getId())
                    .offerName(co.getOffer().getName())
                    .status(co.getApprovalStatus())
                    .requestedAt(co.getRequestedAt())
                    .invitations(invDtos)
                    .build();

        }).toList();
    }

    // ================= REQUEST =================
    @Override
    public List<EmployeeInvitation> requestOffer(Long companyId,
                                                 Long offerId,
                                                 List<String> emails) {

        if (emails == null || emails.isEmpty()) {
            throw new IllegalArgumentException("Email list cannot be empty");
        }

        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new IllegalArgumentException("Offer not found"));

        UserDTO company = userClient.getUserById(companyId);

        if (company == null || company.getRole() != RoleDTO.COMPANY) {
            throw new IllegalArgumentException("User is not a company");
        }

        CompanyOffer companyOffer = companyOfferRepository
                .findByCompanyIdAndOffer_IdAndApprovalStatus(companyId, offerId, ApprovalStatus.PENDING)
                .orElseGet(() -> {

                    CompanyOffer co = new CompanyOffer();
                    co.setCompanyId(companyId);
                    co.setOffer(offer);
                    co.setApprovalStatus(ApprovalStatus.PENDING);
                    co.setRequestedAt(LocalDate.now());

                    return companyOfferRepository.save(co);
                });

        List<EmployeeInvitation> created = new ArrayList<>();

        for (String rawEmail : emails) {

            if (rawEmail == null) continue;

            String email = rawEmail.trim().toLowerCase();

            if (email.isEmpty()) continue;

            boolean exists = employeeInvitationRepository
                    .existsByCompanyOffer_IdCompanyOfferAndEmail(
                            companyOffer.getIdCompanyOffer(),
                            email
                    );

            if (exists) continue;

            EmployeeInvitation invitation = new EmployeeInvitation();

            invitation.setEmail(email);
            invitation.setStatus(InvitationStatus.PENDING);
            invitation.setCompanyOffer(companyOffer);
            invitation.setOffer(offer);

            // ✅ CORRECT
            invitation.setSentDate(LocalDate.now());

            // ✅ CORRECT (LocalDateTime)
            invitation.setExpirationDate(
                    LocalDateTime.now().plusHours((int) offer.getDurationHours())
            );

            created.add(employeeInvitationRepository.save(invitation));
        }

        return created;
    }

    // ================= ADMIN STATUS =================
    @Override
    public CompanyOffer updateApprovalStatus(Long companyOfferId, ApprovalStatus status) {
        CompanyOffer co = companyOfferRepository.findById(companyOfferId)
                .orElseThrow(() -> new IllegalArgumentException("CompanyOffer not found"));
        co.setApprovalStatus(status);
        co.setDecidedAt(LocalDate.now());
        return companyOfferRepository.save(co);
    }

    @Override
    public List<CompanyOfferDTO> getStudentOffers(Integer studentId) {
        if (studentId == null) return List.of();

        List<CompanyOffer> entities = companyOfferRepository.findByStudentsContaining(studentId.longValue());

        return entities.stream().map(co -> {
            List<CourseDTO> courses = new ArrayList<>();
            if (co.getOffer() != null && co.getOffer().getCourseIds() != null) {
                for (Long courseId : co.getOffer().getCourseIds()) {
                    try {
                        CourseDTO course = courseClient.getCourseById(courseId);
                        if (course != null) courses.add(course);
                    } catch (Exception ignored) {}
                }
            }

            return CompanyOfferDTO.builder()
                    .idCompanyOffer(co.getIdCompanyOffer())
                    .paidDate(co.getPaidDate())
                    .requestedAt(co.getRequestedAt())
                    .decidedAt(co.getDecidedAt())
                    .paymentStatus(co.getPaymentStatus())
                    .approvalStatus(co.getApprovalStatus())
                    .companyId(co.getCompanyId())
                    .offer(co.getOffer())
                    .courses(courses)
                    .build();
        }).toList();
    }
}