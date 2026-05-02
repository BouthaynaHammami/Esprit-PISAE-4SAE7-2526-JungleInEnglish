package tn.esprit.employee.Services.ImplServices;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.employee.Dto.UserDTO;
import tn.esprit.employee.Entities.Applicant;
import tn.esprit.employee.Entities.ApplicantStatus;
import tn.esprit.employee.Entities.NotificationType;
import tn.esprit.employee.Feign.EmployeeUserClient;
import tn.esprit.employee.Repositories.ApplicantRepository;
import tn.esprit.employee.Services.IServices.IApplicantService;
import java.util.List;

import tn.esprit.employee.Dto.Role;
import tn.esprit.employee.Services.IServices.INotificationService;

@Service
@RequiredArgsConstructor
public class ApplicantServiceImpl implements IApplicantService {

    private final ApplicantRepository applicantRepository;
    private final EmployeeUserClient userClient;
    private final INotificationService notificationService;

    @Override
    public List<Applicant> getAll() {
        List<Applicant> applicants = applicantRepository.findAll();
        applicants.forEach(this::populateNames);
        return applicants;
    }

    @Override
    public Applicant getById(Long id) {
        Applicant applicant = applicantRepository.findById(id).orElse(null);
        if (applicant != null) populateNames(applicant);
        return applicant;
    }

    @Override
    public void populateNames(Applicant applicant) {
        if (applicant != null && applicant.getUserId() != null) {
            try {
                UserDTO user = userClient.getUserById(applicant.getUserId());
                if (user != null) {
                    applicant.setFirstName(user.getFirstName());
                    applicant.setLastName(user.getLastName());
                }
            } catch (Exception e) {
                System.err.println("Failed to fetch user names: " + e.getMessage());
            }
        }
    }

    @Override
    public Applicant create(Applicant applicant) {
        if (applicant.getUserId() != null && applicant.getRecruitment() != null && applicant.getRecruitment().getId() != null) {
            if (applicantRepository.existsByUserIdAndRecruitmentId(applicant.getUserId(), applicant.getRecruitment().getId())) {
                throw new IllegalStateException("Vous avez déjà postulé à ce recrutement.");
            }
        }
        
        if (applicant.getStatus() == null) {
            applicant.setStatus(ApplicantStatus.PENDING);
        }
        Applicant saved = applicantRepository.save(applicant);

        // Notify Admins
        try {
            notificationService.sendToRole(
                Role.ADMIN, 
                "New Application", 
                "A new application has been received for Applicant ID: " + saved.getUserId(), 
                NotificationType.APPLICANT_CREATED,
                "Applicant",
                saved.getId()
            );
        } catch (Exception e) {
            // Log error but don't fail the application
            System.err.println("[ApplicantService] CRITICAL: Failed to send notification to ADMIN: " + e.getMessage());
            e.printStackTrace();
        }

        populateNames(saved);
        return saved;
    }

    @Override
    public Applicant update(Long id, Applicant applicant) {
        applicant.setId(id);
        return applicantRepository.save(applicant);
    }

    @Override
    public void delete(Long id) {
        applicantRepository.deleteById(id);
    }

    @Override
    public List<Applicant> getByUserId(Long userId) {
        return applicantRepository.findByUserId(userId);
    }

    @Override
    public List<Applicant> getByRecruitmentId(Long recruitmentId) {
        return applicantRepository.findByRecruitmentId(recruitmentId);
    }

    @Override
    public List<Applicant> getByInterviewId(Long interviewId) {
        return applicantRepository.findByInterviewId(interviewId);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userClient.getAllUsers();
    }

    @Override
    public UserDTO getUserOfApplicant(Long applicantId) {
        Applicant applicant = applicantRepository.findById(applicantId).orElse(null);
        if (applicant == null) return null;
        return userClient.getUserById(applicant.getUserId());
    }
}
