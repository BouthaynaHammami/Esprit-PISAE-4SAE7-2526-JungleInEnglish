package tn.esprit.social_interaction_service.Communications.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.social_interaction_service.Communications.Entities.Message;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderId(Integer senderId);
    List<Message> findByReceiverId(Integer receiverId);
    List<Message> findBySenderIdOrReceiverId(Integer senderId, Integer receiverId);
}
