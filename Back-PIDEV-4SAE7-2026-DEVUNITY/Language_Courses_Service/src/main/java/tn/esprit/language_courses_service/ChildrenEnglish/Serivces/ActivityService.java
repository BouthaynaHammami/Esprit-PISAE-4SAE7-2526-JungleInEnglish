package tn.esprit.language_courses_service.ChildrenEnglish.Serivces;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.language_courses_service.ChildrenEnglish.Entities.Activity;
import tn.esprit.language_courses_service.ChildrenEnglish.Repositories.ActivityRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {
    
    private final ActivityRepository activityRepository;
    
    public List<Activity> getAllActivities() {
        return activityRepository.findAll();
    }
    
    public Activity getActivityById(Long id) {
        return activityRepository.findById(id).orElse(null);
    }
}
