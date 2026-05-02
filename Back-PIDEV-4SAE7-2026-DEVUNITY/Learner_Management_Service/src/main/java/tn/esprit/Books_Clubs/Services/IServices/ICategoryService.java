package tn.esprit.Books_Clubs.Services.IServices;

import tn.esprit.Books_Clubs.entities.*;

import java.util.List;

public interface ICategoryService {
    Category addCategory(Category c);
    List<Category> getAllCategories();
    Category getCategory(Long id);
    Category updateCategory(Long id, Category c);
    void deleteCategory(Long id);
    List<Book> getBooksByCategory(Long id);
}

