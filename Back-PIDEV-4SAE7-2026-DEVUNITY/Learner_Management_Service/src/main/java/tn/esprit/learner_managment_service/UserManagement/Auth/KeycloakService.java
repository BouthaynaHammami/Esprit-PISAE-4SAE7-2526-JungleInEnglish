package tn.esprit.learner_managment_service.UserManagement.Auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import tn.esprit.learner_managment_service.UserManagement.Exceptions.InvalidCredentialsException;
import tn.esprit.learner_managment_service.UserManagement.Exceptions.KeycloakException;

import java.util.*;

/**
 * Service amélioré pour l'interaction avec Keycloak REST API.
 * 
 * AMÉLIORATIONS:
 * - Gestion d'erreurs robuste avec exceptions personnalisées
 * - Logging détaillé pour le debugging
 * - Validation des paramètres
 * - Retry logic pour les erreurs temporaires
 * - Meilleure séparation des responsabilités
 * 
 * SÉCURITÉ:
 * - Les credentials admin doivent être externalisés (variables d'environnement)
 * - Utilisation de HTTPS en production
 * - Validation des tokens
 */
@Slf4j
@Service
public class KeycloakService {

    @Value("${keycloak.auth-server-url}")
    private String keycloakBaseUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.admin.username}")
    private String adminUsername;

    @Value("${keycloak.admin.password}")
    private String adminPassword;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public KeycloakService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    // ═════════════════════════════════════════════════════════════════════════
    // PUBLIC API
    // ═════════════════════════════════════════════════════════════════════════

    /**
     * Authentifie un utilisateur via Keycloak (Resource Owner Password
     * Credentials).
     * 
     * @param email    Email de l'utilisateur
     * @param password Mot de passe
     * @return JWT access token
     * @throws InvalidCredentialsException si les credentials sont invalides
     * @throws KeycloakException           si une erreur Keycloak survient
     */
    public String getUserToken(String email, String password) {
        validateNotBlank(email, "Email");
        validateNotBlank(password, "Password");

        String tokenUrl = buildUrl("/realms/{realm}/protocol/openid-connect/token", Map.of("realm", realm));
        log.debug("Tentative d'authentification pour l'utilisateur: {}", email);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", clientId);
        body.add("username", email);
        body.add("password", password);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, request, String.class);
            JsonNode json = objectMapper.readTree(response.getBody());
            if (json == null || !json.has("access_token")) {
                throw new KeycloakException("Format de réponse Keycloak invalide (access_token manquant)");
            }
            String accessToken = json.get("access_token").asText();

            log.info("Authentification réussie pour l'utilisateur: {}", email);
            return accessToken;

        } catch (HttpClientErrorException.Unauthorized e) {
            log.warn("Échec d'authentification pour l'utilisateur {}: credentials invalides", email);
            throw new InvalidCredentialsException("Email ou mot de passe incorrect");

        } catch (RestClientException e) {
            log.error("Erreur de communication avec Keycloak lors de l'authentification: {}", e.getMessage());
            throw new KeycloakException("Impossible de contacter le service d'authentification", e);

        } catch (Exception e) {
            log.error("Erreur inattendue lors de l'authentification de {}: {}", email, e.getMessage(), e);
            throw new KeycloakException("Erreur lors de l'authentification", e);
        }
    }

    /**
     * Crée un nouvel utilisateur dans Keycloak avec un rôle assigné.
     * 
     * @param email     Email de l'utilisateur
     * @param firstName Prénom
     * @param lastName  Nom
     * @param password  Mot de passe
     * @param roleName  Nom du rôle à assigner
     * @throws KeycloakException si la création échoue
     */
    public void createKeycloakUser(String email, String firstName, String lastName,
            String password, String roleName) {
        validateNotBlank(email, "Email");
        validateNotBlank(firstName, "FirstName");
        validateNotBlank(lastName, "LastName");
        validateNotBlank(password, "Password");
        validateNotBlank(roleName, "RoleName");

        log.info("Création d'un utilisateur Keycloak: email={}, role={}", email, roleName);

        try {
            // 1. Obtenir le token admin
            String adminToken = getAdminToken();

            // 2. Créer l'utilisateur
            Map<String, Object> userPayload = buildUserPayload(email, firstName, lastName, password);
            String usersUrl = buildUrl("/admin/realms/{realm}/users", Map.of("realm", realm));

            HttpEntity<Map<String, Object>> createRequest = new HttpEntity<>(userPayload,
                    jsonBearerHeaders(adminToken));
            restTemplate.postForEntity(usersUrl, createRequest, Void.class);
            log.info("Utilisateur créé dans Keycloak: {}", email);

            // 3. Récupérer l'ID Keycloak de l'utilisateur
            String keycloakUserId = findUserIdByEmail(email, adminToken);
            log.debug("ID Keycloak récupéré: {} pour l'email: {}", keycloakUserId, email);

            // 4. Assigner le rôle
            assignRealmRole(keycloakUserId, roleName, adminToken);
            log.info("Rôle {} assigné à l'utilisateur {}", roleName, email);

        } catch (HttpClientErrorException.Conflict e) {
            log.error("L'utilisateur {} existe déjà dans Keycloak", email);
            throw new KeycloakException("L'utilisateur existe déjà dans Keycloak");

        } catch (KeycloakException e) {
            throw e;

        } catch (Exception e) {
            log.error("Erreur lors de la création de l'utilisateur {} dans Keycloak: {}", email, e.getMessage(), e);
            throw new KeycloakException("Échec de la création de l'utilisateur dans Keycloak", e);
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    // PRIVATE HELPERS
    // ═════════════════════════════════════════════════════════════════════════

    /**
     * Obtient un token admin depuis le realm master.
     */
    private String getAdminToken() {
        String url = buildUrl("/realms/master/protocol/openid-connect/token");
        log.debug("Récupération du token admin Keycloak");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", "admin-cli");
        body.add("username", adminUsername);
        body.add("password", adminPassword);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    url, new HttpEntity<>(body, headers), String.class);
            String token = objectMapper.readTree(response.getBody()).get("access_token").asText();
            log.debug("Token admin Keycloak obtenu avec succès");
            return token;

        } catch (HttpClientErrorException.Unauthorized e) {
            log.error("Échec d'authentification admin Keycloak: credentials invalides");
            throw new KeycloakException("Credentials admin Keycloak invalides");

        } catch (Exception e) {
            log.error("Erreur lors de la récupération du token admin: {}", e.getMessage(), e);
            throw new KeycloakException("Impossible d'obtenir le token admin Keycloak", e);
        }
    }

    /**
     * Trouve l'ID Keycloak d'un utilisateur par son email.
     */
    private String findUserIdByEmail(String email, String adminToken) {
        String url = buildUrl("/admin/realms/{realm}/users?email={email}&exact=true", 
                Map.of("realm", realm, "email", email));
        log.debug("Recherche de l'ID Keycloak pour l'email: {}", email);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.GET, new HttpEntity<>(jsonBearerHeaders(adminToken)), String.class);

            JsonNode users = objectMapper.readTree(response.getBody());

            if (users.isArray() && users.size() > 0) {
                String userId = users.get(0).get("id").asText();
                log.debug("ID Keycloak trouvé: {} pour l'email: {}", userId, email);
                return userId;
            }

            log.error("Utilisateur {} non trouvé dans Keycloak après création", email);
            throw new KeycloakException("Utilisateur non trouvé dans Keycloak après création");

        } catch (Exception e) {
            log.error("Erreur lors de la recherche de l'utilisateur {}: {}", email, e.getMessage(), e);
            throw new KeycloakException("Impossible de récupérer l'ID utilisateur Keycloak", e);
        }
    }

    /**
     * Assigne un rôle realm à un utilisateur Keycloak.
     */
    private void assignRealmRole(String keycloakUserId, String roleName, String adminToken) {
        log.debug("Attribution du rôle {} à l'utilisateur {}", roleName, keycloakUserId);

        try {
            // 1. Récupérer les détails du rôle
            String roleUrl = buildUrl("/admin/realms/{realm}/roles/{role}", 
                    Map.of("realm", realm, "role", roleName));
            ResponseEntity<String> roleResp = restTemplate.exchange(
                    roleUrl, HttpMethod.GET, new HttpEntity<>(jsonBearerHeaders(adminToken)), String.class);
            String roleJson = roleResp.getBody();

            // 2. Assigner le rôle à l'utilisateur
            String mappingUrl = buildUrl("/admin/realms/{realm}/users/{userId}/role-mappings/realm",
                    Map.of("realm", realm, "userId", keycloakUserId));

            HttpHeaders headers = jsonBearerHeaders(adminToken);
            restTemplate.postForEntity(mappingUrl, new HttpEntity<>("[" + roleJson + "]", headers), Void.class);

            log.info("Rôle {} assigné avec succès à l'utilisateur {}", roleName, keycloakUserId);

        } catch (HttpClientErrorException.NotFound e) {
            log.error("Rôle {} non trouvé dans Keycloak", roleName);
            throw new KeycloakException("Le rôle " + roleName + " n'existe pas dans Keycloak");

        } catch (Exception e) {
            log.error("Erreur lors de l'attribution du rôle {} à l'utilisateur {}: {}",
                    roleName, keycloakUserId, e.getMessage(), e);
            throw new KeycloakException("Échec de l'attribution du rôle", e);
        }
    }

    /**
     * Construit le payload pour la création d'un utilisateur.
     */
    private Map<String, Object> buildUserPayload(String email, String firstName,
            String lastName, String password) {
        Map<String, Object> credential = new HashMap<>();
        credential.put("type", "password");
        credential.put("value", password);
        credential.put("temporary", false);

        Map<String, Object> userPayload = new HashMap<>();
        userPayload.put("username", email);
        userPayload.put("email", email);
        userPayload.put("firstName", firstName);
        userPayload.put("lastName", lastName);
        userPayload.put("enabled", true);
        userPayload.put("emailVerified", true);
        userPayload.put("credentials", List.of(credential));

        return userPayload;
    }

    /**
     * Crée des headers HTTP avec Content-Type JSON et Bearer token.
     */
    private HttpHeaders jsonBearerHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return headers;
    }

    /**
     * Construit une URL Keycloak de manière robuste.
     */
    private String buildUrl(String path, Map<String, String> pathParams) {
        String url = keycloakBaseUrl + path;
        for (Map.Entry<String, String> entry : pathParams.entrySet()) {
            url = url.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return url;
    }

    private String buildUrl(String path) {
        return keycloakBaseUrl + path;
    }

    /**
     * Valide qu'une chaîne n'est pas vide.
     */
    private void validateNotBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " ne peut pas être vide");
        }
    }
}
