package tn.esprit.community_engagement_service.Events.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventAvailabilityDTO {

    private Long eventId;
    private String eventTitle;

    //  Places
    private int  totalCapacity;
    private long pendingCount;
    private long confirmedCount;
    private long waitlistedCount;
    private long availableSpots;

    // ─── Indicateurs ─────────────────────────────────────────────────────────
    private boolean available;
    private boolean hasWaitlist;

    private String eventStatus;
}