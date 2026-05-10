package tn.esprit.Books_Clubs.Services.ImplServices;

import org.springframework.stereotype.Service;
import tn.esprit.jungleinenglishuser.Services.IServices.IBookService;
import tn.esprit.jungleinenglishuser.entities.*;
import tn.esprit.jungleinenglishuser.repositories.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class BookServiceImpl implements IBookService {

    private final BookRepository bookRepo;
    private final AuthorRepository authorRepo;
    private final CategoryRepository categoryRepo;

    public BookServiceImpl(BookRepository bookRepo,
                           AuthorRepository authorRepo,
                           CategoryRepository categoryRepo) {
        this.bookRepo = bookRepo;
        this.authorRepo = authorRepo;
        this.categoryRepo = categoryRepo;
    }

    // ── CREATE ────────────────────────────────────────────────────────────────
    @Override
    public Book addBook(Book book, Long authorId, Long categoryId, int qte) {

        Author a = authorRepo.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Author not found: " + authorId));

        Category c = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found: " + categoryId));

        book.setAuthor(a);
        book.setCategory(c);

        // Create and link stock
        Stock s = new Stock();
        s.setQuantity(qte);
        s.setLastUpdate(LocalDate.now());
        s.setBook(book);
        book.setStock(s);

        book.setStatus(qte > 0 ? BookStatus.AVAILABLE : BookStatus.OUT_OF_STOCK);

        return bookRepo.save(book);
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────
    @Override
    public Book updateBook(Long id, Book updated, Long authorId, Long categoryId) {

        Book existing = bookRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found: " + id));

        Author a = authorRepo.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Author not found: " + authorId));

        Category c = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found: " + categoryId));

        existing.setTitle(updated.getTitle());
        existing.setIsbn(updated.getIsbn());
        existing.setStatus(updated.getStatus());
        existing.setSalePrice(updated.getSalePrice());
        existing.setImage(updated.getImage());
        existing.setCurrency(updated.getCurrency());
        existing.setPublicationYear(updated.getPublicationYear());
        existing.setAuthor(a);
        existing.setCategory(c);

        return bookRepo.save(existing);
    }

    // ── READ ──────────────────────────────────────────────────────────────────
    @Override
    public List<Book> getAllBooks() {
        return bookRepo.findAll();
    }

    @Override
    public Book getBook(Long id) {
        return bookRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found: " + id));
    }

    /**
     * Retourne le prix de vente d'un livre converti dans la devise demandée.
     *
     * Exemple : book stocké en TND (120 TND)
     *   getSalePriceIn(1L, Currency.EUR) → 35.503 EUR
     */
    @Override
    public BigDecimal getSalePriceIn(Long bookId, Currency targetCurrency) {
        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found: " + bookId));

        return book.getCurrency().convert(book.getSalePrice(), targetCurrency);
    }

    // ── DELETE ────────────────────────────────────────────────────────────────
    @Override
    public void deleteBook(Long id) {
        bookRepo.deleteById(id);
    }
}