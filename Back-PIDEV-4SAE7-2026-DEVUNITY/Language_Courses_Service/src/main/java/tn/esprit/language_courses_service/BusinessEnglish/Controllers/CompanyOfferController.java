package tn.esprit.language_courses_service.BusinessEnglish.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.language_courses_service.BusinessEnglish.Entities.CompanyOffer;
import tn.esprit.language_courses_service.BusinessEnglish.Entities.EmployeeInvitation;
import tn.esprit.language_courses_service.BusinessEnglish.Entities.PaymentStatus;
import tn.esprit.language_courses_service.BusinessEnglish.Services.IServices.CompanyOfferService;
import tn.esprit.language_courses_service.DTO.CompanyOfferDTO;
import tn.esprit.language_courses_service.DTO.CompanyOfferRequestDTO;

import java.util.List;

@RestController
@RequestMapping("/company-offers")
@RequiredArgsConstructor
public class CompanyOfferController {

    private final CompanyOfferService companyOfferService;

    // -------------------------
    // CRUD CompanyOffer
    // -------------------------

    @PostMapping
    public ResponseEntity<CompanyOffer> add(@RequestBody CompanyOffer companyOffer) {
        return new ResponseEntity<>(companyOfferService.addCompanyOffer(companyOffer), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyOffer> update(@PathVariable("id") Long id, @RequestBody CompanyOffer companyOffer) {
        return ResponseEntity.ok(companyOfferService.updateCompanyOffer(id, companyOffer));
    }

    @GetMapping
    public ResponseEntity<List<CompanyOffer>> getAll() {
        return ResponseEntity.ok(companyOfferService.getAllCompanyOffers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyOffer> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(companyOfferService.getCompanyOfferById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        companyOfferService.deleteCompanyOffer(id);
        return ResponseEntity.noContent().build();
    }

    // -------------------------
    // ADMIN : update payment status
    // PUT /company-offers/admin/{id}/payment?status=PAID
    // -------------------------
    @PutMapping("/admin/{id}/payment")
    public ResponseEntity<CompanyOffer> updatePaymentStatus(
            @PathVariable("id") Long id,
            @RequestParam("status") PaymentStatus status
    ) {
        return ResponseEntity.ok(companyOfferService.updatePaymentStatus(id, status));
    }

    // -------------------------
    // COMPANY : Demander une offre + liste d'étudiants (emails)
    // -------------------------
    // Exemple:
    // POST /company-offers/company/12/offer/5/request
    // Body: ["a@x.com","b@x.com"]
    @PostMapping("/company/{companyId}/offer/{offerId}/request")
    public ResponseEntity<List<EmployeeInvitation>> requestOffer(
            @PathVariable("companyId") Long companyId,
            @PathVariable("offerId") Long offerId,
            @RequestBody List<String> emails
    ) {
        return ResponseEntity.ok(companyOfferService.requestOffer(companyId, offerId, emails));
    }

    // -------------------------
    // ADMIN : Voir toutes les demandes (avec détails company/students + invitations)
    // -------------------------
    // GET /company-offers/admin/requests
    @GetMapping("/admin/requests")
    public ResponseEntity<List<CompanyOfferRequestDTO>> getAllRequestsForAdmin() {
        return ResponseEntity.ok(companyOfferService.getAllRequestsForAdmin());
    }

    // -------------------------
    // STUDENT : Get enrolled offers
    // -------------------------
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<CompanyOfferDTO>> getStudentOffers(@PathVariable("studentId") Integer studentId) {
        return ResponseEntity.ok(companyOfferService.getStudentOffers(studentId));
    }
}