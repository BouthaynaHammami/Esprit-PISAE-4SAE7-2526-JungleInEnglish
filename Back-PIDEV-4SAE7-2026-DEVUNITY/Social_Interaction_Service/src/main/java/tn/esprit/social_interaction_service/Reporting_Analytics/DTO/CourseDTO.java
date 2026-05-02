package tn.esprit.social_interaction_service.Reporting_Analytics.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseDTO implements Serializable {
    private Long courseId;
    private String title;
    private String description;
    private String level;
    private String type;
    private Float price;
    private String imageUrl;
}
