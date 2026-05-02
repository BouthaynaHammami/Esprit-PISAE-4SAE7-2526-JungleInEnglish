package tn.esprit.language_courses_service.BusinessEnglish.Services.ImplServices;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.language_courses_service.BusinessEnglish.Entities.Offer;
import tn.esprit.language_courses_service.BusinessEnglish.Entities.Status;
import tn.esprit.language_courses_service.BusinessEnglish.Repositories.CompanyOfferRepository;
import tn.esprit.language_courses_service.BusinessEnglish.Repositories.OfferRepository;
import tn.esprit.language_courses_service.BusinessEnglish.Services.IServices.OfferService;
import tn.esprit.language_courses_service.Clients.CourseClient;
import tn.esprit.language_courses_service.DTO.CourseDTO;

import java.util.List;

@Service
@AllArgsConstructor
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final CompanyOfferRepository companyOfferRepository;
    private final CourseClient courseClient;

    @Override
    public Offer addOffer(Offer offer) {
        if (offer.getCourseIds() != null) {

            for (Long courseId : offer.getCourseIds()) {

                CourseDTO course = courseClient.getCourseById(courseId);

                if (!"BUSINESS_ENGLISH".equals(course.getType())) {
                    throw new RuntimeException(
                            "Le cours ID " + courseId + " n'est pas Business English"
                    );
                }
            }
        }

        return offerRepository.save(offer);
    }

    @Override
    public Offer updateOffer(Long id, Offer offer) {
        if (offerRepository.existsById(id)) {
            offer.setId(id);
            return offerRepository.save(offer);
        }
        throw new IllegalArgumentException("Offer not found");
    }

    @Override
    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }

    @Override
    public Offer getOfferById(Long id) {
        return offerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Offer not found"));
    }

    @Override
    public void deleteOffer(Long id) {
        if (offerRepository.existsById(id)) {
            offerRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Offer not found");
        }
    }

    @Override
    public List<Offer> getActiveOffers() {
        return offerRepository.findByStatus(Status.ACTIVE);
    }

    @Override
    public List<CourseDTO> getCoursesByOfferId(Long offerId) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new IllegalArgumentException("Offer not found"));
        
        java.util.List<CourseDTO> courses = new java.util.ArrayList<>();
        if (offer.getCourseIds() != null) {
            for (Long courseId : offer.getCourseIds()) {
                try {
                    CourseDTO course = courseClient.getCourseById(courseId);
                    if (course != null) {
                        courses.add(course);
                    }
                } catch (Exception e) {
                    System.err.println("Error fetching course " + courseId + ": " + e.getMessage());
                }
            }
        }
        return courses;
    }

    @Override
    public List<Offer> getOffersByStudentId(Long studentId) {
        return companyOfferRepository.findByStudentsContaining(studentId)
                .stream()
                .map(tn.esprit.language_courses_service.BusinessEnglish.Entities.CompanyOffer::getOffer)
                .toList();
    }
}
