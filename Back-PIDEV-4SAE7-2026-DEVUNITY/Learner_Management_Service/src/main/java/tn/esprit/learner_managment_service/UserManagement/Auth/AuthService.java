package tn.esprit.learner_managment_service.UserManagement.Auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.learner_managment_service.UserManagement.Entities.Role;
import tn.esprit.learner_managment_service.UserManagement.Entities.User;
import tn.esprit.learner_managment_service.UserManagement.Exceptions.EmailAlreadyExistsException;
import tn.esprit.learner_managment_service.UserManagement.Exceptions.InvalidCredentialsException;
import tn.esprit.learner_managment_service.UserManagement.Exceptions.KeycloakException;
import tn.esprit.learner_managment_service.UserManagement.Exceptions.UserNotFoundException;
import tn.esprit.learner_managment_service.UserManagement.Repositories.UserRepository;

import java.time.LocalDateTime;

/**
 * Service d'authentification - Version consolidée.
 * 
 * ARCHITECTURE:
 * - Keycloak: Gestion de l'authentification et des mots de passe
 * - Base de données locale: Profils utilisateurs (entité User)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final KeycloakService keycloakService;

    /**
     * Inscription d'un nouvel utilisateur.
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Tentative d'inscription pour l'email: {}", request.getEmail());

        // Validation: ADMIN ne peut pas s'auto-enregistrer
        if (request.getRole() == Role.ADMIN) {
            log.warn("Tentative d'auto-enregistrement avec le rôle ADMIN refusée");
            throw new IllegalArgumentException(
                    "Les comptes ADMIN ne peuvent pas être créés via l'inscription publique.");
        }

        // Validation: email unique
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Tentative d'inscription avec un email déjà existant: {}", request.getEmail());
            throw new EmailAlreadyExistsException("Cet email est déjà utilisé.");
        }

        try {
            // 1. Créer l'utilisateur dans Keycloak
            keycloakService.createKeycloakUser(
                    request.getEmail(),
                    request.getFirstName(),
                    request.getLastName(),
                    request.getPassword(),
                    request.getRole().name());
            log.info("Étape 1/3 réussie : Utilisateur créé dans Keycloak ({})", request.getEmail());

            // 2. Créer le profil local (SANS mot de passe)
            User user = User.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .role(request.getRole())
                    .build();

            user = userRepository.save(user);
            log.info("Étape 2/3 réussie : Profil local créé (ID: {})", user.getUserId());

            // 3. Obtenir le token JWT de Keycloak
            KeycloakService.TokenResult tokens = keycloakService.getUserToken(request.getEmail(), request.getPassword());
            log.info("Étape 3/3 réussie : Token obtenu, inscription complète pour {}", request.getEmail());

            return AuthResponse.builder()
                    .token(tokens.accessToken())
                    .refreshToken(tokens.refreshToken())
                    .role(user.getRole().name())
                    .email(user.getEmail())
                    .userId(user.getUserId())
                    .build();

        } catch (KeycloakException e) {
            log.error("Erreur Keycloak lors de l'inscription {}: {}", request.getEmail(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("ÉCHEC CRITIQUE lors de l'inscription {}: {}", request.getEmail(), e.getMessage());
            throw new RuntimeException("Échec de l'inscription: " + e.getMessage(), e);
        }
    }

    /**
     * Connexion d'un utilisateur existant.
     */
    @Transactional
    public AuthResponse login(LoginRequest request) {
        log.info("Tentative de connexion pour l'email: {}", request.getEmail());

        try {
            // 1. Authentification via Keycloak
            KeycloakService.TokenResult tokens = keycloakService.getUserToken(request.getEmail(), request.getPassword());
            log.info("Authentification Keycloak réussie pour: {}", request.getEmail());

            // 2. Charger le profil depuis la base locale
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> {
                        log.error("Profil local introuvable pour l'email: {}", request.getEmail());
                        return new UserNotFoundException("Profil utilisateur introuvable.");
                    });

            // 3. Mettre à jour la date de dernière connexion
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);

            log.info("Connexion réussie pour userId={}, role={}", user.getUserId(), user.getRole());

            return AuthResponse.builder()
                    .token(tokens.accessToken())
                    .refreshToken(tokens.refreshToken())
                    .role(user.getRole().name())
                    .email(user.getEmail())
                    .userId(user.getUserId())
                    .build();

        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().contains("Identifiants invalides")) {
                log.warn("Échec d'authentification pour l'email: {}", request.getEmail());
                throw new InvalidCredentialsException("Email ou mot de passe incorrect.");
            }
            throw e;
        }
    }

    /**
     * Récupère le profil d'un utilisateur par email.
     */
    public User getUserProfile(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur introuvable: " + email));
    }

    /**
     * Rafraîchit l'access token via le refresh token.
     */
    public AuthResponse refresh(String refreshToken) {
        KeycloakService.TokenResult tokens = keycloakService.refreshToken(refreshToken);
        return AuthResponse.builder()
                .token(tokens.accessToken())
                .refreshToken(tokens.refreshToken())
                .build();
    }
}
