package tn.esprit.language_courses_service.BusinessEnglish.Services.IServices;

import tn.esprit.language_courses_service.BusinessEnglish.Entities.BusinessEnglishPath;

import java.util.List;

public interface BusinessEnglishPathService {
    BusinessEnglishPath addBusinessEnglishPath(BusinessEnglishPath businessEnglishPath);
    BusinessEnglishPath updateBusinessEnglishPath(Long id, BusinessEnglishPath businessEnglishPath);
    List<BusinessEnglishPath> getAllBusinessEnglishPaths();
    BusinessEnglishPath getBusinessEnglishPathById(Long id);
    void deleteBusinessEnglishPath(Long id);
}