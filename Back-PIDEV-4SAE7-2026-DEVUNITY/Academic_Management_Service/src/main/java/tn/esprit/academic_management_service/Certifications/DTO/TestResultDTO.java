package tn.esprit.academic_management_service.Certifications.DTO;

import lombok.Data;

@Data
public class TestResultDTO {
    private Boolean passed;
    private Integer score;
    private Integer earnedPoints;
    private Integer totalPoints;
    private String certificateNumber;
    private String qrCode;
    private String level;
    private String message;
}