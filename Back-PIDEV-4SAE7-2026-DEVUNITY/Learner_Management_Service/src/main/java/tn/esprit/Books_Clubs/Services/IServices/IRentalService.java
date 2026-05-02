package tn.esprit.Books_Clubs.Services.IServices;

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

    // â”€â”€ Currency â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    BigDecimal getDailyPriceIn(Long rentalId, Currency targetCurrency);
    BigDecimal getTotalPriceIn(Long rentalId, Currency targetCurrency);
}
