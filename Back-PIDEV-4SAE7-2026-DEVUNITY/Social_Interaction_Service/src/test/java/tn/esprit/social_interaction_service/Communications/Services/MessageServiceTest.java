package tn.esprit.social_interaction_service.Communications.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.social_interaction_service.Communications.Entities.Message;
import tn.esprit.social_interaction_service.Communications.Repositories.MessageRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageService messageService;

    private Message testMessage;

    @BeforeEach
    void setUp() {
        testMessage = new Message();
        testMessage.setMessageId(1L);
        testMessage.setSenderId(10);
        testMessage.setReceiverId(20);
        testMessage.setContent("Hello World");
        testMessage.setIsRead(false);
    }

    @Test
    void getAllMessages_shouldReturnAllMessages() {
        when(messageRepository.findAll()).thenReturn(List.of(testMessage));

        List<Message> result = messageService.getAllMessages();

        assertEquals(1, result.size());
        verify(messageRepository).findAll();
    }

    @Test
    void getMessageById_shouldReturnMessageWhenExists() {
        when(messageRepository.findById(1L)).thenReturn(Optional.of(testMessage));

        Message result = messageService.getMessageById(1L);

        assertNotNull(result);
        assertEquals("Hello World", result.getContent());
    }

    @Test
    void getMessageById_shouldReturnNullWhenNotFound() {
        when(messageRepository.findById(999L)).thenReturn(Optional.empty());

        Message result = messageService.getMessageById(999L);

        assertNull(result);
    }

    @Test
    void addMessage_shouldSetSentDateAndIsReadFalse() {
        when(messageRepository.save(any(Message.class))).thenReturn(testMessage);

        Message result = messageService.addMessage(testMessage);

        ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);
        verify(messageRepository).save(captor.capture());
        Message saved = captor.getValue();
        assertNotNull(saved.getSentAt());
        assertFalse(saved.getIsRead());
    }

    @Test
    void updateMessage_shouldUpdateAndSave() {
        when(messageRepository.save(testMessage)).thenReturn(testMessage);

        Message result = messageService.updateMessage(testMessage);

        assertNotNull(result);
        verify(messageRepository).save(testMessage);
    }

    @Test
    void deleteMessage_shouldCallRepositoryDelete() {
        messageService.deleteMessage(1L);

        verify(messageRepository).deleteById(1L);
    }

    @Test
    void getMessagesBySenderId_shouldReturnMessagesFromSender() {
        when(messageRepository.findBySenderId(10)).thenReturn(List.of(testMessage));

        List<Message> result = messageService.getMessagesBySenderId(10);

        assertEquals(1, result.size());
        verify(messageRepository).findBySenderId(10);
    }

    @Test
    void getMessagesByReceiverId_shouldReturnMessagesToReceiver() {
        when(messageRepository.findByReceiverId(20)).thenReturn(List.of(testMessage));

        List<Message> result = messageService.getMessagesByReceiverId(20);

        assertEquals(1, result.size());
        verify(messageRepository).findByReceiverId(20);
    }

    @Test
    void getConversation_shouldReturnBiDirectionalMessages() {
        Message msg2 = new Message();
        msg2.setSenderId(20);
        msg2.setReceiverId(10);
        msg2.setContent("Reply");

        when(messageRepository.findAll()).thenReturn(List.of(testMessage, msg2));

        List<Message> result = messageService.getConversation(10, 20);

        assertEquals(2, result.size());
    }
}
