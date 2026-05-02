package tn.esprit.language_courses_service.ChildrenEnglish.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.language_courses_service.ChildrenEnglish.Entities.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
}
