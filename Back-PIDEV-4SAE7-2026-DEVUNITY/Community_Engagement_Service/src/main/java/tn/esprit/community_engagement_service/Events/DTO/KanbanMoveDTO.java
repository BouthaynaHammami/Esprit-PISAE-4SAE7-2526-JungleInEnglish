package tn.esprit.community_engagement_service.Events.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request body for drag & drop: move a task to a new column + new position.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KanbanMoveDTO {

    private String newStatus;   // TODO, DOING, DONE
    private Integer newPosition;
}
