package tn.esprit.language_courses_service.ChildrenEnglish.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.language_courses_service.ChildrenEnglish.DTO.AnalyticsData;
import tn.esprit.language_courses_service.ChildrenEnglish.DTO.OverviewStats;
import tn.esprit.language_courses_service.ChildrenEnglish.Serivces.AnalyticsService;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AnalyticsController {
    
    private final AnalyticsService analyticsService;
    
    @GetMapping
    public ResponseEntity<AnalyticsData> getAnalytics(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return ResponseEntity.ok(analyticsService.getAnalytics(startDate, endDate));
    }
    
    @GetMapping("/overview")
    public ResponseEntity<OverviewStats> getOverviewStats() {
        return ResponseEntity.ok(analyticsService.getOverviewStats());
    }
}
