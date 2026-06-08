package tn.esprit.community_engagement_service.Events.Config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tn.esprit.community_engagement_service.Events.Producer.EventProducer;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventSyncScheduler {

    private final EventProducer eventProducer;

    /**
     * Synchronizes all events to RabbitMQ/Elasticsearch daily at midnight.
     */
    @Scheduled(cron = "0 39 1 * * *")
    public void scheduleEventSync() {
        log.info("Starting scheduled daily event synchronization...");
        try {
            eventProducer.syncAllEvents();
            log.info("Scheduled event synchronization completed successfully.");
        } catch (Exception e) {
            log.error("Error during scheduled event synchronization: {}", e.getMessage());
        }
    }
}
