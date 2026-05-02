package tn.esprit.language_courses_service.Schedules.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.language_courses_service.Clients.UserClient;
import tn.esprit.language_courses_service.DTO.UserDTO;
import tn.esprit.language_courses_service.Schedules.Entities.Schedule;
import tn.esprit.language_courses_service.Schedules.Repositories.ScheduleRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeeklyScheduleQueryService {

    private final ScheduleRepository scheduleRepository;
    private final UserClient userClient;

    public List<Schedule> getTutorSchedulesForWeek(Long tutorId, LocalDate weekStart, List<Long> selectedScheduleIds) {
        LocalDateTime start = weekStart.atStartOfDay();
        LocalDateTime end = weekStart.plusDays(6).atTime(LocalTime.MAX);

        List<Schedule> weekSchedules = scheduleRepository
                .findByUserIdAndStartTimeBetweenOrderByStartTimeAsc(tutorId, start, end);

        return filterSchedules(weekSchedules, selectedScheduleIds);
    }

    public List<Schedule> getStudentSchedulesForWeek(Long studentId, LocalDate weekStart, List<Long> selectedScheduleIds) {
        UserDTO student = userClient.getUserById(studentId);
        if (student == null) {
            throw new RuntimeException("Student not found.");
        }
        if (student.getClassId() == null) {
            throw new RuntimeException("Selected student is not assigned to a class.");
        }

        LocalDateTime start = weekStart.atStartOfDay();
        LocalDateTime end = weekStart.plusDays(6).atTime(LocalTime.MAX);

        List<Schedule> weekSchedules = scheduleRepository
                .findByClassEntityClassIdAndStartTimeBetweenOrderByStartTimeAsc(student.getClassId(), start, end);

        return filterSchedules(weekSchedules, selectedScheduleIds);
    }

    private List<Schedule> filterSchedules(List<Schedule> schedules, List<Long> selectedScheduleIds) {
        List<Schedule> filtered = schedules;
        if (selectedScheduleIds != null && !selectedScheduleIds.isEmpty()) {
            filtered = schedules.stream()
                    .filter(s -> s.getScheduleId() != null && selectedScheduleIds.contains(s.getScheduleId()))
                    .sorted(Comparator.comparing(Schedule::getStartTime))
                    .toList();
        }

        if (filtered.isEmpty()) {
            throw new RuntimeException("No schedules found for the selected week.");
        }

        return filtered;
    }
}
