package tn.esprit.social_interaction_service.Communications.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.social_interaction_service.Communications.Entities.ChatMessage;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    
    // Get last N messages ordered by timestamp (global chat)
    List<ChatMessage> findTop50ByTopicIdIsNullOrderByTimestampDesc();
    
    // Get last N messages for a specific topic
    List<ChatMessage> findTop50ByTopicIdOrderByTimestampDesc(Long topicId);
    
    // Get all messages ordered by timestamp (global chat)
    List<ChatMessage> findAllByTopicIdIsNullOrderByTimestampAsc();
    
    // Get all messages for a specific topic
    List<ChatMessage> findAllByTopicIdOrderByTimestampAsc(Long topicId);
}
