package tn.esprit.academic_management_service.Certifications.DTO;

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
