package tn.esprit.social_interaction_service.Reporting_Analytics.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {
    private Long eventId;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private String location;
    private int capacity;
    private String status;
}
