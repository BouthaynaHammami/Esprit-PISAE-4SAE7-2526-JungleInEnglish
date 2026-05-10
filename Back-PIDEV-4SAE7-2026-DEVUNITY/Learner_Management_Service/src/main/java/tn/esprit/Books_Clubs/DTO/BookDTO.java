package tn.esprit.Books_Clubs.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDTO {
    private Long bookId;
    private String title;
    private String isbn;
    private String status;
    private BigDecimal salePrice;
    private String authorName;
    private String categoryName;
}
