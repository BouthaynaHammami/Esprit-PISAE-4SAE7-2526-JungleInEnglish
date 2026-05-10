package tn.esprit.community_engagement_service.Events.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
