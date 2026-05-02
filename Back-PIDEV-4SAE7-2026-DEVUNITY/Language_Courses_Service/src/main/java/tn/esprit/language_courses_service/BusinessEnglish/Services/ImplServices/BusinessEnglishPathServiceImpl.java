package tn.esprit.language_courses_service.BusinessEnglish.Services.ImplServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.language_courses_service.BusinessEnglish.Entities.BusinessEnglishPath;
import tn.esprit.language_courses_service.BusinessEnglish.Repositories.BusinessEnglishPathRepository;
import tn.esprit.language_courses_service.BusinessEnglish.Services.IServices.BusinessEnglishPathService;

import java.util.List;

@Service
public class BusinessEnglishPathServiceImpl implements BusinessEnglishPathService {

    @Autowired
    private BusinessEnglishPathRepository businessEnglishPathRepository;

    @Override
    public BusinessEnglishPath addBusinessEnglishPath(BusinessEnglishPath businessEnglishPath) {
        return businessEnglishPathRepository.save(businessEnglishPath);
    }

    @Override
    public BusinessEnglishPath updateBusinessEnglishPath(Long id, BusinessEnglishPath businessEnglishPath) {
        if (businessEnglishPathRepository.existsById(id)) {
            businessEnglishPath.setId(id);
            return businessEnglishPathRepository.save(businessEnglishPath);
        }
        throw new IllegalArgumentException("Business English Path not found");
    }

    @Override
    public List<BusinessEnglishPath> getAllBusinessEnglishPaths() {
        return businessEnglishPathRepository.findAll();
    }

    @Override
    public BusinessEnglishPath getBusinessEnglishPathById(Long id) {
        return businessEnglishPathRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Business English Path not found"));
    }

    @Override
    public void deleteBusinessEnglishPath(Long id) {
        if (businessEnglishPathRepository.existsById(id)) {
            businessEnglishPathRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Business English Path not found");
        }
    }
}
