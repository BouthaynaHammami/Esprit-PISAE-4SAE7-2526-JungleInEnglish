package tn.esprit.academic_management_service.Certifications.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Payload sent over WebSocket when a task deadline is 15 minutes away.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReminderNotificationDTO {

    private Long taskId;
    private String title;
    private String description;
    private String module;
    private LocalDateTime deadline;
    private String message;
}
