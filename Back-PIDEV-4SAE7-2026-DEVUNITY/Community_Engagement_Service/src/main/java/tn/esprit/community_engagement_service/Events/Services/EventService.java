package tn.esprit.community_engagement_service.Events.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.community_engagement_service.Events.Entities.Events;
import tn.esprit.community_engagement_service.Events.Repositories.EventRepo;

import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepo eventRepo;

    public List<Events> getAllEvents() {
        return eventRepo.findAll();
    }

    public Events getEventById(Long id) {
        return eventRepo.findById(id).orElse(null);
    }

    public Events addEvent(Events e) {
        return eventRepo.save(e);
    }

    public void deleteEvent(Long id) {
        eventRepo.deleteById(id);
    }

    public Events updateEvent(Long id, Events e) {
        Events existing = eventRepo.findById(id).orElse(null);
        if (existing == null) return null;

        // keep same id then save
        e.setEventId(id);
        return eventRepo.save(e);
    }
}
