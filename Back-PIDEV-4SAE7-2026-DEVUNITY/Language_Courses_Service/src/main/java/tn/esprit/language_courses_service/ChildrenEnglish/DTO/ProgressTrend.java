package tn.esprit.language_courses_service.ChildrenEnglish.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgressTrend {
    private String date;
    private Integer activitiesCompleted;
    private Double averageScore;
    private Integer xpEarned;
}
