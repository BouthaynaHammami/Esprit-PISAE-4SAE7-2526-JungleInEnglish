package tn.esprit.academic_management_service.Certifications.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long studentId;
    private Long sessionId;        // ✅ juste l'ID, pas l'objet TestSession
    private String certificateNumber;
    private String level;
    private Integer score;
    private LocalDateTime issuedAt;

    @Lob
    @Column(columnDefinition = "LONGTEXT")

    private String qrCode;
    // ✅ plus de relation TestSession ici
}