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
public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;
    
    @Column(nullable = false, length = 1000)
    private String message;
    
    @Column(nullable = false)
    private String type;
    
    @Column(nullable = false)
    private Boolean isRead = false;
    
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    
    // ID de l'utilisateur qui reçoit la notification
    @Column(name = "user_id")
    private Integer userId;
    
    // Champ transient pour les infos utilisateur (non persisté en DB)
    @Transient
    private UserDTO user;
}
