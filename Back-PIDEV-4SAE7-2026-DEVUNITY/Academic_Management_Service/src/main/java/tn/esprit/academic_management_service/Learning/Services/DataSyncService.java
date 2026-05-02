package tn.esprit.academic_management_service.Learning.Services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.academic_management_service.Learning.Config.RabbitMQConfig;
import tn.esprit.academic_management_service.DTO.CourseDTO;
import tn.esprit.academic_management_service.Learning.Entities.Course;
import tn.esprit.academic_management_service.Learning.Repositories.CourseRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataSyncService {

    private final CourseRepository courseRepository;
    private final RabbitTemplate rabbitTemplate;

    /**
     * Synchronizes all existing courses to Elasticsearch via RabbitMQ.
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void syncAllCourses() {
        System.out.println("Scheduled synchronization: Sending all Courses to RabbitMQ...");
        List<Course> courses = courseRepository.findAll();
        
        for (Course course : courses) {
            sendCourseToQueue(course);
        }
        
        log.info("Successfully sent {} courses to {}", courses.size(), RabbitMQConfig.COURSE_QUEUE);
    }

    public void sendCourseToQueue(Course course) {
        CourseDTO courseDTO = CourseDTO.builder()
                .courseId(course.getCourseId())
                .title(course.getTitle())
                .description(course.getDescription())
                .level(course.getLevel() != null ? course.getLevel().name() : null)
                .type(course.getType() != null ? course.getType().name() : null)
                .price(course.getPrice())
                .imageUrl(course.getImageUrl())
                .build();

        rabbitTemplate.convertAndSend(RabbitMQConfig.COURSE_QUEUE, courseDTO);
        log.debug("Sent Course {} to queue", course.getCourseId());
        System.out.println("📤 [Academic Service] Course [" + course.getTitle() + "] sent to queue for indexing.");
    }
}
