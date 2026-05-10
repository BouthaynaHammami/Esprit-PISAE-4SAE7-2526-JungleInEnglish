package tn.esprit.Books_Clubs.Services.ImplServices;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.jungleinenglishuser.Services.IServices.IRentalService;
import tn.esprit.jungleinenglishuser.Services.ImplServices.DiscountService;
import tn.esprit.jungleinenglishuser.entities.*;
import tn.esprit.jungleinenglishuser.repositories.BookRepository;
import tn.esprit.jungleinenglishuser.repositories.RentalRepository;
import tn.esprit.jungleinenglishuser.repositories.StockRepository;
import tn.esprit.jungleinenglishuser.repositories.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements IRentalService {

    private final RentalRepository rentalRepository;
    private final BookRepository bookRepository;
    private final StockRepository stockRepository;
    private final DiscountService discountService;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Rental createRental(Long userId,
                               Long bookId,
                               LocalDate startDate,
                               LocalDate dueDate,
                               BigDecimal dailyPrice,
                               Currency currency) {

        User user = userRepository.findById(userId.intValue())
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found: " + bookId));

        Stock stock = book.getStock();
        if (stock == null) {
            throw new RuntimeException("No stock for book");
        }

        if (stock.getQuantity() <= 0) {
            throw new RuntimeException("Book out of stock");
        }

        stock.setQuantity(stock.getQuantity() - 1);
        stock.setLastUpdate(LocalDate.now());
        stockRepository.save(stock);

        if (stock.getQuantity() <= 0) {
            book.setStatus(BookStatus.OUT_OF_STOCK);
            bookRepository.save(book);
        }

        long days = ChronoUnit.DAYS.between(startDate, dueDate);
        if (days <= 0) {
            days = 1;
        }

        Rental rental = new Rental();
        rental.setUser(user);
        rental.setBook(book);
        rental.setStartDate(startDate);
        rental.setDueDate(dueDate);
        rental.setStatus(RentalStatus.ACTIVE);
        rental.setDailyRentalPrice(dailyPrice);
        rental.setCurrency(currency);
        rental.setDiscountApplied(false);

        // IMPORTANT :
        // On crée la location NON payée.
        rental.setPaid(false);
        rental.setPaymentDate(null);

        rental.setTotalRentalPrice(
                dailyPrice.multiply(BigDecimal.valueOf(days)).setScale(3, java.math.RoundingMode.HALF_UP)
        );

        return rentalRepository.save(rental);
    }

    @Override
    @Transactional
    public Rental payRental(Long rentalId) {

        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RuntimeException("Rental not found: " + rentalId));

        if (rental.isPaid()) {
            throw new RuntimeException("Rental already paid");
        }

        User user = rental.getUser();

        if (user != null
                && user.getUserId() != null
                && discountService.isEligibleForDiscount(user.getUserId())) {

            BigDecimal factor = BigDecimal.valueOf(1 - discountService.getDiscountRate());

            BigDecimal discounted = rental.getTotalRentalPrice()
                    .multiply(factor)
                    .setScale(3, java.math.RoundingMode.HALF_UP);

            rental.setTotalRentalPrice(discounted);
            rental.setDiscountApplied(true);
        }

        rental.setPaid(true);
        rental.setPaymentDate(LocalDate.now());

        return rentalRepository.save(rental);
    }

    @Override
    @Transactional
    public Rental returnRental(Long rentalId, LocalDate returnDate) {

        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RuntimeException("Rental not found: " + rentalId));

        if (rental.getStatus() != RentalStatus.ACTIVE &&
                rental.getStatus() != RentalStatus.LATE) {
            throw new RuntimeException("Rental not returnable");
        }

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
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RuntimeException("Rental not found: " + rentalId));
        return rental.getCurrency().convert(rental.getDailyRentalPrice(), targetCurrency);
    }

    @Override
    public BigDecimal getTotalPriceIn(Long rentalId, Currency targetCurrency) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RuntimeException("Rental not found: " + rentalId));
        return rental.getCurrency().convert(rental.getTotalRentalPrice(), targetCurrency);
    }

    @Override
    public Rental getById(Long id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rental not found: " + id));
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