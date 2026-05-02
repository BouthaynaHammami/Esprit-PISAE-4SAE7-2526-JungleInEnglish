package tn.esprit.LevelTest.Services.IServices;

import tn.esprit.LevelTest.Entities.TestStatus;
import tn.esprit.LevelTest.Entities.TestTentative;

import java.util.List;

public interface ITestTentativeService {

    List<TestTentative> getAll();

    TestTentative getById(Long id);
    TestTentative create(TestTentative testTentative);

    TestTentative update(Long id, TestTentative testTentative);

    void delete(Long id);

    List<TestTentative> getSubmissionsForCorrection();
}
