package tn.esprit.Books_Clubs.Services.ImplServices;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.Books_Clubs.repositories.OrderRepository;
import tn.esprit.Books_Clubs.repositories.RentalRepository;

@Service
@RequiredArgsConstructor
public class DiscountService {

    private final OrderRepository  orderRepository;
    private final RentalRepository rentalRepository;


    private static final double DISCOUNT_RATE    = 0.20;
    private static final int    DISCOUNT_EVERY_N = 3;

    /**
     * Retourne true si la prochaine transaction est la 3Ã¨me (ou 6Ã¨me, 9Ã¨me...)
     * = (nbPaidOrders + nbPaidRentals + 1) % 3 == 0
     */
    public boolean isEligibleForDiscount(Integer userId) {
        long paidOrders  = orderRepository.countByUserUserIdAndPaidTrue(userId);
        long paidRentals = rentalRepository.countByUserUserIdAndPaidTrue(userId);
        long total       = paidOrders + paidRentals;
        return (total + 1) % DISCOUNT_EVERY_N == 0;
    }

    public double getDiscountRate() {
        return DISCOUNT_RATE;
    }
}
