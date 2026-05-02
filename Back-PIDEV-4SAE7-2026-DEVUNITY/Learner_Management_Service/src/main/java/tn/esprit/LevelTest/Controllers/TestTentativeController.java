package tn.esprit.LevelTest.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;
import tn.esprit.LevelTest.Entities.TestTentative;
import tn.esprit.LevelTest.Services.ImplServices.TestTentativeServiceImpl;


import java.util.List;

@RestController
@RequestMapping("/api/test-tentatives")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class TestTentativeController {

    private final TestTentativeServiceImpl testTentativeService;

    @GetMapping
    public List<TestTentative> getAll() {
        return testTentativeService.getAll();
    }

    @GetMapping("/{id}")
    public TestTentative getById(@PathVariable Long id) {
        return testTentativeService.getById(id);
    }

    @GetMapping("/corrections")
    public List<TestTentative> getSubmissionsForCorrection() {
        return testTentativeService.getSubmissionsForCorrection();
    }

    @PostMapping
    public TestTentative create(@RequestBody TestTentative testTentative) {
        return testTentativeService.create(testTentative);
    }

    @PutMapping("/{id}")
    public TestTentative update(@PathVariable Long id, @RequestBody TestTentative testTentative) {
        return testTentativeService.update(id, testTentative);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        testTentativeService.delete(id);
    }
}
