package tn.esprit.academic_management_service.Learning.Controllers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.academic_management_service.Learning.Entities.Course;
import tn.esprit.academic_management_service.Learning.Entities.Level;
import tn.esprit.academic_management_service.Learning.Entities.TypeCourse;
import tn.esprit.academic_management_service.Learning.Services.CourseService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;
    @Autowired
    private Cloudinary cloudinary;


    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllCourses() {
        try {
            return ResponseEntity.ok(courseService.getAllCourses());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> createCourse(@RequestBody Course input) {
        try {
            return ResponseEntity.ok(courseService.createCourse(input));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    /*@GetMapping("/all")
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }*/

    @GetMapping("/{id}")
    public Course getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id).orElse(null);
    }

    @GetMapping("/level/{level}")
    public List<Course> getCoursesByLevel(@PathVariable Level level) {
        return courseService.getCoursesByLevel(level);
    }

    /*@PostMapping("/add")
    public Course createCourse(@RequestBody Course input) {
        return courseService.createCourse(input);
    }*/

    @PutMapping("/update/{id}")
    public Course updateCourse(@PathVariable Long id,
                               @RequestBody Course input) {
        return courseService.updateCourse(id, input);
    }

    @DeleteMapping("/delete/{id}")
    public Boolean deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return true;
    }
    @PostMapping("/{id}/lessons")
    public Course assignLessons(@PathVariable Long id, @RequestBody List<Long> lessonIds) {
        return courseService.assignLessons(id, lessonIds);
    }

    @PostMapping("/{id}/quiz/{quizId}")
    public Course assignQuiz(@PathVariable Long id, @PathVariable Long quizId) {
        return courseService.assignQuiz(id, quizId);
    }
    @PutMapping("/hide/{id}")
    public Course toggleVisibility(@PathVariable Long id) {
        return courseService.toggleVisibility(id);
    }
    @PostMapping("/{id}/image")
    public Course uploadImage(@PathVariable Long id,
                              @RequestParam("file") MultipartFile file) throws IOException {

        if (!file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Only image files allowed");
        }

        // Upload to Cloudinary
        Map result = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "folder", "courses",
                        "public_id", "course_" + id,  // consistent name → auto-replaces old image
                        "overwrite", true
                )
        );

        String imageUrl = (String) result.get("secure_url");
        return courseService.updateImageUrl(id, imageUrl);
    }
    @GetMapping("/catalog")
    public List<Course> getCatalog() {
        return courseService.getVisibleCourses();
    }

    @GetMapping("/enrolled/{userId}")
    public List<Course> getEnrolledCourses(@PathVariable Integer userId) {
        return courseService.getEnrolledCourses(userId);
    }

    @GetMapping("/{courseId}/access/{userId}")
    public boolean checkAccess(@PathVariable Long courseId, @PathVariable Integer userId) {
        return courseService.isEnrolled(userId, courseId);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<?> getCoursesByType(@PathVariable("type") String typeStr) {
        try {
            TypeCourse type = TypeCourse.valueOf(typeStr.toUpperCase());
            return ResponseEntity.ok(courseService.getCoursesByType(type));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("Invalid course type: " + typeStr);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error fetching courses by type: " + e.getMessage());
        }
    }
}