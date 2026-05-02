package tn.esprit.language_courses_service.Schedules.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.language_courses_service.Schedules.Entities.Schedule;
import tn.esprit.language_courses_service.Schedules.Services.CourseSchedulingService;
import tn.esprit.language_courses_service.Schedules.Services.SchedulePdfExportService;
import tn.esprit.language_courses_service.Schedules.Services.WeeklyScheduleQueryService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/schedules/course-planning")
@RequiredArgsConstructor
public class CourseSchedulingController {

    private final CourseSchedulingService courseSchedulingService;
    private final WeeklyScheduleQueryService weeklyScheduleQueryService;
    private final SchedulePdfExportService schedulePdfExportService;

    @PostMapping("/add")
    public ResponseEntity<?> scheduleCourseForClassAndTutor(
            @RequestParam Long classId,
            @RequestParam Long tutorId,
            @RequestParam Long courseId,
            @RequestParam Long roomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime) {
        
        try {
            Schedule schedule = courseSchedulingService.scheduleCourse(classId, tutorId, courseId, roomId, startTime);
            return ResponseEntity.ok(schedule);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/weekly/tutor")
    public ResponseEntity<?> getTutorWeeklySchedules(
            @RequestParam Long tutorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart) {
        try {
            List<Schedule> schedules = weeklyScheduleQueryService.getTutorSchedulesForWeek(tutorId, weekStart, null);
            return ResponseEntity.ok(schedules);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/weekly/student")
    public ResponseEntity<?> getStudentWeeklySchedules(
            @RequestParam Long studentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart) {
        try {
            List<Schedule> schedules = weeklyScheduleQueryService.getStudentSchedulesForWeek(studentId, weekStart, null);
            return ResponseEntity.ok(schedules);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/weekly-pdf/tutor")
    public ResponseEntity<?> exportTutorWeeklySchedulePdf(
            @RequestParam Long tutorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart,
            @RequestParam(required = false) List<Long> scheduleIds) {
        try {
            List<Schedule> schedules = weeklyScheduleQueryService.getTutorSchedulesForWeek(tutorId, weekStart, scheduleIds);
            byte[] pdf = schedulePdfExportService.generateWeeklySchedulePdf(
                    "Tutor ID: " + tutorId,
                    weekStart,
                    schedules
            );
            String filename = "tutor-" + tutorId + "-week-" + weekStart + ".pdf";

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/weekly-pdf/student")
    public ResponseEntity<?> exportStudentWeeklySchedulePdf(
            @RequestParam Long studentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart,
            @RequestParam(required = false) List<Long> scheduleIds) {
        try {
            List<Schedule> schedules = weeklyScheduleQueryService.getStudentSchedulesForWeek(studentId, weekStart, scheduleIds);
            byte[] pdf = schedulePdfExportService.generateWeeklySchedulePdf(
                    "Student ID: " + studentId,
                    weekStart,
                    schedules
            );
            String filename = "student-" + studentId + "-week-" + weekStart + ".pdf";

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
