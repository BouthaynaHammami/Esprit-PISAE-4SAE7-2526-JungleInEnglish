package tn.esprit.employee.Services.IServices;

import org.springframework.web.multipart.MultipartFile;
import tn.esprit.employee.Dto.CvAnalysisResult;

public interface ICvAnalysisService {
    CvAnalysisResult analyzeCv(MultipartFile cvFile, String skills, Integer experienceYears);
    CvAnalysisResult analyzeCvFromUrl(String cvUrl, String skills, Integer experienceYears);
    CvAnalysisResult analyzeCvFromText(String cvText, String skills, Integer experienceYears);
    String extractTextFromPdf(MultipartFile file);
}
