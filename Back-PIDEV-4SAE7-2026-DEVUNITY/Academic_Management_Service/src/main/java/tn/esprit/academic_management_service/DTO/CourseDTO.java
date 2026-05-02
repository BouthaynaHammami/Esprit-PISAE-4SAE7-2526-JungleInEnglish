package tn.esprit.academic_management_service.DTO;

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
    private float price;
    private String imageUrl;
}
