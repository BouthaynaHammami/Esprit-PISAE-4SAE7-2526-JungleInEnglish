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
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    private String title;
    private String isbn;

    @Enumerated(EnumType.STRING)
    private BookStatus status;

    // prix de vente
    private BigDecimal salePrice;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String image;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(nullable = false)
    private LocalDate publicationYear;

    @OneToOne(mappedBy = "book", cascade = CascadeType.ALL)
    private Stock stock;

    @ManyToOne
    private Author author;

    @ManyToOne
    private Category category;
}