package tn.esprit.learner_managment_service.UserManagement.Exceptions;

/**
 * Exception levée lors d'erreurs d'interaction avec Keycloak.
 */
public class KeycloakException extends RuntimeException {
    public KeycloakException(String message) {
        super(message);
    }

    public KeycloakException(String message, Throwable cause) {
        super(message, cause);
    }
}
