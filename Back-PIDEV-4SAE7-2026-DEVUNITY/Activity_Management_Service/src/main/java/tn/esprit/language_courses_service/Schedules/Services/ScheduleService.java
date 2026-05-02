package tn.esprit.language_courses_service.Schedules.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.language_courses_service.Clients.UserClient;
import tn.esprit.language_courses_service.Schedules.Entities.ClassEntity;
import tn.esprit.language_courses_service.Schedules.Entities.Room;
import tn.esprit.language_courses_service.Schedules.Entities.Schedule;
import tn.esprit.language_courses_service.Schedules.Entities.ScheduleType;
import tn.esprit.language_courses_service.Schedules.Repositories.ClassRepository;
import tn.esprit.language_courses_service.Schedules.Repositories.RoomRepository;
import tn.esprit.language_courses_service.Schedules.Repositories.ScheduleRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final RoomRepository roomRepository;
    private final ClassRepository classRepository;
    private final UserClient userClient;

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public Schedule getScheduleById(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found with id: " + id));
    }

    public Schedule createSchedule(Schedule schedule, Long roomId, Long classId) {
        userClient.getUserById(schedule.getUserId());

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + roomId));
        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found with id: " + classId));

        schedule.setRoom(room);
        schedule.setClassEntity(classEntity);
        return scheduleRepository.save(schedule);
    }

    public Schedule updateSchedule(Long id, Schedule schedule) {
        Schedule existing = getScheduleById(id);
        existing.setTitle(schedule.getTitle());
        existing.setType(schedule.getType());
        existing.setStartTime(schedule.getStartTime());
        existing.setEndTime(schedule.getEndTime());
        existing.setUserId(schedule.getUserId());
        return scheduleRepository.save(existing);
    }

    public void deleteSchedule(Long id) {
        scheduleRepository.deleteById(id);
    }

    public List<Schedule> getSchedulesByProfessor(Long userId) {
        return scheduleRepository.findByUserId(userId);
    }

    public List<Schedule> getSchedulesByRoom(Long roomId) {
        return scheduleRepository.findByRoomRoomId(roomId);
    }

    public List<Schedule> getSchedulesByClass(Long classId) {
        return scheduleRepository.findByClassEntityClassId(classId);
    }

    public List<Schedule> getSchedulesByType(ScheduleType type) {
        return scheduleRepository.findByType(type);
    }

    public List<Schedule> getSchedulesBetween(LocalDateTime start, LocalDateTime end) {
        return scheduleRepository.findByStartTimeBetween(start, end);
    }
}
