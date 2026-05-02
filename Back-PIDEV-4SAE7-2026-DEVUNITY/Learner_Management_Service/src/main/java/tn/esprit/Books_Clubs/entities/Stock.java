package tn.esprit.Books_Clubs.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stockId;
// quantité dispo

    private int quantity;

    // NEW
    private int reservedQty=0;

    private LocalDate lastUpdate;

    @JsonIgnore
    @OneToOne
    private Book book;   // owner
}