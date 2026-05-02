package tn.esprit.employee.Services.ImplServices;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.employee.Dto.Role;
import tn.esprit.employee.Dto.UserDTO;
import tn.esprit.employee.Entities.Notification;
import tn.esprit.employee.Entities.NotificationType;
import tn.esprit.employee.Feign.EmployeeUserClient;
import tn.esprit.employee.Repositories.NotificationRepository;
import tn.esprit.employee.Services.IServices.INotificationService;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements INotificationService {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final EmployeeUserClient userClient;

    @Override
    @Transactional
    public void sendToAll(String message, NotificationType type) {
        createNotification("ALL", null, "System Notification", message, type, null, null);
    }

    @Override
    @Transactional
    public void sendToUser(String username, String message, NotificationType type) {
        createNotification(username, null, "New Alert", message, type, null, null);
    }

    @Override
    @Transactional
    public void sendToRole(Role role, String title, String message, NotificationType type, String entityType, Long entityId) {
        System.out.println("[NotificationService] Fetching all users to send notifications for role: " + role);
        List<UserDTO> users = userClient.getAllUsers();
        if (users != null) {
            long count = users.stream().filter(u -> u.getRole() == role).count();
            System.out.println("[NotificationService] Found " + users.size() + " users total, " + count + " matching role " + role);
            
            users.stream()
                .filter(u -> u.getRole() == role)
                .forEach(u -> {
                    System.out.println("[NotificationService] Creating notification for user: " + u.getEmail());
                    createNotification(u.getEmail(), u.getUserId(), title, message, type, entityType, entityId);
                });
        } else {
            System.err.println("[NotificationService] userClient.getAllUsers() returned NULL!");
        }
    }

    @Override
    public Page<Notification> getMyNotifications(String username, Pageable pageable) {
        List<String> searchEmails = List.of(username, "ALL");
        System.out.println("[NotificationService] Querying notifications for: " + searchEmails);
        Page<Notification> result = notificationRepository.findByRecipientEmailInOrderByCreatedAtDesc(searchEmails, pageable);
        System.out.println("[NotificationService] Query result: " + result.getTotalElements() + " total items found.");
        if (result.getTotalElements() > 0) {
            System.out.println("[NotificationService] First item recipient: " + result.getContent().get(0).getRecipientEmail());
        }
        return result;
    }

    @Override
    @Transactional
    public void markAsRead(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(n -> {
            n.setRead(true);
            n.setReadAt(java.time.LocalDateTime.now());
            notificationRepository.save(n);
        });
    }

    @Override
    public long getUnreadCount(String username) {
        List<String> searchEmails = List.of(username, "ALL");
        long count = notificationRepository.countByRecipientEmailInAndIsReadFalse(searchEmails);
        System.out.println("[NotificationService] Unread count for " + searchEmails + ": " + count);
        return count;
    }

    @Override
    @Transactional
    public void createNotification(String targetUserEmail, Long userId, String title, String message, NotificationType type, String entityType, Long entityId) {
        Notification notification = Notification.builder()
                .recipientEmail(targetUserEmail)
                .userId(userId)
                .title(title)
                .message(message)
                .type(type)
                .relatedEntityType(entityType)
                .relatedEntityId(entityId)
                .isRead(false)
                .status("NEW")
                .createdAt(java.time.LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
        System.out.println("[NotificationService] Notification saved successfully to DB for: " + targetUserEmail);
        
        // Push via WebSocket (Wrap in try-catch to avoid DB rollback on WS failure)
        try {
            if ("ALL".equals(targetUserEmail)) {
                messagingTemplate.convertAndSend("/topic/broadcast", notification);
            } else {
                messagingTemplate.convertAndSendToUser(targetUserEmail, "/queue/notifications", notification);
            }
        } catch (Exception e) {
            System.err.println("[NotificationService] WebSocket push failed: " + e.getMessage());
        }
    }
}
