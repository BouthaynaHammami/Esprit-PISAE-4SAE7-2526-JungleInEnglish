package tn.esprit.Services.ImplServices;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.Services.IServices.IOrderService;
import tn.esprit.Books_Clubs.entities.*;
import tn.esprit.repositories.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final BookRepository bookRepository;
    private final StockRepository stockRepository;
    private final DiscountService discountService;
    private final BookClubUserRepository BookClubUserRepository;

    @Override
    @Transactional
    public Order createOrder(Integer userId, Currency currency) {

        User user = BookClubUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDate.now());
        order.setCurrency(currency);
        order.setStatus(OrderStatus.CREATED);
        order.setPaid(false);
        order.setDiscountApplied(false);
        order.setTotalAmount(BigDecimal.ZERO);

        if (order.getItems() == null) {
            order.setItems(new ArrayList<>());
        }

        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order addItem(Long orderId, Long bookId, int qty) {

        if (qty <= 0) {
            throw new RuntimeException("qty must be > 0");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        if (order.getStatus() != OrderStatus.CREATED) {
            throw new RuntimeException("Order not CREATED");
        }

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found: " + bookId));

        Stock stock = book.getStock();
        if (stock == null) {
            throw new RuntimeException("No stock for book: " + book.getTitle());
        }

        int available = stock.getQuantity() - stock.getReservedQty();
        if (book.getStatus() != BookStatus.AVAILABLE || available < qty) {
            throw new RuntimeException("Book not available: " + book.getTitle());
        }

        stock.setReservedQty(stock.getReservedQty() + qty);
        stock.setLastUpdate(LocalDate.now());
        stockRepository.save(stock);

        BigDecimal unitPrice = book.getCurrency().convert(book.getSalePrice(), order.getCurrency());
        BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(qty));

        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setBook(book);
        item.setQuantity(qty);
        item.setUnitSalePrice(unitPrice);
        item.setLineTotal(lineTotal);
        orderItemRepository.save(item);

        if (order.getItems() == null) {
            order.setItems(new ArrayList<>());
        }
        order.getItems().add(item);

        order.setTotalAmount(order.getTotalAmount().add(lineTotal));

        // IMPORTANT :
        // On ne paie PAS ici.
        // La commande reste CREATED jusqu'au clic sur "payer".
        order.setPaid(false);
        order.setStatus(OrderStatus.CREATED);
        order.setPaymentDate(null);

        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order payOrder(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        if (order.getStatus() != OrderStatus.CREATED) {
            throw new RuntimeException("Order not payable");
        }

        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new RuntimeException("Order has no items");
        }

        User user = order.getUser();

        if (user != null
                && user.getUserId() != null
                && discountService.isEligibleForDiscount(user.getUserId())) {

            BigDecimal factor = BigDecimal.valueOf(1 - discountService.getDiscountRate());

            BigDecimal discountedTotal = order.getTotalAmount()
                    .multiply(factor)
                    .setScale(3, java.math.RoundingMode.HALF_UP);

            order.setTotalAmount(discountedTotal);
            order.setDiscountApplied(true);

            for (OrderItem item : order.getItems()) {
                BigDecimal newLine = item.getLineTotal()
                        .multiply(factor)
                        .setScale(3, java.math.RoundingMode.HALF_UP);

                item.setLineTotal(newLine);
                orderItemRepository.save(item);
            }
        }

        for (OrderItem item : order.getItems()) {
            Book book = item.getBook();
            Stock stock = book.getStock();

            stock.setQuantity(stock.getQuantity() - item.getQuantity());
            stock.setReservedQty(stock.getReservedQty() - item.getQuantity());
            stock.setLastUpdate(LocalDate.now());
            stockRepository.save(stock);

            if (stock.getQuantity() <= 0) {
                book.setStatus(BookStatus.OUT_OF_STOCK);
                bookRepository.save(book);
            }
        }

        order.setStatus(OrderStatus.PAID);
        order.setPaid(true);
        order.setPaymentDate(LocalDate.now());

        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order cancelOrder(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        if (order.getStatus() != OrderStatus.CREATED) {
            throw new RuntimeException("Only CREATED can cancel");
        }

        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                Stock stock = item.getBook().getStock();
                stock.setReservedQty(stock.getReservedQty() - item.getQuantity());
                stock.setLastUpdate(LocalDate.now());
                stockRepository.save(stock);
            }
        }

        order.setStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    @Override
    public BigDecimal getOrderTotalIn(Long orderId, Currency targetCurrency) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        return order.getCurrency().convert(order.getTotalAmount(), targetCurrency);
    }

    @Override
    @Transactional(readOnly = true)
    public Order getById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Order not found: " + id);
        }
        orderRepository.deleteById(id);
    }
}