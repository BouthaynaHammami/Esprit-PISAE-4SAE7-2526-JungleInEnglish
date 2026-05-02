package tn.esprit.academic_management_service.Learning.Config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tn.esprit.academic_management_service.Learning.Producer.CourseProducer;

@Component
@RequiredArgsConstructor
@Slf4j
public class CourseSyncScheduler {

    private final CourseProducer courseProducer;

    /**
     * Synchronizes all courses to RabbitMQ/Elasticsearch every 1 minute.
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void scheduleCourseSync() {
        log.info("Starting scheduled daily course synchronization at midnight (CRON)...");
        try {
            courseProducer.syncAllCourses();
            log.info("Scheduled course synchronization completed successfully.");
        } catch (Exception e) {
            log.error("Error during scheduled course synchronization: {}", e.getMessage());
        }
    }
}
