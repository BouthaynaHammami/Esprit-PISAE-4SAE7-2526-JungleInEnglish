package tn.esprit.academic_management_service.Learning.Controllers;

import org.springframework.web.bind.annotation.*;
import tn.esprit.academic_management_service.Learning.Entities.QuizAttempt;
import tn.esprit.academic_management_service.Learning.Services.QuizAttemptService;

import java.util.List;

@RestController
@RequestMapping("/quiz-attempts")
public class QuizAttemptController {

    private final QuizAttemptService quizAttemptService;

    public QuizAttemptController(QuizAttemptService quizAttemptService) {
        this.quizAttemptService = quizAttemptService;
    }

    @GetMapping("/all")
    public List<QuizAttempt> getAll() {
        return quizAttemptService.getAllQuizAttempts();
    }

    @GetMapping("/{id}")
    public QuizAttempt getById(@PathVariable Long id) {
        return quizAttemptService.getQuizAttemptById(id).orElse(null);
    }

    @GetMapping("/user/{userId}")
    public List<QuizAttempt> getByUser(@PathVariable Integer userId) {
        return quizAttemptService.getQuizAttemptsByUserId(userId);
    }

    @GetMapping("/quiz/{quizId}")
    public List<QuizAttempt> getByQuiz(@PathVariable Long quizId) {
        return quizAttemptService.getQuizAttemptsByQuizId(quizId);
    }

    @PostMapping("/submit")
    public QuizAttempt submit(
            @RequestParam Integer userId,
            @RequestParam Long quizId,
            @RequestBody List<String> answers) {
        return quizAttemptService.submitQuiz(userId, quizId, answers);
    }

    @DeleteMapping("/delete/{id}")
    public Boolean delete(@PathVariable Long id) {
        quizAttemptService.deleteQuizAttempt(id);
        return true;
    }
}