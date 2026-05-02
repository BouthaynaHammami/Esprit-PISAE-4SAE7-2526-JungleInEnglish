package tn.esprit.academic_management_service.Learning.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.academic_management_service.Learning.Entities.QuizAttempt;

import java.util.List;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {

    List<QuizAttempt> findByUserId(Integer userId);

    List<QuizAttempt> findByQuiz_QuizId(Long quizId);
}