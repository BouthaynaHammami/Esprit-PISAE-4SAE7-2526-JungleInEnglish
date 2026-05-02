package tn.esprit.community_engagement_service.Events.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Daily analysis summary for the Community Engagement Kanban board.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyAnalysisDTO {

    private String date;
    private String module;

    private long totalTasks;
    private long todoCount;
    private long doingCount;
    private long doneCount;
    private long overdueCount;

    private List<KanbanTaskDTO> tasksDueToday;
    private List<KanbanTaskDTO> overdueTasks;
}
