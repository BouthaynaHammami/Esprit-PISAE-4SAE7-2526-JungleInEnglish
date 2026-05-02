package tn.esprit.language_courses_service.ChildrenEnglish.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.language_courses_service.ChildrenEnglish.Entities.Activity;
import tn.esprit.language_courses_service.ChildrenEnglish.Serivces.ActivityService;

import java.util.List;

@RestController
@RequestMapping("/activities")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ActivityController {
    
    private final ActivityService activityService;
    
    @GetMapping
    public List<Activity> getAllActivities() {
        return activityService.getAllActivities();
    }
    
    @GetMapping("/{id}")
    public Activity getActivityById(@PathVariable Long id) {
        return activityService.getActivityById(id);
    }
    
    @GetMapping("/child/{childId}")
    public List<Activity> getActivitiesForChild(@PathVariable Long childId) {
        // For now, return all activities - can filter by child level later
        return activityService.getAllActivities();
    }
}
