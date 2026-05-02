package tn.esprit.community_engagement_service.Events.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.community_engagement_service.Events.Entities.RegistrationStatus;
import tn.esprit.community_engagement_service.Events.Entities.RegsitrationEvent;

import java.util.List;

public interface RegsitrationEventRepo extends JpaRepository<RegsitrationEvent, Long> {

    // COUNT CONFIRMED (CAPACITY CHECK)
    long countByEvents_EventIdAndStatus(
            Long eventId,
            RegistrationStatus status
    );

    // WAITLIST ORDERED BY DATE
    List<RegsitrationEvent>
    findByEvents_EventIdAndStatusOrderByRegistrationDateAsc(
            Long eventId,
            RegistrationStatus status
    );

    // FIND BY USER
    List<RegsitrationEvent>
    findByUser(Integer userId);
}