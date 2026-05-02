package tn.esprit.academic_management_service.Learning.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.academic_management_service.Clients.UserClient;
import tn.esprit.academic_management_service.DTO.UserDTO;
import tn.esprit.academic_management_service.Learning.Entities.Course;
import tn.esprit.academic_management_service.Learning.Entities.Enrollment;
import tn.esprit.academic_management_service.Learning.Repositories.CourseRepository;
import tn.esprit.academic_management_service.Learning.Repositories.EnrollmentRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserClient userClient;

    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    public Optional<Enrollment> getEnrollmentById(Long id) {
        return enrollmentRepository.findById(id);
    }

    public List<Enrollment> getEnrollmentsByUserId(Integer userId) {
        return enrollmentRepository.findByUserId(userId);
    }

    public List<Enrollment> getEnrollmentsByCourseId(Long courseId) {
        return enrollmentRepository.findByCourse_CourseId(courseId);
    }

    public Enrollment createEnrollment(Enrollment enrollment, Integer userId, Long courseId) {

        UserDTO user = userClient.getUserById(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        enrollment.setUserId(userId);
        enrollment.setCourse(course);

        return enrollmentRepository.save(enrollment);
    }

    public Enrollment updateEnrollment(Long id, Enrollment enrollmentDetails) {

        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        enrollment.setEnrollmentDate(enrollmentDetails.getEnrollmentDate());
        enrollment.setDuration(enrollmentDetails.getDuration());
        enrollment.setProgress(enrollmentDetails.getProgress());
        enrollment.setIsCompleted(enrollmentDetails.getIsCompleted());
        enrollment.setScore(enrollmentDetails.getScore());

        return enrollmentRepository.save(enrollment);
    }

    public void deleteEnrollment(Long id) {
        enrollmentRepository.deleteById(id);
    }

    public Enrollment enrollStudent(Integer userId, Long courseId) {

        UserDTO user = userClient.getUserById(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (enrollmentRepository.existsByUserIdAndCourse_CourseId(userId, courseId)) {
            throw new RuntimeException("Student already enrolled in this course");
        }

        Enrollment enrollment = Enrollment.builder()
                .userId(userId)
                .course(course)
                .enrollmentDate(LocalDate.now())
                .progress(0)
                .isCompleted(false)
                .build();

        return enrollmentRepository.save(enrollment);
    }
}