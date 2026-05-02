package tn.esprit.language_courses_service.ChildrenEnglish.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.language_courses_service.ChildrenEnglish.Entities.Reward;
import tn.esprit.language_courses_service.ChildrenEnglish.Entities.TypeReward;

import java.util.List;

@Repository
public interface RewardRepository extends JpaRepository<Reward, Long> {
    List<Reward> findByType(TypeReward type);
    List<Reward> findByPointsRequiredLessThanEqual(Integer points);
}
