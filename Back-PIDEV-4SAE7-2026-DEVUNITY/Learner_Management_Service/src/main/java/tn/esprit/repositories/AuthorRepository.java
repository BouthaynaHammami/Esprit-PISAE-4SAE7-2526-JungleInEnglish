package tn.esprit.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.Books_Clubs.entities.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {}
