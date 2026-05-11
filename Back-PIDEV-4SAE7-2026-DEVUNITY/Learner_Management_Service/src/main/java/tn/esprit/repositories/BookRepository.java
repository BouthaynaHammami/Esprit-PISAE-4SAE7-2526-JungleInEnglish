package tn.esprit.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.Books_Clubs.entities.Book;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByAuthor_AuthorId(Long authorId);
    List<Book> findByCategory_CategoryId(Long categoryId);
    List<Book> findByTitleContainingIgnoreCase(String title);
}