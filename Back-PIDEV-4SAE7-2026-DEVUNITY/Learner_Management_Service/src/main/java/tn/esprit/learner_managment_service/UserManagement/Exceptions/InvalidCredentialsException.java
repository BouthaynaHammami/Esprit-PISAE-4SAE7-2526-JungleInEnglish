package tn.esprit.learner_managment_service.UserManagement.Exceptions;

/**
 * Exception levée lorsque les credentials de connexion sont invalides.
 */
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
