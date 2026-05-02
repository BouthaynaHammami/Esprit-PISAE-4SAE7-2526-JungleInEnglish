package tn.esprit.Books_Clubs.entities;

import java.math.BigDecimal;
import java.math.RoundingMode;

public enum Currency {

    TND(1.0),
    EUR(3.38),   // 1 EUR = 3.38 TND
    USD(3.10);   // 1 USD = 3.10 TND

    private final double rateToTND;

    Currency(double rateToTND) {
        this.rateToTND = rateToTND;
    }

    public BigDecimal getRateToTND() {
        return BigDecimal.valueOf(rateToTND);
    }

    /** Taux de change : 1 unité de (this) = combien en (target) */
    public BigDecimal getRate(Currency target) {
        if (this == target) return BigDecimal.ONE;
        return this.getRateToTND()
                .divide(target.getRateToTND(), 6, RoundingMode.HALF_UP);
    }

    /** Convertir un montant de (this) vers (target) */
    public BigDecimal convert(BigDecimal amount, Currency target) {
        if (amount == null) throw new IllegalArgumentException("Amount must not be null");
        if (this == target) return amount.setScale(3, RoundingMode.HALF_UP);

        // amount (this) → TND → target
        BigDecimal inTND = amount.multiply(this.getRateToTND());
        return inTND.divide(target.getRateToTND(), 3, RoundingMode.HALF_UP);
    }
}