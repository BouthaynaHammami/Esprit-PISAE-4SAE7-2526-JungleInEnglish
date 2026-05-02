package tn.esprit.employee.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import tn.esprit.employee.Dto.UserDTO;
import tn.esprit.employee.Entities.Interview;
import tn.esprit.employee.Services.IServices.IInterviewService; // ✅ Interface

import java.util.List;

@RestController
@RequestMapping("/api/interviews")
@RequiredArgsConstructor
public class InterviewController {

    private final IInterviewService interviewService; // ✅ Interface

    @GetMapping
    public ResponseEntity<List<Interview>> getAll() {
        return ResponseEntity.ok(interviewService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Interview> getById(@PathVariable Long id) {
        Interview interview = interviewService.getById(id);
        return interview != null
                ? ResponseEntity.ok(interview)
                : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Interview> create(@RequestBody Interview interview) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(interviewService.create(interview));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Interview> update(@PathVariable Long id,
                                            @RequestBody Interview interview) {
        return ResponseEntity.ok(interviewService.update(id, interview));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        interviewService.delete(id);
        return ResponseEntity.ok("Interview supprimé avec succès.");
    }

    // ─── Feign : User lié à un Interview ──────────────────────

    @GetMapping("/{id}/user")
    public ResponseEntity<UserDTO> getUserOfInterview(@PathVariable Long id) {
        UserDTO user = interviewService.getUserOfInterview(id);
        return user != null
                ? ResponseEntity.ok(user)
                : ResponseEntity.notFound().build();
    }

    // ─── Filtres ───────────────────────────────────────────────

    @GetMapping("/recruitment/{recruitmentId}")
    public ResponseEntity<List<Interview>> getByRecruitment(@PathVariable Long recruitmentId) {
        return ResponseEntity.ok(interviewService.getByRecruitmentId(recruitmentId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Interview>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(interviewService.getByUserId(userId));
    }
}
