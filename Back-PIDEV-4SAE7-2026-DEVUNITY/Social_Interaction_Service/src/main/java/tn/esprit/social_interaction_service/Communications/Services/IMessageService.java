package tn.esprit.social_interaction_service.Communications.Services;

import tn.esprit.social_interaction_service.Communications.Entities.Message;

import java.util.List;

public interface IMessageService {
    List<Message> getAllMessages();
    Message getMessageById(Long id);
    Message addMessage(Message message);
    Message updateMessage(Message message);
    void deleteMessage(Long id);
    List<Message> getMessagesBySenderId(Integer senderId);
    List<Message> getMessagesByReceiverId(Integer receiverId);
    List<Message> getConversation(Integer userId1, Integer userId2);
}
