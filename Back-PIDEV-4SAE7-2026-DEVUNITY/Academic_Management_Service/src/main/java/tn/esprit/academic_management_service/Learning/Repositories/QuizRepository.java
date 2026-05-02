package tn.esprit.academic_management_service.Learning.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.academic_management_service.Learning.Entities.Course;
import tn.esprit.academic_management_service.Learning.Entities.Quiz;

import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    Optional<Quiz> findByCourse_CourseId(Long courseId);

    Quiz findByCourse(Course course);
}
