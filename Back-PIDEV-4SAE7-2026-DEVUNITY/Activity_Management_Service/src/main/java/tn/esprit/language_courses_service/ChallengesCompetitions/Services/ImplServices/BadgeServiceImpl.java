package tn.esprit.language_courses_service.ChallengesCompetitions.Services.ImplServices;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.Badge;
import tn.esprit.language_courses_service.ChallengesCompetitions.Repositories.BadgeRepository;
import tn.esprit.language_courses_service.ChallengesCompetitions.Services.IServices.BadgeService;

import java.util.List;

@Service
@AllArgsConstructor
public class BadgeServiceImpl implements BadgeService {

    private final BadgeRepository badgeRepository;

    @Override
    public Badge addBadge(Badge badge) {
        return badgeRepository.save(badge);
    }

    @Override
    public Badge updateBadge(Long id, Badge badge) {
        Badge existing = badgeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Badge not found"));

        existing.setName(badge.getName());
        existing.setDescription(badge.getDescription());
        existing.setPointsRequired(badge.getPointsRequired());

        return badgeRepository.save(existing);
    }


    @Override
    public List<Badge> getAllBadges() {
        return badgeRepository.findAll();
    }

    @Override
    public Badge getBadgeById(Long id) {
        return badgeRepository.findById(id).orElseThrow(() -> new RuntimeException("Badge not found"));
    }

    @Override
    public void deleteBadge(Long id) {
        if(badgeRepository.existsById(id)){
            badgeRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Badge not found");
        }
    }
}
