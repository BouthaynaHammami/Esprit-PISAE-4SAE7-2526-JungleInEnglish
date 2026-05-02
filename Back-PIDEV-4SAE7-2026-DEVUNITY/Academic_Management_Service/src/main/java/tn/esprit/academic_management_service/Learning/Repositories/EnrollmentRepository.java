package tn.esprit.academic_management_service.Learning.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.academic_management_service.Learning.Entities.Enrollment;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByUserId(Integer userId);

    List<Enrollment> findByCourse_CourseId(Long courseId);

    Optional<Enrollment> findByUserIdAndCourse_CourseId(Integer userId, Long courseId);

    boolean existsByUserIdAndCourse_CourseId(Integer userId, Long courseId);
}