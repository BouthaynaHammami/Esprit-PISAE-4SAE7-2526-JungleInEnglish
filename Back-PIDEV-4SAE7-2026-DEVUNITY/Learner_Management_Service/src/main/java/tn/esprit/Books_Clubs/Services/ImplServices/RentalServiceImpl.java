package tn.esprit.Books_Clubs.Services.ImplServices;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.Books_Clubs.Services.IServices.IRentalService;
import tn.esprit.Books_Clubs.entities.*;
import tn.esprit.Books_Clubs.repositories.*;
import tn.esprit.learner_managment_service.UserManagement.Entities.User;
import tn.esprit.learner_managment_service.UserManagement.Repositories.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements IRentalService {

    private final RentalRepository rentalRepository;
    private final BookRepository   bookRepository;
    private final StockRepository  stockRepository;
    private final DiscountService  discountService;
    private final UserRepository userRepository;

    @Override

    public Rental createRental(Long userId,
                               Long bookId,
                               LocalDate startDate,
                               LocalDate dueDate,
                               BigDecimal dailyPrice,
                               Currency currency) {

        Book  book  = bookRepository.findById(bookId).orElseThrow();
        Stock stock = book.getStock();

        if (stock != null && stock.getQuantity() > 0) {
            stock.setQuantity(stock.getQuantity() - 1);
            stock.setLastUpdate(LocalDate.now());
            stockRepository.save(stock);
        }

        long days = ChronoUnit.DAYS.between(startDate, dueDate);
        if (days <= 0) days = 1;

        Rental rental = new Rental();
        rental.setBook(book);
        rental.setStartDate(startDate);
        rental.setDueDate(dueDate);
        rental.setStatus(RentalStatus.ACTIVE);
        rental.setDailyRentalPrice(dailyPrice);
        rental.setCurrency(currency);
        rental.setPaid(false);
        rental.setTotalRentalPrice(dailyPrice.multiply(BigDecimal.valueOf(days)));

        return rentalRepository.save(rental);
    }

    @Override
    public Rental payRental(Long rentalId) {

        Rental rental = rentalRepository.findById(rentalId).orElseThrow();

        // â”€â”€ VÃ©rification rÃ©duction â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        User user = rental.getUser();

        if (user != null &&
                user.getUserId() != null &&
                discountService.isEligibleForDiscount(user.getUserId()))  {

            BigDecimal discounted = rental.getTotalRentalPrice()
                    .multiply(BigDecimal.valueOf(1 - discountService.getDiscountRate()))
                    .setScale(3, java.math.RoundingMode.HALF_UP);
            rental.setTotalRentalPrice(discounted);
            rental.setDiscountApplied(true);
        }

        rental.setPaid(true);
        rental.setPaymentDate(LocalDate.now());
        return rentalRepository.save(rental);
    }

    @Override
    public Rental returnRental(Long rentalId, LocalDate returnDate) {

        Rental rental = rentalRepository.findById(rentalId).orElseThrow();

        if (rental.getStatus() != RentalStatus.ACTIVE &&
                rental.getStatus() != RentalStatus.LATE)
            throw new RuntimeException("Rental not returnable");

        rental.setReturnDate(returnDate);

        Stock stock = rental.getBook().getStock();
        if (stock != null) {
            stock.setQuantity(stock.getQuantity() + 1);
            stock.setLastUpdate(LocalDate.now());
            stockRepository.save(stock);
        }

        rental.setStatus(
                returnDate.isAfter(rental.getDueDate())
                        ? RentalStatus.LATE
                        : RentalStatus.RETURNED
        );

        return rentalRepository.save(rental);
    }

    @Override
    public BigDecimal getDailyPriceIn(Long rentalId, Currency targetCurrency) {
        Rental rental = rentalRepository.findById(rentalId).orElseThrow();
        return rental.getCurrency().convert(rental.getDailyRentalPrice(), targetCurrency);
    }

    @Override
    public BigDecimal getTotalPriceIn(Long rentalId, Currency targetCurrency) {
        Rental rental = rentalRepository.findById(rentalId).orElseThrow();
        return rental.getCurrency().convert(rental.getTotalRentalPrice(), targetCurrency);
    }

    @Override
    public Rental getById(Long id) {
        return rentalRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Rental> getAll() {
        return rentalRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        rentalRepository.deleteById(id);
    }
}
