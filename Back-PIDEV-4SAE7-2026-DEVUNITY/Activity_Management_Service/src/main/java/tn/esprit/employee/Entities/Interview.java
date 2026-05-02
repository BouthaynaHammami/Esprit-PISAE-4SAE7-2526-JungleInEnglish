package tn.esprit.employee.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Interview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private Date startDateTime;
    private Integer durationMinutes;
    private String meetingLink;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private Status meetingStatus;

    @ManyToOne
    @JoinColumn(name = "recruitment_id")
    @JsonIgnoreProperties({"interviews", "applicants"})
    private Recruitment recruitment;

    @OneToMany(mappedBy = "interview", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("interview")
    private List<Applicant> applicants;
}
