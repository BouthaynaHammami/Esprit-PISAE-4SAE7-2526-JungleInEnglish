package tn.esprit.learner_managment_service.DropoutPrediction.Services.Impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import tn.esprit.learner_managment_service.DropoutPrediction.Dto.DropoutFormRequest;
import tn.esprit.learner_managment_service.DropoutPrediction.Dto.DropoutFormResponse;
import tn.esprit.learner_managment_service.DropoutPrediction.Dto.MlPredictRequest;
import tn.esprit.learner_managment_service.DropoutPrediction.Dto.MlPredictResponse;
import tn.esprit.learner_managment_service.DropoutPrediction.Entities.DropoutForm;
import tn.esprit.learner_managment_service.DropoutPrediction.Repositories.DropoutFormRepository;
import tn.esprit.learner_managment_service.UserManagement.Entities.User;
import tn.esprit.learner_managment_service.UserManagement.Repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
class DropoutFormServiceImplTest {

    @Mock
    private DropoutFormRepository dropoutFormRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private DropoutFormServiceImpl service;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "mlServiceBaseUrl", "http://ml-service");
        Jwt jwt = Jwt.withTokenValue("token")
            .header("alg", "none")
            .claim("email", "learner@example.com")
            .build();
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(jwt, null, List.of())
        );
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void createForm_persistsAndReturnsResponse() {
        User user = new User();
        user.setUserId(10);
        user.setEmail("learner@example.com");

        DropoutFormRequest request = new DropoutFormRequest();
        request.setMotivationLevel(4);
        request.setWeeklyStudyHours(6.5);
        request.setFreeTimeHoursPerWeek(5.0);
        request.setSatisfactionLevel(3);
        request.setPreferredLearningMode("online");
        request.setAttendanceCommitment(4);
        request.setHomeworkCompletionSelf(5);
        request.setFinancialStressLevel(2);
        request.setInteractionWithTeacher(3);
        request.setEnglishLevelSelf("B1");
        request.setGoalClarityLevel(4);
        request.setClassDifficultyLevel(3);
        request.setPeerInteractionLevel(2);
        request.setTechnicalIssuesFrequency(1);

        MlPredictResponse mlPredictResponse = new MlPredictResponse();
        mlPredictResponse.setDropout("no");
        mlPredictResponse.setProbability(0.2);
        mlPredictResponse.setModel("model-v1");

        DropoutForm saved = DropoutForm.builder()
            .id(1L)
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
            .predictedDropout(mlPredictResponse.getDropout())
            .predictedProbability(mlPredictResponse.getProbability())
            .modelName(mlPredictResponse.getModel())
            .createdAt(LocalDateTime.of(2024, 1, 10, 12, 0))
            .build();

        when(userRepository.findByEmail("learner@example.com"))
            .thenReturn(Optional.of(user));
        when(restTemplate.postForEntity(
            eq("http://ml-service/predict"),
            any(MlPredictRequest.class),
            eq(MlPredictResponse.class)))
            .thenReturn(ResponseEntity.ok(mlPredictResponse));
        when(dropoutFormRepository.save(any(DropoutForm.class)))
            .thenReturn(saved);

        DropoutFormResponse response = service.createForm(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(10, response.getUserId());
        assertEquals("learner@example.com", response.getUserEmail());
        assertEquals("no", response.getPredictedDropout());
        verify(dropoutFormRepository).save(any(DropoutForm.class));
    }

    @Test
    void getAllForms_returnsResponses() {
        DropoutForm form = DropoutForm.builder()
            .id(5L)
            .user(new User())
            .motivationLevel(2)
            .weeklyStudyHours(3.0)
            .freeTimeHoursPerWeek(2.0)
            .satisfactionLevel(2)
            .preferredLearningMode("offline")
            .attendanceCommitment(3)
            .homeworkCompletionSelf(4)
            .financialStressLevel(1)
            .interactionWithTeacher(2)
            .englishLevelSelf("A2")
            .goalClarityLevel(2)
            .classDifficultyLevel(2)
            .peerInteractionLevel(2)
            .technicalIssuesFrequency(1)
            .predictedDropout("yes")
            .predictedProbability(0.7)
            .modelName("model-v2")
            .createdAt(LocalDateTime.of(2024, 2, 1, 8, 30))
            .build();
        form.getUser().setUserId(2);
        form.getUser().setEmail("user2@example.com");

        when(dropoutFormRepository.findAll()).thenReturn(List.of(form));

        List<DropoutFormResponse> responses = service.getAllForms();

        assertEquals(1, responses.size());
        assertEquals(5L, responses.get(0).getId());
    }

    @Test
    void getFormById_returnsResponse() {
        DropoutForm form = DropoutForm.builder()
            .id(7L)
            .user(new User())
            .motivationLevel(3)
            .weeklyStudyHours(4.0)
            .freeTimeHoursPerWeek(4.0)
            .satisfactionLevel(3)
            .preferredLearningMode("hybrid")
            .attendanceCommitment(4)
            .homeworkCompletionSelf(4)
            .financialStressLevel(2)
            .interactionWithTeacher(3)
            .englishLevelSelf("B2")
            .goalClarityLevel(3)
            .classDifficultyLevel(3)
            .peerInteractionLevel(3)
            .technicalIssuesFrequency(2)
            .predictedDropout("no")
            .predictedProbability(0.1)
            .modelName("model-v3")
            .createdAt(LocalDateTime.of(2024, 3, 1, 9, 0))
            .build();
        form.getUser().setUserId(3);
        form.getUser().setEmail("user3@example.com");

        when(dropoutFormRepository.findById(7L)).thenReturn(Optional.of(form));

        DropoutFormResponse response = service.getFormById(7L);

        assertEquals(7L, response.getId());
        assertEquals("user3@example.com", response.getUserEmail());
    }

    @Test
    void getFormById_throwsWhenMissing() {
        when(dropoutFormRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.getFormById(99L));
    }

    @Test
    void getFormsByUserId_returnsResponses() {
        DropoutForm form = DropoutForm.builder()
            .id(9L)
            .user(new User())
            .motivationLevel(1)
            .weeklyStudyHours(2.0)
            .freeTimeHoursPerWeek(1.0)
            .satisfactionLevel(1)
            .preferredLearningMode("offline")
            .attendanceCommitment(1)
            .homeworkCompletionSelf(1)
            .financialStressLevel(4)
            .interactionWithTeacher(1)
            .englishLevelSelf("A1")
            .goalClarityLevel(1)
            .classDifficultyLevel(1)
            .peerInteractionLevel(1)
            .technicalIssuesFrequency(4)
            .predictedDropout("yes")
            .predictedProbability(0.9)
            .modelName("model-v4")
            .createdAt(LocalDateTime.of(2024, 4, 1, 10, 0))
            .build();
        form.getUser().setUserId(4);
        form.getUser().setEmail("user4@example.com");

        when(dropoutFormRepository.findByUserUserId(4)).thenReturn(List.of(form));

        List<DropoutFormResponse> responses = service.getFormsByUserId(4);

        assertEquals(1, responses.size());
        assertEquals(9L, responses.get(0).getId());
    }
}
