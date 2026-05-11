package tn.esprit.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.Books_Clubs.entities.Rental;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    long countByUserUserIdAndPaidTrue(Integer userId);
    void deleteByBook_BookId(Long bookId);

}