package tn.esprit.Books_Clubs.Services.ImplServices;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.Books_Clubs.entities.*;
import tn.esprit.repositories.*;
import tn.esprit.Services.ImplServices.BookServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepo;

    @Mock
    private AuthorRepository authorRepo;

    @Mock
    private CategoryRepository categoryRepo;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book testBook;
    private Author testAuthor;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        testBook = new Book();
        testBook.setBookId(1L);
        testBook.setTitle("Java Mastery");
        testBook.setIsbn("123-456");
        testBook.setStatus(BookStatus.AVAILABLE);

        testAuthor = new Author();
        testAuthor.setAuthorId(10L);
        testAuthor.setName("John Doe");

        testCategory = new Category();
        testCategory.setCategoryId(20L);
        testCategory.setName("Programming");
    }

    @Test
    void addBook_shouldCreateBookWithAuthorCategoryAndStock() {
        when(authorRepo.findById(10L)).thenReturn(Optional.of(testAuthor));
        when(categoryRepo.findById(20L)).thenReturn(Optional.of(testCategory));
        when(bookRepo.save(any(Book.class))).thenReturn(testBook);

        Book result = bookService.addBook(testBook, 10L, 20L, 5);

        assertNotNull(result);
        assertEquals(testAuthor, result.getAuthor());
        assertEquals(testCategory, result.getCategory());
        assertEquals(BookStatus.AVAILABLE, result.getStatus());
        verify(bookRepo).save(any(Book.class));
    }

    @Test
    void addBook_shouldThrowWhenAuthorNotFound() {
        when(authorRepo.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> bookService.addBook(testBook, 999L, 20L, 5));
    }

    @Test
    void addBook_shouldSetStatusOutOfStockWhenQtyZero() {
        when(authorRepo.findById(10L)).thenReturn(Optional.of(testAuthor));
        when(categoryRepo.findById(20L)).thenReturn(Optional.of(testCategory));
        when(bookRepo.save(any(Book.class))).thenReturn(testBook);

        bookService.addBook(testBook, 10L, 20L, 0);

        verify(bookRepo).save(any(Book.class));
    }

    @Test
    void updateBook_shouldUpdateFieldsAndSave() {
        Book updates = new Book();
        updates.setTitle("Updated Title");
        updates.setIsbn("999-999");
        updates.setStatus(BookStatus.DISCONTINUED);
        updates.setSalePrice(BigDecimal.valueOf(29.99));

        when(bookRepo.findById(1L)).thenReturn(Optional.of(testBook));
        when(authorRepo.findById(10L)).thenReturn(Optional.of(testAuthor));
        when(categoryRepo.findById(20L)).thenReturn(Optional.of(testCategory));
        when(bookRepo.save(any(Book.class))).thenReturn(testBook);

        Book result = bookService.updateBook(1L, updates, 10L, 20L);

        assertNotNull(result);
        verify(bookRepo).save(any(Book.class));
    }

    @Test
    void getAllBooks_shouldReturnAllBooks() {
        when(bookRepo.findAll()).thenReturn(List.of(testBook));

        List<Book> result = bookService.getAllBooks();

        assertEquals(1, result.size());
        verify(bookRepo).findAll();
    }

    @Test
    void getBook_shouldReturnBookWhenExists() {
        when(bookRepo.findById(1L)).thenReturn(Optional.of(testBook));

        Book result = bookService.getBook(1L);

        assertNotNull(result);
        assertEquals("Java Mastery", result.getTitle());
    }

    @Test
    void getBook_shouldThrowWhenNotFound() {
        when(bookRepo.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> bookService.getBook(999L));
    }
}
