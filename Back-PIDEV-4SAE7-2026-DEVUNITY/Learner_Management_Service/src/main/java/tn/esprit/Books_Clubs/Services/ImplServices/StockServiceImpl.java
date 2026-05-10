package tn.esprit.Books_Clubs.Services.ImplServices;

import org.springframework.stereotype.Service;
import tn.esprit.jungleinenglishuser.Services.IServices.IStockService;
import tn.esprit.jungleinenglishuser.entities.Book;
import tn.esprit.jungleinenglishuser.entities.BookStatus;
import tn.esprit.jungleinenglishuser.entities.Stock;
import tn.esprit.jungleinenglishuser.repositories.BookRepository;
import tn.esprit.jungleinenglishuser.repositories.StockRepository;

import java.time.LocalDate;

@Service
public class StockServiceImpl implements IStockService {

    private final StockRepository stockRepo;
    private final BookRepository bookRepo;

    public StockServiceImpl(StockRepository stockRepo, BookRepository bookRepo) {
        this.stockRepo = stockRepo;
        this.bookRepo = bookRepo;
    }

    // ✅ récupère le stock ou le crée s'il n'existe pas
    private Stock getOrCreate(Long bookId) {
        return stockRepo.findByBook_BookId(bookId).orElseGet(() -> {
            Book book = bookRepo.findById(bookId)
                    .orElseThrow(() -> new RuntimeException("Book not found: " + bookId));

            Stock s = new Stock();
            s.setBook(book);
            s.setQuantity(0);
            s.setReservedQty(0);
            s.setLastUpdate(LocalDate.now());
            return stockRepo.save(s);
        });
    }

    private void updateBookStatus(Book book, int quantity) {
        if (quantity > 0) book.setStatus(BookStatus.AVAILABLE);
        else book.setStatus(BookStatus.OUT_OF_STOCK);
        bookRepo.save(book);
    }

    @Override
    public Stock getStockByBookId(Long bookId) {
        return getOrCreate(bookId);
    }

    @Override
    public Stock updateStock(Long bookId, int quantity) {
        Stock s = getOrCreate(bookId);

        s.setQuantity(Math.max(quantity, 0));
        s.setLastUpdate(LocalDate.now());

        Stock saved = stockRepo.save(s);
        updateBookStatus(saved.getBook(), saved.getQuantity());
        return saved;
    }

    @Override
    public Stock addQuantity(Long bookId, int qte) {
        Stock s = getOrCreate(bookId);

        int newQte = s.getQuantity() + Math.max(qte, 0);
        s.setQuantity(newQte);
        s.setLastUpdate(LocalDate.now());

        Stock saved = stockRepo.save(s);
        updateBookStatus(saved.getBook(), newQte);
        return saved;
    }

    @Override
    public Stock removeQuantity(Long bookId, int qte) {
        Stock s = getOrCreate(bookId);

        int newQte = s.getQuantity() - Math.max(qte, 0);
        if (newQte < 0) newQte = 0;

        s.setQuantity(newQte);
        s.setLastUpdate(LocalDate.now());

        Stock saved = stockRepo.save(s);
        updateBookStatus(saved.getBook(), newQte);
        return saved;
    }
}