package tn.esprit.language_courses_service.Schedules.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;

    private String title;

    @Enumerated(EnumType.STRING)
    private ScheduleType type;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private Long userId;
    private Long courseId;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private ClassEntity classEntity;
}