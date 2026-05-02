package tn.esprit.language_courses_service.ChildrenEnglish.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OverviewStats {
    private Integer totalChildren;
    private Integer totalActivitiesCompleted;
    private Double averageScore;
    private Integer totalXpEarned;
    private Integer activeLearners;
}
