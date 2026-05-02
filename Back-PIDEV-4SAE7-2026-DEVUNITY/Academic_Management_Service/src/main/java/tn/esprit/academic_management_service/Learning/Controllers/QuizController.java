package tn.esprit.academic_management_service.Learning.Controllers;

import org.springframework.web.bind.annotation.*;
import tn.esprit.academic_management_service.Learning.Entities.Quiz;
import tn.esprit.academic_management_service.Learning.Services.QuizService;

import java.util.List;

@RestController
@RequestMapping("/quizzes")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/all")
    public List<Quiz> getAll() {
        return quizService.getAllQuizzes();
    }

    @GetMapping("/{id}")
    public Quiz getById(@PathVariable Long id) {
        return quizService.getQuizById(id).orElse(null);
    }

    @GetMapping("/course/{courseId}")
    public Quiz getByCourseId(@PathVariable Long courseId) {
        return quizService.getQuizByCourseId(courseId).orElse(null);
    }

    @PostMapping("/add")
    public Quiz create(@RequestBody Quiz quiz) {
        return quizService.createQuiz(quiz);
    }

    @PutMapping("/update/{id}")
    public Quiz update(@PathVariable Long id, @RequestBody Quiz quiz) {
        return quizService.updateQuiz(id, quiz);
    }

    @DeleteMapping("/delete/{id}")
    public Boolean delete(@PathVariable Long id) {
        quizService.deleteQuiz(id);
        return true;
    }
}