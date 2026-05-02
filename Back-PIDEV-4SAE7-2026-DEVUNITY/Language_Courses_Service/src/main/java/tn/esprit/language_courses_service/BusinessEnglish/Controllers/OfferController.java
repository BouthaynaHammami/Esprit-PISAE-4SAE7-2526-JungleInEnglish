package tn.esprit.language_courses_service.BusinessEnglish.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.language_courses_service.BusinessEnglish.Entities.Offer;
import tn.esprit.language_courses_service.BusinessEnglish.Services.IServices.OfferService;
import tn.esprit.language_courses_service.DTO.CourseDTO;

import java.util.List;

@RestController
@RequestMapping("/offers")
@RequiredArgsConstructor
public class OfferController {

    private final OfferService offerService;

    @PostMapping
    public Offer addOffer(@RequestBody Offer offer) {
        return offerService.addOffer(offer);
    }

    @PutMapping("/{id}")
    public Offer updateOffer(@PathVariable Long id, @RequestBody Offer offer) {
        return offerService.updateOffer(id, offer);
    }

    @GetMapping
    public List<Offer> getAllOffers() {
        return offerService.getAllOffers();
    }

    @GetMapping("/{id}")
    public Offer getOfferById(@PathVariable Long id) {
        return offerService.getOfferById(id);
    }

    @GetMapping("/{id}/courses")
    public List<CourseDTO> getCoursesByOfferId(@PathVariable Long id) {
        return offerService.getCoursesByOfferId(id);
    }

    @DeleteMapping("/{id}")
    public void deleteOffer(@PathVariable Long id) {
        offerService.deleteOffer(id);
    }

    @GetMapping("/active")
    public List<Offer> active() {
        return offerService.getActiveOffers();
    }

    @GetMapping("/student/{studentId}")
    public List<Offer> getOffersByStudentId(@PathVariable Long studentId) {
        return offerService.getOffersByStudentId(studentId);
    }
}
