package tn.esprit.language_courses_service.ChildrenEnglish.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChildPerformance {
    private Long childId;
    private String childName;
    private Integer currentLevel;
    private Integer totalXp;
    private Double averageScore;
    private Integer activitiesCompleted;
    private Integer badgesEarned;
}
