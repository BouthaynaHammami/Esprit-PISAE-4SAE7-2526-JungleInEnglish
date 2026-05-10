package tn.esprit.Books_Clubs.Services.IServices;

import tn.esprit.jungleinenglishuser.entities.*;

import java.math.BigDecimal;
import java.util.List;

public interface IBookService {
    Book addBook(Book book, Long authorId, Long categoryId, int qte);
    Book updateBook(Long id, Book book, Long authorId, Long categoryId);
    List<Book> getAllBooks();
    Book getBook(Long id);
    void deleteBook(Long id);

    // ── Currency ──────────────────────────────────────────────────────────────
    BigDecimal getSalePriceIn(Long bookId, Currency targetCurrency);
}