package tn.esprit.employee.Controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.employee.Dto.CvAnalysisResult;
import tn.esprit.employee.Services.IServices.ICvAnalysisService;

@RestController
@RequestMapping("/api/cv-analysis")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(
        origins = "http://localhost:4200",
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}
)
public class CvAnalysisController {

    private final ICvAnalysisService cvAnalysisService;

    @PostMapping("/analyze")
    public ResponseEntity<?> analyzeCv(
            @RequestParam("cvFile") MultipartFile cvFile,
            @RequestParam("skills") String skills,
            @RequestParam("experienceYears") Integer experienceYears
    ) {
        try {
            log.info("Réception d'une demande d'analyse de CV: skills={}, experienceYears={}", 
                    skills, experienceYears);

            // Validation du fichier
            if (cvFile.isEmpty()) {
                return ResponseEntity
                        .badRequest()
                        .body("Le fichier CV est requis");
            }

            if (!cvFile.getContentType().equals("application/pdf")) {
                return ResponseEntity
                        .badRequest()
                        .body("Seuls les fichiers PDF sont acceptés");
            }

            // Analyse du CV
            CvAnalysisResult result = cvAnalysisService.analyzeCv(cvFile, skills, experienceYears);
            
            log.info("Analyse terminée avec succès: decision={}", result.getDecision());
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error("Erreur lors de l'analyse du CV", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'analyse: " + e.getMessage());
        }
    }
}
