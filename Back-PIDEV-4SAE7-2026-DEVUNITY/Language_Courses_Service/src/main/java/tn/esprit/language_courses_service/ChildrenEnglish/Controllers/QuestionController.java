package tn.esprit.language_courses_service.ChildrenEnglish.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.language_courses_service.ChildrenEnglish.Entities.Question;
import tn.esprit.language_courses_service.ChildrenEnglish.Serivces.QuestionService;

import java.util.*;

@RestController
@RequestMapping("/activities")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class QuestionController {
    
    private final QuestionService questionService;
    
    @GetMapping("/{activityId}/questions")
    public ResponseEntity<List<Map<String, Object>>> getQuestionsForActivity(@PathVariable Long activityId) {
        List<Question> questions = questionService.getQuestionsByActivityId(activityId);
        List<Map<String, Object>> response = new ArrayList<>();
        
        for (Question q : questions) {
            Map<String, Object> questionMap = new HashMap<>();
            questionMap.put("questionId", q.getQuestionId());
            questionMap.put("activityId", q.getActivity().getActivityId());
            questionMap.put("questionText", q.getQuestionText());
            questionMap.put("questionType", q.getQuestionType());
            questionMap.put("correctAnswer", q.getCorrectAnswer());
            questionMap.put("imageUrl", q.getImageUrl());
            questionMap.put("audioUrl", q.getAudioUrl());
            questionMap.put("explanation", q.getExplanation());
            questionMap.put("points", q.getPoints());
            
            // Parse options from JSON string
            if (q.getOptions() != null && !q.getOptions().isEmpty()) {
                try {
                    String[] options = q.getOptions().split(",");
                    questionMap.put("options", Arrays.asList(options));
                } catch (Exception e) {
                    questionMap.put("options", new ArrayList<>());
                }
            } else {
                questionMap.put("options", new ArrayList<>());
            }
            
            response.add(questionMap);
        }
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{activityId}/submit")
    public ResponseEntity<Object> submitQuizAttempt(@PathVariable Long activityId, @RequestBody Object attempt) {
        // Return the attempt as-is for now
        return ResponseEntity.ok(attempt);
    }
}
