package tn.esprit.Books_Clubs.controllers;

import org.springframework.web.bind.annotation.*;
import tn.esprit.Services.IServices.IBookService;
import tn.esprit.Books_Clubs.entities.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@CrossOrigin(origins = "http://localhost:4200")
public class BookController {

    private final IBookService service;

    public BookController(IBookService service) {
        this.service = service;
    }

    // ── CREATE ────────────────────────────────────────────────────────────────

    @PostMapping
    public Book add(@RequestBody Book book,
                    @RequestParam Long authorId,
                    @RequestParam Long categoryId,
                    @RequestParam int qte) {
        return service.addBook(book, authorId, categoryId, qte);
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────

    @PutMapping("/{id}")
    public Book update(@PathVariable Long id,
                       @RequestBody Book book,
                       @RequestParam Long authorId,
                       @RequestParam Long categoryId) {
        return service.updateBook(id, book, authorId, categoryId);
    }

    // ── READ ──────────────────────────────────────────────────────────────────

    @GetMapping
    public List<Book> all() {
        return service.getAllBooks();
    }

    @GetMapping("/{id}")
    public Book one(@PathVariable Long id) {
        return service.getBook(id);
    }

    // ── DELETE ────────────────────────────────────────────────────────────────

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteBook(id);
    }
}