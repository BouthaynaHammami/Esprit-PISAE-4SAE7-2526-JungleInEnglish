package tn.esprit.LevelTest.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.LevelTest.Entities.TestStatus;
import tn.esprit.LevelTest.Entities.TestTentative;

import java.util.List;

@Repository
public interface TestTentativeRepository extends JpaRepository<TestTentative, Long> {
    List<TestTentative> findByStatus(TestStatus status);
}
