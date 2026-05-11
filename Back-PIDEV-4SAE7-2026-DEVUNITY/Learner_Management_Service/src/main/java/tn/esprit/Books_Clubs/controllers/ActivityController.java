package tn.esprit.Books_Clubs.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.Services.IServices.IActivityService;
import tn.esprit.Books_Clubs.entities.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityController {

    private final IActivityService activityService;

    // Excursion
    @PostMapping("/excursions")
    public Excursion addExcursion(@RequestBody Excursion e) { return activityService.addExcursion(e); }

    @PutMapping("/excursions")
    public Excursion updateExcursion(@RequestBody Excursion e) { return activityService.updateExcursion(e); }

    @GetMapping("/excursions/{id}")
    public Excursion findExcursion(@PathVariable Long id) { return activityService.findExcursion(id); }

    @DeleteMapping("/excursions/{id}")
    public void deleteExcursion(@PathVariable Long id) { activityService.deleteExcursion(id); }

    @GetMapping("/excursions/club/{clubId}")
    public List<Excursion> excursionsByClub(@PathVariable Long clubId) { return activityService.excursionsByClub(clubId); }

    @PostMapping("/excursions/register")
    public ExcursionParticipation registerExcursion(@RequestParam Long memberId, @RequestParam Long excursionId) {
        return activityService.registerExcursion(memberId, excursionId);
    }

    @GetMapping("/excursions/{id}/participants")
    public List<ExcursionParticipation> excursionParticipants(@PathVariable Long id) {
        return activityService.excursionParticipants(id);
    }

    // Training
    @PostMapping("/trainings")
    public Training addTraining(@RequestBody Training t) { return activityService.addTraining(t); }

    @PutMapping("/trainings")
    public Training updateTraining(@RequestBody Training t) { return activityService.updateTraining(t); }

    @GetMapping("/trainings/{id}")
    public Training findTraining(@PathVariable Long id) { return activityService.findTraining(id); }

    @DeleteMapping("/trainings/{id}")
    public void deleteTraining(@PathVariable Long id) { activityService.deleteTraining(id); }

    @GetMapping("/trainings/club/{clubId}")
    public List<Training> trainingsByClub(@PathVariable Long clubId) { return activityService.trainingsByClub(clubId); }

    @PostMapping("/trainings/register")
    public TrainingParticipation registerTraining(@RequestParam Long memberId, @RequestParam Long trainingId) {
        return activityService.registerTraining(memberId, trainingId);
    }

    @GetMapping("/trainings/{id}/participants")
    public List<TrainingParticipation> trainingParticipants(@PathVariable Long id) {
        return activityService.trainingParticipants(id);
    }
}