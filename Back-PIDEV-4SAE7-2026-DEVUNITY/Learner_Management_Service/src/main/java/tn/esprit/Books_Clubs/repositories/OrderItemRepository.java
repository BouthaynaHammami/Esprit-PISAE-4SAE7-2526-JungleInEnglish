package tn.esprit.Books_Clubs.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.Books_Clubs.entities.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {}
