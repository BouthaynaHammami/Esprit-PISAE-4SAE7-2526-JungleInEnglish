package tn.esprit.academic_management_service.Learning.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.academic_management_service.Learning.Entities.Course;
import tn.esprit.academic_management_service.Learning.Entities.Level;
import tn.esprit.academic_management_service.Learning.Entities.TypeCourse;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByLevel(Level level);
    List<Course> findByIsHiddenFalse();
    List<Course> findByType(TypeCourse type);
}
