package tn.esprit.social_interaction_service.Reporting_Analytics.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateDTO {
    private Long id;
    private Long studentId;
    private Long sessionId;
    private String certificateNumber;
    private String level;
    private Integer score;
    private Date issuedAt;
}
