package tn.esprit.community_engagement_service.Events.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.community_engagement_service.Events.Entities.KanbanStatus;
import tn.esprit.community_engagement_service.Events.Entities.KanbanTask;

import java.time.LocalDateTime;
import java.util.List;

public interface KanbanTaskRepository extends JpaRepository<KanbanTask, Long> {

    List<KanbanTask> findByUserIdOrderByPositionAsc(Long userId);

    List<KanbanTask> findByUserIdAndStatusOrderByPositionAsc(Long userId, KanbanStatus status);

    /** Tasks due today for a specific user */
    @Query("SELECT t FROM KanbanTask t WHERE t.userId = :userId " +
           "AND t.deadline >= :startOfDay AND t.deadline < :endOfDay " +
           "ORDER BY t.deadline ASC")
    List<KanbanTask> findTasksDueToday(@Param("userId") Long userId,
                                       @Param("startOfDay") LocalDateTime startOfDay,
                                       @Param("endOfDay") LocalDateTime endOfDay);

    /** Tasks whose deadline is between now and now+15min and reminder not yet sent */
    @Query("SELECT t FROM KanbanTask t WHERE t.deadline BETWEEN :now AND :limit " +
           "AND t.reminderSent = false AND t.status <> :doneStatus")
    List<KanbanTask> findTasksNeedingReminder(@Param("now") LocalDateTime now,
                                               @Param("limit") LocalDateTime limit,
                                               @Param("doneStatus") KanbanStatus doneStatus);

    /** Count tasks by status for a user (for daily analysis) */
    long countByUserIdAndStatus(Long userId, KanbanStatus status);

    /** Overdue tasks: deadline passed but not DONE */
    @Query("SELECT t FROM KanbanTask t WHERE t.userId = :userId " +
           "AND t.deadline < :now AND t.status <> :doneStatus " +
           "ORDER BY t.deadline ASC")
    List<KanbanTask> findOverdueTasks(@Param("userId") Long userId,
                                      @Param("now") LocalDateTime now,
                                      @Param("doneStatus") KanbanStatus doneStatus);
}
