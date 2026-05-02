package tn.esprit.social_interaction_service.Reporting_Analytics.Documents;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;
import java.util.Date;

@Document(indexName = "analytics-index")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalyticsDocument {

    @Id
    private String id; // Use prefixed ID e.g., COURSE_1 or CHALLENGE_1

    @Field(type = FieldType.Keyword)
    private String type; // "COURSE" or "CHALLENGE"

    @Field(type = FieldType.Date)
    private Date indexedAt;

    // --- Course Fields ---
    @Field(type = FieldType.Long)
    private Long courseId;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String courseTitle;

    @Field(type = FieldType.Text)
    private String courseDescription;

    @Field(type = FieldType.Keyword)
    private String courseLevel;

    @Field(type = FieldType.Keyword)
    private String courseType;

    @Field(type = FieldType.Float)
    private Float coursePrice;

    @Field(type = FieldType.Keyword)
    private String courseImageUrl;

    @Field(type = FieldType.Keyword)
    private String courseStatus;

    @Field(type = FieldType.Float)
    private Float courseScore;

    @Field(type = FieldType.Boolean)
    private Boolean courseIsCertified;

    @Field(type = FieldType.Integer)
    private Integer courseEnrollmentCount;

    @Field(type = FieldType.Long)
    private Long courseTimeSpentSeconds;

    // --- Challenge Fields ---
    @Field(type = FieldType.Long)
    private Long challengeId;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String challengeTitle;

    @Field(type = FieldType.Text)
    private String challengeDescription;

    @Field(type = FieldType.Keyword)
    private String challengeType;

    @Field(type = FieldType.Keyword)
    private String challengeLevel;

    @Field(type = FieldType.Date)
    private LocalDate challengeStartDate;

    @Field(type = FieldType.Date)
    private LocalDate challengeEndDate;

    @Field(type = FieldType.Integer)
    private Integer challengeParticipationCount;

    @Field(type = FieldType.Integer)
    private Integer challengeCompletionCount;

    @Field(type = FieldType.Long)
    private Long challengeAverageCompletionTimeSeconds;

    @Field(type = FieldType.Float)
    private Float challengePopularityScore;
}
