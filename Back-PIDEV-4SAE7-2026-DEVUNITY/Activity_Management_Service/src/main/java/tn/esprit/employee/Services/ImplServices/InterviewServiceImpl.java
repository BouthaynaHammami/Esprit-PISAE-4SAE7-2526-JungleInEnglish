package tn.esprit.employee.Services.ImplServices;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.employee.Dto.UserDTO;
import tn.esprit.employee.Entities.Interview;
import tn.esprit.employee.Feign.EmployeeUserClient;
import tn.esprit.employee.Repositories.InterviewRepository;
import tn.esprit.employee.Services.IServices.IInterviewService;
import tn.esprit.employee.Services.IServices.INotificationService;
import tn.esprit.employee.Entities.NotificationType;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewServiceImpl implements IInterviewService {

    private final InterviewRepository interviewRepository;
    private final EmployeeUserClient userClient;
    private final INotificationService notificationService;

    @Override
    public List<Interview> getAll() {
        return interviewRepository.findAll();
    }

    @Override
    public Interview getById(Long id) {
        return interviewRepository.findById(id).orElse(null);
    }

    @Override
    public Interview create(Interview interview) {
        Interview saved = interviewRepository.save(interview);

        // Notify User
        try {
            if (saved.getUserId() != null) {
                UserDTO user = userClient.getUserById(saved.getUserId());
                if (user != null) {
                    notificationService.createNotification(
                        user.getEmail(),
                        saved.getUserId(),
                        "Interview Scheduled",
                        "A new interview has been scheduled: " + saved.getTitle(),
                        NotificationType.INTERVIEW_SCHEDULED,
                        "Interview",
                        saved.getId()
                    );
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to send interview notification: " + e.getMessage());
        }

        return saved;
    }

    @Override
    public Interview update(Long id, Interview interview) {
        interview.setId(id);
        return interviewRepository.save(interview);
    }

    @Override
    public void delete(Long id) {
        interviewRepository.deleteById(id);
    }

    @Override
    public List<Interview> getByRecruitmentId(Long recruitmentId) {
        return interviewRepository.findByRecruitmentId(recruitmentId);
    }

    @Override
    public List<Interview> getByUserId(Long userId) {
        return interviewRepository.findByUserId(userId);
    }

    @Override
    public UserDTO getUserOfInterview(Long interviewId) {
        Interview interview = interviewRepository.findById(interviewId).orElse(null);
        if (interview == null) return null;
        return userClient.getUserById(interview.getUserId());
    }
}
