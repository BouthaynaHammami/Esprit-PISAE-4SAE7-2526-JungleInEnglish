package tn.esprit.academic_management_service.Certifications.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.academic_management_service.Certifications.Entities.SessionQuestion;

public interface SessionQuestionRepository extends JpaRepository<SessionQuestion, Long> {
}