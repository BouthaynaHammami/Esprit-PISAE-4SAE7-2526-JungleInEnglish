package tn.esprit.learner_managment_service.UserManagement.Exceptions;

/**
 * Exception levée lorsqu'un utilisateur n'est pas trouvé.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
