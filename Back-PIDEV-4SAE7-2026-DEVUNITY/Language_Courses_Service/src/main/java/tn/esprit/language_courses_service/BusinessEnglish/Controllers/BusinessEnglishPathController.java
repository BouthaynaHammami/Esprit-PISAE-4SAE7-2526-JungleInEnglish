package tn.esprit.language_courses_service.BusinessEnglish.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.language_courses_service.BusinessEnglish.Entities.BusinessEnglishPath;
import tn.esprit.language_courses_service.BusinessEnglish.Services.IServices.BusinessEnglishPathService;

import java.util.List;

@RestController
@RequestMapping("/paths")
@RequiredArgsConstructor
public class BusinessEnglishPathController {

    private final BusinessEnglishPathService businessEnglishPathService;

    @PostMapping
    public BusinessEnglishPath addBusinessEnglishPath(@RequestBody BusinessEnglishPath path) {
        return businessEnglishPathService.addBusinessEnglishPath(path);
    }

    @PutMapping("/{id}")
    public BusinessEnglishPath updateBusinessEnglishPath(@PathVariable Long id, @RequestBody BusinessEnglishPath path) {
        return businessEnglishPathService.updateBusinessEnglishPath(id, path);
    }

    @GetMapping
    public List<BusinessEnglishPath> getAllBusinessEnglishPaths() {
        return businessEnglishPathService.getAllBusinessEnglishPaths();
    }

    @GetMapping("/{id}")
    public BusinessEnglishPath getBusinessEnglishPathById(@PathVariable Long id) {
        return businessEnglishPathService.getBusinessEnglishPathById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteBusinessEnglishPath(@PathVariable Long id) {
        businessEnglishPathService.deleteBusinessEnglishPath(id);
    }
}
