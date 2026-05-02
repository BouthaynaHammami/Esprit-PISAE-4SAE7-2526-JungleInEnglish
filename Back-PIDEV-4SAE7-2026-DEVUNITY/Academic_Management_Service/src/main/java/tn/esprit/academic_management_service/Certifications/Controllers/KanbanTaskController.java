package tn.esprit.academic_management_service.Certifications.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.academic_management_service.Certifications.DTO.DailyAnalysisDTO;
import tn.esprit.academic_management_service.Certifications.DTO.KanbanMoveDTO;
import tn.esprit.academic_management_service.Certifications.DTO.KanbanTaskDTO;
import tn.esprit.academic_management_service.Certifications.Entities.KanbanTask;
import tn.esprit.academic_management_service.Certifications.Services.KanbanTaskService;

import java.util.List;

@RestController
@RequestMapping("/certification/kanban")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class KanbanTaskController {

    private final KanbanTaskService kanbanTaskService;

    // =====================================================
    // CRUD
    // =====================================================

    @PostMapping("/tasks")
    public ResponseEntity<KanbanTask> createTask(@RequestBody KanbanTaskDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(kanbanTaskService.createTask(dto));
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity<KanbanTask> updateTask(
            @PathVariable("id") Long id,
            @RequestBody KanbanTaskDTO dto) {
        return ResponseEntity.ok(kanbanTaskService.updateTask(id, dto));
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable("id") Long id) {
        kanbanTaskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<KanbanTask> getTask(@PathVariable("id") Long id) {
        return ResponseEntity.ok(kanbanTaskService.getTask(id));
    }

    // =====================================================
    // KANBAN BOARD
    // =====================================================

    /** Get the full board (all columns) for a user */
    @GetMapping("/board/{userId}")
    public ResponseEntity<List<KanbanTask>> getBoard(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(kanbanTaskService.getBoard(userId));
    }

    /** Get a single column for a user */
    @GetMapping("/board/{userId}/column/{status}")
    public ResponseEntity<List<KanbanTask>> getColumn(
            @PathVariable("userId") Long userId,
            @PathVariable("status") String status) {
        return ResponseEntity.ok(kanbanTaskService.getColumn(userId, status));
    }

    // =====================================================
    // DRAG & DROP
    // =====================================================

    /** Move a task to a different column / position (drag & drop) */
    @PatchMapping("/tasks/{id}/move")
    public ResponseEntity<KanbanTask> moveTask(
            @PathVariable("id") Long id,
            @RequestBody KanbanMoveDTO moveDTO) {
        return ResponseEntity.ok(kanbanTaskService.moveTask(id, moveDTO));
    }

    // =====================================================
    // DAILY ANALYSIS DASHBOARD
    // =====================================================

    @GetMapping("/daily-analysis/{userId}")
    public ResponseEntity<DailyAnalysisDTO> getDailyAnalysis(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(kanbanTaskService.getDailyAnalysis(userId));
    }
}
