package tn.esprit.Services.IServices;

import tn.esprit.Books_Clubs.entities.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IRentalService {

    Rental createRental(Long userId,
                        Long bookId,
                        LocalDate startDate,
                        LocalDate dueDate,
                        BigDecimal dailyPrice,
                        Currency currency);
    Rental payRental(Long rentalId);
    Rental returnRental(Long rentalId, LocalDate returnDate);
    Rental getById(Long id);
    List<Rental> getAll();
    void delete(Long id);

    // ── Currency ──────────────────────────────────────────────────────────────
    BigDecimal getDailyPriceIn(Long rentalId, Currency targetCurrency);
    BigDecimal getTotalPriceIn(Long rentalId, Currency targetCurrency);
}