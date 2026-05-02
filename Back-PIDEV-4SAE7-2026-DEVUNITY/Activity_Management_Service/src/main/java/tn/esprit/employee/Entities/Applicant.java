package tn.esprit.employee.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Applicant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date date;
    private String reponse;
    private Long userId;
    private String cv;
    
    @Transient
    private String firstName;
    
    @Transient
    private String lastName;

    @Enumerated(EnumType.STRING)
    private ApplicantStatus status;

    @ManyToOne
    @JoinColumn(name = "recruitment_id")
    @JsonIgnoreProperties({"applicants", "interviews"})
    private Recruitment recruitment;

    @ManyToOne
    @JoinColumn(name = "interview_id")
    @JsonIgnoreProperties("applicants")
    private Interview interview;
}
