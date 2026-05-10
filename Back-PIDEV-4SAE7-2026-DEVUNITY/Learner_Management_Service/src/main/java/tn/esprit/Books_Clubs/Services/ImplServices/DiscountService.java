package tn.esprit.Books_Clubs.Services.ImplServices;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.jungleinenglishuser.repositories.OrderRepository;
import tn.esprit.jungleinenglishuser.repositories.RentalRepository;

@Service
@RequiredArgsConstructor
public class DiscountService {

    private final OrderRepository orderRepository;
    private final RentalRepository rentalRepository;

    private static final double DISCOUNT_RATE = 0.20;
    private static final int DISCOUNT_EVERY_N = 3;

    public boolean isEligibleForDiscount(Integer userId) {
        long paidOrders = orderRepository.countByUserUserIdAndPaidTrue(userId);
        long paidRentals = rentalRepository.countByUserUserIdAndPaidTrue(userId);
        long totalPaid = paidOrders + paidRentals;

        // La réduction doit s'appliquer sur la transaction en cours
        long currentTransactionNumber = totalPaid + 1;

        return currentTransactionNumber % DISCOUNT_EVERY_N == 0;
    }

    public double getDiscountRate() {
        return DISCOUNT_RATE;
    }
}