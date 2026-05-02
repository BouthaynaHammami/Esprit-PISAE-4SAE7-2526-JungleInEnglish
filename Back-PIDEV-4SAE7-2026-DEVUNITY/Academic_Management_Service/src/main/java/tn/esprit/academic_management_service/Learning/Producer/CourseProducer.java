package tn.esprit.academic_management_service.Learning.Producer;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import tn.esprit.academic_management_service.DTO.CourseDTO;
import tn.esprit.academic_management_service.Learning.Entities.Course;
import tn.esprit.academic_management_service.Learning.Repositories.CourseRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseProducer {

    private final RabbitTemplate rabbitTemplate;
    private final CourseRepository courseRepository;

    public void sendCourse(CourseDTO courseDTO) {
        rabbitTemplate.convertAndSend("course.queue", courseDTO);
        System.out.println("Course message sent: " + courseDTO.getTitle());
    }

    public void syncAllCourses() {
        List<Course> courses = courseRepository.findAll();
        for (Course course : courses) {
            CourseDTO dto = CourseDTO.builder()
                    .courseId(course.getCourseId())
                    .title(course.getTitle())
                    .description(course.getDescription())
                    .level(course.getLevel().toString())
                    .type(course.getType().toString())
                    .price(course.getPrice())
                    .imageUrl(course.getImageUrl())
                    .build();
            sendCourse(dto);
        }
        System.out.println("All existing courses synchronized via RabbitMQ.");
    }
}
