package tn.esprit.learner_managment_service.DropoutPrediction.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tn.esprit.learner_managment_service.DropoutPrediction.Entities.DropoutForm;

public interface DropoutFormRepository extends JpaRepository<DropoutForm, Long> {
    List<DropoutForm> findByUserUserId(Integer userId);
}
