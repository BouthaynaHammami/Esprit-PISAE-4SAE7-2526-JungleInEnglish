package tn.esprit.LevelTest.Services.ImplServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tn.esprit.LevelTest.Dto.CourseRecommendation;
import tn.esprit.LevelTest.Dto.LevelTestResult;
import tn.esprit.LevelTest.Services.IServices.ILevelTestEvaluationService;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class LevelTestEvaluationServiceImpl implements ILevelTestEvaluationService {

    private final RestTemplate restTemplate;

    @Value("${ml.service.url:http://ML-SERVICE}")
    private String mlServiceUrl;

    @Override
    public LevelTestResult evaluateParagraphTest(String paragraphText, String subjectTitle) {
        try {
            log.info("Evaluating paragraph test for subject: {}", subjectTitle);
            
            Map<String, Object> request = new HashMap<>();
            request.put("paragraphText", paragraphText);
            request.put("subjectTitle", subjectTitle);

            String url = mlServiceUrl + "/ml/api/evaluate-paragraph";
            log.info("Calling ML service: {}", url);

            LevelTestResult result = callMlService(
                url,
                request,
                LevelTestResult.class,
                "Paragraph evaluation returned empty body"
            );
            log.info("Paragraph evaluation result: level={}, score={}",
                result.getLevel(), result.getScore());
            return result;

        } catch (Exception e) {
            log.error("Error evaluating paragraph test", e);
            throw new RuntimeException("Failed to evaluate paragraph test: " + e.getMessage(), e);
        }
    }

    @Override
    public LevelTestResult evaluateOralTest(String transcript, Integer durationSeconds) {
        try {
            log.info("Evaluating oral test (duration: {} seconds)", durationSeconds);
            
            Map<String, Object> request = new HashMap<>();
            request.put("transcript", transcript);
            request.put("durationSeconds", durationSeconds != null ? durationSeconds : 60);

            String url = mlServiceUrl + "/ml/api/evaluate-oral";
            log.info("Calling ML service: {}", url);

            LevelTestResult result = callMlService(
                url,
                request,
                LevelTestResult.class,
                "Oral evaluation returned empty body"
            );
            log.info("Oral evaluation result: level={}, score={}",
                result.getLevel(), result.getScore());
            return result;

        } catch (Exception e) {
            log.error("Error evaluating oral test", e);
            throw new RuntimeException("Failed to evaluate oral test: " + e.getMessage(), e);
        }
    }

    @Override
    public CourseRecommendation getCourseRecommendations(String level, Integer score) {
        try {
            log.info("Getting course recommendations for level: {}, score: {}", level, score);
            
            Map<String, Object> request = new HashMap<>();
            request.put("level", level);
            request.put("score", score);

            String url = mlServiceUrl + "/ml/api/recommend-courses";
            log.info("Calling ML service: {}", url);

            CourseRecommendation result = callMlService(
                url,
                request,
                CourseRecommendation.class,
                "Course recommendations returned empty body"
            );
            int courseCount = result.getRecommendedCourses() == null ? 0 : result.getRecommendedCourses().size();
            log.info("Course recommendations retrieved: {} courses for level {}",
                courseCount, level);
            return result;

        } catch (Exception e) {
            log.error("Error getting course recommendations", e);
            throw new RuntimeException("Failed to get course recommendations: " + e.getMessage(), e);
        }
    }

    private <T> T requireResponseBody(ResponseEntity<T> response, String errorMessage) {
        if (response == null || response.getBody() == null) {
            throw new IllegalStateException(errorMessage);
        }
        return response.getBody();
    }

    private <T> T callMlService(
        String url,
        Map<String, Object> requestBody,
        Class<T> responseType,
        String errorMessage
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<T> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            entity,
            responseType
        );

        T result = requireResponseBody(response, errorMessage);
        if (result == null) {
            throw new IllegalStateException(errorMessage);
        }
        return result;
    }
}
