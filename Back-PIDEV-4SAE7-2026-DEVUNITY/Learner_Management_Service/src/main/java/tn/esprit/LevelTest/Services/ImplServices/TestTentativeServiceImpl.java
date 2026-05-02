package tn.esprit.LevelTest.Services.ImplServices;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.LevelTest.Entities.TestStatus;
import tn.esprit.LevelTest.Entities.TestTentative;
import tn.esprit.LevelTest.Repositories.TestTentativeRepository;
import tn.esprit.LevelTest.Services.IServices.ITestTentativeService;


import java.util.List;

@Service
@RequiredArgsConstructor
public class TestTentativeServiceImpl implements ITestTentativeService {

    private final TestTentativeRepository testTentativeRepository;

    @Override
    public List<TestTentative> getAll() {
        return testTentativeRepository.findAll();
    }

    @Override
    public TestTentative getById(Long id) {
        return testTentativeRepository.findById(id).orElse(null);
    }



    @Override
    public TestTentative create(TestTentative testTentative) {
        return testTentativeRepository.save(testTentative);
    }

    @Override
    public TestTentative update(Long id, TestTentative testTentative) {
        TestTentative existing = testTentativeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TestTentative not found with id: " + id));

        // Only patch the fields the tutor is allowed to change
        existing.setScore(testTentative.getScore());
        existing.setTutorFeedback(testTentative.getTutorFeedback());
        existing.setStatus(testTentative.getStatus());

        return testTentativeRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        testTentativeRepository.deleteById(id);
    }

    @Override
    public List<TestTentative> getSubmissionsForCorrection() {
        return testTentativeRepository.findByStatus(TestStatus.PENDING);
    }

}
