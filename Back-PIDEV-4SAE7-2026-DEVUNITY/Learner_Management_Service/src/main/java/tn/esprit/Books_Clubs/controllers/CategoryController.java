package tn.esprit.Books_Clubs.controllers;

import org.springframework.web.bind.annotation.*;
import tn.esprit.Services.IServices.ICategoryService;
import tn.esprit.Books_Clubs.entities.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@CrossOrigin(origins = "http://localhost:4200")
public class CategoryController {

    private final ICategoryService service;

    public CategoryController(ICategoryService service) {
        this.service = service;
    }

    @PostMapping
    public Category add(@RequestBody Category c) {
        return service.addCategory(c);
    }

    @GetMapping
    public List<Category> all() {
        return service.getAllCategories();
    }

    @GetMapping("/{id}")
    public Category one(@PathVariable Long id) {
        return service.getCategory(id);
    }

    @PutMapping("/{id}")
    public Category update(@PathVariable Long id,
                           @RequestBody Category c) {
        return service.updateCategory(id, c);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteCategory(id);
    }

    @GetMapping("/{id}/books")
    public List<Book> books(@PathVariable Long id) {
        return service.getBooksByCategory(id);
    }
}
