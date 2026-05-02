package tn.esprit.learner_managment_service.UserManagement.Exceptions;

/**
 * Exception levée lorsqu'un email est déjà utilisé lors de l'inscription.
 */
public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
