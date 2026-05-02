package tn.esprit.Books_Clubs.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.learner_managment_service.UserManagement.Entities.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    private String type; // CREDIT / DEBIT

    private LocalDateTime date;

    // ✅ relation directe avec User (remplace userId)
    @ManyToOne
    @JsonIgnore
    private User user;

    // ✅ relation avec Wallet
    @ManyToOne
    @JsonIgnore
    private Wallet wallet;
}