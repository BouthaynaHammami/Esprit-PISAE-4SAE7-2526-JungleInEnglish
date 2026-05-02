package tn.esprit.social_interaction_service.Communications.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.esprit.social_interaction_service.Communications.DTO.UserDTO;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;
    
    @Column(nullable = false, length = 2000)
    private String content;
    
    @Column(nullable = false)
    private LocalDate createdAt;
    
    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;
    
    // Stocker uniquement l'ID de l'utilisateur
    @Column(name = "user_id")
    private Integer userId;
    
    // Champ transient pour les infos utilisateur
    @Transient
    private UserDTO user;
}
