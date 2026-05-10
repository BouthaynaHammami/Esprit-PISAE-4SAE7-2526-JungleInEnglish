package tn.esprit.Books_Clubs.Services.ImplServices;

import org.springframework.stereotype.Service;
import tn.esprit.jungleinenglishuser.Services.IServices.*;
import tn.esprit.jungleinenglishuser.entities.*;
import tn.esprit.jungleinenglishuser.repositories.*;

import java.util.List;
@Service
public class CategoryServiceImpl implements ICategoryService {

    private final CategoryRepository repo;
    private final BookRepository bookRepo;

    public CategoryServiceImpl(CategoryRepository repo,BookRepository bookRepo){
        this.repo=repo;
        this.bookRepo=bookRepo;
    }

    public Category addCategory(Category c){return repo.save(c);}
    public List<Category> getAllCategories(){return repo.findAll();}
    public Category getCategory(Long id){return repo.findById(id).orElseThrow();}
    public Category updateCategory(Long id,Category c){
        Category ex=getCategory(id);
        ex.setName(c.getName());
        return repo.save(ex);
    }
    public void deleteCategory(Long id){repo.deleteById(id);}
    public List<Book> getBooksByCategory(Long id){return bookRepo.findByCategory_CategoryId(id);}
}
