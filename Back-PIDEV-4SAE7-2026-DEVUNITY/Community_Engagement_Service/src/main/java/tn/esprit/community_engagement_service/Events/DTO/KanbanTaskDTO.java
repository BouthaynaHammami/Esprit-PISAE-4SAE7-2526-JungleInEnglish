package tn.esprit.community_engagement_service.Events.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KanbanTaskDTO {

    private Long id;
    private String title;
    private String description;
    private String status;       // TODO, DOING, DONE
    private Long userId;
    private LocalDateTime deadline;
    private Integer position;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
