package tn.esprit.language_courses_service.Schedules.Services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.language_courses_service.Clients.CourseClient;
import tn.esprit.language_courses_service.Clients.UserClient;
import tn.esprit.language_courses_service.DTO.CourseDTO;
import tn.esprit.language_courses_service.DTO.RoleDTO;
import tn.esprit.language_courses_service.DTO.UserDTO;
import tn.esprit.language_courses_service.Schedules.Entities.ClassEntity;
import tn.esprit.language_courses_service.Schedules.Entities.Room;
import tn.esprit.language_courses_service.Schedules.Entities.Schedule;
import tn.esprit.language_courses_service.Schedules.Entities.ScheduleType;
import tn.esprit.language_courses_service.Schedules.Repositories.ClassRepository;
import tn.esprit.language_courses_service.Schedules.Repositories.RoomRepository;
import tn.esprit.language_courses_service.Schedules.Repositories.ScheduleRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseSchedulingServiceTest {

    @Mock
    private ScheduleRepository scheduleRepository;
    @Mock
    private ClassRepository classRepository;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private UserClient userClient;
    @Mock
    private CourseClient courseClient;

    @InjectMocks
    private CourseSchedulingService courseSchedulingService;

    @Test
    void scheduleCourse_shouldCreateScheduleWhenValidAndNoConflicts() {
        Long classId = 1L;
        Long tutorId = 20L;
        Long courseId = 5L;
        Long roomId = 3L;
        LocalDateTime start = LocalDateTime.of(2026, 4, 14, 9, 0);

        when(userClient.getUserById(tutorId)).thenReturn(UserDTO.builder().userId(tutorId).role(RoleDTO.TUTOR).build());
        when(courseClient.getCourseById(courseId)).thenReturn(CourseDTO.builder().courseId(courseId).title("Business Basics").build());
        when(scheduleRepository.existsByUserIdAndStartTimeLessThanAndEndTimeGreaterThan(tutorId, start.plusHours(3), start)).thenReturn(false);
        when(scheduleRepository.existsByClassEntityClassIdAndStartTimeLessThanAndEndTimeGreaterThan(classId, start.plusHours(3), start)).thenReturn(false);
        when(scheduleRepository.existsByRoomRoomIdAndStartTimeLessThanAndEndTimeGreaterThan(roomId, start.plusHours(3), start)).thenReturn(false);
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(Room.builder().roomId(roomId).name("R-1").build()));
        when(classRepository.findById(classId)).thenReturn(Optional.of(ClassEntity.builder().classId(classId).name("C1").build()));
        when(scheduleRepository.save(any(Schedule.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Schedule result = courseSchedulingService.scheduleCourse(classId, tutorId, courseId, roomId, start);

        assertEquals("Course Study: Business Basics", result.getTitle());
        assertEquals(ScheduleType.COURSE, result.getType());
        assertEquals(start.plusHours(3), result.getEndTime());
        assertEquals(tutorId, result.getUserId());
        verify(scheduleRepository).save(any(Schedule.class));
    }

    @Test
    void scheduleCourse_shouldThrowWhenRoomBusy() {
        Long classId = 1L;
        Long tutorId = 2L;
        Long courseId = 3L;
        Long roomId = 4L;
        LocalDateTime start = LocalDateTime.of(2026, 4, 14, 12, 0);

        when(userClient.getUserById(tutorId)).thenReturn(UserDTO.builder().role(RoleDTO.TUTOR).build());
        when(courseClient.getCourseById(courseId)).thenReturn(CourseDTO.builder().courseId(courseId).title("General").build());
        when(scheduleRepository.existsByUserIdAndStartTimeLessThanAndEndTimeGreaterThan(tutorId, start.plusHours(3), start)).thenReturn(false);
        when(scheduleRepository.existsByClassEntityClassIdAndStartTimeLessThanAndEndTimeGreaterThan(classId, start.plusHours(3), start)).thenReturn(false);
        when(scheduleRepository.existsByRoomRoomIdAndStartTimeLessThanAndEndTimeGreaterThan(roomId, start.plusHours(3), start)).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> courseSchedulingService.scheduleCourse(classId, tutorId, courseId, roomId, start));

        assertEquals("The selected room is already busy during this time.", ex.getMessage());
        verify(scheduleRepository, never()).save(any(Schedule.class));
    }
}
