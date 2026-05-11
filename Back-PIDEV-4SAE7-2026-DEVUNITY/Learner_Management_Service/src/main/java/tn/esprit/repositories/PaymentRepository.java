package tn.esprit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.Books_Clubs.entities.Payment;
import tn.esprit.Books_Clubs.entities.PaymentStatus;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByMember_UserIdAndTraining_TrainingId(Integer userId, Long trainingId);

    boolean existsByMember_UserIdAndTraining_TrainingIdAndStatus(Integer userId, Long trainingId, PaymentStatus status);
}