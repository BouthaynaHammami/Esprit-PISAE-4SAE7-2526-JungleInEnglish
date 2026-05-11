package tn.esprit.Books_Clubs.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.Services.IServices.IRentalService;
import tn.esprit.Books_Clubs.entities.*;
import tn.esprit.repositories.RentalRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/rentals")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class RentalController {

    private final IRentalService   rentalService;
    private final RentalRepository rentalRepository;
    @PostMapping
    public Rental create(@RequestParam Long bookId,
                         @RequestParam String startDate,
                         @RequestParam String dueDate,
                         @RequestParam BigDecimal dailyPrice,
                         @RequestParam Currency currency,
                         @RequestParam(required = false) Long userId) {

        return rentalService.createRental(
                userId, // 🔥 IMPORTANT
                bookId,
                LocalDate.parse(startDate),
                LocalDate.parse(dueDate),
                dailyPrice,
                currency
        );
    }
    @PostMapping("/{rentalId}/pay")
    public Rental payRental(@PathVariable Long rentalId) {
        return rentalService.payRental(rentalId);
    }

    @PostMapping("/{id}/return")
    public Rental returnBook(@PathVariable Long id,
                             @RequestParam String returnDate) {
        return rentalService.returnRental(id, LocalDate.parse(returnDate));
    }

    @GetMapping
    public List<Rental> all() {
        return rentalService.getAll();
    }

    @GetMapping("/{id}")
    public Rental get(@PathVariable Long id) {
        return rentalService.getById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        rentalService.delete(id);
    }
}