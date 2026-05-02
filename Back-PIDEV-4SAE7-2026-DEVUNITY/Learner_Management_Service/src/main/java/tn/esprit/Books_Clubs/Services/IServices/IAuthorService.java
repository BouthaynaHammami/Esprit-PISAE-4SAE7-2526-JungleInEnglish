package tn.esprit.Books_Clubs.Services.IServices;


import tn.esprit.Books_Clubs.entities.*;

import java.util.List;

public interface IAuthorService {
    Author addAuthor(Author author);
    Author updateAuthor(Long id, Author author);
    Author getAuthor(Long id);
    List<Author> getAllAuthors();
    void deleteAuthor(Long id);

    List<Book> getBooksByAuthor(Long authorId);
}


