package tn.esprit.academic_management_service.Certifications.Config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tn.esprit.academic_management_service.Certifications.Producer.CertificationProducer;

@Component
@RequiredArgsConstructor
@Slf4j
public class CertificationSyncScheduler {

    private final CertificationProducer certificationProducer;

    /**
     * Synchronizes all certifications to RabbitMQ/Elasticsearch daily at midnight.
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void scheduleCertificationSync() {
        log.info("Starting scheduled daily certification synchronization...");
        try {
            certificationProducer.syncAllCertifications();
            log.info("Scheduled certification synchronization completed successfully.");
        } catch (Exception e) {
            log.error("Error during scheduled certification synchronization: {}", e.getMessage());
        }
    }
}
