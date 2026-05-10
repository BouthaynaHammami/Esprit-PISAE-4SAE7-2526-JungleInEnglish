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

    // --- Certification Fields ---
    @Field(type = FieldType.Long)
    private Long certificateId;

    @Field(type = FieldType.Keyword)
    private String certificateNumber;

    @Field(type = FieldType.Keyword)
    private String certificateLevel;

    @Field(type = FieldType.Integer)
    private Integer certificateScore;

    @Field(type = FieldType.Date)
    private Date certificateIssuedAt;

    @Field(type = FieldType.Long)
    private Long studentId;

    // --- Event Fields ---
    @Field(type = FieldType.Long)
    private Long eventId;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String eventTitle;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String eventDescription;

    @Field(type = FieldType.Date)
    private Date eventStartDate;

    @Field(type = FieldType.Date)
    private Date eventEndDate;

    @Field(type = FieldType.Keyword)
    private String eventLocation;

    @Field(type = FieldType.Integer)
    private Integer eventCapacity;

    @Field(type = FieldType.Keyword)
    private String eventStatus;

    // --- Book Fields ---
    @Field(type = FieldType.Long)
    private Long bookId;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String bookTitle;

    @Field(type = FieldType.Keyword)
    private String bookIsbn;

    @Field(type = FieldType.Keyword)
    private String bookStatus;

    @Field(type = FieldType.Double)
    private Double bookPrice;

    @Field(type = FieldType.Keyword)
    private String bookAuthor;

    @Field(type = FieldType.Keyword)
    private String bookCategory;

    // --- Club Fields ---
    @Field(type = FieldType.Long)
    private Long clubId;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String clubName;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String clubDescription;

    @Field(type = FieldType.Keyword)
    private String clubType;

    @Field(type = FieldType.Keyword)
    private String clubStatus;

    @Field(type = FieldType.Date)
    private Date clubCreationDate;
}
