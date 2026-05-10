package tn.esprit.learner_managment_service.UserManagement.Auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiChatController {

    @Value("${gemini.api-key}")
    private String geminiApiKey;

    private static final String GEMINI_URL =
        "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=";

    private static final String SYSTEM_PROMPT =
        "You are a friendly AI assistant for DevUnity, an online learning platform for developers. " +
        "Your role is to: welcome visitors warmly, answer questions about the platform (courses, features, registration), " +
        "help users navigate the app, and answer general programming or learning questions. " +
        "Keep answers short, helpful, and friendly.";

    /**
     * POST /learners/api/ai/chat
     * Body: { "history": [{"role":"user","content":"..."},{"role":"assistant","content":"..."},...], "message": "..." }
     */
    @PostMapping("/chat")
    public ResponseEntity<Map<String, String>> chat(@RequestBody Map<String, Object> body) {
        String userMessage = (String) body.get("message");
        @SuppressWarnings("unchecked")
        List<Map<String, String>> history = (List<Map<String, String>>) body.getOrDefault("history", List.of());

        // Build Gemini "contents" array
        // Gemini roles: "user" | "model"  (not "assistant")
        List<Map<String, Object>> contents = new ArrayList<>();
        for (Map<String, String> msg : history) {
            String role = "assistant".equals(msg.get("role")) ? "model" : "user";
            contents.add(Map.of(
                "role", role,
                "parts", List.of(Map.of("text", msg.get("content")))
            ));
        }
        // Add current user message
        contents.add(Map.of(
            "role", "user",
            "parts", List.of(Map.of("text", userMessage))
        ));

        // System instruction (Gemini v1beta supports systemInstruction)
        Map<String, Object> systemInstruction = Map.of(
            "parts", List.of(Map.of("text", SYSTEM_PROMPT))
        );

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("systemInstruction", systemInstruction);
        requestBody.put("contents", contents);
        requestBody.put("generationConfig", Map.of(
            "maxOutputTokens", 300,
            "temperature", 0.7
        ));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                GEMINI_URL + geminiApiKey,
                HttpMethod.POST,
                new HttpEntity<>(requestBody, headers),
                Map.class
            );

            Map<?, ?> responseBody = response.getBody();
            if (responseBody == null) {
                return ResponseEntity.status(502).body(Map.of("error", "Empty response from Gemini"));
            }

            List<?> candidates = (List<?>) responseBody.get("candidates");
            Map<?, ?> candidate = (Map<?, ?>) candidates.get(0);
            Map<?, ?> content = (Map<?, ?>) candidate.get("content");
            List<?> parts = (List<?>) content.get("parts");
            String reply = (String) ((Map<?, ?>) parts.get(0)).get("text");

            return ResponseEntity.ok(Map.of("reply", reply));

        } catch (Exception e) {
            return ResponseEntity.status(502).body(Map.of("error", "AI service error: " + e.getMessage()));
        }
    }
}
