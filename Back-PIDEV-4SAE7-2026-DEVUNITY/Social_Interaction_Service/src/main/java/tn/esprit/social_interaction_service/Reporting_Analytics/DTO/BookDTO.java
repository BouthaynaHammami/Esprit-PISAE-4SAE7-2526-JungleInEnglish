package tn.esprit.social_interaction_service.Reporting_Analytics.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private Long bookId;
    private String title;
    private String isbn;
    private String status;
    private BigDecimal salePrice;
    private String authorName;
    private String categoryName;
}
