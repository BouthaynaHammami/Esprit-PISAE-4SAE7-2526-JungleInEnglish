package tn.esprit.social_interaction_service.Communications.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "chat_messages")
public class ChatMessage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String sender;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    @Column(nullable = false)
    private String type; // JOIN, CHAT, LEAVE, REACTION
    
    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    
    // Topic ID for topic-specific chats (null = global chat)
    @Column(name = "topic_id")
    private Long topicId;
    
    // Unique message identifier for reactions
    @Column(name = "message_id", unique = true)
    private String messageId;
    
    // Store reactions as JSON string
    @Column(columnDefinition = "TEXT")
    private String reactions;
}
