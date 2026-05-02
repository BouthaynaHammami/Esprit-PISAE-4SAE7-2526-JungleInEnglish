package tn.esprit.Books_Clubs.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.Books_Clubs.entities.Order;


public interface OrderRepository extends JpaRepository<Order, Long> {
    long countByUserUserIdAndPaidTrue(Integer userId);
}
