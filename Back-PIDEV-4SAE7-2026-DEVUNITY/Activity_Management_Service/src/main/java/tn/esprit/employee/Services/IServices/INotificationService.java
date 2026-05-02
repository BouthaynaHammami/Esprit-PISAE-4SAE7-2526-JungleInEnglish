package tn.esprit.employee.Services.IServices;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tn.esprit.employee.Entities.Notification;
import tn.esprit.employee.Entities.NotificationType;
import tn.esprit.employee.Dto.Role;

import java.util.List;

public interface INotificationService {
    
    void sendToAll(String message, NotificationType type);
    
    void sendToUser(String username, String message, NotificationType type);
    
    void sendToRole(Role role, String title, String message, NotificationType type, String entityType, Long entityId);

    Page<Notification> getMyNotifications(String username, Pageable pageable);
    
    void markAsRead(Long notificationId);
    
    long getUnreadCount(String username);

    void createNotification(String targetUserEmail, Long userId, String title, String message, NotificationType type, String entityType, Long entityId);
}
