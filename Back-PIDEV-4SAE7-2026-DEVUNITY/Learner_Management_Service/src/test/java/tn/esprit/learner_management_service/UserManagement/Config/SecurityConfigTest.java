package tn.esprit.learner_managment_service.UserManagement.Config;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.web.cors.CorsConfigurationSource;
import tn.esprit.learner_managment_service.UserManagement.Config.SecurityConfig;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class SecurityConfigTest {

    private final SecurityConfig securityConfig = new SecurityConfig();

    @Test
    void jwtAuthenticationConverterShouldNotBeNull() {
        JwtAuthenticationConverter converter = securityConfig.jwtAuthenticationConverter();
        assertNotNull(converter);
    }

    @Test
    void corsConfigurationSourceShouldNotBeNull() {
        CorsConfigurationSource source = securityConfig.corsConfigurationSource();
        assertNotNull(source);
    }
}