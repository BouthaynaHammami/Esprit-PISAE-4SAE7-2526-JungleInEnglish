package tn.esprit.learner_managment_service.UserManagement.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Application beans.
 * Auth est maintenant délégué à Keycloak → AuthenticationProvider et
 * AuthenticationManager ne sont plus nécessaires.
 * PasswordEncoder est gardé pour encoder le mot de passe stocké en DB locale.
 */
@Configuration
public class ApplicationConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}