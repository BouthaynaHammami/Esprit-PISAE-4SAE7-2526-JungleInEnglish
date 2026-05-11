package tn.esprit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.Books_Clubs.entities.TrainingParticipation;

import java.util.List;
import java.util.Optional;

public interface TrainingParticipationRepository extends JpaRepository<TrainingParticipation, Long> {

    boolean existsByMember_UserIdAndTraining_TrainingId(Integer userId, Long trainingId);

    List<TrainingParticipation> findByTraining_TrainingId(Long trainingId);

    Optional<TrainingParticipation> findByMember_UserIdAndTraining_TrainingId(Integer userId, Long trainingId);
}