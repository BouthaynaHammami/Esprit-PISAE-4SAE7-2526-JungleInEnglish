package tn.esprit.academic_management_service.Learning.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.academic_management_service.Learning.Entities.Quiz;
import tn.esprit.academic_management_service.Learning.Repositories.QuizRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;

    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    public Optional<Quiz> getQuizById(Long id) {
        return quizRepository.findById(id);
    }

    public Optional<Quiz> getQuizByCourseId(Long courseId) {
        return quizRepository.findByCourse_CourseId(courseId);
    }

    public Quiz createQuiz(Quiz quiz) {

        if (quiz.getQuestions() != null) {
            quiz.getQuestions().forEach(q -> q.setQuiz(quiz));
        }

        return quizRepository.save(quiz);
    }

    public Quiz updateQuiz(Long id, Quiz quizDetails) {

        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        quiz.setTitle(quizDetails.getTitle());

        quiz.getQuestions().clear();

        if (quizDetails.getQuestions() != null) {
            quizDetails.getQuestions().forEach(q -> {
                q.setQuiz(quiz); // VERY IMPORTANT
                quiz.getQuestions().add(q);
            });
        }

        return quizRepository.save(quiz);
    }

    public void deleteQuiz(Long id) {
        quizRepository.deleteById(id);
    }
}