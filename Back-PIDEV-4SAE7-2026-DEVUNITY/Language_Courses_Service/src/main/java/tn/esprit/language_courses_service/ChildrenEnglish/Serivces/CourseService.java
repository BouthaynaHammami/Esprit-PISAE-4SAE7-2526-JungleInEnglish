package tn.esprit.language_courses_service.ChildrenEnglish.Serivces;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.language_courses_service.ChildrenEnglish.Entities.Course;
import tn.esprit.language_courses_service.ChildrenEnglish.Repositories.CourseRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService implements ICourseService {
    
    private final CourseRepository repository;
    
    @Override
    public Course addCourse(Course course) {
        return repository.save(course);
    }
    
    @Override
    public Course updateCourse(Course course) {
        return repository.save(course);
    }
    
    @Override
    public void deleteCourse(long id) {
        repository.deleteById(id);
    }
    
    @Override
    public Course getCourse(long id) {
        return repository.findById(id).orElseThrow(() -> 
            new RuntimeException("Course not found with id: " + id));
    }
    
    @Override
    public List<Course> getAllCourses() {
        return repository.findAll();
    }
}
