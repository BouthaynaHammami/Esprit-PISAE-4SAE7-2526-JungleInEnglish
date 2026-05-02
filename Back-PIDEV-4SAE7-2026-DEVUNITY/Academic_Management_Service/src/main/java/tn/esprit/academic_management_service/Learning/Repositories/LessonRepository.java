package tn.esprit.academic_management_service.Learning.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.academic_management_service.Learning.Entities.Course;
import tn.esprit.academic_management_service.Learning.Entities.Lesson;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByCourse_CourseId(Long courseId);

    List<Lesson> findByCourse(Course course);
}
