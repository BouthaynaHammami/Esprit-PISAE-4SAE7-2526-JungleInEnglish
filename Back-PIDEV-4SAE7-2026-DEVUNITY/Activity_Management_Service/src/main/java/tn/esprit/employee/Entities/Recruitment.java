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
public class Recruitment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String positionTitle;
    private String department;

    @Enumerated(EnumType.STRING)
    private RecruitmentStatus status;

    private Date openedAt;

    @OneToMany(mappedBy = "recruitment", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("recruitment")
    private List<Interview> interviews;

    @OneToMany(mappedBy = "recruitment", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("recruitment")
    private List<Applicant> applicants;
}
