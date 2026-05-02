package tn.esprit.Books_Clubs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.Books_Clubs.entities.ExcursionParticipation;

import java.util.List;

public interface ExcursionParticipationRepository extends JpaRepository<ExcursionParticipation, Long> {
    List<ExcursionParticipation> findByExcursion_ExcursionId(Long excursionId);
    boolean existsByMemberIdAndExcursion_ExcursionId(Long memberId, Long excursionId);

    long countByExcursion_ExcursionId(Long excursionId);
}
