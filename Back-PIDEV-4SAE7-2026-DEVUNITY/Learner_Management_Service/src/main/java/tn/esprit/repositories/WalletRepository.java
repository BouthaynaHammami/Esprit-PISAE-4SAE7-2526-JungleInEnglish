package tn.esprit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.Books_Clubs.entities.User;
import tn.esprit.Books_Clubs.entities.Wallet;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    // ✅ relation directe avec User
    Optional<Wallet> findByUser(User user);
}