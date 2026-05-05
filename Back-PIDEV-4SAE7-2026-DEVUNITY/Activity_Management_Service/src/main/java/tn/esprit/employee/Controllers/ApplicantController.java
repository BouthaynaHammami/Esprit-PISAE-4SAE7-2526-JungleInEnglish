package tn.esprit.employee.Controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import tn.esprit.employee.Dto.CvAnalysisResult;
import tn.esprit.employee.Dto.UserDTO;
import tn.esprit.employee.Entities.Applicant;
import tn.esprit.employee.Services.IServices.IApplicantService;
import tn.esprit.employee.Services.IServices.ICvAnalysisService;
import java.util.List;

@RestController
@RequestMapping("/api/applicants")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(
        origins = "http://localhost:4200",
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}
)
public class ApplicantController {

    private final IApplicantService applicantService;
    private final ICvAnalysisService cvAnalysisService;

    @GetMapping
    public ResponseEntity<List<Applicant>> getAll() {
        return ResponseEntity.ok(applicantService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Applicant> getById(@PathVariable Long id) {
        Applicant applicant = applicantService.getById(id);
        return applicant != null
                ? ResponseEntity.ok(applicant)
                : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Applicant> create(@RequestBody Applicant applicant) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(applicantService.create(applicant));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Applicant> update(@PathVariable Long id,
                                            @RequestBody Applicant applicant) {
        return ResponseEntity.ok(applicantService.update(id, applicant));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        applicantService.delete(id);
        return ResponseEntity.ok("Applicant supprimé avec succès.");
    }

    // ─── Feign : User lié à un Applicant ──────────────────────

    @GetMapping("/{id}/user")
    public ResponseEntity<UserDTO> getUserOfApplicant(@PathVariable Long id) {
        UserDTO user = applicantService.getUserOfApplicant(id);
        return user != null
                ? ResponseEntity.ok(user)
                : ResponseEntity.notFound().build();
    }

    // ─── Filtres ───────────────────────────────────────────────

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Applicant>> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(applicantService.getByUserId(userId));
    }

    @GetMapping("/recruitment/{recruitmentId}")
    public ResponseEntity<List<Applicant>> getByRecruitment(@PathVariable Long recruitmentId) {
        return ResponseEntity.ok(applicantService.getByRecruitmentId(recruitmentId));
    }

    @GetMapping("/interview/{interviewId}")
    public ResponseEntity<List<Applicant>> getByInterview(@PathVariable Long interviewId) {
        return ResponseEntity.ok(applicantService.getByInterviewId(interviewId));
    }

    // ─── AI CV Analysis ────────────────────────────────────────

    @PostMapping("/{applicantId}/analyze")
    public ResponseEntity<?> analyzeCV(
            @PathVariable Long applicantId,
            @RequestParam(required = false) Long recruitmentId
    ) {
        try {
            log.info("Analyzing CV for applicant ID: {}, recruitment ID: {}", applicantId, recruitmentId);
            
            CvAnalysisResult result = applicantService.analyzeCvWithML(applicantId, recruitmentId);
            
            log.info("CV analysis completed successfully for applicant {}: {}", applicantId, result.getDecision());
            return ResponseEntity.ok(result);
            
        } catch (IllegalArgumentException e) {
            log.error("Invalid request for CV analysis: {}", e.getMessage());
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        } catch (Exception e) {
            log.error("Error analyzing CV for applicant {}", applicantId, e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to analyze CV: " + e.getMessage());
        }
    }
}
