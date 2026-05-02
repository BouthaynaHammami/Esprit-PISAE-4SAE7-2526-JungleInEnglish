package tn.esprit.academic_management_service.Certifications.Entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class SessionQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String studentAnswer;
    private Boolean correct;

    @ManyToOne
    private TestSession session;

    @ManyToOne
    private CertificationQuestion question;
}