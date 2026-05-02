package tn.esprit.Books_Clubs.Services.IServices;

import tn.esprit.Books_Clubs.entities.*;

import java.math.BigDecimal;
import java.util.List;

public interface IOrderService {

    Order createOrder(Integer userId, Currency currency);
    Order addItem(Long orderId, Long bookId, int qty);
    Order payOrder(Long orderId);
    Order cancelOrder(Long orderId);
    Order getById(Long id);
    List<Order> getAll();
    void delete(Long id);

    // â”€â”€ Currency â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    BigDecimal getOrderTotalIn(Long orderId, Currency targetCurrency);
}
