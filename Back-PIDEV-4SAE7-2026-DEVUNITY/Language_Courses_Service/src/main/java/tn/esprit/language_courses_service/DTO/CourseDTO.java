package tn.esprit.language_courses_service.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDTO {

    private Long courseId;
    private String title;
    private String description;
    private String level;
    private Float price;
    private String type;
}
