package tn.esprit.social_interaction_service.Communications.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.esprit.social_interaction_service.Communications.DTO.UserDTO;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Topic {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long topicId;
    
    @Column(nullable = false)
    private String title;
    
    @Column(length = 2000)
    private String description;
    
    @Column
    private LocalDate createdAt;
    
    // Stocker uniquement l'ID de l'utilisateur
    @Column(name = "user_id")
    private Integer userId;
    
    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Comment> comments;
    
    // Champ transient pour les infos utilisateur (non persisté en DB)
    @Transient
    private UserDTO user;
}
