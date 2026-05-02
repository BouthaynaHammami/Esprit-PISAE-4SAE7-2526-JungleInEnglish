package tn.esprit.social_interaction_service.Communications.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.social_interaction_service.Communications.DTO.ChatMessageDTO;
import tn.esprit.social_interaction_service.Communications.Entities.ChatMessage;
import tn.esprit.social_interaction_service.Communications.Repositories.ChatMessageRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatHistoryService {
    
    private final ChatMessageRepository chatMessageRepository;
    
    public ChatMessageDTO saveMessage(ChatMessageDTO messageDTO) {
        ChatMessage entity = ChatMessage.builder()
                .sender(messageDTO.getSender())
                .content(messageDTO.getContent())
                .type(messageDTO.getType())
                .timestamp(messageDTO.getTimestamp() != null ? messageDTO.getTimestamp() : LocalDateTime.now())
                .topicId(messageDTO.getTopicId())
                .messageId(messageDTO.getMessageId())
                .build();
        
        ChatMessage saved = chatMessageRepository.save(entity);
        
        return convertToDTO(saved);
    }
    
    public List<ChatMessageDTO> getRecentMessages(int limit) {
        List<ChatMessage> messages = chatMessageRepository.findTop50ByTopicIdIsNullOrderByTimestampDesc();
        
        // Reverse to get chronological order (oldest first)
        Collections.reverse(messages);
        
        return messages.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<ChatMessageDTO> getRecentMessagesByTopic(Long topicId, int limit) {
        List<ChatMessage> messages = chatMessageRepository.findTop50ByTopicIdOrderByTimestampDesc(topicId);
        
        // Reverse to get chronological order (oldest first)
        Collections.reverse(messages);
        
        return messages.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<ChatMessageDTO> getAllMessages() {
        return chatMessageRepository.findAllByTopicIdIsNullOrderByTimestampAsc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<ChatMessageDTO> getAllMessagesByTopic(Long topicId) {
        return chatMessageRepository.findAllByTopicIdOrderByTimestampAsc(topicId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    private ChatMessageDTO convertToDTO(ChatMessage entity) {
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setSender(entity.getSender());
        dto.setContent(entity.getContent());
        dto.setType(entity.getType());
        dto.setTimestamp(entity.getTimestamp());
        dto.setTopicId(entity.getTopicId());
        dto.setMessageId(entity.getMessageId());
        return dto;
    }
}
