package tn.esprit.academic_management_service.Learning.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.academic_management_service.Learning.Entities.LearningQuestion;
import tn.esprit.academic_management_service.Learning.Entities.Quiz;
import tn.esprit.academic_management_service.Learning.Entities.QuizAttempt;
import tn.esprit.academic_management_service.Learning.Repositories.QuizAttemptRepository;
import tn.esprit.academic_management_service.Learning.Repositories.QuizRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizAttemptService {

    private final QuizAttemptRepository quizAttemptRepository;
    private final QuizRepository quizRepository;

    public List<QuizAttempt> getAllQuizAttempts() {
        return quizAttemptRepository.findAll();
    }

    public Optional<QuizAttempt> getQuizAttemptById(Long id) {
        return quizAttemptRepository.findById(id);
    }

    public List<QuizAttempt> getQuizAttemptsByUserId(Integer userId) {
        return quizAttemptRepository.findByUserId(userId);
    }

    public List<QuizAttempt> getQuizAttemptsByQuizId(Long quizId) {
        return quizAttemptRepository.findByQuiz_QuizId(quizId);
    }

    public QuizAttempt submitQuiz(Integer userId, Long quizId, List<String> answers) {

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        List<LearningQuestion> questions = quiz.getQuestions();

        int correct = 0;
        for (int i = 0; i < questions.size(); i++) {
            if (i < answers.size()) {
                String submitted = answers.get(i);
                String expected = questions.get(i).getCorrectAnswer();
                if (submitted != null && submitted.equalsIgnoreCase(expected)) {
                    correct++;
                }
            }
        }

        QuizAttempt attempt = QuizAttempt.builder()
                .userId(userId)
                .quiz(quiz)
                .answers(answers)
                .score(correct)
                .isCompleted(true)
                .attemptDate(LocalDate.now())
                .build();

        return quizAttemptRepository.save(attempt);
    }

    public void deleteQuizAttempt(Long id) {
        quizAttemptRepository.deleteById(id);
    }
}