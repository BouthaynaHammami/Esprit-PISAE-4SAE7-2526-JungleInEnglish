package tn.esprit.social_interaction_service.Reporting_Analytics.DTO;

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
public class ChallengeDTO implements Serializable {
    private Long id;
    private String title;
    private String description;
    private String type;
    private String level;
    private LocalDate startDate;
    private LocalDate endDate;
}
