package tn.esprit.social_interaction_service.Communications.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.social_interaction_service.Communications.Entities.Report;
import tn.esprit.social_interaction_service.Communications.Repositories.ReportRepository;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService implements IReportService {
    
    private final ReportRepository reportRepository;
    
    @Override
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }
    
    @Override
    public Report getReportById(Long id) {
        return reportRepository.findById(id).orElse(null);
    }
    
    @Override
    public Report addReport(Report report) {
        report.setCreatedAt(new Date());
        report.setStatus("PENDING");
        return reportRepository.save(report);
    }
    
    @Override
    public Report updateReport(Report report) {
        return reportRepository.save(report);
    }
    
    @Override
    public void deleteReport(Long id) {
        reportRepository.deleteById(id);
    }
    
    @Override
    public List<Report> getReportsByUserId(Integer userId) {
        return reportRepository.findByUserId(userId);
    }
    
    @Override
    public List<Report> getReportsByStatus(String status) {
        return reportRepository.findByStatus(status);
    }
}
