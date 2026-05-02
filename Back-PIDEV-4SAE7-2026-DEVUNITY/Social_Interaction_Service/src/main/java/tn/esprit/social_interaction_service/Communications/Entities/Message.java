package tn.esprit.social_interaction_service.Communications.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.esprit.social_interaction_service.Communications.DTO.UserDTO;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;
    
    @Column(nullable = false, length = 2000)
    private String content;
    
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date sentAt;
    
    @Column(nullable = false)
    private Boolean isRead = false;
    
    // ID de l'expéditeur
    @Column(name = "sender_id")
    private Integer senderId;
    
    // ID du destinataire
    @Column(name = "receiver_id")
    private Integer receiverId;
    
    // Champs transients pour les infos utilisateur (non persistés en DB)
    @Transient
    private UserDTO sender;
    
    @Transient
    private UserDTO receiver;
}
