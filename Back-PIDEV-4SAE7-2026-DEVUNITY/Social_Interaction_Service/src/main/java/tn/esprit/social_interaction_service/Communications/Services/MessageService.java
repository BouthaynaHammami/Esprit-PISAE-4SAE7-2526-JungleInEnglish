package tn.esprit.social_interaction_service.Communications.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.social_interaction_service.Communications.Entities.Message;
import tn.esprit.social_interaction_service.Communications.Repositories.MessageRepository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService implements IMessageService {
    
    private final MessageRepository messageRepository;
    
    @Override
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }
    
    @Override
    public Message getMessageById(Long id) {
        return messageRepository.findById(id).orElse(null);
    }
    
    @Override
    public Message addMessage(Message message) {
        message.setSentAt(new Date());
        message.setIsRead(false);
        return messageRepository.save(message);
    }
    
    @Override
    public Message updateMessage(Message message) {
        return messageRepository.save(message);
    }
    
    @Override
    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }
    
    @Override
    public List<Message> getMessagesBySenderId(Integer senderId) {
        return messageRepository.findBySenderId(senderId);
    }
    
    @Override
    public List<Message> getMessagesByReceiverId(Integer receiverId) {
        return messageRepository.findByReceiverId(receiverId);
    }
    
    @Override
    public List<Message> getConversation(Integer userId1, Integer userId2) {
        List<Message> allMessages = messageRepository.findAll();
        return allMessages.stream()
            .filter(m -> (m.getSenderId().equals(userId1) && m.getReceiverId().equals(userId2)) ||
                        (m.getSenderId().equals(userId2) && m.getReceiverId().equals(userId1)))
            .collect(Collectors.toList());
    }
}
