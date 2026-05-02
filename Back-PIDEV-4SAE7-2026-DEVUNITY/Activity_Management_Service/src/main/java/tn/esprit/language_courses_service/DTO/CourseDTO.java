package tn.esprit.language_courses_service.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseDTO {
    private Long courseId;
    private String title;
    private String description;
}
