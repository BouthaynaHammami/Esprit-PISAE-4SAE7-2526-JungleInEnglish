package tn.esprit.Books_Clubs.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalId;

    private LocalDate startDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    @Enumerated(EnumType.STRING)
    private RentalStatus status;

    // prix location / jour (≠ salePrice)
    private BigDecimal dailyRentalPrice;

    // calculé selon durée
    private BigDecimal totalRentalPrice;

    @Enumerated(EnumType.STRING)
    private Currency currency;
    // cash à la caisse
    private boolean paid;
    private LocalDate paymentDate;
    @Column(nullable = false)
    private boolean discountApplied = false;

    @ManyToOne
    private Book book;
    @ManyToOne
    private User user;


}