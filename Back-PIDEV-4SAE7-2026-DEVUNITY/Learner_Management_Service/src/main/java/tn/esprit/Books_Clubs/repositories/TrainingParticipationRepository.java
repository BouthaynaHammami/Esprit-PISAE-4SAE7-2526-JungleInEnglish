package tn.esprit.Books_Clubs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.Books_Clubs.entities.TrainingParticipation;

import java.util.List;


public interface TrainingParticipationRepository extends JpaRepository<TrainingParticipation, Long> {
    List<TrainingParticipation> findByTraining_TrainingId(Long trainingId);
    boolean existsByMemberIdAndTraining_TrainingId(Long memberId, Long trainingId);

    long countByTraining_TrainingId(Long trainingId);
}
