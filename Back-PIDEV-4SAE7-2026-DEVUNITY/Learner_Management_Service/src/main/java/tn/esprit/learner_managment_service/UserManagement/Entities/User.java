package tn.esprit.learner_managment_service.UserManagement.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer userId;

    String firstName;

    String lastName;

    @Column(unique = true, nullable = false)
    String email;

    /**
     * Mot de passe - Optionnel car l'auth est déléguée à Keycloak.
     * Conservé pour compatibilité avec certains modules locaux si nécessaire.
     */
    String password;

    LocalDateTime lastLogin;

    String cv;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    Role role;

    Long classId;

    @Column(updatable = false)
    LocalDateTime createdAt;

    LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
