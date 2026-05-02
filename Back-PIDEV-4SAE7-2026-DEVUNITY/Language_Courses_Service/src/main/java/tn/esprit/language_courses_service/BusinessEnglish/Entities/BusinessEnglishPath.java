package tn.esprit.language_courses_service.BusinessEnglish.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessEnglishPath {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate creationDate;
    private LocalDate expectedEndDate;
    private float overallScore;
    private LocalDate lastUpdateDate;

    @Enumerated(EnumType.STRING)
    private LearningStatus status;

    @ManyToOne
    @JsonIgnore
    private Offer offer;
}
