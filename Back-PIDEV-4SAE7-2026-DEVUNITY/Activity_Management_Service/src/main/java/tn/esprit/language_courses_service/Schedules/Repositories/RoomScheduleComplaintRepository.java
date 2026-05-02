package tn.esprit.language_courses_service.Schedules.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.language_courses_service.Schedules.Entities.RoomScheduleComplaint;

import java.util.List;

public interface RoomScheduleComplaintRepository extends JpaRepository<RoomScheduleComplaint, Long> {
    List<RoomScheduleComplaint> findByRoomRoomId(Long roomId);
    List<RoomScheduleComplaint> findByStatus(boolean status);
    List<RoomScheduleComplaint> findByScheduleScheduleId(Long scheduleId);
}