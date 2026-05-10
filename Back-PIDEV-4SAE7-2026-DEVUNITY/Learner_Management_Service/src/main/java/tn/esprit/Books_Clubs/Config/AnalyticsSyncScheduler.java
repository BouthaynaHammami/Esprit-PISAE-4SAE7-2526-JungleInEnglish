package tn.esprit.Books_Clubs.Config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tn.esprit.Books_Clubs.Producer.BookProducer;
import tn.esprit.Books_Clubs.Producer.ClubProducer;

@Component
@RequiredArgsConstructor
@Slf4j
public class AnalyticsSyncScheduler {

    private final BookProducer bookProducer;
    private final ClubProducer clubProducer;

    /**
     * Synchronizes all books and clubs to RabbitMQ/Elasticsearch daily at midnight.
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void scheduleAnalyticsSync() {
        log.info("Starting scheduled daily analytics synchronization (Books & Clubs)...");
        try {
            bookProducer.syncAllBooks();
            clubProducer.syncAllClubs();
            log.info("Scheduled analytics synchronization completed successfully.");
        } catch (Exception e) {
            log.error("Error during scheduled analytics synchronization: {}", e.getMessage());
        }
    }
}
