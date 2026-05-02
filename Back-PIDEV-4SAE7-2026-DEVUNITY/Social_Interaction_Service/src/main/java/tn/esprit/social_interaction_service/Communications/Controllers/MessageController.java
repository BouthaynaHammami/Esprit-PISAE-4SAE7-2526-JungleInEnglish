package tn.esprit.social_interaction_service.Communications.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.social_interaction_service.Communications.Entities.Message;
import tn.esprit.social_interaction_service.Communications.Services.IMessageService;

import java.util.List;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class MessageController {
    
    private final IMessageService messageService;
    
    @GetMapping
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable Long id) {
        Message message = messageService.getMessageById(id);
        return message != null ? ResponseEntity.ok(message) : ResponseEntity.notFound().build();
    }
    
    @PostMapping("/add")
    public ResponseEntity<Message> addMessage(@RequestBody Message message) {
        return ResponseEntity.ok(messageService.addMessage(message));
    }
    
    @PutMapping("/update")
    public ResponseEntity<Message> updateMessage(@RequestBody Message message) {
        return ResponseEntity.ok(messageService.updateMessage(message));
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/sender/{senderId}")
    public ResponseEntity<List<Message>> getMessagesBySender(@PathVariable Integer senderId) {
        return ResponseEntity.ok(messageService.getMessagesBySenderId(senderId));
    }
    
    @GetMapping("/receiver/{receiverId}")
    public ResponseEntity<List<Message>> getMessagesByReceiver(@PathVariable Integer receiverId) {
        return ResponseEntity.ok(messageService.getMessagesByReceiverId(receiverId));
    }
    
    @GetMapping("/conversation/{userId1}/{userId2}")
    public ResponseEntity<List<Message>> getConversation(@PathVariable Integer userId1, @PathVariable Integer userId2) {
        return ResponseEntity.ok(messageService.getConversation(userId1, userId2));
    }
}
