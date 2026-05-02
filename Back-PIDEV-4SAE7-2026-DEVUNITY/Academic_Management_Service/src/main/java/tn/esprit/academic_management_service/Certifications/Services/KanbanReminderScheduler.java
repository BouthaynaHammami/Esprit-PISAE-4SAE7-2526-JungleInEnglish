package tn.esprit.academic_management_service.Certifications.Services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.academic_management_service.Certifications.DTO.ReminderNotificationDTO;
import tn.esprit.academic_management_service.Certifications.Entities.KanbanTask;
import tn.esprit.academic_management_service.Certifications.Repositories.KanbanTaskRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Runs every 30 seconds, finds Kanban tasks whose deadline is within the
 * next 15 minutes and pushes a real-time WebSocket notification to the user.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class KanbanReminderScheduler {

    private final KanbanTaskRepository kanbanTaskRepository;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Checks every 30 seconds for tasks approaching their deadline.
     * Sends a STOMP message to /topic/reminders/{userId}.
     */
    @Scheduled(fixedRate = 30_000) // every 30 seconds
    @Transactional
    public void checkUpcomingDeadlines() {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime limit = now.plusMinutes(15);

        List<KanbanTask> tasks = kanbanTaskRepository.findTasksNeedingReminder(now, limit,
                tn.esprit.academic_management_service.Certifications.Entities.KanbanStatus.DONE);

        for (KanbanTask task : tasks) {

            ReminderNotificationDTO notification = ReminderNotificationDTO.builder()
                    .taskId(task.getId())
                    .title(task.getTitle())
                    .description(task.getDescription())
                    .module("Certifications")
                    .deadline(task.getDeadline())
                    .message("Reminder: Task '" + task.getTitle()
                            + "' is due in less than 15 minutes! (Deadline: "
                            + task.getDeadline() + ")")
                    .build();

            // Send to user-specific topic
            String destination = "/topic/reminders/" + task.getUserId();
            messagingTemplate.convertAndSend(destination, notification);

            log.info("Sent reminder for task {} to user {}", task.getId(), task.getUserId());

            // Mark as sent so we don't spam
            task.setReminderSent(true);
            kanbanTaskRepository.save(task);
        }
    }
}
