package tn.esprit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.Books_Clubs.entities.Excursion;

import java.util.List;

public interface ExcursionRepository extends JpaRepository<Excursion, Long> {

    List<Excursion> findByClub_ClubId(Long clubId);
}