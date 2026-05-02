package tn.esprit.language_courses_service.ChallengesCompetitions.Config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tn.esprit.language_courses_service.ChallengesCompetitions.Producer.ChallengeProducer;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChallengeSyncScheduler {

    private final ChallengeProducer challengeProducer;

    /**
     * Synchronizes all challenges to RabbitMQ/Elasticsearch every 1 minute.
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void scheduleChallengeSync() {
        log.info("Starting scheduled daily challenge synchronization at midnight (CRON)...");
        try {
            challengeProducer.syncAllChallenges();
            log.info("Scheduled challenge synchronization completed successfully.");
        } catch (Exception e) {
            log.error("Error during scheduled challenge synchronization: {}", e.getMessage());
        }
    }
}
