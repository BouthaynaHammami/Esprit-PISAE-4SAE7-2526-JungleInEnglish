package tn.esprit.Books_Clubs.Services.ImplServices;

import org.springframework.stereotype.Service;
import tn.esprit.jungleinenglishuser.Services.IServices.*;
import tn.esprit.jungleinenglishuser.entities.*;
import tn.esprit.jungleinenglishuser.repositories.*;

import java.util.List;

@Service
public class AuthorServiceImpl implements IAuthorService {

    private final AuthorRepository repo;
    private final BookRepository bookRepo;

    public AuthorServiceImpl(AuthorRepository repo, BookRepository bookRepo){
        this.repo = repo;
        this.bookRepo = bookRepo;
    }

    public Author addAuthor(Author a){ return repo.save(a);}
    public List<Author> getAllAuthors(){ return repo.findAll();}
    public Author getAuthor(Long id){ return repo.findById(id).orElseThrow();}
    public Author updateAuthor(Long id, Author a){
        Author ex=getAuthor(id);
        ex.setName(a.getName());
        return repo.save(ex);
    }
    public void deleteAuthor(Long id){ repo.deleteById(id);}
    public List<Book> getBooksByAuthor(Long id){ return bookRepo.findByAuthor_AuthorId(id);}
}
