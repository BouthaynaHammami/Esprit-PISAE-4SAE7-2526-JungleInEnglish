package tn.esprit.social_interaction_service.Reporting_Analytics.Consumer;

// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.amqp.rabbit.annotation.RabbitListener;
// import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
// import org.springframework.stereotype.Service;
// import tn.esprit.social_interaction_service.Reporting_Analytics.Config.RabbitMQConfig;
// import tn.esprit.social_interaction_service.Reporting_Analytics.DTO.ChallengeDTO;
// import tn.esprit.social_interaction_service.Reporting_Analytics.DTO.CourseDTO;
// import tn.esprit.social_interaction_service.Reporting_Analytics.DTO.CertificateDTO;
// import tn.esprit.social_interaction_service.Reporting_Analytics.DTO.EventDTO;
// import tn.esprit.social_interaction_service.Reporting_Analytics.DTO.BookDTO;
// import tn.esprit.social_interaction_service.Reporting_Analytics.DTO.ClubDTO;
// import tn.esprit.social_interaction_service.Reporting_Analytics.Documents.AnalyticsDocument;
// import tn.esprit.social_interaction_service.Reporting_Analytics.Repositories.AnalyticsElasticRepository;
//
// import java.util.Date;

// @Service
// @ConditionalOnProperty(name = "spring.data.elasticsearch.repositories.enabled", havingValue = "true")
// @RequiredArgsConstructor
// @Slf4j
public class ReportingConsumer {

    /*
    private final AnalyticsElasticRepository analyticsElasticRepository;

    @RabbitListener(queues = RabbitMQConfig.COURSE_QUEUE)
    public void consumeCourse(CourseDTO courseDTO) {
        log.info("Processing Course message for Reporting: {}", courseDTO.getTitle());

        AnalyticsDocument analyticsDoc = AnalyticsDocument.builder()
                .id("COURSE_" + courseDTO.getCourseId())
                .type("COURSE")
                .indexedAt(new Date())
                .courseId(courseDTO.getCourseId())
                .courseTitle(courseDTO.getTitle())
                .courseDescription(courseDTO.getDescription())
                .courseLevel(courseDTO.getLevel())
                .courseType(courseDTO.getType())
                .coursePrice(courseDTO.getPrice())
                .courseImageUrl(courseDTO.getImageUrl())
                .courseStatus("IN_PROGRESS")
                .courseScore(0.0f)
                .courseIsCertified(false)
                .courseEnrollmentCount(1)
                .courseTimeSpentSeconds(0L)
                .build();

        analyticsElasticRepository.save(analyticsDoc);
        log.info("Course data directly indexed in Elasticsearch (Analytics).");
    }

    @RabbitListener(queues = RabbitMQConfig.CHALLENGE_QUEUE)
    public void consumeChallenge(ChallengeDTO challengeDTO) {
        // disabled temporarily
    }

    @RabbitListener(queues = RabbitMQConfig.CERTIFICATION_QUEUE)
    public void consumeCertification(CertificateDTO certificateDTO) {
        // disabled temporarily
    }

    @RabbitListener(queues = RabbitMQConfig.EVENT_QUEUE)
    public void consumeEvent(EventDTO eventDTO) {
        // disabled temporarily
    }

    @RabbitListener(queues = RabbitMQConfig.BOOK_QUEUE)
    public void consumeBook(BookDTO bookDTO) {
        // disabled temporarily
    }

    @RabbitListener(queues = RabbitMQConfig.CLUB_QUEUE)
    public void consumeClub(ClubDTO clubDTO) {
        // disabled temporarily
    }
    */
}