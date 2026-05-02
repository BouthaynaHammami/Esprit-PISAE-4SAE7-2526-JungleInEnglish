package tn.esprit.language_courses_service.ChildrenEnglish.Serivces;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.language_courses_service.ChildrenEnglish.Entities.Course;
import tn.esprit.language_courses_service.ChildrenEnglish.Entities.LevelChildren;
import tn.esprit.language_courses_service.ChildrenEnglish.Repositories.CourseRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository repository;

    @InjectMocks
    private CourseService courseService;

    private Course testCourse;

    @BeforeEach
    void setUp() {
        testCourse = new Course();
        testCourse.setCourseId(1L);
        testCourse.setTitle("Beginner English");
        LevelChildren level = new LevelChildren();
        level.setName("A1");
        level.setMinAge(6);
        level.setMaxAge(8);
        testCourse.setLevelChildren(level);
    }

    @Test
    void addCourse_shouldPersistCourse() {
        when(repository.save(testCourse)).thenReturn(testCourse);

        Course result = courseService.addCourse(testCourse);

        assertNotNull(result);
        assertEquals("Beginner English", result.getTitle());
        verify(repository).save(testCourse);
    }

    @Test
    void updateCourse_shouldUpdateAndSave() {
        when(repository.save(testCourse)).thenReturn(testCourse);

        Course result = courseService.updateCourse(testCourse);

        assertNotNull(result);
        verify(repository).save(testCourse);
    }

    @Test
    void deleteCourse_shouldCallRepositoryDelete() {
        courseService.deleteCourse(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void getCourse_shouldReturnCourseWhenExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(testCourse));

        Course result = courseService.getCourse(1L);

        assertNotNull(result);
        assertEquals("Beginner English", result.getTitle());
    }

    @Test
    void getCourse_shouldThrowWhenNotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> courseService.getCourse(999L));
    }

    @Test
    void getAllCourses_shouldReturnAllCourses() {
        when(repository.findAll()).thenReturn(List.of(testCourse));

        List<Course> result = courseService.getAllCourses();

        assertEquals(1, result.size());
        verify(repository).findAll();
    }
}
