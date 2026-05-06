package tn.esprit.learner_managment_service.DropoutPrediction.Controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import tn.esprit.learner_managment_service.DropoutPrediction.Dto.DropoutFormRequest;
import tn.esprit.learner_managment_service.DropoutPrediction.Dto.DropoutFormResponse;
import tn.esprit.learner_managment_service.DropoutPrediction.Services.DropoutFormService;

@WebMvcTest(DropoutFormController.class)
@AutoConfigureMockMvc(addFilters = false)
class DropoutFormControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DropoutFormService dropoutFormService;

    @Test
    void createForm_returnsCreated() throws Exception {
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

        DropoutFormResponse response = DropoutFormResponse.builder()
            .id(1L)
            .userId(10)
            .userEmail("learner@example.com")
            .motivationLevel(4)
            .weeklyStudyHours(6.5)
            .freeTimeHoursPerWeek(5.0)
            .satisfactionLevel(3)
            .preferredLearningMode("online")
            .attendanceCommitment(4)
            .homeworkCompletionSelf(5)
            .financialStressLevel(2)
            .interactionWithTeacher(3)
            .englishLevelSelf("B1")
            .goalClarityLevel(4)
            .classDifficultyLevel(3)
            .peerInteractionLevel(2)
            .technicalIssuesFrequency(1)
            .predictedDropout("no")
            .predictedProbability(0.2)
            .modelName("model-v1")
            .createdAt(LocalDateTime.of(2024, 1, 10, 12, 0))
            .build();

        when(dropoutFormService.createForm(any(DropoutFormRequest.class))).thenReturn(response);

        mockMvc.perform(post("/dropout-forms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.userId").value(10))
            .andExpect(jsonPath("$.predictedDropout").value("no"));
    }

    @Test
    void createForm_returnsUnauthorizedWhenNotAuthenticated() throws Exception {
        DropoutFormRequest request = new DropoutFormRequest();
        request.setMotivationLevel(4);

        when(dropoutFormService.createForm(any(DropoutFormRequest.class)))
            .thenThrow(new IllegalStateException("User is not authenticated"));

        mockMvc.perform(post("/dropout-forms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void getAllForms_returnsList() throws Exception {
        DropoutFormResponse response = DropoutFormResponse.builder()
            .id(2L)
            .userId(20)
            .userEmail("user2@example.com")
            .predictedDropout("yes")
            .predictedProbability(0.8)
            .modelName("model-v2")
            .createdAt(LocalDateTime.of(2024, 2, 1, 8, 30))
            .build();

        when(dropoutFormService.getAllForms()).thenReturn(List.of(response));

        mockMvc.perform(get("/dropout-forms"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id").value(2));
    }

    @Test
    void getForm_returnsResponse() throws Exception {
        DropoutFormResponse response = DropoutFormResponse.builder()
            .id(3L)
            .userId(30)
            .userEmail("user3@example.com")
            .predictedDropout("no")
            .predictedProbability(0.1)
            .modelName("model-v3")
            .createdAt(LocalDateTime.of(2024, 3, 1, 9, 0))
            .build();

        when(dropoutFormService.getFormById(eq(3L))).thenReturn(response);

        mockMvc.perform(get("/dropout-forms/3"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(3))
            .andExpect(jsonPath("$.userEmail").value("user3@example.com"));
    }

    @Test
    void getFormsByUser_returnsList() throws Exception {
        DropoutFormResponse response = DropoutFormResponse.builder()
            .id(4L)
            .userId(40)
            .userEmail("user4@example.com")
            .predictedDropout("yes")
            .predictedProbability(0.6)
            .modelName("model-v4")
            .createdAt(LocalDateTime.of(2024, 4, 1, 10, 0))
            .build();

        when(dropoutFormService.getFormsByUserId(eq(40))).thenReturn(List.of(response));

        mockMvc.perform(get("/dropout-forms/user/40"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id").value(4));
    }
}
