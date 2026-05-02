package tn.esprit.language_courses_service.ChildrenEnglish.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.language_courses_service.ChildrenEnglish.Entities.Course;
import tn.esprit.language_courses_service.ChildrenEnglish.Serivces.ICourseService;

import java.util.List;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class CourseController {
    
    private final ICourseService service;
    
    @PostMapping("/add")
    public Course addCourse(@RequestBody Course course) {
        return service.addCourse(course);
    }
    
    @PutMapping("/update")
    public Course updateCourse(@RequestBody Course course) {
        return service.updateCourse(course);
    }
    
    @DeleteMapping("/delete/{id}")
    public void deleteCourse(@PathVariable long id) {
        service.deleteCourse(id);
    }
    
    @GetMapping("/{id}")
    public Course getCourse(@PathVariable long id) {
        return service.getCourse(id);
    }
    
    @GetMapping
    public List<Course> getAllCourses() {
        return service.getAllCourses();
    }
    
    @GetMapping("/child/{childId}")
    public List<Course> getCoursesByChild(@PathVariable Long childId) {
        // For now, return all courses - you can filter by child level later
        return service.getAllCourses();
    }
}
