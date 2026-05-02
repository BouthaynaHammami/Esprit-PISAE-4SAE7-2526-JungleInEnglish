package tn.esprit.social_interaction_service.Reporting_Analytics.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.social_interaction_service.Reporting_Analytics.Documents.AnalyticsDocument;
import tn.esprit.social_interaction_service.Reporting_Analytics.Services.ReportingService;

import java.util.Map;

@RestController
@RequestMapping("/reporting/kpis")
@ConditionalOnProperty(name = "spring.data.elasticsearch.repositories.enabled", havingValue = "true")
@RequiredArgsConstructor
public class ReportingController {

    private final ReportingService reportingService;

    @GetMapping("/academic")
    public ResponseEntity<Map<String, Object>> getAcademicKPIs() {
        return ResponseEntity.ok(reportingService.getAcademicKPIs());
    }

    @GetMapping("/challenges")
    public ResponseEntity<Map<String, Object>> getChallengeKPIs() {
        return ResponseEntity.ok(reportingService.getChallengeKPIs());
    }

    @GetMapping("/search-courses")
    public ResponseEntity<SearchHits<AnalyticsDocument>> searchCourses(@RequestParam String keyword) {
        return ResponseEntity.ok(reportingService.searchCoursesByKeyword(keyword));
    }
}
