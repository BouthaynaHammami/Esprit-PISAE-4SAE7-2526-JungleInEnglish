package tn.esprit.language_courses_service.ChallengesCompetitions.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.AttemptStatus;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.ChallengeAttempt;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChallengeAttemptRepository extends JpaRepository<ChallengeAttempt, Long> {

    List<ChallengeAttempt> findByIdUser(Long idUser);

    List<ChallengeAttempt> findByChallengeId(Long challengeId);

    List<ChallengeAttempt> findByIdUserAndChallengeId(Long idUser, Long challengeId);

    @Query("SELECT ca FROM ChallengeAttempt ca WHERE ca.status = :status")
    List<ChallengeAttempt> findByStatus(@Param("status") AttemptStatus status);

    Optional<ChallengeAttempt> findByIdUserAndChallengeIdAndStatus(Long idUser, Long challengeId, AttemptStatus status);
}