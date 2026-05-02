package tn.esprit.social_interaction_service.Reporting_Analytics.Services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import tn.esprit.social_interaction_service.Reporting_Analytics.Documents.AnalyticsDocument;
import tn.esprit.social_interaction_service.Reporting_Analytics.Repositories.AnalyticsElasticRepository;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportingServiceTest {

    @Mock
    private AnalyticsElasticRepository analyticsElasticRepository;
    @Mock
    private ElasticsearchOperations elasticsearchOperations;

    @InjectMocks
    private ReportingService reportingService;

    @Test
    void getAcademicKPIs_shouldAggregateOnlyCourseDocuments() {
        AnalyticsDocument c1 = AnalyticsDocument.builder()
                .id("COURSE_1")
                .type("COURSE")
                .courseScore(80f)
                .courseIsCertified(true)
                .courseEnrollmentCount(12)
                .build();
        AnalyticsDocument c2 = AnalyticsDocument.builder()
                .id("COURSE_2")
                .type("COURSE")
                .courseScore(60f)
                .courseIsCertified(false)
                .courseEnrollmentCount(8)
                .build();
        AnalyticsDocument challenge = AnalyticsDocument.builder().id("CHALLENGE_1").type("CHALLENGE").build();

        when(analyticsElasticRepository.findAll()).thenReturn(List.of(c1, c2, challenge));

        Map<String, Object> kpis = reportingService.getAcademicKPIs();

        assertEquals(2L, kpis.get("total_courses"));
        assertEquals(70.0, (double) kpis.get("average_score"));
        assertEquals(20, kpis.get("total_enrollments"));
        assertEquals(50.0, (double) kpis.get("certification_rate"));
    }

    @Test
    void searchCoursesByKeyword_shouldDelegateToElasticsearchOperations() {
        @SuppressWarnings("unchecked")
        SearchHits<AnalyticsDocument> hits = (SearchHits<AnalyticsDocument>) org.mockito.Mockito.mock(SearchHits.class);
        when(elasticsearchOperations.search(any(Query.class), org.mockito.ArgumentMatchers.eq(AnalyticsDocument.class))).thenReturn(hits);

        SearchHits<AnalyticsDocument> result = reportingService.searchCoursesByKeyword("grammar");

        assertEquals(hits, result);
        verify(elasticsearchOperations).search(any(Query.class), org.mockito.ArgumentMatchers.eq(AnalyticsDocument.class));
    }
}
