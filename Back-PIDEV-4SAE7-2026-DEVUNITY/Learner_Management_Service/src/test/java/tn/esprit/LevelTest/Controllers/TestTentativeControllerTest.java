package tn.esprit.LevelTest.Controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import tn.esprit.LevelTest.Dto.CourseRecommendation;
import tn.esprit.LevelTest.Dto.LevelTestResult;
import tn.esprit.LevelTest.Entities.Subject;
import tn.esprit.LevelTest.Entities.TestTentative;
import tn.esprit.LevelTest.Services.ImplServices.LevelTestEvaluationServiceImpl;
import tn.esprit.LevelTest.Services.ImplServices.TestTentativeServiceImpl;

@ExtendWith(MockitoExtension.class)
class TestTentativeControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private TestTentativeServiceImpl testTentativeService;

    @Mock
    private LevelTestEvaluationServiceImpl levelTestEvaluationService;

    @InjectMocks
    private TestTentativeController testTentativeController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
            .standaloneSetup(testTentativeController)
            .build();
    }

    @Test
    void getAll_returnsList() throws Exception {
        TestTentative test = new TestTentative();
        test.setId(1L);
        when(testTentativeService.getAll()).thenReturn(List.of(test));

        mockMvc.perform(get("/api/test-tentatives"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void getById_returnsTest() throws Exception {
        TestTentative test = new TestTentative();
        test.setId(2L);
        when(testTentativeService.getById(2L)).thenReturn(test);

        mockMvc.perform(get("/api/test-tentatives/2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(2));
    }

    @Test
    void getSubmissionsForCorrection_returnsList() throws Exception {
        TestTentative test = new TestTentative();
        test.setId(3L);
        when(testTentativeService.getSubmissionsForCorrection()).thenReturn(List.of(test));

        mockMvc.perform(get("/api/test-tentatives/corrections"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id").value(3));
    }

    @Test
    void create_returnsCreatedEntity() throws Exception {
        TestTentative request = new TestTentative();
        request.setParagraph("sample");
        TestTentative response = new TestTentative();
        response.setId(4L);
        response.setParagraph("sample");
        when(testTentativeService.create(any(TestTentative.class))).thenReturn(response);

        mockMvc.perform(post("/api/test-tentatives")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(4));
    }

    @Test
    void update_returnsUpdatedEntity() throws Exception {
        TestTentative request = new TestTentative();
        request.setParagraph("updated");
        TestTentative response = new TestTentative();
        response.setId(5L);
        response.setParagraph("updated");
        when(testTentativeService.update(eq(5L), any(TestTentative.class))).thenReturn(response);

        mockMvc.perform(put("/api/test-tentatives/5")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(5));
    }

    @Test
    void delete_returnsOk() throws Exception {
        mockMvc.perform(delete("/api/test-tentatives/6"))
            .andExpect(status().isOk());

        verify(testTentativeService).delete(6L);
    }

    @Test
    void evaluateParagraph_returnsNotFoundWhenMissing() throws Exception {
        when(testTentativeService.getById(7L)).thenReturn(null);

        mockMvc.perform(post("/api/test-tentatives/7/evaluate-paragraph"))
            .andExpect(status().isNotFound());
    }

    @Test
    void evaluateParagraph_returnsResultAndUpdatesTest() throws Exception {
        Subject subject = new Subject();
        subject.setTitle("English");
        TestTentative test = new TestTentative();
        test.setId(8L);
        test.setParagraph("paragraph");
        test.setSubject(subject);

        LevelTestResult result = new LevelTestResult("B1", 72, "Good", null);
        when(testTentativeService.getById(8L)).thenReturn(test);
        when(levelTestEvaluationService.evaluateParagraphTest("paragraph", "English"))
            .thenReturn(result);
        when(testTentativeService.update(eq(8L), any(TestTentative.class))).thenReturn(test);

        mockMvc.perform(post("/api/test-tentatives/8/evaluate-paragraph"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.level").value("B1"))
            .andExpect(jsonPath("$.score").value(72));

        ArgumentCaptor<TestTentative> captor = ArgumentCaptor.forClass(TestTentative.class);
        verify(testTentativeService).update(eq(8L), captor.capture());
        TestTentative updated = captor.getValue();
        assertEquals(72, updated.getScore());
    }

    @Test
    void evaluateOral_returnsNotFoundWhenMissing() throws Exception {
        when(testTentativeService.getById(9L)).thenReturn(null);

        mockMvc.perform(post("/api/test-tentatives/9/evaluate-oral")
                .param("durationSeconds", "45"))
            .andExpect(status().isNotFound());
    }

    @Test
    void evaluateOral_returnsResultAndUpdatesTest() throws Exception {
        TestTentative test = new TestTentative();
        test.setId(10L);
        test.setParagraph("transcript");

        LevelTestResult result = new LevelTestResult("A2", 60, "Ok", null);
        when(testTentativeService.getById(10L)).thenReturn(test);
        when(levelTestEvaluationService.evaluateOralTest("transcript", 45))
            .thenReturn(result);
        when(testTentativeService.update(eq(10L), any(TestTentative.class))).thenReturn(test);

        mockMvc.perform(post("/api/test-tentatives/10/evaluate-oral")
                .param("durationSeconds", "45"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.level").value("A2"))
            .andExpect(jsonPath("$.score").value(60));

        verify(testTentativeService).update(eq(10L), any(TestTentative.class));
    }

    @Test
    void recommendCourses_returnsResponse() throws Exception {
        CourseRecommendation recommendation = new CourseRecommendation();
        recommendation.setCurrentLevel("B1");
        recommendation.setScore(80);
        when(levelTestEvaluationService.getCourseRecommendations("B1", 80))
            .thenReturn(recommendation);

        mockMvc.perform(get("/api/test-tentatives/recommend-courses")
                .param("level", "B1")
                .param("score", "80"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.currentLevel").value("B1"))
            .andExpect(jsonPath("$.score").value(80));
    }

    @Test
    void getRecommendationsForTest_returnsNotFoundWhenMissing() throws Exception {
        when(testTentativeService.getById(11L)).thenReturn(null);

        mockMvc.perform(post("/api/test-tentatives/11/get-recommendations"))
            .andExpect(status().isNotFound());
    }

    @Test
    void getRecommendationsForTest_evaluatesWhenNeeded() throws Exception {
        Subject subject = new Subject();
        subject.setTitle("General");
        TestTentative test = new TestTentative();
        test.setId(12L);
        test.setParagraph("text");
        test.setSubject(subject);
        test.setScore(0);
        test.setTutorFeedback(null);

        LevelTestResult result = new LevelTestResult("A2", 70, "Ok", null);
        CourseRecommendation recommendation = new CourseRecommendation();
        recommendation.setCurrentLevel("A2");

        when(testTentativeService.getById(12L)).thenReturn(test);
        when(levelTestEvaluationService.evaluateParagraphTest("text", "General"))
            .thenReturn(result);
        when(testTentativeService.update(eq(12L), any(TestTentative.class))).thenReturn(test);
        when(levelTestEvaluationService.getCourseRecommendations("A2", 70))
            .thenReturn(recommendation);

        mockMvc.perform(post("/api/test-tentatives/12/get-recommendations"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.currentLevel").value("A2"));
    }

    @Test
    void getRecommendationsForTest_usesExistingFeedback() throws Exception {
        TestTentative test = new TestTentative();
        test.setId(13L);
        test.setScore(80);
        test.setTutorFeedback("AI Evaluation: Good | Level: B2");

        CourseRecommendation recommendation = new CourseRecommendation();
        recommendation.setCurrentLevel("B2");

        when(testTentativeService.getById(13L)).thenReturn(test);
        when(levelTestEvaluationService.getCourseRecommendations("B2", 80))
            .thenReturn(recommendation);

        mockMvc.perform(post("/api/test-tentatives/13/get-recommendations"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.currentLevel").value("B2"));

        verify(levelTestEvaluationService).getCourseRecommendations("B2", 80);
    }
}
