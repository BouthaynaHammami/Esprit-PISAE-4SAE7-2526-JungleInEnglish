package tn.esprit.community_engagement_service.Events.Services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.community_engagement_service.Events.DTO.ReminderNotificationDTO;
import tn.esprit.community_engagement_service.Events.Entities.Events;
import tn.esprit.community_engagement_service.Events.Entities.KanbanTask;
import tn.esprit.community_engagement_service.Events.Entities.RegistrationStatus;
import tn.esprit.community_engagement_service.Events.Entities.RegsitrationEvent;
import tn.esprit.community_engagement_service.Events.Repositories.EventRepo;
import tn.esprit.community_engagement_service.Events.Repositories.KanbanTaskRepository;
import tn.esprit.community_engagement_service.Events.Repositories.RegsitrationEventRepo;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Runs every 30 seconds:
 * 1) Checks Kanban tasks whose deadline is within 15 minutes
 * 2) Checks upcoming Events whose startDate is within 15 minutes
 *    (for users with CONFIRMED registrations)
 *
 * Sends real-time WebSocket notifications via STOMP.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class KanbanReminderScheduler {

    private final KanbanTaskRepository kanbanTaskRepository;
    private final EventRepo eventRepo;
    private final RegsitrationEventRepo registrationEventRepo;
    private final SimpMessagingTemplate messagingTemplate;

    // =====================================================
    // 1) KANBAN TASK DEADLINE REMINDERS
    // =====================================================

    @Scheduled(fixedRate = 30_000) // every 30 seconds
    @Transactional
    public void checkUpcomingTaskDeadlines() {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime limit = now.plusMinutes(15);

        List<KanbanTask> tasks = kanbanTaskRepository.findTasksNeedingReminder(now, limit,
                tn.esprit.community_engagement_service.Events.Entities.KanbanStatus.DONE);

        for (KanbanTask task : tasks) {

            ReminderNotificationDTO notification = ReminderNotificationDTO.builder()
                    .taskId(task.getId())
                    .title(task.getTitle())
                    .description(task.getDescription())
                    .module("Community Engagement Events")
                    .deadline(task.getDeadline())
                    .message("Reminder: Task '" + task.getTitle()
                            + "' is due in less than 15 minutes! (Deadline: "
                            + task.getDeadline() + ")")
                    .build();

            String destination = "/topic/reminders/" + task.getUserId();
            messagingTemplate.convertAndSend(destination, notification);

            log.info("Sent task reminder for task {} to user {}", task.getId(), task.getUserId());

            task.setReminderSent(true);
            kanbanTaskRepository.save(task);
        }
    }

    // =====================================================
    // 2) EVENT START-TIME REMINDERS (15 min before event)
    // =====================================================

    @Scheduled(fixedRate = 30_000) // every 30 seconds
    @Transactional
    public void checkUpcomingEventStartTimes() {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime limit = now.plusMinutes(15);

        // Find all events starting in the next 15 minutes
        List<Events> allEvents = eventRepo.findAll();

        for (Events event : allEvents) {
            if (event.getStartDate() == null) continue;
            if (event.getStartDate().isBefore(now) || event.getStartDate().isAfter(limit)) continue;

            // Get all confirmed registrations for this event
            List<RegsitrationEvent> registrations =
                    registrationEventRepo.findByEvents_EventIdAndStatusOrderByRegistrationDateAsc(
                            event.getEventId(), RegistrationStatus.CONFIRMED);

            for (RegsitrationEvent reg : registrations) {

                ReminderNotificationDTO notification = ReminderNotificationDTO.builder()
                        .taskId(event.getEventId())
                        .title(event.getTitle())
                        .description(event.getDescription())
                        .module("Community Engagement Events")
                        .deadline(event.getStartDate())
                        .message("Reminder: Event '" + event.getTitle()
                                + "' starts in less than 15 minutes! (Start: "
                                + event.getStartDate() + ", Location: "
                                + event.getLocation() + ")")
                        .build();

                String destination = "/topic/reminders/" + reg.getUser();
                messagingTemplate.convertAndSend(destination, notification);

                log.info("Sent event reminder for event {} to user {}",
                        event.getEventId(), reg.getUser());
            }
        }
    }
}
