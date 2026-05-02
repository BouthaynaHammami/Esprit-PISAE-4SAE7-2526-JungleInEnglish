package tn.esprit.social_interaction_service.Communications.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.social_interaction_service.Communications.Entities.Report;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByUserId(Integer userId);
    List<Report> findByStatus(String status);
}
