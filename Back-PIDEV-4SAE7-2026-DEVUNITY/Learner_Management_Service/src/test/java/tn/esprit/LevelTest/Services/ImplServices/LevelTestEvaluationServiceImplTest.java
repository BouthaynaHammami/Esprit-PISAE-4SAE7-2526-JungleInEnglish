package tn.esprit.LevelTest.Services.ImplServices;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import tn.esprit.LevelTest.Dto.CourseRecommendation;
import tn.esprit.LevelTest.Dto.LevelTestResult;

@ExtendWith(MockitoExtension.class)
class LevelTestEvaluationServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private LevelTestEvaluationServiceImpl service;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "mlServiceUrl", "http://ml-service");
    }

    @Test
    void evaluateParagraphTest_returnsResultAndBuildsRequest() {
        LevelTestResult result = new LevelTestResult("B1", 75, "ok", Map.of("detail", "value"));
        when(restTemplate.exchange(
            eq("http://ml-service/ml/api/evaluate-paragraph"),
            eq(HttpMethod.POST),
            any(HttpEntity.class),
            eq(LevelTestResult.class)))
            .thenReturn(ResponseEntity.ok(result));

        LevelTestResult response = service.evaluateParagraphTest("sample paragraph", "English");

        assertSame(result, response);
        ArgumentCaptor<HttpEntity> entityCaptor = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTemplate).exchange(
            eq("http://ml-service/ml/api/evaluate-paragraph"),
            eq(HttpMethod.POST),
            entityCaptor.capture(),
            eq(LevelTestResult.class));

        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) entityCaptor.getValue().getBody();
        assertEquals("sample paragraph", body.get("paragraphText"));
        assertEquals("English", body.get("subjectTitle"));
    }

    @Test
    void evaluateParagraphTest_throwsWhenBodyMissing() {
        when(restTemplate.exchange(
            eq("http://ml-service/ml/api/evaluate-paragraph"),
            eq(HttpMethod.POST),
            any(HttpEntity.class),
            eq(LevelTestResult.class)))
            .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        assertThrows(IllegalStateException.class,
            () -> service.evaluateParagraphTest("sample paragraph", "English"));
    }

    @Test
    void evaluateOralTest_defaultsDurationWhenNull() {
        LevelTestResult result = new LevelTestResult("A2", 68, "ok", Map.of());
        when(restTemplate.exchange(
            eq("http://ml-service/ml/api/evaluate-oral"),
            eq(HttpMethod.POST),
            any(HttpEntity.class),
            eq(LevelTestResult.class)))
            .thenReturn(ResponseEntity.ok(result));

        LevelTestResult response = service.evaluateOralTest("transcript", null);

        assertSame(result, response);
        ArgumentCaptor<HttpEntity> entityCaptor = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTemplate).exchange(
            eq("http://ml-service/ml/api/evaluate-oral"),
            eq(HttpMethod.POST),
            entityCaptor.capture(),
            eq(LevelTestResult.class));

        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) entityCaptor.getValue().getBody();
        assertEquals("transcript", body.get("transcript"));
        assertEquals(60, body.get("durationSeconds"));
    }

    @Test
    void evaluateOralTest_throwsWhenBodyMissing() {
        when(restTemplate.exchange(
            eq("http://ml-service/ml/api/evaluate-oral"),
            eq(HttpMethod.POST),
            any(HttpEntity.class),
            eq(LevelTestResult.class)))
            .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        assertThrows(IllegalStateException.class,
            () -> service.evaluateOralTest("transcript", 45));
    }

    @Test
    void getCourseRecommendations_returnsResult() {
        CourseRecommendation recommendation = new CourseRecommendation();
        recommendation.setRecommendedCourses(List.of(
            new CourseRecommendation.RecommendedCourse("Title", "Desc", List.of("topic"), "2h", "A1")
        ));
        when(restTemplate.exchange(
            eq("http://ml-service/ml/api/recommend-courses"),
            eq(HttpMethod.POST),
            any(HttpEntity.class),
            eq(CourseRecommendation.class)))
            .thenReturn(ResponseEntity.ok(recommendation));

        CourseRecommendation response = service.getCourseRecommendations("A1", 80);

        assertSame(recommendation, response);
    }

    @Test
    void getCourseRecommendations_throwsWhenBodyMissing() {
        when(restTemplate.exchange(
            eq("http://ml-service/ml/api/recommend-courses"),
            eq(HttpMethod.POST),
            any(HttpEntity.class),
            eq(CourseRecommendation.class)))
            .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        assertThrows(IllegalStateException.class,
            () -> service.getCourseRecommendations("A1", 80));
    }
}
