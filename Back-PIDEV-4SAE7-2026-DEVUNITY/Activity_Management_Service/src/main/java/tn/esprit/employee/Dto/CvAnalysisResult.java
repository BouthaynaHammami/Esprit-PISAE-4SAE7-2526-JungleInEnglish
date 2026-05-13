package tn.esprit.employee.Dto;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CvAnalysisResult {
    private String cluster;
    private String decision;
    @jakarta.persistence.Column(length = 2000)
    private String raison;
    private Integer score;
    private String model;
}
