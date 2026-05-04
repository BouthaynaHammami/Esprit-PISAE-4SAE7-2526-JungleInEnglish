package tn.esprit.learner_managment_service.DropoutPrediction.Dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DropoutFormResponse {

    private Long id;
    private Integer userId;
    private String userEmail;

    @JsonProperty("motivation_level")
    private Integer motivationLevel;

    @JsonProperty("weekly_study_hours")
    private Double weeklyStudyHours;

    @JsonProperty("free_time_hours_per_week")
    private Double freeTimeHoursPerWeek;

    @JsonProperty("satisfaction_level")
    private Integer satisfactionLevel;

    @JsonProperty("preferred_learning_mode")
    private String preferredLearningMode;

    @JsonProperty("attendance_commitment")
    private Integer attendanceCommitment;

    @JsonProperty("homework_completion_self")
    private Integer homeworkCompletionSelf;

    @JsonProperty("financial_stress_level")
    private Integer financialStressLevel;

    @JsonProperty("interaction_with_teacher")
    private Integer interactionWithTeacher;

    @JsonProperty("english_level_self")
    private String englishLevelSelf;

    @JsonProperty("goal_clarity_level")
    private Integer goalClarityLevel;

    @JsonProperty("class_difficulty_level")
    private Integer classDifficultyLevel;

    @JsonProperty("peer_interaction_level")
    private Integer peerInteractionLevel;

    @JsonProperty("technical_issues_frequency")
    private Integer technicalIssuesFrequency;

    private String predictedDropout;
    private Double predictedProbability;
    private String modelName;
    private LocalDateTime createdAt;
}
