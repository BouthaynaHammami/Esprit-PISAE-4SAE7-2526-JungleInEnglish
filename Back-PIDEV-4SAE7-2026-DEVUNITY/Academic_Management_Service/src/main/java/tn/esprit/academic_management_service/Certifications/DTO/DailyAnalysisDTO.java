package tn.esprit.academic_management_service.Certifications.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Daily analysis summary for the Certifications Kanban board.
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
