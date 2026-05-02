package tn.esprit.Books_Clubs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.Books_Clubs.entities.Transaction;
import tn.esprit.learner_managment_service.UserManagement.Entities.User;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // âœ… utiliser la relation User
    List<Transaction> findByUser(User user);
}
