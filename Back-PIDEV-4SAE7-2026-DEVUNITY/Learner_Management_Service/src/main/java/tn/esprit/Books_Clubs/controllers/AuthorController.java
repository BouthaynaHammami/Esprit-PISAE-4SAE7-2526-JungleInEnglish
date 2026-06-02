package tn.esprit.Books_Clubs.controllers;

import org.springframework.web.bind.annotation.*;
import tn.esprit.Services.IServices.IAuthorService;
import tn.esprit.Books_Clubs.entities.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthorController {

    private final IAuthorService service;

    public AuthorController(IAuthorService service) {
        this.service = service;
    }

    @PostMapping
    public Author add(@RequestBody Author a) {
        return service.addAuthor(a);
    }

    @GetMapping
    public List<Author> all() {
        return service.getAllAuthors();
    }

    @GetMapping("/{id}")
    public Author one(@PathVariable Long id) {
        return service.getAuthor(id);
    }

    @PutMapping("/{id}")
    public Author update(@PathVariable Long id,
                         @RequestBody Author a) {
        return service.updateAuthor(id, a);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteAuthor(id);
    }

    @GetMapping("/{id}/books")
    public List<Book> books(@PathVariable Long id) {
        return service.getBooksByAuthor(id);
    }
}
