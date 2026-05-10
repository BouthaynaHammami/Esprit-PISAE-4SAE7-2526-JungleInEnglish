package tn.esprit.social_interaction_service.Reporting_Analytics.Consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import tn.esprit.social_interaction_service.Reporting_Analytics.Config.RabbitMQConfig;
import tn.esprit.social_interaction_service.Reporting_Analytics.DTO.ChallengeDTO;
import tn.esprit.social_interaction_service.Reporting_Analytics.DTO.CourseDTO;
import tn.esprit.social_interaction_service.Reporting_Analytics.DTO.CertificateDTO;
import tn.esprit.social_interaction_service.Reporting_Analytics.DTO.EventDTO;
import tn.esprit.social_interaction_service.Reporting_Analytics.DTO.BookDTO;
import tn.esprit.social_interaction_service.Reporting_Analytics.DTO.ClubDTO;
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
        log.info("Processing Challenge message for Reporting: {}", challengeDTO.getTitle());

        AnalyticsDocument analyticsDoc = AnalyticsDocument.builder()
                .id("CHALLENGE_" + challengeDTO.getId())
                .type("CHALLENGE")
                .indexedAt(new Date())
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
    }

    @RabbitListener(queues = RabbitMQConfig.CERTIFICATION_QUEUE)
    public void consumeCertification(CertificateDTO certificateDTO) {
        log.info("Processing Certification message for Reporting: {}", certificateDTO.getCertificateNumber());

        AnalyticsDocument analyticsDoc = AnalyticsDocument.builder()
                .id("CERTIFICATE_" + certificateDTO.getId())
                .type("CERTIFICATION")
                .indexedAt(new Date())
                .certificateId(certificateDTO.getId())
                .certificateNumber(certificateDTO.getCertificateNumber())
                .certificateLevel(certificateDTO.getLevel())
                .certificateScore(certificateDTO.getScore())
                .certificateIssuedAt(certificateDTO.getIssuedAt())
                .studentId(certificateDTO.getStudentId())
                .build();

        analyticsElasticRepository.save(analyticsDoc);
        log.info("Certification data directly indexed in Elasticsearch (Analytics).");
        System.out.println("✅ Reporting Analytics: Certificate [" + certificateDTO.getCertificateNumber() + "] successfully indexed in Elasticsearch.");
    }

    @RabbitListener(queues = RabbitMQConfig.EVENT_QUEUE)
    public void consumeEvent(EventDTO eventDTO) {
        log.info("Processing Event message for Reporting: {}", eventDTO.getTitle());

        AnalyticsDocument analyticsDoc = AnalyticsDocument.builder()
                .id("EVENT_" + eventDTO.getEventId())
                .type("EVENT")
                .indexedAt(new Date())
                // Event Mapping
                .eventId(eventDTO.getEventId())
                .eventTitle(eventDTO.getTitle())
                .eventDescription(eventDTO.getDescription())
                .eventStartDate(eventDTO.getStartDate())
                .eventEndDate(eventDTO.getEndDate())
                .eventLocation(eventDTO.getLocation())
                .eventCapacity(eventDTO.getCapacity())
                .eventStatus(eventDTO.getStatus())
                .build();

        analyticsElasticRepository.save(analyticsDoc);
        log.info("Event data directly indexed in Elasticsearch (Analytics).");
        System.out.println("✅ Reporting Analytics: Event [" + eventDTO.getTitle() + "] successfully indexed in Elasticsearch.");
    }

    @RabbitListener(queues = RabbitMQConfig.BOOK_QUEUE)
    public void consumeBook(BookDTO bookDTO) {
        log.info("Processing Book message for Reporting: {}", bookDTO.getTitle());

        AnalyticsDocument analyticsDoc = AnalyticsDocument.builder()
                .id("BOOK_" + bookDTO.getBookId())
                .type("BOOK")
                .indexedAt(new Date())
                // Book Mapping
                .bookId(bookDTO.getBookId())
                .bookTitle(bookDTO.getTitle())
                .bookIsbn(bookDTO.getIsbn())
                .bookStatus(bookDTO.getStatus())
                .bookPrice(bookDTO.getSalePrice() != null ? bookDTO.getSalePrice().doubleValue() : 0.0)
                .bookAuthor(bookDTO.getAuthorName())
                .bookCategory(bookDTO.getCategoryName())
                .build();

        analyticsElasticRepository.save(analyticsDoc);
        log.info("Book data directly indexed in Elasticsearch (Analytics).");
        System.out.println("✅ Reporting Analytics: Book [" + bookDTO.getTitle() + "] successfully indexed in Elasticsearch.");
    }

    @RabbitListener(queues = RabbitMQConfig.CLUB_QUEUE)
    public void consumeClub(ClubDTO clubDTO) {
        log.info("Processing Club message for Reporting: {}", clubDTO.getName());

        AnalyticsDocument analyticsDoc = AnalyticsDocument.builder()
                .id("CLUB_" + clubDTO.getClubId())
                .type("CLUB")
                .indexedAt(new Date())
                // Club Mapping
                .clubId(clubDTO.getClubId())
                .clubName(clubDTO.getName())
                .clubDescription(clubDTO.getDescription())
                .clubType(clubDTO.getType())
                .clubStatus(clubDTO.getStatus())
                .clubCreationDate(clubDTO.getCreationDate())
                .build();

        analyticsElasticRepository.save(analyticsDoc);
        log.info("Club data directly indexed in Elasticsearch (Analytics).");
        System.out.println("✅ Reporting Analytics: Club [" + clubDTO.getName() + "] successfully indexed in Elasticsearch.");
    }
}
