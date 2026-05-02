package tn.esprit.social_interaction_service.Reporting_Analytics.Consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import tn.esprit.social_interaction_service.Reporting_Analytics.Config.RabbitMQConfig;
import tn.esprit.social_interaction_service.Reporting_Analytics.DTO.ChallengeDTO;
import tn.esprit.social_interaction_service.Reporting_Analytics.DTO.CourseDTO;
import tn.esprit.social_interaction_service.Reporting_Analytics.Documents.AnalyticsDocument;
import tn.esprit.social_interaction_service.Reporting_Analytics.Repositories.AnalyticsElasticRepository;

import java.util.Date;

@Service
@ConditionalOnProperty(name = "spring.data.elasticsearch.repositories.enabled", havingValue = "true")
@RequiredArgsConstructor
@Slf4j
public class ReportingConsumer {

    private final AnalyticsElasticRepository analyticsElasticRepository;

    @RabbitListener(queues = RabbitMQConfig.COURSE_QUEUE)
    public void consumeCourse(CourseDTO courseDTO) {
        log.info("Processing Course message for Reporting: {}", courseDTO.getTitle());

        // Map to Unified Analytics Document with Course-specific fields
        AnalyticsDocument analyticsDoc = AnalyticsDocument.builder()
                .id("COURSE_" + courseDTO.getCourseId())
                .type("COURSE")
                .indexedAt(new Date())
                // Course Mapping
                .courseId(courseDTO.getCourseId())
                .courseTitle(courseDTO.getTitle())
                .courseDescription(courseDTO.getDescription())
                .courseLevel(courseDTO.getLevel())
                .courseType(courseDTO.getType())
                .coursePrice(courseDTO.getPrice())
                .courseImageUrl(courseDTO.getImageUrl())
                .courseStatus("IN_PROGRESS") // Initial status for analytics
                .courseScore(0.0f)
                .courseIsCertified(false)
                .courseEnrollmentCount(1)
                .courseTimeSpentSeconds(0L)
                .build();

        analyticsElasticRepository.save(analyticsDoc);
        log.info("Course data directly indexed in Elasticsearch (Analytics).");
        System.out.println("✅ Reporting Analytics: Course [" + courseDTO.getTitle() + "] successfully indexed in Elasticsearch.");
    }

    @RabbitListener(queues = RabbitMQConfig.CHALLENGE_QUEUE)
    public void consumeChallenge(ChallengeDTO challengeDTO) {
        log.info("Processing Challenge message for Reporting: {}", challengeDTO.getTitle());

        // Map to Unified Analytics Document with Challenge-specific fields
        AnalyticsDocument analyticsDoc = AnalyticsDocument.builder()
                .id("CHALLENGE_" + challengeDTO.getId())
                .type("CHALLENGE")
                .indexedAt(new Date())
                // Challenge Mapping
                .challengeId(challengeDTO.getId())
                .challengeTitle(challengeDTO.getTitle())
                .challengeDescription(challengeDTO.getDescription())
                .challengeType(challengeDTO.getType())
                .challengeLevel(challengeDTO.getLevel())
                .challengeStartDate(challengeDTO.getStartDate())
                .challengeEndDate(challengeDTO.getEndDate())
                .challengeParticipationCount(0)
                .challengeCompletionCount(0)
                .challengeAverageCompletionTimeSeconds(0L)
                .challengePopularityScore(0.0f)
                .build();

        analyticsElasticRepository.save(analyticsDoc);
        log.info("Challenge data directly indexed in Elasticsearch (Analytics).");
        System.out.println("✅ Reporting Analytics: Challenge [" + challengeDTO.getTitle() + "] successfully indexed in Elasticsearch.");
    }
}
