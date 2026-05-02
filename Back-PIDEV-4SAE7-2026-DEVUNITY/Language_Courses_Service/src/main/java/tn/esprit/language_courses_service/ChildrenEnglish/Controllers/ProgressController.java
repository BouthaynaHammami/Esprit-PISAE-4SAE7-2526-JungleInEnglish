package tn.esprit.language_courses_service.ChildrenEnglish.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.language_courses_service.ChildrenEnglish.Entities.Progress;
import tn.esprit.language_courses_service.ChildrenEnglish.Serivces.IProgressService;

import java.util.List;

@RestController
@RequestMapping("/progress")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ProgressController {
    
    private final IProgressService progressService;
    
    @GetMapping
    public ResponseEntity<List<Progress>> getAllProgress() {
        return ResponseEntity.ok(progressService.getAllProgress());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Progress> getProgressById(@PathVariable Long id) {
        Progress progress = progressService.getProgress(id);
        return progress != null ? ResponseEntity.ok(progress) : ResponseEntity.notFound().build();
    }
    
    @PostMapping("/add")
    public ResponseEntity<Progress> addProgress(@RequestBody Progress progress) {
        return ResponseEntity.ok(progressService.addProgress(progress));
    }
    
    @PutMapping("/update")
    public ResponseEntity<Progress> updateProgress(@RequestBody Progress progress) {
        return ResponseEntity.ok(progressService.updateProgress(progress));
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProgress(@PathVariable Long id) {
        progressService.deleteProgress(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/child/{childId}")
    public ResponseEntity<List<Progress>> getProgressByChild(@PathVariable Long childId) {
        return ResponseEntity.ok(progressService.getProgressByChild(childId));
    }
    
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Progress>> getProgressByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(progressService.getProgressByCourse(courseId));
    }
    
    @GetMapping("/child/{childId}/course/{courseId}")
    public ResponseEntity<List<Progress>> getProgressByChildAndCourse(@PathVariable Long childId, @PathVariable Long courseId) {
        return ResponseEntity.ok(progressService.getProgressByChildAndCourse(childId, courseId));
    }
    
    @GetMapping("/child/{childId}/summary")
    public ResponseEntity<Object> getChildProgressSummary(@PathVariable Long childId) {
        // Get child to retrieve XP and level
        tn.esprit.language_courses_service.ChildrenEnglish.Entities.Child child = 
            progressService.getChildById(childId);
        
        if (child == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Get all progress records for this child
        List<Progress> progressList = progressService.getProgressByChild(childId);
        
        // Calculate statistics from progress records
        int totalActivitiesCompleted = progressList.size();
        double averageCompletion = 0.0;
        
        if (!progressList.isEmpty()) {
            double totalCompletion = 0.0;
            for (Progress p : progressList) {
                if (p.getCompletionRate() != null) {
                    totalCompletion += p.getCompletionRate();
                }
            }
            averageCompletion = totalCompletion / progressList.size();
        }
        
        int currentXp = child.getXp() != null ? child.getXp() : 0;
        int currentLevel = child.getLevel() != null ? child.getLevel() : 1;
        int xpForNextLevel = currentLevel * 1000;
        int xpToNextLevel = xpForNextLevel - currentXp;
        
        // Build summary
        java.util.Map<String, Object> summary = new java.util.HashMap<>();
        summary.put("childId", childId);
        summary.put("totalXp", currentXp);
        summary.put("currentLevel", currentLevel);
        summary.put("xpToNextLevel", xpToNextLevel);
        summary.put("totalActivitiesCompleted", totalActivitiesCompleted);
        summary.put("accuracyPercentage", Math.round(averageCompletion * 10.0) / 10.0);
        summary.put("currentStreak", 0);
        summary.put("longestStreak", 0);
        summary.put("badges", new java.util.ArrayList<>());
        
        // Build recent activities from progress
        java.util.List<java.util.Map<String, Object>> recentActivities = new java.util.ArrayList<>();
        for (Progress p : progressList.stream().limit(5).toList()) {
            java.util.Map<String, Object> activity = new java.util.HashMap<>();
            activity.put("courseId", p.getCourse() != null ? p.getCourse().getCourseId() : null);
            activity.put("title", p.getCourse() != null ? p.getCourse().getTitle() : "Unknown Course");
            activity.put("completionRate", p.getCompletionRate());
            activity.put("lastAccess", p.getLastAccess());
            recentActivities.add(activity);
        }
        summary.put("recentActivities", recentActivities);
        
        return ResponseEntity.ok(summary);
    }
}
