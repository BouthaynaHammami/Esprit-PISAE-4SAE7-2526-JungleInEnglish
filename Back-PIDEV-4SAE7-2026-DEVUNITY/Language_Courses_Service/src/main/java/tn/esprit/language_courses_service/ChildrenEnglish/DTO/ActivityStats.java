package tn.esprit.language_courses_service.ChildrenEnglish.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityStats {
    private Long activityId;
    private String activityTitle;
    private String activityType;
    private Integer completionCount;
    private Double averageScore;
    private Double completionRate;
}
