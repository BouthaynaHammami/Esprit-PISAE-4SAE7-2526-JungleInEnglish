package tn.esprit.language_courses_service.ChildrenEnglish.Serivces;

import tn.esprit.language_courses_service.ChildrenEnglish.Entities.Reward;
import tn.esprit.language_courses_service.ChildrenEnglish.Entities.TypeReward;

import java.util.List;

public interface IRewardService {
    Reward addReward(Reward reward);
    Reward updateReward(Reward reward);
    void deleteReward(Long id);
    Reward getReward(Long id);
    List<Reward> getAllRewards();
    List<Reward> getRewardsByType(TypeReward type);
    List<Reward> getAvailableRewards(Integer points);
}
