package tn.esprit.academic_management_service.Learning.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.academic_management_service.Clients.UserClient;
import tn.esprit.academic_management_service.DTO.UserDTO;
import tn.esprit.academic_management_service.Learning.Entities.Course;
import tn.esprit.academic_management_service.Learning.Entities.Enrollment;
import tn.esprit.academic_management_service.Learning.Repositories.CourseRepository;
import tn.esprit.academic_management_service.Learning.Repositories.EnrollmentRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private EnrollmentService enrollmentService;

    private Enrollment testEnrollment;
    private Course testCourse;
    private UserDTO testUser;

    @BeforeEach
    void setUp() {
        testCourse = new Course();
        testCourse.setCourseId(1L);
        testCourse.setTitle("Java Basics");

        testEnrollment = new Enrollment();
        testEnrollment.setEnrollmentId(1L);
        testEnrollment.setUserId(100);
        testEnrollment.setCourse(testCourse);
        testEnrollment.setEnrollmentDate(LocalDate.now());

        testUser = new UserDTO();
        testUser.setUserId(100);
        testUser.setFirstName("john");
    }

    @Test
    void getAllEnrollments_shouldReturnAll() {
        when(enrollmentRepository.findAll()).thenReturn(List.of(testEnrollment));

        List<Enrollment> result = enrollmentService.getAllEnrollments();

        assertEquals(1, result.size());
        verify(enrollmentRepository).findAll();
    }

    @Test
    void getEnrollmentById_shouldReturnEnrollmentWhenExists() {
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(testEnrollment));

        Optional<Enrollment> result = enrollmentService.getEnrollmentById(1L);

        assertTrue(result.isPresent());
        assertEquals(100, result.get().getUserId());
    }

    @Test
    void getEnrollmentsByUserId_shouldReturnEnrollmentsForUser() {
        when(enrollmentRepository.findByUserId(100)).thenReturn(List.of(testEnrollment));

        List<Enrollment> result = enrollmentService.getEnrollmentsByUserId(100);

        assertEquals(1, result.size());
        verify(enrollmentRepository).findByUserId(100);
    }

    @Test
    void createEnrollment_shouldThrowWhenUserNotFound() {
        when(userClient.getUserById(999)).thenReturn(null);

        assertThrows(RuntimeException.class,
                () -> enrollmentService.createEnrollment(testEnrollment, 999, 1L));
    }

    @Test
    void createEnrollment_shouldCreateWhenUserAndCourseExist() {
        when(userClient.getUserById(100)).thenReturn(testUser);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(testEnrollment);

        Enrollment result = enrollmentService.createEnrollment(testEnrollment, 100, 1L);

        assertNotNull(result);
        verify(enrollmentRepository).save(any(Enrollment.class));
    }

    @Test
    void enrollStudent_shouldThrowWhenAlreadyEnrolled() {
        when(userClient.getUserById(100)).thenReturn(testUser);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        when(enrollmentRepository.existsByUserIdAndCourse_CourseId(100, 1L)).thenReturn(true);

        assertThrows(RuntimeException.class,
                () -> enrollmentService.enrollStudent(100, 1L));
    }

    @Test
    void enrollStudent_shouldCreateEnrollmentWhenNotEnrolled() {
        when(userClient.getUserById(100)).thenReturn(testUser);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        when(enrollmentRepository.existsByUserIdAndCourse_CourseId(100, 1L)).thenReturn(false);
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(testEnrollment);

        Enrollment result = enrollmentService.enrollStudent(100, 1L);

        assertNotNull(result);
        assertEquals(100, result.getUserId());
        verify(enrollmentRepository).save(any(Enrollment.class));
    }
}
