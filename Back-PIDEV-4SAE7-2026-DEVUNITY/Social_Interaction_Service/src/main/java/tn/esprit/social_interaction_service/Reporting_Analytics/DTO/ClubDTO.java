package tn.esprit.social_interaction_service.Reporting_Analytics.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClubDTO {
    private Long clubId;
    private String name;
    private String description;
    private String type;
    private String status;
    private Date creationDate;
}
