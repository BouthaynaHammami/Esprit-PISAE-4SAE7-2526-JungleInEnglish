package tn.esprit.social_interaction_service.Communications.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.social_interaction_service.Communications.Entities.Notification;
import tn.esprit.social_interaction_service.Communications.Repositories.NotificationRepository;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService implements INotificationService {
    
    private final NotificationRepository notificationRepository;
    
    @Override
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }
    
    @Override
    public Notification getNotificationById(Long id) {
        return notificationRepository.findById(id).orElse(null);
    }
    
    @Override
    public Notification addNotification(Notification notification) {
        notification.setDate(new Date());
        notification.setIsRead(false);
        return notificationRepository.save(notification);
    }
    
    @Override
    public Notification updateNotification(Notification notification) {
        return notificationRepository.save(notification);
    }
    
    @Override
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }
    
    @Override
    public List<Notification> getNotificationsByUserId(Integer userId) {
        return notificationRepository.findByUserId(userId);
    }
    
    @Override
    public List<Notification> getUnreadNotifications(Integer userId) {
        return notificationRepository.findByUserIdAndIsRead(userId, false);
    }
    
    @Override
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElse(null);
        if (notification != null) {
            notification.setIsRead(true);
            notificationRepository.save(notification);
        }
    }
}
