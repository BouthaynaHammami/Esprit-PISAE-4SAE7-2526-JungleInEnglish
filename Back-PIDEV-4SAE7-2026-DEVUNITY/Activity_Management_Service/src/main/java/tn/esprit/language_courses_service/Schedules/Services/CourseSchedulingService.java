package tn.esprit.language_courses_service.Schedules.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.language_courses_service.Schedules.Entities.ClassEntity;
import tn.esprit.language_courses_service.Schedules.Entities.Schedule;
import tn.esprit.language_courses_service.Schedules.Entities.ScheduleType;
import tn.esprit.language_courses_service.Schedules.Repositories.ClassRepository;
import tn.esprit.language_courses_service.Schedules.Repositories.ScheduleRepository;
import tn.esprit.language_courses_service.Schedules.Repositories.RoomRepository;
import tn.esprit.language_courses_service.Schedules.Entities.Room;
import tn.esprit.language_courses_service.DTO.CourseDTO;
import tn.esprit.language_courses_service.DTO.RoleDTO;
import tn.esprit.language_courses_service.DTO.UserDTO;
import tn.esprit.language_courses_service.Clients.CourseClient;
import tn.esprit.language_courses_service.Clients.UserClient;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CourseSchedulingService {

    private final ScheduleRepository scheduleRepository;
    private final ClassRepository classRepository;
    private final RoomRepository roomRepository;
    private final UserClient userClient;
    private final CourseClient courseClient;

    public Schedule scheduleCourse(Long classId, Long tutorId, Long courseId, Long roomId, LocalDateTime startTime) {
        // 1. Validate Tutor using Feign
        UserDTO tutor = userClient.getUserById(tutorId);
        if (tutor == null) {
            throw new RuntimeException("Tutor not found in User Microservice.");
        }
        if (tutor.getRole() != RoleDTO.TUTOR) {
            throw new RuntimeException("The specified user is not a TUTOR.");
        }

        // 2. Validate Course using Feign
        CourseDTO course = courseClient.getCourseById(courseId);
        if (course == null) {
            throw new RuntimeException("Course not found in Course Microservice.");
        }

        LocalDateTime endTime = startTime.plusHours(3);

        // 3. Local schedule conflict checks
        boolean isTutorBusy = scheduleRepository.existsByUserIdAndStartTimeLessThanAndEndTimeGreaterThan(tutorId, endTime, startTime);
        if (isTutorBusy) {
            throw new RuntimeException("The selected tutor is already busy during this time.");
        }

        boolean isClassBusy = scheduleRepository.existsByClassEntityClassIdAndStartTimeLessThanAndEndTimeGreaterThan(classId, endTime, startTime);
        if (isClassBusy) {
            throw new RuntimeException("The selected class is already busy during this time.");
        }

        boolean isRoomBusy = scheduleRepository.existsByRoomRoomIdAndStartTimeLessThanAndEndTimeGreaterThan(roomId, endTime, startTime);
        if (isRoomBusy) {
            throw new RuntimeException("The selected room is already busy during this time.");
        }

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + roomId));

        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found with id: " + classId));

        Schedule schedule = Schedule.builder()
                .title("Course Study: " + course.getTitle())
                .type(ScheduleType.COURSE)
                .startTime(startTime)
                .endTime(endTime)
                .userId(tutorId)
                .courseId(courseId)
                .classEntity(classEntity)
                .room(room)
                .build();

        return scheduleRepository.save(schedule);
    }
}
