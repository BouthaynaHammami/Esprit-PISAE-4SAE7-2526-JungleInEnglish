package tn.esprit.language_courses_service.BusinessEnglish.Services.ImplServices;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.language_courses_service.BusinessEnglish.Entities.*;
import tn.esprit.language_courses_service.BusinessEnglish.Repositories.CompanyOfferRepository;
import tn.esprit.language_courses_service.BusinessEnglish.Repositories.EmployeeInvitationRepository;
import tn.esprit.language_courses_service.BusinessEnglish.Services.IServices.EmployeeInvitationService;
import tn.esprit.language_courses_service.Clients.CourseClient;
import tn.esprit.language_courses_service.Clients.UserClient;
import tn.esprit.language_courses_service.DTO.ActivationResponseDTO;
import tn.esprit.language_courses_service.DTO.CompanyOfferDTO;
import tn.esprit.language_courses_service.DTO.CourseDTO;
import tn.esprit.language_courses_service.DTO.UserDTO;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class EmployeeInvitationServiceImpl implements EmployeeInvitationService {

    private final EmployeeInvitationRepository employeeInvitationRepository;
    private final CompanyOfferRepository companyOfferRepository;
    private final EmailServiceImpl emailService;
    private final UserClient userClient;
    private final CourseClient courseClient;
    private final tn.esprit.language_courses_service.BusinessEnglish.Services.IServices.CompanyOfferService companyOfferService;

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";

    @Override
    public EmployeeInvitation addEmployeeInvitation(EmployeeInvitation employeeInvitation) {
        if (employeeInvitation == null) {
            throw new IllegalArgumentException("EmployeeInvitation is null");
        }
        if (employeeInvitation.getEmail() == null || employeeInvitation.getEmail().trim().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        employeeInvitation.setEmail(employeeInvitation.getEmail().trim().toLowerCase());
        return employeeInvitationRepository.save(employeeInvitation);
    }

    @Override
    public EmployeeInvitation updateEmployeeInvitation(Long id, EmployeeInvitation employeeInvitation) {
        EmployeeInvitation existing = employeeInvitationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invitation not found"));

        existing.setEmail(employeeInvitation.getEmail() != null
                ? employeeInvitation.getEmail().trim().toLowerCase()
                : existing.getEmail());

        existing.setStatus(employeeInvitation.getStatus() != null
                ? employeeInvitation.getStatus()
                : existing.getStatus());

        return employeeInvitationRepository.save(existing);
    }

    @Override
    public List<EmployeeInvitation> getAllEmployeeInvitations() {
        return employeeInvitationRepository.findAll();
    }

    @Override
    public EmployeeInvitation getEmployeeInvitationById(Long id) {
        return employeeInvitationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invitation not found"));
    }

    @Override
    public void deleteEmployeeInvitation(Long id) {
        employeeInvitationRepository.deleteById(id);
    }

    @Override
    public List<EmployeeInvitation> getAllByStatus(InvitationStatus status) {
        return employeeInvitationRepository.findAllByStatus(status);
    }

    @Override
    public List<EmployeeInvitation> getInvitationsByCompanyOfferAndStatus(Long companyOfferId, InvitationStatus status) {
        return employeeInvitationRepository.findByCompanyOffer_IdCompanyOfferAndStatus(companyOfferId, status);
    }

    @Override
    public List<EmployeeInvitation> updateEmailsForCompanyOffer(
            Long companyOfferId,
            List<String> emails
    ) {
        if (emails == null || emails.isEmpty()) {
            throw new IllegalArgumentException("Email list cannot be empty");
        }
        List<EmployeeInvitation> invitations =
                employeeInvitationRepository.findByCompanyOffer_IdCompanyOffer(companyOfferId);
        if (invitations.isEmpty()) {
            throw new IllegalArgumentException("No invitations found");
        }
        int i = 0;
        for (EmployeeInvitation inv : invitations) {
            if (i >= emails.size()) break;
            String email = emails.get(i);
            if (email != null && !email.trim().isEmpty()) {
                inv.setEmail(email.trim().toLowerCase());
            }
            i++;
        }
        return employeeInvitationRepository.saveAll(invitations);
    }

    // ================= APPROVE =================
    @Override
    public List<EmployeeInvitation> approveAndSendCodes(Long companyOfferId) {

        CompanyOffer companyOffer = companyOfferRepository.findById(companyOfferId)
                .orElseThrow(() -> new IllegalArgumentException("CompanyOffer not found"));

        if (companyOffer.getPaymentStatus() != PaymentStatus.PAID) {
            throw new IllegalStateException("Payment must be PAID first");
        }

        List<EmployeeInvitation> invitations =
                employeeInvitationRepository.findByCompanyOffer_IdCompanyOfferAndStatus(
                        companyOfferId,
                        InvitationStatus.PENDING
                );

        LocalDateTime now = LocalDateTime.now();

        for (EmployeeInvitation inv : invitations) {

            String code = generateCode(8);

            inv.setActivationCode(code);

            // ✅ date d'envoi
            inv.setSentDate(LocalDate.now());

            // ✅ expiration calculée ICI
            int hours = (int) companyOffer.getOffer().getDurationHours();
            inv.setExpirationDate(now.plusHours(hours));

            inv.setStatus(InvitationStatus.SENT);

            // ✅ email
            emailService.sendInvitationEmail(inv.getEmail(), code);
        }

        employeeInvitationRepository.saveAll(invitations);

        companyOffer.setApprovalStatus(ApprovalStatus.APPROVED);
        companyOffer.setDecidedAt(LocalDate.now());

        companyOfferRepository.save(companyOffer);

        return invitations;
    }

    // ================= REJECT =================
    @Override
    public List<EmployeeInvitation> rejectCompanyOffer(Long companyOfferId) {

        CompanyOffer co = companyOfferRepository.findById(companyOfferId)
                .orElseThrow(() -> new IllegalArgumentException("CompanyOffer not found"));

        List<EmployeeInvitation> pending =
                employeeInvitationRepository.findByCompanyOffer_IdCompanyOfferAndStatus(
                        companyOfferId,
                        InvitationStatus.PENDING
                );

        for (EmployeeInvitation inv : pending) {
            inv.setStatus(InvitationStatus.REJECTED);
        }

        co.setApprovalStatus(ApprovalStatus.REJECTED);
        co.setDecidedAt(LocalDate.now());

        companyOfferRepository.save(co);

        // ✅ EMAIL COMPANY
        try {
            String email = userClient.getUserById(co.getCompanyId()).getEmail();
            emailService.sendRejectionEmail(email, co.getOffer().getName());
        } catch (Exception e) {
            System.out.println("Error sending rejection email");
        }

        return employeeInvitationRepository.saveAll(pending);
    }

    @Override
    public ActivationResponseDTO activate(Integer studentId, String email, String code) {

        String normalizedEmail = email.trim().toLowerCase();

        EmployeeInvitation invitation = employeeInvitationRepository
                .findByEmailAndActivationCode(normalizedEmail, code)
                .orElseThrow(() -> new IllegalArgumentException("Invitation not found"));

        if (invitation.getStatus() == InvitationStatus.REJECTED) {
            throw new IllegalArgumentException("Invitation rejected");
        }

        if (invitation.getStatus() == InvitationStatus.USED) {
            List<CourseDTO> courses = fetchCourses(invitation.getOffer());
            List<CompanyOfferDTO> allOffers = companyOfferService.getStudentOffers(studentId);
            return new ActivationResponseDTO(invitation, courses, allOffers);
        }

        if (invitation.getExpirationDate().isBefore(LocalDateTime.now())) {
            invitation.setStatus(InvitationStatus.EXPIRED);
            employeeInvitationRepository.save(invitation);
            return new ActivationResponseDTO(invitation, new ArrayList<>(), new ArrayList<>());
        }

        // resolve student
        Integer effectiveStudentId = studentId;

        if (effectiveStudentId == null) {
            try {
                UserDTO user = userClient.getUserByEmail(normalizedEmail);
                if (user != null) {
                    effectiveStudentId = user.getUserId();
                }
            } catch (Exception e) {
                System.out.println("Cannot resolve user");
            }
        }

        // link student to company offer
        if (effectiveStudentId != null && invitation.getCompanyOffer() != null) {

            CompanyOffer companyOffer = invitation.getCompanyOffer();

            if (companyOffer.getStudents() == null) {
                companyOffer.setStudents(new ArrayList<>());
            }

            Long sid = effectiveStudentId.longValue();

            if (!companyOffer.getStudents().contains(sid)) {
                companyOffer.getStudents().add(sid);
                companyOfferRepository.save(companyOffer);
            }
        }

        invitation.setStatus(InvitationStatus.USED);
        EmployeeInvitation saved = employeeInvitationRepository.save(invitation);

        List<CourseDTO> courses = fetchCourses(saved.getOffer());
        List<CompanyOfferDTO> allOffers = companyOfferService.getStudentOffers(effectiveStudentId);

        return new ActivationResponseDTO(saved, courses, allOffers);
    }

    private List<CourseDTO> fetchCourses(Offer offer) {
        List<CourseDTO> courses = new ArrayList<>();
        if (offer != null && offer.getCourseIds() != null) {
            for (Long courseId : offer.getCourseIds()) {
                try {
                    CourseDTO course = courseClient.getCourseById(courseId);
                    if (course != null) {
                        courses.add(course);
                    }
                } catch (Exception e) {
                    System.out.println("Could not fetch course: " + courseId);
                }
            }
        }
        return courses;
    }

    private String generateCode(int length) {
        StringBuilder code = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            code.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }

        return code.toString();
    }
}