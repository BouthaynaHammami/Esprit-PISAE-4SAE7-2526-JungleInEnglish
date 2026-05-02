package tn.esprit.language_courses_service.ChallengesCompetitions.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.Badge;
import tn.esprit.language_courses_service.ChallengesCompetitions.Services.IServices.BadgeService;

import java.util.List;

@RestController
@RequestMapping("/badges")
@RequiredArgsConstructor
public class BadgeController {
    private final BadgeService badgeService;

    @PostMapping()
    public Badge addBadge(@RequestBody Badge badge) {
        return badgeService.addBadge(badge);
    }

    @PutMapping("/{id}")
    public Badge updateBadge(@PathVariable Long id, @RequestBody Badge badge) {
        return badgeService.updateBadge(id, badge);
    }

    @GetMapping()
    public List<Badge> getAllBadges() {
        return badgeService.getAllBadges();
    }

    @GetMapping("/{id}")
    public Badge getBadgeById(@PathVariable long id) {
        return badgeService.getBadgeById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteBadge(@PathVariable long id) {
        badgeService.deleteBadge(id);
    }
}