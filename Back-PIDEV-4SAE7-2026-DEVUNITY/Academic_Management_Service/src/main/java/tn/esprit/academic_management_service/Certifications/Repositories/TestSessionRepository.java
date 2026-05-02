package tn.esprit.academic_management_service.Certifications.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.academic_management_service.Certifications.Entities.TestSession;

import java.util.List;
import java.util.Optional;

public interface TestSessionRepository extends JpaRepository<TestSession, Long> {

    List<TestSession> findByStudentId(Long studentId);

    // ✅ Compter uniquement les sessions soumises (score != null)
    int countByStudentIdAndScoreIsNotNull(Long studentId);

    boolean existsByStudentIdAndSuspiciousTrue(Long studentId);

    Optional<TestSession> findTopByStudentIdOrderByTakenAtDesc(Long studentId);

    List<TestSession> findBySuspiciousTrue();
}