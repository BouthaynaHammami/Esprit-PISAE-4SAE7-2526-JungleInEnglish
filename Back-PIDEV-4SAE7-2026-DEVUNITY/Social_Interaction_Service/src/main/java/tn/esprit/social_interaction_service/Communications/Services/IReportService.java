package tn.esprit.social_interaction_service.Communications.Services;

import tn.esprit.social_interaction_service.Communications.Entities.Report;

import java.util.List;

public interface IReportService {
    List<Report> getAllReports();
    Report getReportById(Long id);
    Report addReport(Report report);
    Report updateReport(Report report);
    void deleteReport(Long id);
    List<Report> getReportsByUserId(Integer userId);
    List<Report> getReportsByStatus(String status);
}
