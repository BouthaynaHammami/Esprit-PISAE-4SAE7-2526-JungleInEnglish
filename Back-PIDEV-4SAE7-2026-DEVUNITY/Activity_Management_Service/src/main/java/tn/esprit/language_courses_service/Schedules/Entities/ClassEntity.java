package tn.esprit.language_courses_service.Schedules.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "class")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ClassEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long classId;

    private String name;

    private int numberStudents;
    private String level;

    @JsonIgnore
    @OneToMany(mappedBy = "classEntity", cascade = CascadeType.ALL)
    private List<Schedule> schedules;
}