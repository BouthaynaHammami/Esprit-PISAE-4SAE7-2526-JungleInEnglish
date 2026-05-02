package tn.esprit.language_courses_service.BusinessEnglish.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private float price;
    private String category;
    private float durationHours;

    @Enumerated(EnumType.STRING)
    private Level level;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "offer")
    private List<BusinessEnglishPath> businessEnglishPaths;

    @OneToMany(mappedBy = "offer")
    private List<EmployeeInvitation> employeeInvitations;

    @OneToMany(mappedBy = "offer")
    @JsonIgnore
    private List<CompanyOffer> companyOffers;

    @ElementCollection
    private List<Long> courseIds;
}
