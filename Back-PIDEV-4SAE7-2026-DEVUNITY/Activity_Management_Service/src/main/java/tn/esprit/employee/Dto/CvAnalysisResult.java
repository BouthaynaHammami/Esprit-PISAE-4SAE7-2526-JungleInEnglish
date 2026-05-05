package tn.esprit.employee.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CvAnalysisResult {
    private String cluster;
    private String decision;
    private String raison;
    private Integer score;
    private String model;
}
