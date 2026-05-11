package tn.esprit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.Books_Clubs.entities.ExcursionParticipation;

import java.util.List;

public interface ExcursionParticipationRepository extends JpaRepository<ExcursionParticipation, Long> {

    List<ExcursionParticipation> findByExcursion_ExcursionId(Long excursionId);

    boolean existsByMember_UserIdAndExcursion_ExcursionId(Integer userId, Long excursionId);

    long countByExcursion_ExcursionId(Long excursionId);
}