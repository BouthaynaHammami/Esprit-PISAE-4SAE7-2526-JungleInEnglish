package tn.esprit.social_interaction_service.Communications.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tn.esprit.social_interaction_service.Communications.DTO.ChatMessageDTO;
import tn.esprit.social_interaction_service.Communications.Services.ChatHistoryService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping
public class ChatController {

    private final ChatHistoryService chatHistoryService;

    // Global chat
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessageDTO sendMessage(@Payload ChatMessageDTO chatMessage) {
        chatMessage.setTimestamp(LocalDateTime.now());
        chatMessage.setTopicId(null); // Global chat
        
        // Save CHAT messages to database, but broadcast all types (including REACTION)
        if ("CHAT".equals(chatMessage.getType())) {
            chatHistoryService.saveMessage(chatMessage);
        }
        
        // Always return the message to broadcast it to all users
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessageDTO addUser(@Payload ChatMessageDTO chatMessage, 
                                   SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        chatMessage.setType("JOIN");
        chatMessage.setTimestamp(LocalDateTime.now());
        chatMessage.setTopicId(null);
        
        return chatMessage;
    }
    
    // Topic-specific chat
    @MessageMapping("/chat.sendMessage.topic.{topicId}")
    @SendTo("/topic/chat/{topicId}")
    public ChatMessageDTO sendMessageToTopic(@DestinationVariable Long topicId,
                                              @Payload ChatMessageDTO chatMessage) {
        chatMessage.setTimestamp(LocalDateTime.now());
        chatMessage.setTopicId(topicId);
        
        if ("CHAT".equals(chatMessage.getType())) {
            chatHistoryService.saveMessage(chatMessage);
        }
        
        return chatMessage;
    }

    @MessageMapping("/chat.addUser.topic.{topicId}")
    @SendTo("/topic/chat/{topicId}")
    public ChatMessageDTO addUserToTopic(@DestinationVariable Long topicId,
                                          @Payload ChatMessageDTO chatMessage,
                                          SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        chatMessage.setType("JOIN");
        chatMessage.setTimestamp(LocalDateTime.now());
        chatMessage.setTopicId(topicId);
        
        return chatMessage;
    }
    
    // REST endpoints
    @GetMapping("/api/chat/history")
    @ResponseBody
    public ResponseEntity<List<ChatMessageDTO>> getChatHistory() {
        List<ChatMessageDTO> messages = chatHistoryService.getRecentMessages(50);
        return ResponseEntity.ok(messages);
    }
    
    @GetMapping("/api/chat/history/topic/{topicId}")
    @ResponseBody
    public ResponseEntity<List<ChatMessageDTO>> getChatHistoryByTopic(@PathVariable Long topicId) {
        List<ChatMessageDTO> messages = chatHistoryService.getRecentMessagesByTopic(topicId, 50);
        return ResponseEntity.ok(messages);
    }
}
