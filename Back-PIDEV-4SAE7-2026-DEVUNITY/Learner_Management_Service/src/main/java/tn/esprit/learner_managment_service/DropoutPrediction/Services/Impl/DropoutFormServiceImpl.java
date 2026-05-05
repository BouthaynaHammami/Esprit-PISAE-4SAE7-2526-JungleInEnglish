package tn.esprit.learner_managment_service.DropoutPrediction.Services.Impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import tn.esprit.learner_managment_service.DropoutPrediction.Dto.DropoutFormRequest;
import tn.esprit.learner_managment_service.DropoutPrediction.Dto.DropoutFormResponse;
import tn.esprit.learner_managment_service.DropoutPrediction.Dto.MlPredictRequest;
import tn.esprit.learner_managment_service.DropoutPrediction.Dto.MlPredictResponse;
import tn.esprit.learner_managment_service.DropoutPrediction.Entities.DropoutForm;
import tn.esprit.learner_managment_service.DropoutPrediction.Repositories.DropoutFormRepository;
import tn.esprit.learner_managment_service.DropoutPrediction.Services.DropoutFormService;
import tn.esprit.learner_managment_service.UserManagement.Entities.User;
import tn.esprit.learner_managment_service.UserManagement.Exceptions.UserNotFoundException;
import tn.esprit.learner_managment_service.UserManagement.Repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class DropoutFormServiceImpl implements DropoutFormService {

    private final DropoutFormRepository dropoutFormRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Value("${ml.service.base-url}")
    private String mlServiceBaseUrl;

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated");
        }

        String email = null;

        // Extract email from JWT claims if available
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            email = jwt.getClaimAsString("email");
        }

        // Fallback: try to get from authentication name
        if (email == null || email.isBlank()) {
            email = authentication.getName();
        }

        if (email == null || email.isBlank() || "anonymousUser".equalsIgnoreCase(email)) {
            throw new IllegalStateException("User is not authenticated");
        }

        String finalEmail = email;
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("User not found with email: " + finalEmail));
    }

    @Override
    public DropoutFormResponse createForm(DropoutFormRequest request) {
        User user = getAuthenticatedUser();

        MlPredictRequest mlRequest = MlPredictRequest.builder()
            .motivationLevel(request.getMotivationLevel())
            .weeklyStudyHours(request.getWeeklyStudyHours())
            .freeTimeHoursPerWeek(request.getFreeTimeHoursPerWeek())
            .satisfactionLevel(request.getSatisfactionLevel())
            .preferredLearningMode(request.getPreferredLearningMode())
            .attendanceCommitment(request.getAttendanceCommitment())
            .homeworkCompletionSelf(request.getHomeworkCompletionSelf())
            .financialStressLevel(request.getFinancialStressLevel())
            .interactionWithTeacher(request.getInteractionWithTeacher())
            .englishLevelSelf(request.getEnglishLevelSelf())
            .goalClarityLevel(request.getGoalClarityLevel())
            .classDifficultyLevel(request.getClassDifficultyLevel())
            .peerInteractionLevel(request.getPeerInteractionLevel())
            .technicalIssuesFrequency(request.getTechnicalIssuesFrequency())
            .build();

        ResponseEntity<MlPredictResponse> response = restTemplate.postForEntity(
            mlServiceBaseUrl + "/predict",
            mlRequest,
            MlPredictResponse.class
        );

        MlPredictResponse mlResponse = response.getBody();
        if (mlResponse == null) {
            throw new IllegalStateException("ML response body is empty");
        }

        DropoutForm form = DropoutForm.builder()
            .user(user)
            .motivationLevel(request.getMotivationLevel())
            .weeklyStudyHours(request.getWeeklyStudyHours())
            .freeTimeHoursPerWeek(request.getFreeTimeHoursPerWeek())
            .satisfactionLevel(request.getSatisfactionLevel())
            .preferredLearningMode(request.getPreferredLearningMode())
            .attendanceCommitment(request.getAttendanceCommitment())
            .homeworkCompletionSelf(request.getHomeworkCompletionSelf())
            .financialStressLevel(request.getFinancialStressLevel())
            .interactionWithTeacher(request.getInteractionWithTeacher())
            .englishLevelSelf(request.getEnglishLevelSelf())
            .goalClarityLevel(request.getGoalClarityLevel())
            .classDifficultyLevel(request.getClassDifficultyLevel())
            .peerInteractionLevel(request.getPeerInteractionLevel())
            .technicalIssuesFrequency(request.getTechnicalIssuesFrequency())
            .predictedDropout(mlResponse.getDropout())
            .predictedProbability(mlResponse.getProbability())
            .modelName(mlResponse.getModel())
            .build();

        DropoutForm saved = dropoutFormRepository.save(form);
        return toResponse(saved);
    }

    @Override
    public List<DropoutFormResponse> getAllForms() {
        return dropoutFormRepository.findAll()
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public DropoutFormResponse getFormById(Long id) {
        DropoutForm form = dropoutFormRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Form not found"));
        return toResponse(form);
    }

    @Override
    public List<DropoutFormResponse> getFormsByUserId(Integer userId) {
        return dropoutFormRepository.findByUserUserId(userId)
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    private DropoutFormResponse toResponse(DropoutForm form) {
        return DropoutFormResponse.builder()
            .id(form.getId())
            .userId(form.getUser().getUserId())
            .userEmail(form.getUser().getEmail())
            .motivationLevel(form.getMotivationLevel())
            .weeklyStudyHours(form.getWeeklyStudyHours())
            .freeTimeHoursPerWeek(form.getFreeTimeHoursPerWeek())
            .satisfactionLevel(form.getSatisfactionLevel())
            .preferredLearningMode(form.getPreferredLearningMode())
            .attendanceCommitment(form.getAttendanceCommitment())
            .homeworkCompletionSelf(form.getHomeworkCompletionSelf())
            .financialStressLevel(form.getFinancialStressLevel())
            .interactionWithTeacher(form.getInteractionWithTeacher())
            .englishLevelSelf(form.getEnglishLevelSelf())
            .goalClarityLevel(form.getGoalClarityLevel())
            .classDifficultyLevel(form.getClassDifficultyLevel())
            .peerInteractionLevel(form.getPeerInteractionLevel())
            .technicalIssuesFrequency(form.getTechnicalIssuesFrequency())
            .predictedDropout(form.getPredictedDropout())
            .predictedProbability(form.getPredictedProbability())
            .modelName(form.getModelName())
            .createdAt(form.getCreatedAt())
            .build();
    }
}
