package tn.esprit.Books_Clubs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.Books_Clubs.entities.Club;

public interface ClubRepository extends JpaRepository<Club, Long> { }
