package tn.esprit.language_courses_service.ChallengesCompetitions.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.StudentChallengeSession;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.SessionStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentChallengeSessionRepository extends JpaRepository<StudentChallengeSession, Long> {
    
    /**
     * Get active session for user
     */
    @Query("SELECT s FROM StudentChallengeSession s WHERE s.idUser = :userId AND s.status = 'IN_PROGRESS'")
    Optional<StudentChallengeSession> findActiveSessionByUser(@Param("userId") Long userId);

    /**
     * Get all sessions for user
     */
    List<StudentChallengeSession> findByIdUserOrderBySessionStartTimeDesc(Long idUser);

    /**
     * Get sessions by status
     */
    List<StudentChallengeSession> findByStatus(SessionStatus status);
}
