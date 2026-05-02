package tn.esprit.Books_Clubs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.Books_Clubs.entities.Stock;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    // Find stock by the bookId of the associated Book
    Optional<Stock> findByBook_BookId(Long bookId);
}
