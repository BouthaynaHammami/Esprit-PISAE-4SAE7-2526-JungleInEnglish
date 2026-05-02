package tn.esprit.language_courses_service.Schedules.Entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "room_complaint")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RoomScheduleComplaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long complaintId;

    private String subject;
    private String description;
    private boolean status;
    private String answer;
    private String complainantEmail;
    private String complainantRole;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;
}