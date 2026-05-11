package tn.esprit.community_engagement_service.Events.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.community_engagement_service.Events.Entities.Events;
import tn.esprit.community_engagement_service.Events.Repositories.EventRepo;
import tn.esprit.community_engagement_service.Events.Repositories.RegsitrationEventRepo;
import tn.esprit.community_engagement_service.Events.Producer.EventProducer;
import tn.esprit.community_engagement_service.Events.DTO.EventDTO;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepo eventRepo;

    @Autowired
    private RegsitrationEventRepo regsitrationEventRepo;

    @Autowired
    private EventProducer eventProducer;

    public List<Events> getAllEvents() {
        return eventRepo.findAll();
    }

    public Events getEventById(Long id) {
        return eventRepo.findById(id).orElse(null);
    }

    public Events addEvent(Events e) {
        Events saved = eventRepo.save(e);
        sendToElastic(saved);
        return saved;
    }

    @Transactional
    public void deleteEvent(Long id) {
        regsitrationEventRepo.deleteByEvents_EventId(id);
        eventRepo.deleteById(id);
    }

    public Events updateEvent(Long id, Events e) {
        Events existing = eventRepo.findById(id).orElse(null);
        if (existing == null) return null;

        e.setEventId(id);
        Events updated = eventRepo.save(e);
        sendToElastic(updated);
        return updated;
    }

    private void sendToElastic(Events event) {
        eventProducer.sendEvent(EventDTO.builder()
                .eventId(event.getEventId())
                .title(event.getTitle())
                .description(event.getDescription())
                .startDate(Date.from(event.getStartDate().atZone(ZoneId.systemDefault()).toInstant()))
                .endDate(Date.from(event.getEndDate().atZone(ZoneId.systemDefault()).toInstant()))
                .location(event.getLocation())
                .capacity(event.getCapacity())
                .status(event.getStatus() != null ? event.getStatus().name() : null)
                .build());
    }
}
