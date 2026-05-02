package tn.esprit.language_courses_service.BusinessEnglish.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.language_courses_service.BusinessEnglish.Entities.Offer;
import tn.esprit.language_courses_service.BusinessEnglish.Entities.Status;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
    List<Offer> findByStatus(Status status);
}
