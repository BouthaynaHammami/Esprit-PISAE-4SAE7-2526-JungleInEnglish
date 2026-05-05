package tn.esprit.LevelTest.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;
import tn.esprit.LevelTest.Dto.CourseRecommendation;
import tn.esprit.LevelTest.Dto.LevelTestResult;
import tn.esprit.LevelTest.Entities.TestTentative;
import tn.esprit.LevelTest.Services.ImplServices.LevelTestEvaluationServiceImpl;
import tn.esprit.LevelTest.Services.ImplServices.TestTentativeServiceImpl;


import java.util.List;

@RestController
@RequestMapping("/api/test-tentatives")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class TestTentativeController {

    private final TestTentativeServiceImpl testTentativeService;
    private final LevelTestEvaluationServiceImpl levelTestEvaluationService;

    @GetMapping
    public List<TestTentative> getAll() {
        return testTentativeService.getAll();
    }

    @GetMapping("/{id}")
    public TestTentative getById(@PathVariable Long id) {
        return testTentativeService.getById(id);
    }

    @GetMapping("/corrections")
    public List<TestTentative> getSubmissionsForCorrection() {
        return testTentativeService.getSubmissionsForCorrection();
    }

    @PostMapping
    public TestTentative create(@RequestBody TestTentative testTentative) {
        return testTentativeService.create(testTentative);
    }

    @PutMapping("/{id}")
    public TestTentative update(@PathVariable Long id, @RequestBody TestTentative testTentative) {
        return testTentativeService.update(id, testTentative);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        testTentativeService.delete(id);
    }
    
    @PostMapping("/{id}/evaluate-paragraph")
    public ResponseEntity<LevelTestResult> evaluateParagraph(@PathVariable Long id) {
        TestTentative test = testTentativeService.getById(id);
        if (test == null) {
            return ResponseEntity.notFound().build();
        }
        
        String subjectTitle = test.getSubject() != null ? test.getSubject().getTitle() : "General";
        LevelTestResult result = levelTestEvaluationService.evaluateParagraphTest(
            test.getParagraph(), 
            subjectTitle
        );
        
        // Update test with ML results
        test.setScore(result.getScore());
        test.setTutorFeedback("AI Evaluation: " + result.getFeedback() + " | Level: " + result.getLevel());
        testTentativeService.update(id, test);
        
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/{id}/evaluate-oral")
    public ResponseEntity<LevelTestResult> evaluateOral(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "60") Integer durationSeconds) {
        TestTentative test = testTentativeService.getById(id);
        if (test == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Use paragraph field as transcript for oral test
        LevelTestResult result = levelTestEvaluationService.evaluateOralTest(
            test.getParagraph(), 
            durationSeconds
        );
        
        // Update test with ML results
        test.setScore(result.getScore());
        test.setTutorFeedback("AI Oral Evaluation: " + result.getFeedback() + " | Level: " + result.getLevel());
        testTentativeService.update(id, test);
        
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/recommend-courses")
    public ResponseEntity<CourseRecommendation> recommendCourses(
            @RequestParam String level,
            @RequestParam Integer score) {
        CourseRecommendation recommendations = levelTestEvaluationService.getCourseRecommendations(level, score);
        return ResponseEntity.ok(recommendations);
    }
    
    @PostMapping("/{id}/get-recommendations")
    public ResponseEntity<CourseRecommendation> getRecommendationsForTest(@PathVariable Long id) {
        TestTentative test = testTentativeService.getById(id);
        if (test == null) {
            return ResponseEntity.notFound().build();
        }
        
        // First evaluate if not already evaluated
        if (test.getScore() == 0 || test.getTutorFeedback() == null || test.getTutorFeedback().isEmpty()) {
            String subjectTitle = test.getSubject() != null ? test.getSubject().getTitle() : "General";
            LevelTestResult result = levelTestEvaluationService.evaluateParagraphTest(
                test.getParagraph(), 
                subjectTitle
            );
            test.setScore(result.getScore());
            test.setTutorFeedback("AI Evaluation: " + result.getFeedback() + " | Level: " + result.getLevel());
            testTentativeService.update(id, test);
            
            // Get recommendations based on evaluation
            CourseRecommendation recommendations = levelTestEvaluationService.getCourseRecommendations(
                result.getLevel(), 
                result.getScore()
            );
            return ResponseEntity.ok(recommendations);
        }
        
        // Extract level from feedback if already evaluated
        String feedback = test.getTutorFeedback();
        String level = "B1"; // Default
        if (feedback != null && feedback.contains("Level:")) {
            String[] parts = feedback.split("Level:");
            if (parts.length > 1) {
                level = parts[1].trim().split("\\s+")[0];
            }
        }
        
        CourseRecommendation recommendations = levelTestEvaluationService.getCourseRecommendations(
            level, 
            test.getScore()
        );
        return ResponseEntity.ok(recommendations);
    }
}
