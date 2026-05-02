package tn.esprit.language_courses_service.Schedules.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import tn.esprit.language_courses_service.Schedules.Entities.Schedule;
import tn.esprit.language_courses_service.Schedules.Entities.ScheduleType;
import tn.esprit.language_courses_service.Schedules.Services.ScheduleService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping("/all")
    public List<Schedule> getAllSchedules() {
        return scheduleService.getAllSchedules();
    }

    @GetMapping("/{id}")
    public Schedule getScheduleById(@PathVariable Long id) {
        return scheduleService.getScheduleById(id);
    }

    @PostMapping("/add/room/{roomId}/class/{classId}")
    public Schedule createSchedule(@RequestBody Schedule schedule,
                                   @PathVariable Long roomId,
                                   @PathVariable Long classId) {
        return scheduleService.createSchedule(schedule, roomId, classId);
    }

    @PutMapping("/update/{id}")
    public Schedule updateSchedule(@PathVariable Long id,
                                   @RequestBody Schedule schedule) {
        return scheduleService.updateSchedule(id, schedule);
    }

    @DeleteMapping("/delete/{id}")
    public Boolean deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return true;
    }

    @GetMapping("/professor/{userId}")
    public List<Schedule> getSchedulesByProfessor(@PathVariable Long userId) {
        return scheduleService.getSchedulesByProfessor(userId);
    }

    @GetMapping("/room/{roomId}")
    public List<Schedule> getSchedulesByRoom(@PathVariable Long roomId) {
        return scheduleService.getSchedulesByRoom(roomId);
    }

    @GetMapping("/class/{classId}")
    public List<Schedule> getSchedulesByClass(@PathVariable Long classId) {
        return scheduleService.getSchedulesByClass(classId);
    }

    @GetMapping("/type/{type}")
    public List<Schedule> getSchedulesByType(@PathVariable ScheduleType type) {
        return scheduleService.getSchedulesByType(type);
    }

    @GetMapping("/between")
    public List<Schedule> getSchedulesBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return scheduleService.getSchedulesBetween(start, end);
    }
}