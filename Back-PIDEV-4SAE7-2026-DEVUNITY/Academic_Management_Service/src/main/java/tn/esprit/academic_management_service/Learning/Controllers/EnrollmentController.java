package tn.esprit.academic_management_service.Learning.Controllers;

import org.springframework.web.bind.annotation.*;
import tn.esprit.academic_management_service.Learning.Entities.Enrollment;
import tn.esprit.academic_management_service.Learning.Services.EnrollmentService;

import java.util.List;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping("/all")
    public List<Enrollment> getAllEnrollments() {
        return enrollmentService.getAllEnrollments();
    }

    @GetMapping("/{id}")
    public Enrollment getEnrollmentById(@PathVariable Long id) {
        return enrollmentService.getEnrollmentById(id).orElse(null);
    }

    @GetMapping("/user/{userId}")
    public List<Enrollment> getEnrollmentsByUserId(@PathVariable Integer userId) {
        return enrollmentService.getEnrollmentsByUserId(userId);
    }

    @GetMapping("/course/{courseId}")
    public List<Enrollment> getEnrollmentsByCourseId(@PathVariable Long courseId) {
        return enrollmentService.getEnrollmentsByCourseId(courseId);
    }

    @PostMapping("/add/{userId}/{courseId}")
    public Enrollment createEnrollment(@RequestBody Enrollment enrollment,
                                       @PathVariable Integer userId,
                                       @PathVariable Long courseId) {
        return enrollmentService.createEnrollment(enrollment, userId, courseId);
    }

    @PutMapping("/update/{id}")
    public Enrollment updateEnrollment(@PathVariable Long id,
                                       @RequestBody Enrollment input) {
        return enrollmentService.updateEnrollment(id, input);
    }

    @DeleteMapping("/delete/{id}")
    public Boolean deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
        return true;
    }
    @PostMapping("/enroll")
    public Enrollment enroll(@RequestParam Integer userId, @RequestParam Long courseId) {
        return enrollmentService.enrollStudent(userId, courseId);
    }
}