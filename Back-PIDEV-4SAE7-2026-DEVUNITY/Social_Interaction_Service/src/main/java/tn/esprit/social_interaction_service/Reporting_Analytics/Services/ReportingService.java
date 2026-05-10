package tn.esprit.social_interaction_service.Reporting_Analytics.Services;

import lombok.RequiredArgsConstructor;
// import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
// import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
// import org.springframework.data.elasticsearch.core.SearchHits;
// import org.springframework.data.elasticsearch.core.query.Query;
// import org.springframework.data.elasticsearch.core.query.StringQuery;
// import org.springframework.stereotype.Service;
// import tn.esprit.social_interaction_service.Reporting_Analytics.Documents.AnalyticsDocument;
// import tn.esprit.social_interaction_service.Reporting_Analytics.Repositories.AnalyticsElasticRepository;

import java.util.HashMap;
import java.util.Map;
// import java.util.stream.StreamSupport;

// @Service
// @ConditionalOnProperty(name = "spring.data.elasticsearch.repositories.enabled", havingValue = "true")
@RequiredArgsConstructor
public class ReportingService {

    // private final AnalyticsElasticRepository analyticsElasticRepository;
    // private final ElasticsearchOperations elasticsearchOperations;

    /*
    // 1. Learning / Academic KPIs — queries only Elasticsearch
    public Map<String, Object> getAcademicKPIs() {
        Map<String, Object> kpis = new HashMap<>();

        var allCourses = StreamSupport.stream(analyticsElasticRepository.findAll().spliterator(), false)
                .filter(doc -> "COURSE".equals(doc.getType()))
                .toList();

        long totalCourses = allCourses.size();
        kpis.put("total_courses", totalCourses);

        double avgScore = allCourses.stream()
                .filter(doc -> doc.getCourseScore() != null)
                .mapToDouble(AnalyticsDocument::getCourseScore)
                .average()
                .orElse(0.0);
        kpis.put("average_score", avgScore);

        long certifiedCount = allCourses.stream()
                .filter(doc -> Boolean.TRUE.equals(doc.getCourseIsCertified()))
                .count();
        kpis.put("certification_rate", totalCourses > 0 ? (double) certifiedCount / totalCourses * 100 : 0);

        long totalEnrollments = allCourses.stream()
                .filter(doc -> doc.getCourseEnrollmentCount() != null)
                .mapToInt(AnalyticsDocument::getCourseEnrollmentCount)
                .sum();
        kpis.put("total_enrollments", totalEnrollments);

        return kpis;
    }

    // 2. Challenge KPIs — queries only Elasticsearch
    public Map<String, Object> getChallengeKPIs() {
        Map<String, Object> kpis = new HashMap<>();

        var allChallenges = StreamSupport.stream(analyticsElasticRepository.findAll().spliterator(), false)
                .filter(doc -> "CHALLENGE".equals(doc.getType()))
                .toList();

        long totalChallenges = allChallenges.size();
        kpis.put("total_challenges", totalChallenges);

        long totalParticipation = allChallenges.stream()
                .filter(doc -> doc.getChallengeParticipationCount() != null)
                .mapToLong(AnalyticsDocument::getChallengeParticipationCount)
                .sum();
        kpis.put("total_participation", totalParticipation);

        double avgCompletionTime = allChallenges.stream()
                .filter(doc -> doc.getChallengeAverageCompletionTimeSeconds() != null)
                .mapToLong(AnalyticsDocument::getChallengeAverageCompletionTimeSeconds)
                .average()
                .orElse(0.0);
        kpis.put("avg_completion_time_seconds", avgCompletionTime);

        long totalCompletions = allChallenges.stream()
                .filter(doc -> doc.getChallengeCompletionCount() != null)
                .mapToInt(AnalyticsDocument::getChallengeCompletionCount)
                .sum();
        kpis.put("total_completions", totalCompletions);

        return kpis;
    }

    // Advanced search — queries Elasticsearch directly
    public SearchHits<AnalyticsDocument> searchCoursesByKeyword(String keyword) {
        Query query = new StringQuery("{\"match\": {\"courseTitle\": \"" + keyword + "\"}}");
        return elasticsearchOperations.search(query, AnalyticsDocument.class);
    }
    */
}