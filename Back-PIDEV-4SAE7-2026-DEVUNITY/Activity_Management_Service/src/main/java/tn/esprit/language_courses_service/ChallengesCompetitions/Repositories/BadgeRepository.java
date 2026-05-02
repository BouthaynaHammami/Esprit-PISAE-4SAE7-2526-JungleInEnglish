package tn.esprit.language_courses_service.ChallengesCompetitions.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.Badge;

import java.util.List;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long> {
    List<Badge> findByPointsRequiredLessThanEqualOrderByPointsRequiredAsc(int score);
}
