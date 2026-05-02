package tn.esprit.academic_management_service.Learning.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.academic_management_service.Learning.Entities.*;
import tn.esprit.academic_management_service.Learning.Repositories.CourseRepository;
import tn.esprit.academic_management_service.Learning.Repositories.EnrollmentRepository;
import tn.esprit.academic_management_service.Learning.Repositories.LessonRepository;
import tn.esprit.academic_management_service.Learning.Repositories.QuizRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    private CourseService courseService;

    private Course testCourse;

    @BeforeEach
    void setUp() {
        testCourse = new Course();
        testCourse.setCourseId(1L);
        testCourse.setTitle("Advanced Java");
        testCourse.setLevel(Level.B2);
        testCourse.setPrice(99.99f);
    }

    @Test
    void getAllCourses_shouldReturnAllCourses() {
        when(courseRepository.findAll()).thenReturn(List.of(testCourse));

        List<Course> result = courseService.getAllCourses();

        assertEquals(1, result.size());
        assertEquals("Advanced Java", result.get(0).getTitle());
        verify(courseRepository).findAll();
    }

    @Test
    void getCourseById_shouldReturnCourseWhenExists() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));

        Optional<Course> result = courseService.getCourseById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getCourseId());
    }

    @Test
    void createCourse_shouldPersistCourse() {
        when(courseRepository.save(testCourse)).thenReturn(testCourse);

        Course result = courseService.createCourse(testCourse);

        assertNotNull(result);
        assertEquals("Advanced Java", result.getTitle());
        verify(courseRepository).save(testCourse);
    }

    @Test
    void updateCourse_shouldUpdateCourseFieldsAndSave() {
        Course updates = new Course();
        updates.setTitle("Updated Java");
        updates.setDescription("New description");
        updates.setLevel(Level.C1);
        updates.setPrice(149.99f);
        updates.setLessonsNumber(20);
        updates.setHidden(false);
        updates.setImageUrl("img.jpg");

        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        when(courseRepository.save(any(Course.class))).thenReturn(testCourse);

        Course result = courseService.updateCourse(1L, updates);

        assertNotNull(result);
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    void deleteCourse_shouldCallRepositoryDelete() {
        courseService.deleteCourse(1L);

        verify(courseRepository).deleteById(1L);
    }

    @Test
    void assignLessons_shouldReassignLessonsAndUpdateCount() {
        Lesson l1 = new Lesson();
          l1.setLessonId(10L);
        Lesson l2 = new Lesson();
          l2.setLessonId(11L);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        when(lessonRepository.findByCourse(testCourse)).thenReturn(List.of());
        when(lessonRepository.findAllById(List.of(10L, 11L))).thenReturn(List.of(l1, l2));
        when(courseRepository.save(any(Course.class))).thenReturn(testCourse);

        courseService.assignLessons(1L, List.of(10L, 11L));

        assertEquals(2, testCourse.getLessonsNumber());
        verify(lessonRepository, times(2)).save(any(Lesson.class));
        verify(courseRepository).save(testCourse);
    }

    @Test
    void toggleVisibility_shouldFlipHiddenFlag() {
        testCourse.setHidden(false);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        when(courseRepository.save(testCourse)).thenReturn(testCourse);

        Course result = courseService.toggleVisibility(1L);

        assertTrue(result.isHidden());
        verify(courseRepository).save(testCourse);
    }

    @Test
    void getEnrolledCourses_shouldReturnCoursesForUser() {
        Enrollment e1 = new Enrollment();
        e1.setCourse(testCourse);
        when(enrollmentRepository.findByUserId(1)).thenReturn(List.of(e1));

        List<Course> result = courseService.getEnrolledCourses(1);

        assertEquals(1, result.size());
        verify(enrollmentRepository).findByUserId(1);
    }

    @Test
    void isEnrolled_shouldReturnTrueWhenUserEnrolled() {
        when(enrollmentRepository.existsByUserIdAndCourse_CourseId(1, 1L)).thenReturn(true);

        boolean result = courseService.isEnrolled(1, 1L);

        assertTrue(result);
    }
}
