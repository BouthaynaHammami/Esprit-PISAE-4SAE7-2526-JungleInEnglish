package tn.esprit.academic_management_service.Certifications.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class TestSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long studentId;

    private Integer score;

    private Boolean passed;

    private LocalDateTime takenAt;

    // 🔥 NOUVEAU — expiration du test (timer backend sécurisé)
    private LocalDateTime expiresAt;

    // 🔥 NOUVEAU — durée réelle du test en secondes
    private Integer durationSeconds;

    // 🔥 NOUVEAU — numéro de tentative
    private Integer attemptNumber;

    // 🔥 NOUVEAU — détection fraude
    private Boolean suspicious = false;

    private String suspiciousReason;
    private Integer tabSwitchCount = 0;
}