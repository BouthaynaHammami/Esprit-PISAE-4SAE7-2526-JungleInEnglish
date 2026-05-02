package tn.esprit.academic_management_service.Certifications.Entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CertificationQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question_text")
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctAnswer;
    private String level;
    private String category;
    private Integer points;
    private Boolean active = true;

}