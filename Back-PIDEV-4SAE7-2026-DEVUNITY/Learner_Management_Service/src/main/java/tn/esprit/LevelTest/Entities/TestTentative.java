package tn.esprit.LevelTest.Entities;

import jakarta.persistence.*;
import lombok.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestTentative {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    private int score;
    private Long userId;

    @Column(columnDefinition = "TEXT")
    private String paragraph;

    @Enumerated(EnumType.STRING)
    private TestStatus status;

    @Column(columnDefinition = "TEXT")
    private String tutorFeedback;

}
