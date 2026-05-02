package tn.esprit.academic_management_service.Certifications.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.academic_management_service.Certifications.Entities.CertificationQuestion;

import java.util.List;

public interface QuestionRepository extends JpaRepository<CertificationQuestion, Long> {

    @Query(
            value = "SELECT * FROM certification_question WHERE active = true ORDER BY RAND() LIMIT 20",
            nativeQuery = true
    )
    List<CertificationQuestion> findRandomActiveQuestions();

    // 🔧 Admin
    List<CertificationQuestion> findByActiveTrue();
}