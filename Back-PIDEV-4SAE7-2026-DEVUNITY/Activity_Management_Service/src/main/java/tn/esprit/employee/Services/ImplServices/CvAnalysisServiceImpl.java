package tn.esprit.employee.Services.ImplServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.employee.Dto.CvAnalysisResult;
import tn.esprit.employee.Services.IServices.ICvAnalysisService;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CvAnalysisServiceImpl implements ICvAnalysisService {

    private final RestTemplate restTemplate;

    @Value("${ml.service.url:http://ML-SERVICE}")
    private String mlServiceUrl;

    @Value("${ml.service.endpoint:/api/analyze-cv}")
    private String mlServiceEndpoint;

    @Override
    public CvAnalysisResult analyzeCv(MultipartFile cvFile, String skills, Integer experienceYears) {
        try {
            // Extraction du texte du PDF
            String cvText = extractTextFromPdf(cvFile);
            
            // Préparation de la requête pour le service ML
            Map<String, Object> request = new HashMap<>();
            request.put("cvText", cvText);
            request.put("skills", skills);
            request.put("experienceYears", experienceYears);

            // Appel au microservice ML via Eureka
            String url = mlServiceUrl + mlServiceEndpoint;
            log.info("Appel au service ML: {}", url);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            ResponseEntity<CvAnalysisResult> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    CvAnalysisResult.class
            );

            log.info("Réponse du service ML: {}", response.getBody());
            return response.getBody();

        } catch (Exception e) {
            log.error("Erreur lors de l'analyse du CV", e);
            throw new RuntimeException("Erreur lors de l'analyse du CV: " + e.getMessage(), e);
        }
    }

    @Override
    public String extractTextFromPdf(MultipartFile file) {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            log.info("Texte extrait du PDF ({} caractères)", text.length());
            return text;
        } catch (IOException e) {
            log.error("Erreur lors de l'extraction du texte du PDF", e);
            throw new RuntimeException("Impossible d'extraire le texte du PDF: " + e.getMessage(), e);
        }
    }

    @Override
    public CvAnalysisResult analyzeCvFromText(String cvText, String skills, Integer experienceYears) {
        try {
            log.info("Analyzing CV from text with skills: {}", skills);
            
            // Préparer la requête pour le service ML
            Map<String, Object> request = new HashMap<>();
            request.put("cvText", cvText);
            request.put("skills", skills);
            request.put("experienceYears", experienceYears);

            // Appel au microservice ML via Eureka
            String url = mlServiceUrl + mlServiceEndpoint;
            log.info("Appel au service ML: {}", url);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            ResponseEntity<CvAnalysisResult> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    CvAnalysisResult.class
            );

            log.info("Réponse du service ML: {}", response.getBody());
            return response.getBody();

        } catch (Exception e) {
            log.error("Erreur lors de l'analyse du CV depuis texte", e);
            throw new RuntimeException("Erreur lors de l'analyse du CV: " + e.getMessage(), e);
        }
    }

    @Override
    public CvAnalysisResult analyzeCvFromUrl(String cvUrl, String skills, Integer experienceYears) {
        try {
            log.info("Downloading CV from URL: {}", cvUrl);
            
            // Télécharger le PDF depuis l'URL (Cloudinary)
            String cvText = downloadAndExtractPdfText(cvUrl);
            
            // Préparer la requête pour le service ML
            Map<String, Object> request = new HashMap<>();
            request.put("cvText", cvText);
            request.put("skills", skills);
            request.put("experienceYears", experienceYears);

            // Appel au microservice ML via Eureka
            String url = mlServiceUrl + mlServiceEndpoint;
            log.info("Appel au service ML: {}", url);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            ResponseEntity<CvAnalysisResult> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    CvAnalysisResult.class
            );

            log.info("Réponse du service ML: {}", response.getBody());
            return response.getBody();

        } catch (Exception e) {
            log.error("Erreur lors de l'analyse du CV depuis URL", e);
            throw new RuntimeException("Erreur lors de l'analyse du CV: " + e.getMessage(), e);
        }
    }

    private String downloadAndExtractPdfText(String pdfUrl) {
        try {
            log.info("Downloading PDF from: {}", pdfUrl);
            URL url = new URL(pdfUrl);
            
            try (InputStream inputStream = url.openStream();
                 PDDocument document = PDDocument.load(inputStream)) {
                
                PDFTextStripper stripper = new PDFTextStripper();
                String text = stripper.getText(document);
                log.info("Texte extrait du PDF téléchargé ({} caractères)", text.length());
                return text;
            }
        } catch (IOException e) {
            log.error("Erreur lors du téléchargement ou extraction du PDF", e);
            throw new RuntimeException("Impossible de télécharger ou extraire le texte du PDF: " + e.getMessage(), e);
        }
    }
}
