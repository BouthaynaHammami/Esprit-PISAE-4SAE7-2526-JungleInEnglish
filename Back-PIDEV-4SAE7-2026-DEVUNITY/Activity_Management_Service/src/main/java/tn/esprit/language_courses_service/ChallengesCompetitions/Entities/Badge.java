package tn.esprit.language_courses_service.ChallengesCompetitions.Entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class Badge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBadge;
    private String name;
    private String description;
    private String imageUrl;
    private int pointsRequired;
}
