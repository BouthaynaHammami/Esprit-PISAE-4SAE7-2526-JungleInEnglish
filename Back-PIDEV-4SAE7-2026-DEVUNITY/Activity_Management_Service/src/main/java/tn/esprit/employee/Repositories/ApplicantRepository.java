package tn.esprit.employee.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.employee.Entities.Applicant;

import java.util.List;

@Repository
public interface ApplicantRepository extends JpaRepository<Applicant, Long> {
    List<Applicant> findByUserId(Long userId);
    List<Applicant> findByRecruitmentId(Long recruitmentId);
    List<Applicant> findByInterviewId(Long interviewId);
    boolean existsByUserIdAndRecruitmentId(Long userId, Long recruitmentId);
}
