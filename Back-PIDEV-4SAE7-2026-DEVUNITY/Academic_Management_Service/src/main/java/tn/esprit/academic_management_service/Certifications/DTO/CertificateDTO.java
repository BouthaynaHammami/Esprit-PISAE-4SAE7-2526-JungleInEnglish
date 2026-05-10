package tn.esprit.academic_management_service.Certifications.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificateDTO {
    private Long id;
    private Long studentId;
    private Long sessionId;
    private String certificateNumber;
    private String level;
    private Integer score;
    private Date issuedAt;
}
