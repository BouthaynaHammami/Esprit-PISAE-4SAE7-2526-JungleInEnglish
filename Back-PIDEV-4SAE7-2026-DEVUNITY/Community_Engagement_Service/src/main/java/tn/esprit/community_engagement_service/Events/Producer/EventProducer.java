package tn.esprit.community_engagement_service.Events.Producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import tn.esprit.community_engagement_service.Config.RabbitMQConfig;
import tn.esprit.community_engagement_service.Events.DTO.EventDTO;
import tn.esprit.community_engagement_service.Events.Entities.Events;
import tn.esprit.community_engagement_service.Events.Repositories.EventRepo;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventProducer {

    private final RabbitTemplate rabbitTemplate;
    private final EventRepo eventRepository;

    public void sendEvent(EventDTO eventDTO) {
        log.info("Sending Event to RabbitMQ: {}", eventDTO.getTitle());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EVENT_QUEUE, eventDTO);
    }

    public void syncAllEvents() {
        log.info("Synchronizing all existing events to Elasticsearch...");
        List<Events> events = eventRepository.findAll();
        for (Events event : events) {
            EventDTO dto = EventDTO.builder()
                    .eventId(event.getEventId())
                    .title(event.getTitle())
                    .description(event.getDescription())
                    .startDate(Date.from(event.getStartDate().atZone(ZoneId.systemDefault()).toInstant()))
                    .endDate(Date.from(event.getEndDate().atZone(ZoneId.systemDefault()).toInstant()))
                    .location(event.getLocation())
                    .capacity(event.getCapacity())
                    .status(event.getStatus() != null ? event.getStatus().name() : null)
                    .build();
            sendEvent(dto);
        }
        log.info("Synchronization of {} events completed.", events.size());
    }
}
