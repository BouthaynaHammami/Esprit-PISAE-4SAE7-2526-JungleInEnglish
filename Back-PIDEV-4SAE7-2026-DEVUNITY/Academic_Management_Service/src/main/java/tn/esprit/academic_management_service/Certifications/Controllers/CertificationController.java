package tn.esprit.academic_management_service.Certifications.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.academic_management_service.Certifications.DTO.AnswerDTO;
import tn.esprit.academic_management_service.Certifications.Entities.CertificationQuestion;
import tn.esprit.academic_management_service.Certifications.Entities.TestSession;
import tn.esprit.academic_management_service.Certifications.Services.QuestionService;
import tn.esprit.academic_management_service.Certifications.Services.TestService;
import tn.esprit.academic_management_service.Clients.UserClient;
import tn.esprit.academic_management_service.DTO.UserDTO;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/certification")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class CertificationController {

    private final QuestionService questionService;
    private final TestService testService;
    private final UserClient userClient;

    // =====================================================
    // GET STUDENT ID FROM USER SERVICE
    // =====================================================

    private Long getStudentIdFromEmail(String email) {

        UserDTO user = userClient.getUserByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        return Long.valueOf(user.getUserId());
    }

    // =====================================================
    // ================= STUDENT ===========================
    // =====================================================

    @GetMapping("/student/questions")
    public ResponseEntity<?> getQuestionsForCurrentStudent(
            @RequestParam String email) {

        try {

            Long studentId = getStudentIdFromEmail(email);

            testService.startSession(studentId);

            return ResponseEntity.ok(
                    questionService.getQuestionsForStudent(studentId)
            );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/student/submit")
    public ResponseEntity<?> submitTest(
            @RequestParam String email,
            @RequestBody List<AnswerDTO> answers) {

        try {

            Long studentId = getStudentIdFromEmail(email);

            return ResponseEntity.ok(
                    testService.submitTest(studentId, answers)
            );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/student/certificates")
    public ResponseEntity<?> getMyCertificates(
            @RequestParam String email) {

        try {

            Long studentId = getStudentIdFromEmail(email);

            return ResponseEntity.ok(
                    testService.getCertificatesForStudent(studentId)
            );

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/student/tab-violation")
    public ResponseEntity<?> reportTabViolation(
            @RequestParam String email) {

        try {

            Long studentId = getStudentIdFromEmail(email);

            testService.registerTabViolation(studentId);

            return ResponseEntity.ok("Violation recorded");

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/student/status")
    public ResponseEntity<?> getExamStatus(
            @RequestParam String email) {

        try {

            Long studentId = getStudentIdFromEmail(email);

            boolean blocked = testService.isExamBlocked(studentId);

            return ResponseEntity.ok(blocked);

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    // =====================================================
    // ================= ADMIN =============================
    // =====================================================

    @GetMapping("/questions/all")
    public ResponseEntity<?> getAllQuestions() {
        try {
            return ResponseEntity.ok(
                    questionService.getAllActiveQuestions()
            );
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/questions/add")
    public ResponseEntity<CertificationQuestion> addQuestion(@RequestBody CertificationQuestion q) {

        return ResponseEntity.ok(
                questionService.addQuestion(q)
        );
    }

    @PutMapping("/questions/update/{id}")
    public ResponseEntity<CertificationQuestion> updateQuestion(
            @PathVariable Long id,
            @RequestBody CertificationQuestion q) {

        return ResponseEntity.ok(
                questionService.updateQuestion(id, q)
        );
    }

    @DeleteMapping("/questions/delete/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {

        questionService.deleteQuestion(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sessions/all")
    public ResponseEntity<?> getAllSessions() {
        try {
            return ResponseEntity.ok(
                    testService.getAllSessions()
            );
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/sessions/suspicious")
    public ResponseEntity<List<TestSession>> getSuspiciousSessions() {

        return ResponseEntity.ok(
                testService.getSuspiciousSessions()
        );
    }

    @GetMapping("/certificates/all")
    public ResponseEntity<?> getAllCertificates() {
        try {
            return ResponseEntity.ok(
                    testService.getAllCertificates()
            );
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
}