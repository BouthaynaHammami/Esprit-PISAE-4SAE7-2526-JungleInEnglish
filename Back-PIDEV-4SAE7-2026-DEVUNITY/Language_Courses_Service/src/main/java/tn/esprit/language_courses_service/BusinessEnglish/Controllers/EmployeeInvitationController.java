package tn.esprit.language_courses_service.BusinessEnglish.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.language_courses_service.BusinessEnglish.Entities.EmployeeInvitation;
import tn.esprit.language_courses_service.BusinessEnglish.Entities.InvitationStatus;
import tn.esprit.language_courses_service.BusinessEnglish.Services.IServices.EmployeeInvitationService;
import tn.esprit.language_courses_service.DTO.ActivationResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/invitations")
@RequiredArgsConstructor
public class EmployeeInvitationController {

    private final EmployeeInvitationService employeeInvitationService;

    // -------------------------
    // CRUD (optionnel)
    // -------------------------

    @PostMapping
    public ResponseEntity<EmployeeInvitation> add(@RequestBody EmployeeInvitation invitation) {
        return ResponseEntity.ok(employeeInvitationService.addEmployeeInvitation(invitation));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeInvitation> update(
            @PathVariable Long id,
            @RequestBody EmployeeInvitation invitation
    ) {
        return ResponseEntity.ok(employeeInvitationService.updateEmployeeInvitation(id, invitation));
    }

    @GetMapping
    public ResponseEntity<List<EmployeeInvitation>> getAll() {
        return ResponseEntity.ok(employeeInvitationService.getAllEmployeeInvitations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeInvitation> getById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeInvitationService.getEmployeeInvitationById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        employeeInvitationService.deleteEmployeeInvitation(id);
        return ResponseEntity.noContent().build();
    }

    // -------------------------
    // ADMIN : update emails
    // -------------------------
    // PUT /invitations/admin/company-offer/10/emails
    @PutMapping("/admin/company-offer/{companyOfferId}/emails")
    public ResponseEntity<List<EmployeeInvitation>> updateEmails(
            @PathVariable Long companyOfferId,
            @RequestBody List<String> emails
    ) {
        return ResponseEntity.ok(
                employeeInvitationService.updateEmailsForCompanyOffer(companyOfferId, emails)
        );
    }

    // -------------------------
    // ADMIN : consulter les invitations PENDING globalement
    // -------------------------
    // GET /invitations/admin/pending
    @GetMapping("/admin/pending")
    public ResponseEntity<List<EmployeeInvitation>> getAllPending() {
        return ResponseEntity.ok(employeeInvitationService.getAllByStatus(InvitationStatus.PENDING));
    }

    // ADMIN : consulter les invitations PENDING d'une demande (companyOfferId)
    // GET /invitations/admin/company-offer/10/pending
    @GetMapping("/admin/company-offer/{companyOfferId}/pending")
    public ResponseEntity<List<EmployeeInvitation>> getPendingByCompanyOffer(@PathVariable Long companyOfferId) {
        return ResponseEntity.ok(
                employeeInvitationService.getInvitationsByCompanyOfferAndStatus(companyOfferId, InvitationStatus.PENDING)
        );
    }

    // -------------------------
    // ADMIN : approve => génération/envoi des codes
    // -------------------------
    // POST /invitations/admin/company-offer/10/approve
    @PostMapping("/admin/company-offer/{companyOfferId}/approve")
    public ResponseEntity<List<EmployeeInvitation>> approve(@PathVariable Long companyOfferId) {
        return ResponseEntity.ok(employeeInvitationService.approveAndSendCodes(companyOfferId));
    }

    // -------------------------
    // ADMIN : reject => status REJECTED
    // -------------------------
    // POST /invitations/admin/company-offer/10/reject
    @PostMapping("/admin/company-offer/{companyOfferId}/reject")
    public ResponseEntity<List<EmployeeInvitation>> reject(@PathVariable Long companyOfferId) {
        return ResponseEntity.ok(employeeInvitationService.rejectCompanyOffer(companyOfferId));
    }

    // -------------------------
    // STUDENT : activation
    // -------------------------
    // POST /invitations/activate?email=a@x.com&code=ABCDEFGH
    @PostMapping("/activate")
    public ResponseEntity<ActivationResponseDTO> activate(
            @RequestParam(required = false) Integer studentId,
            @RequestParam String email,
            @RequestParam String code
    ) {
        return ResponseEntity.ok(
                employeeInvitationService.activate(studentId, email, code)
        );
    }
}