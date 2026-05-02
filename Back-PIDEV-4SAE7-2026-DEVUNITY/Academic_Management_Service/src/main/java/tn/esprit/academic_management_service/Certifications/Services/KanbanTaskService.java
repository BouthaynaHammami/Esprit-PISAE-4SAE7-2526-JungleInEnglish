package tn.esprit.academic_management_service.Certifications.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.academic_management_service.Certifications.DTO.DailyAnalysisDTO;
import tn.esprit.academic_management_service.Certifications.DTO.KanbanMoveDTO;
import tn.esprit.academic_management_service.Certifications.DTO.KanbanTaskDTO;
import tn.esprit.academic_management_service.Certifications.Entities.KanbanStatus;
import tn.esprit.academic_management_service.Certifications.Entities.KanbanTask;
import tn.esprit.academic_management_service.Certifications.Repositories.KanbanTaskRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class KanbanTaskService {

    private final KanbanTaskRepository kanbanTaskRepository;

    // =====================================================
    // CRUD
    // =====================================================

    public KanbanTask createTask(KanbanTaskDTO dto) {
        KanbanTask task = KanbanTask.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .status(dto.getStatus() != null
                        ? KanbanStatus.valueOf(dto.getStatus().toUpperCase())
                        : KanbanStatus.TODO)
                .userId(dto.getUserId())
                .deadline(dto.getDeadline())
                .position(dto.getPosition() != null ? dto.getPosition() : 0)
                .reminderSent(false)
                .build();
        return kanbanTaskRepository.save(task);
    }

    public KanbanTask updateTask(Long id, KanbanTaskDTO dto) {
        KanbanTask task = kanbanTaskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found: " + id));

        if (dto.getTitle() != null) task.setTitle(dto.getTitle());
        if (dto.getDescription() != null) task.setDescription(dto.getDescription());
        if (dto.getStatus() != null) task.setStatus(KanbanStatus.valueOf(dto.getStatus().toUpperCase()));
        if (dto.getDeadline() != null) task.setDeadline(dto.getDeadline());
        if (dto.getPosition() != null) task.setPosition(dto.getPosition());

        return kanbanTaskRepository.save(task);
    }

    public void deleteTask(Long id) {
        if (!kanbanTaskRepository.existsById(id)) {
            throw new RuntimeException("Task not found: " + id);
        }
        kanbanTaskRepository.deleteById(id);
    }

    public KanbanTask getTask(Long id) {
        return kanbanTaskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found: " + id));
    }

    // =====================================================
    // KANBAN BOARD (all columns for a user)
    // =====================================================

    public List<KanbanTask> getBoard(Long userId) {
        return kanbanTaskRepository.findByUserIdOrderByPositionAsc(userId);
    }

    public List<KanbanTask> getColumn(Long userId, String status) {
        KanbanStatus ks = KanbanStatus.valueOf(status.toUpperCase());
        return kanbanTaskRepository.findByUserIdAndStatusOrderByPositionAsc(userId, ks);
    }

    // =====================================================
    // DRAG & DROP — move task between columns
    // =====================================================

    public KanbanTask moveTask(Long taskId, KanbanMoveDTO moveDTO) {
        KanbanTask task = kanbanTaskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found: " + taskId));

        task.setStatus(KanbanStatus.valueOf(moveDTO.getNewStatus().toUpperCase()));
        if (moveDTO.getNewPosition() != null) {
            task.setPosition(moveDTO.getNewPosition());
        }

        return kanbanTaskRepository.save(task);
    }

    // =====================================================
    // DAILY ANALYSIS DASHBOARD
    // =====================================================

    public DailyAnalysisDTO getDailyAnalysis(Long userId) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        LocalDateTime now = LocalDateTime.now();

        long todoCount = kanbanTaskRepository.countByUserIdAndStatus(userId, KanbanStatus.TODO);
        long doingCount = kanbanTaskRepository.countByUserIdAndStatus(userId, KanbanStatus.DOING);
        long doneCount = kanbanTaskRepository.countByUserIdAndStatus(userId, KanbanStatus.DONE);

        List<KanbanTask> dueToday = kanbanTaskRepository.findTasksDueToday(userId, startOfDay, endOfDay);
        List<KanbanTask> overdue = kanbanTaskRepository.findOverdueTasks(userId, now, KanbanStatus.DONE);

        return DailyAnalysisDTO.builder()
                .date(today.toString())
                .module("Certifications")
                .totalTasks(todoCount + doingCount + doneCount)
                .todoCount(todoCount)
                .doingCount(doingCount)
                .doneCount(doneCount)
                .overdueCount(overdue.size())
                .tasksDueToday(dueToday.stream().map(this::toDTO).toList())
                .overdueTasks(overdue.stream().map(this::toDTO).toList())
                .build();
    }

    // =====================================================
    // MAPPER
    // =====================================================

    private KanbanTaskDTO toDTO(KanbanTask t) {
        return KanbanTaskDTO.builder()
                .id(t.getId())
                .title(t.getTitle())
                .description(t.getDescription())
                .status(t.getStatus().name())
                .userId(t.getUserId())
                .deadline(t.getDeadline())
                .position(t.getPosition())
                .createdAt(t.getCreatedAt())
                .updatedAt(t.getUpdatedAt())
                .build();
    }
}
