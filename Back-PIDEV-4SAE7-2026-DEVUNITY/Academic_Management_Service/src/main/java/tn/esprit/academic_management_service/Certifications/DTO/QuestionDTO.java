package tn.esprit.academic_management_service.Certifications.DTO;

import lombok.Data;

@Data
public class QuestionDTO {
    private Long id;
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String level;
    private String category;
    private Integer points;
}