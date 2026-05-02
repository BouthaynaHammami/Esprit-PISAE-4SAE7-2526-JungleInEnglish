package tn.esprit.language_courses_service.ChildrenEnglish.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.language_courses_service.ChildrenEnglish.Entities.Progress;

import java.util.List;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, Long> {
    List<Progress> findByChild_ChildId(Long childId);
    List<Progress> findByCourse_CourseId(Long courseId);
    List<Progress> findByChild_ChildIdAndCourse_CourseId(Long childId, Long courseId);
}
