package tn.esprit.learner_managment_service.DropoutPrediction.Entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import tn.esprit.learner_managment_service.UserManagement.Entities.User;

@Entity
@Table(name = "dropout_forms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DropoutForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(name = "motivation_level", nullable = false)
    Integer motivationLevel;

    @Column(name = "weekly_study_hours", nullable = false)
    Double weeklyStudyHours;

    @Column(name = "free_time_hours_per_week", nullable = false)
    Double freeTimeHoursPerWeek;

    @Column(name = "satisfaction_level", nullable = false)
    Integer satisfactionLevel;

    @Column(name = "preferred_learning_mode", nullable = false)
    String preferredLearningMode;

    @Column(name = "attendance_commitment", nullable = false)
    Integer attendanceCommitment;

    @Column(name = "homework_completion_self", nullable = false)
    Integer homeworkCompletionSelf;

    @Column(name = "financial_stress_level", nullable = false)
    Integer financialStressLevel;

    @Column(name = "interaction_with_teacher", nullable = false)
    Integer interactionWithTeacher;

    @Column(name = "english_level_self", nullable = false)
    String englishLevelSelf;

    @Column(name = "goal_clarity_level", nullable = false)
    Integer goalClarityLevel;

    @Column(name = "class_difficulty_level", nullable = false)
    Integer classDifficultyLevel;

    @Column(name = "peer_interaction_level", nullable = false)
    Integer peerInteractionLevel;

    @Column(name = "technical_issues_frequency", nullable = false)
    Integer technicalIssuesFrequency;

    @Column(name = "predicted_dropout", nullable = false)
    String predictedDropout;

    @Column(name = "predicted_probability")
    Double predictedProbability;

    @Column(name = "model_name", nullable = false)
    String modelName;

    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
