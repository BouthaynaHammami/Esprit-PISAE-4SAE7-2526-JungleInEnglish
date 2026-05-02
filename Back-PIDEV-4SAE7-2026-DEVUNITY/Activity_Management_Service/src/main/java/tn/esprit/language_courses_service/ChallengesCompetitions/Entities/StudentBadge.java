package tn.esprit.language_courses_service.ChallengesCompetitions.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Represents a badge earned by a student
 * Links a Student to a Badge with acquisition date
 * 
 * Relations:
 * - One StudentBadge can reference one Badge (many-to-one)
 * - One Student can have many StudentBadges (one-to-many)
 * - One StudentBadge cannot be duplicated (unique constraint: student + badge)
 */
@Entity
@Table(
  name = "student_badges",
  uniqueConstraints = @UniqueConstraint(columnNames = {"idUser", "idBadge"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentBadge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Student ID (foreign key to Student entity in Learner_Management_Service)
     * We store the ID rather than relationship to avoid circular dependencies
     */
    private Long idUser;

    /**
     * Badge reference
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idBadge", nullable = false)
    private Badge badge;

    /**
     * When the badge was earned
     */
    private LocalDateTime dateAcquisition;

    /**
     * Optional: The score at time of earning (for history/audit purposes)
     */
    private Integer scoreAtAcquisition;

    /**
     * Optional: The challenge session that triggered this badge
     */
    private Long sessionIdThatTriggeredBadge;

    @PrePersist
    protected void onCreate() {
        if (dateAcquisition == null) {
            dateAcquisition = LocalDateTime.now();
        }
    }
}
