package tn.esprit.Books_Clubs.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.Books_Clubs.Services.IServices.IOrderService;
import tn.esprit.Books_Clubs.entities.*;
import tn.esprit.Books_Clubs.repositories.OrderRepository;

import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService   orderService;
    private final OrderRepository orderRepository;

    @PostMapping
    public Order create(@RequestParam Integer userId,
                        @RequestParam Currency currency) {
        return orderService.createOrder(userId, currency);
    }

    @PostMapping("/{orderId}/items")
    public Order addItem(@PathVariable Long orderId,
                         @RequestParam Long bookId,
                         @RequestParam int qty) {
        return orderService.addItem(orderId, bookId, qty);
    }

    @PostMapping("/{orderId}/pay")
    public Order payOrder(@PathVariable Long orderId) {
        return orderService.payOrder(orderId);
    }

    @PostMapping("/{orderId}/cancel")
    public Order cancel(@PathVariable Long orderId) {
        return orderService.cancelOrder(orderId);
    }

    @GetMapping
    public List<Order> all() {
        return orderService.getAll();
    }

    @GetMapping("/{id}")
    public Order get(@PathVariable Long id) {
        return orderService.getById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        orderService.delete(id);
    }
}
