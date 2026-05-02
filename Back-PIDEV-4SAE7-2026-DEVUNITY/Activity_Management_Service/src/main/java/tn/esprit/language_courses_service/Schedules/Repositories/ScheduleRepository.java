package tn.esprit.language_courses_service.Schedules.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.language_courses_service.Schedules.Entities.Schedule;
import tn.esprit.language_courses_service.Schedules.Entities.ScheduleType;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByUserId(Long userId);
    List<Schedule> findByUserIdAndStartTimeBetweenOrderByStartTimeAsc(Long userId, LocalDateTime start, LocalDateTime end);
    List<Schedule> findByClassEntityClassIdAndStartTimeBetweenOrderByStartTimeAsc(Long classId, LocalDateTime start, LocalDateTime end);

    boolean existsByUserIdAndStartTimeLessThanAndEndTimeGreaterThan(Long userId, LocalDateTime endTime, LocalDateTime startTime);
    boolean existsByClassEntityClassIdAndStartTimeLessThanAndEndTimeGreaterThan(Long classId, LocalDateTime endTime, LocalDateTime startTime);
    boolean existsByRoomRoomIdAndStartTimeLessThanAndEndTimeGreaterThan(Long roomId, LocalDateTime endTime, LocalDateTime startTime);
    List<Schedule> findByRoomRoomId(Long roomId);
    List<Schedule> findByClassEntityClassId(Long classId);
    List<Schedule> findByType(ScheduleType type);
    List<Schedule> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
}