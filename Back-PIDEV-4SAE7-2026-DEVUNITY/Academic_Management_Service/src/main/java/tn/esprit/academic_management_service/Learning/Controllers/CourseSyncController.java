package tn.esprit.academic_management_service.Learning.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.academic_management_service.Learning.Producer.CourseProducer;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseSyncController {

    private final CourseProducer courseProducer;

    @PostMapping("/sync")
    public ResponseEntity<String> syncCourses() {
        courseProducer.syncAllCourses();
        return ResponseEntity.ok("Synchronization triggered for all courses.");
    }
}
