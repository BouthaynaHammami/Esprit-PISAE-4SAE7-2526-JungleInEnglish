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
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            ResponseEntity<LevelTestResult> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    LevelTestResult.class
            );

            log.info("Paragraph evaluation result: level={}, score={}", 
                response.getBody().getLevel(), response.getBody().getScore());
            return response.getBody();

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
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            ResponseEntity<LevelTestResult> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    LevelTestResult.class
            );

            log.info("Oral evaluation result: level={}, score={}", 
                response.getBody().getLevel(), response.getBody().getScore());
            return response.getBody();

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
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            ResponseEntity<CourseRecommendation> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    CourseRecommendation.class
            );

            log.info("Course recommendations retrieved: {} courses for level {}", 
                response.getBody().getRecommendedCourses().size(), level);
            return response.getBody();

        } catch (Exception e) {
            log.error("Error getting course recommendations", e);
            throw new RuntimeException("Failed to get course recommendations: " + e.getMessage(), e);
        }
    }
}
