package tn.esprit.Books_Clubs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.Books_Clubs.entities.Wallet;
import tn.esprit.learner_managment_service.UserManagement.Entities.User;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    // âœ… relation directe avec User
    Optional<Wallet> findByUser(User user);
}
