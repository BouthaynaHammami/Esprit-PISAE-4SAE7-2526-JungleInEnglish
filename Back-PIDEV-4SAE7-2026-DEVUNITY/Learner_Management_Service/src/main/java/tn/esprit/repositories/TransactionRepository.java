package tn.esprit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.Books_Clubs.entities.Transaction;
import tn.esprit.Books_Clubs.entities.User;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // ✅ utiliser la relation User
    List<Transaction> findByUser(User user);
}