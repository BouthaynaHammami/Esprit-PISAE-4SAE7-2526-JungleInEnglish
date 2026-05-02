package tn.esprit.employee.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.employee.Entities.Recruitment;
import tn.esprit.employee.Services.ImplServices.RecruitmentServiceImpl;


import java.util.List;
@RestController
@RequestMapping("/api/recruitments")
@RequiredArgsConstructor
@CrossOrigin(
        origins = "http://localhost:4200",
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}
)
public class RecruitmentController {

    private final RecruitmentServiceImpl recruitmentService;

    @GetMapping
    public List<Recruitment> getAll() {
        return recruitmentService.getAll();
    }

    @GetMapping("/{id}")
    public Recruitment getById(@PathVariable Long id) {
        return recruitmentService.getById(id);
    }

    @PostMapping
    public Recruitment create(@RequestBody Recruitment recruitment) {
        return recruitmentService.create(recruitment);
    }

    @PutMapping("/{id}")
    public Recruitment update(@PathVariable Long id, @RequestBody Recruitment recruitment) {
        return recruitmentService.update(id, recruitment);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        recruitmentService.delete(id);
    }
}
