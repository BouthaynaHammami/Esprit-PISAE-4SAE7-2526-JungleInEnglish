package tn.esprit.academic_management_service.Learning.Services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import tn.esprit.academic_management_service.DTO.CourseDTO;
import tn.esprit.academic_management_service.Learning.Config.RabbitMQConfig;
import tn.esprit.academic_management_service.Learning.Entities.Course;
import tn.esprit.academic_management_service.Learning.Entities.Level;
import tn.esprit.academic_management_service.Learning.Entities.TypeCourse;
import tn.esprit.academic_management_service.Learning.Repositories.CourseRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataSyncServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private DataSyncService dataSyncService;

    @Test
    void syncAllCourses_shouldSendAllCoursesToQueue() {
        Course c1 = Course.builder().courseId(1L).title("A1 Basics").price(100f).build();
        Course c2 = Course.builder().courseId(2L).title("B1 Grammar").price(120f).build();
        when(courseRepository.findAll()).thenReturn(List.of(c1, c2));

        dataSyncService.syncAllCourses();

        verify(rabbitTemplate, times(2)).convertAndSend(
                org.mockito.ArgumentMatchers.eq(RabbitMQConfig.COURSE_QUEUE),
                org.mockito.ArgumentMatchers.any(CourseDTO.class)
        );
    }

    @Test
    void sendCourseToQueue_shouldMapCourseFieldsIncludingEnums() {
        Course course = Course.builder()
                .courseId(7L)
                .title("Conversation")
                .description("Practice speaking")
                .level(Level.B1)
                .type(TypeCourse.GENERAL_ENGLISH)
                .price(59.9f)
                .imageUrl("img.png")
                .build();

        dataSyncService.sendCourseToQueue(course);

        ArgumentCaptor<CourseDTO> dtoCaptor = ArgumentCaptor.forClass(CourseDTO.class);
        verify(rabbitTemplate).convertAndSend(org.mockito.ArgumentMatchers.eq(RabbitMQConfig.COURSE_QUEUE), dtoCaptor.capture());

        CourseDTO dto = dtoCaptor.getValue();
        assertEquals(7L, dto.getCourseId());
        assertEquals("Conversation", dto.getTitle());
        assertEquals("B1", dto.getLevel());
        assertEquals("GENERAL_ENGLISH", dto.getType());
        assertEquals(59.9f, dto.getPrice());
    }

    @Test
    void sendCourseToQueue_shouldHandleNullEnums() {
        Course course = Course.builder().courseId(9L).title("No Enum").price(0f).build();

        dataSyncService.sendCourseToQueue(course);

        ArgumentCaptor<CourseDTO> dtoCaptor = ArgumentCaptor.forClass(CourseDTO.class);
        verify(rabbitTemplate).convertAndSend(org.mockito.ArgumentMatchers.eq(RabbitMQConfig.COURSE_QUEUE), dtoCaptor.capture());
        assertNull(dtoCaptor.getValue().getLevel());
        assertNull(dtoCaptor.getValue().getType());
    }
}
