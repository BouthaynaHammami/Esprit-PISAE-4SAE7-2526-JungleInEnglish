package tn.esprit.employee.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.employee.Entities.Notification;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    Page<Notification> findByRecipientEmailInOrderByCreatedAtDesc(List<String> emails, Pageable pageable);
    
    long countByRecipientEmailInAndIsReadFalse(List<String> emails);

    Page<Notification> findByRecipientEmailOrderByCreatedAtDesc(String recipientEmail, Pageable pageable);
    
    Page<Notification> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    long countByRecipientEmailAndIsReadFalse(String recipientEmail);
    
    long countByUserIdAndIsReadFalse(Long userId);
}
