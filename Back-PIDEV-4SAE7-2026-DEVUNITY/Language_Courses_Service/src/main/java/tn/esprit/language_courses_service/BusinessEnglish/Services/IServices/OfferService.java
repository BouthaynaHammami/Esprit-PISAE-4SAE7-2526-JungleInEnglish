package tn.esprit.language_courses_service.BusinessEnglish.Services.IServices;

import tn.esprit.language_courses_service.BusinessEnglish.Entities.Offer;
import tn.esprit.language_courses_service.DTO.CourseDTO;

import java.util.List;

public interface OfferService {
    Offer addOffer(Offer offer);

    Offer updateOffer(Long id, Offer offer);

    List<Offer> getAllOffers();

    Offer getOfferById(Long id);

    void deleteOffer(Long id);

    List<Offer> getActiveOffers();

    List<CourseDTO> getCoursesByOfferId(Long offerId);
    
    List<Offer> getOffersByStudentId(Long studentId);
}
