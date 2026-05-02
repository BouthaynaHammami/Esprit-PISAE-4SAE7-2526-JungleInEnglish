package tn.esprit.language_courses_service.ChildrenEnglish.Serivces;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.language_courses_service.ChildrenEnglish.Entities.Reward;
import tn.esprit.language_courses_service.ChildrenEnglish.Entities.TypeReward;
import tn.esprit.language_courses_service.ChildrenEnglish.Repositories.RewardRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RewardService implements IRewardService {
    
    private final RewardRepository rewardRepository;
    
    @Override
    public Reward addReward(Reward reward) {
        return rewardRepository.save(reward);
    }
    
    @Override
    public Reward updateReward(Reward reward) {
        return rewardRepository.save(reward);
    }
    
    @Override
    public void deleteReward(Long id) {
        rewardRepository.deleteById(id);
    }
    
    @Override
    public Reward getReward(Long id) {
        return rewardRepository.findById(id).orElse(null);
    }
    
    @Override
    public List<Reward> getAllRewards() {
        return rewardRepository.findAll();
    }
    
    @Override
    public List<Reward> getRewardsByType(TypeReward type) {
        return rewardRepository.findByType(type);
    }
    
    @Override
    public List<Reward> getAvailableRewards(Integer points) {
        return rewardRepository.findByPointsRequiredLessThanEqual(points);
    }
}
