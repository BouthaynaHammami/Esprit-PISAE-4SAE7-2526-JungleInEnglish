package tn.esprit.social_interaction_service.Communications.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {
    private String sender;
    private String content;
    private String type; // JOIN, CHAT, LEAVE, REACTION
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    
    private Long topicId; // For topic-specific chats
    private String messageId; // Unique identifier for reactions
    private List<MessageReactionDTO> reactions; // List of reactions
}
