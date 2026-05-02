package tn.esprit.language_courses_service.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChallengeDTO implements Serializable {
    private Long id;
    private String title;
    private String description;
    private String type;
    private String level;
    private LocalDate startDate;
    private LocalDate endDate;
}
