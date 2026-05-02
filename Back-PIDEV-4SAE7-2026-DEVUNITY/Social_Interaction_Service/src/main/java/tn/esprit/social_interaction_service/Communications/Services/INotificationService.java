package tn.esprit.social_interaction_service.Communications.Services;

import tn.esprit.social_interaction_service.Communications.Entities.Notification;

import java.util.List;

public interface INotificationService {
    List<Notification> getAllNotifications();
    Notification getNotificationById(Long id);
    Notification addNotification(Notification notification);
    Notification updateNotification(Notification notification);
    void deleteNotification(Long id);
    List<Notification> getNotificationsByUserId(Integer userId);
    List<Notification> getUnreadNotifications(Integer userId);
    void markAsRead(Long notificationId);
}
