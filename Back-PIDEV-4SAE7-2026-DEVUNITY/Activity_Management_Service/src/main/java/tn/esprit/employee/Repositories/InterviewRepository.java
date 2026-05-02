package tn.esprit.employee.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.employee.Entities.Interview;

import java.util.List;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {
    // ✅ Trouver tous les interviews d'un Recruitment
    List<Interview> findByRecruitmentId(Long recruitmentId);

    // ✅ Trouver tous les interviews d'un User
    List<Interview> findByUserId(Long userId);

}
