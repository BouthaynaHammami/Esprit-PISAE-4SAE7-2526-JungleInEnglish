package tn.esprit.Books_Clubs.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal balance;



    // ✅ relation JPA SANS conflit
    @OneToOne
    private User user;

    // ✅ relation transactions
    @OneToMany(mappedBy = "wallet")
    private List<Transaction> transactions;
}