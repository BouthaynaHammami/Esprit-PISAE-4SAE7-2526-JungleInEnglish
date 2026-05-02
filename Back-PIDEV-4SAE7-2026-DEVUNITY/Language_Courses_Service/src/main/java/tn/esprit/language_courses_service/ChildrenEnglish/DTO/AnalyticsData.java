package tn.esprit.language_courses_service.ChildrenEnglish.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsData {
    private OverviewStats overviewStats;
    private List<ChildPerformance> topPerformers;
    private List<ActivityStats> activityStats;
    private List<ProgressTrend> progressTrends;
    private Map<String, Integer> levelDistribution;
}
