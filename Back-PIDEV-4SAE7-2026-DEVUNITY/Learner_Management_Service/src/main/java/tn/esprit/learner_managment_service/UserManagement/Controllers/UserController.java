package tn.esprit.learner_managment_service.UserManagement.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.learner_managment_service.UserManagement.Entities.User;
import tn.esprit.learner_managment_service.UserManagement.Repositories.UserRepository;

import java.util.List;

/**
 * Internal-facing controller exposing user data for inter-service Feign calls.
 * Secured routes are opened in SecurityConfig: /users/** is permitAll().
 */
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable("email") String email) {

        return userRepository.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Integer id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Integer id,
                                           @RequestBody User payload) {
        return userRepository.findById(id)
                .map(existing -> {
                    existing.setFirstName(payload.getFirstName());
                    existing.setLastName(payload.getLastName());
                    existing.setEmail(payload.getEmail());
                    // Le mot de passe n'est PAS mis à jour ici (délégué à Keycloak)
                    existing.setCv(payload.getCv());
                    existing.setRole(payload.getRole());
                    existing.setClassId(payload.getClassId());
                    User updated = userRepository.save(existing);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
