package tn.esprit.employee.Services.ImplServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.employee.Dto.CvAnalysisResult;
import tn.esprit.employee.Dto.UserDTO;
import tn.esprit.employee.Entities.Applicant;
import tn.esprit.employee.Entities.ApplicantStatus;
import tn.esprit.employee.Entities.NotificationType;
import tn.esprit.employee.Entities.Recruitment;
import tn.esprit.employee.Feign.EmployeeUserClient;
import tn.esprit.employee.Repositories.ApplicantRepository;
import tn.esprit.employee.Repositories.RecruitmentRepository;
import tn.esprit.employee.Services.IServices.IApplicantService;
import java.util.List;
import java.util.Map;

import tn.esprit.employee.Dto.Role;
import tn.esprit.employee.Services.IServices.ICvAnalysisService;
import tn.esprit.employee.Services.IServices.INotificationService;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicantServiceImpl implements IApplicantService {

    private final ApplicantRepository applicantRepository;
    private final RecruitmentRepository recruitmentRepository;
    private final EmployeeUserClient userClient;
    private final INotificationService notificationService;
    private final ICvAnalysisService cvAnalysisService;
    private final Cloudinary cloudinary;

    @Override
    public String uploadCv(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
            return (String) uploadResult.get("url");
        } catch (Exception e) {
            log.error("Failed to upload CV to Cloudinary", e);
            throw new RuntimeException("CV upload failed: " + e.getMessage());
        }
    }

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
                if (user != null && user.getFirstName() != null) {
                    applicant.setFirstName(user.getFirstName());
                    applicant.setLastName(user.getLastName());
                } else {
                    applicant.setFirstName("User");
                    applicant.setLastName(applicant.getUserId().toString());
                }
            } catch (Exception e) {
                log.warn("Failed to fetch user names for user {}: {}", applicant.getUserId(), e.getMessage());
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

    @Override
    public CvAnalysisResult analyzeCvWithML(Long applicantId, Long recruitmentId) {
        log.info("Starting CV analysis for applicant ID: {}", applicantId);
        
        // 1. Récupérer le candidat
        Applicant applicant = applicantRepository.findById(applicantId)
                .orElseThrow(() -> new IllegalArgumentException("Applicant not found with ID: " + applicantId));
        
        // 2. Vérifier que le CV existe
        if (applicant.getCv() == null || applicant.getCv().isEmpty()) {
            throw new IllegalArgumentException("No CV found for applicant ID: " + applicantId);
        }
        
        // 3. Récupérer les informations du recrutement
        String skills = "";
        Integer experienceYears = 0;
        Recruitment recruitment = null;
        
        if (recruitmentId != null) {
            recruitment = recruitmentRepository.findById(recruitmentId).orElse(null);
        } else if (applicant.getRecruitment() != null) {
            recruitment = applicant.getRecruitment();
        }

        if (recruitment != null) {
            skills = recruitment.getRequiredSkills() != null ? recruitment.getRequiredSkills() : recruitment.getPositionTitle();
            experienceYears = recruitment.getExperienceYears() != null ? recruitment.getExperienceYears() : 0;
        }
        
        log.info("Analyzing CV from URL: {} with skills: {} and exp: {}", applicant.getCv(), skills, experienceYears);
        
        // 4. Analyser le CV
        try {
            String cvUrl = applicant.getCv();
            log.info("CV URL from database: {}", cvUrl);
            
            // Vérifier si c'est un lien Google Docs ou autre lien non-PDF
            if (cvUrl != null && (cvUrl.contains("docs.google.com") || cvUrl.contains("drive.google.com"))) {
                log.warn("CV is a Google Docs link, using placeholder text for analysis");
                // Pour les liens Google Docs, utiliser un texte générique
                String placeholderText = "Candidate CV - Skills: " + skills + ", Experience: " + experienceYears + " years. Position: " + skills;
                CvAnalysisResult result = cvAnalysisService.analyzeCvFromText(placeholderText, skills, experienceYears);
                log.info("CV analysis completed: decision={}, score={}", result.getDecision(), result.getScore());
                
                // Mettre à jour le statut du candidat automatiquement
                updateApplicantStatus(applicant, result);
                
                return result;
            }
            
            // Vérifier si c'est une URL valide (Cloudinary ou autre)
            if (cvUrl != null && (cvUrl.startsWith("http://") || cvUrl.startsWith("https://"))) {
                log.info("CV is a URL, attempting to download and analyze");
                CvAnalysisResult result = cvAnalysisService.analyzeCvFromUrl(cvUrl, skills, experienceYears);
                log.info("CV analysis completed: decision={}, score={}", result.getDecision(), result.getScore());
                
                // Mettre à jour le statut du candidat automatiquement
                updateApplicantStatus(applicant, result);
                
                return result;
            }
            
            // Si ce n'est pas une URL, utiliser le texte directement
            log.warn("CV is not a URL, using as text for analysis");
            CvAnalysisResult result = cvAnalysisService.analyzeCvFromText(cvUrl, skills, experienceYears);
            log.info("CV analysis completed: decision={}, score={}", result.getDecision(), result.getScore());
            
            // Mettre à jour le statut du candidat automatiquement
            updateApplicantStatus(applicant, result);
            
            return result;
            
        } catch (Exception e) {
            log.error("Error during CV analysis", e);
            throw new RuntimeException("Failed to analyze CV: " + e.getMessage(), e);
        }
    }
    
    /**
     * Met à jour automatiquement le statut du candidat selon la décision ML
     */
    private void updateApplicantStatus(Applicant applicant, CvAnalysisResult result) {
        ApplicantStatus newStatus;
        String notificationMessage;
        String title = "Recruitment AI Analysis";
        
        // Stocker le résultat IA dans le candidat
        applicant.setAiResult(result);
        
        switch (result.getDecision()) {
            case "ACCEPTED":
                newStatus = ApplicantStatus.PENDING; // Keep PENDING but with high AI score for admin to decide
                notificationMessage = String.format("Good news! Your CV for '%s' was analyzed as a '%s' profile with a score of %d/100.", 
                    applicant.getRecruitment() != null ? applicant.getRecruitment().getPositionTitle() : "the position",
                    result.getCluster(), result.getScore());
                break;
                
            case "REJECTED":
                newStatus = ApplicantStatus.REJECTED;
                notificationMessage = String.format("We regret to inform you that your profile for '%s' does not match our current requirements (AI Score: %d/100).", 
                    applicant.getRecruitment() != null ? applicant.getRecruitment().getPositionTitle() : "the position",
                    result.getScore());
                break;
                
            case "PENDING":
            default:
                newStatus = ApplicantStatus.PENDING;
                notificationMessage = String.format("Your CV for '%s' is being reviewed by our team. AI Cluster: %s.", 
                    applicant.getRecruitment() != null ? applicant.getRecruitment().getPositionTitle() : "the position",
                    result.getCluster());
                break;
        }
        
        // Mettre à jour le statut
        applicant.setStatus(newStatus);
        applicantRepository.save(applicant);
        log.info("Updated applicant {} status to {} with AI decision {}", applicant.getId(), newStatus, result.getDecision());
        
        // Envoyer une notification WebSocket au candidat
        try {
            if (applicant.getUserId() != null) {
                UserDTO user = userClient.getUserById(applicant.getUserId());
                if (user != null && user.getEmail() != null) {
                    notificationService.createNotification(
                        user.getEmail(),
                        user.getUserId(),
                        title,
                        notificationMessage,
                        NotificationType.APPLICANT_STATUS_CHANGED,
                        "Applicant",
                        applicant.getId()
                    );
                    log.info("WebSocket Notification sent to user {}", user.getEmail());
                }
            }
        } catch (Exception e) {
            log.warn("Failed to notify applicant about status change: {}", e.getMessage());
        }
    }
}
