package tn.esprit.language_courses_service.ChallengesCompetitions.Services.IServices;


import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.Badge;

import java.util.List;

public interface BadgeService {
    public Badge addBadge(Badge badge);
    public Badge updateBadge(Long id, Badge badge);
    public List<Badge> getAllBadges();
    public Badge getBadgeById(Long id);
    public void deleteBadge(Long id);
}
