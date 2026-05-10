package tn.esprit.academic_management_service.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;



@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class KeycloakSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // CORS configuration
            .cors(cors -> {})
            
            // Désactiver CSRF pour les API REST
            .csrf(csrf -> csrf.disable())
            
            // Session stateless (JWT)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // Règles d'autorisation
            .authorizeHttpRequests(auth -> auth
                // OPTIONS requests (CORS preflight)
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                
                // Endpoints publics
                .requestMatchers("/events/**").permitAll()
                .requestMatchers("/registrations/**").permitAll()
                .requestMatchers("/uploads/**").permitAll()
                
                // Kanban (ToDo) endpoints — open for task management
                .requestMatchers("/certification/kanban/**").permitAll()
                
                // WebSocket endpoints
                .requestMatchers("/ws-reminders/**").permitAll()
                
                // Swagger/OpenAPI
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                
                // Actuator (monitoring)
                .requestMatchers("/actuator/health", "/actuator/info","/actuator/prometheus").permitAll()
                .requestMatchers("/actuator/**").hasRole("ADMIN")
                
                // Certification endpoints - protégés par rôle
                .requestMatchers("/certification/admin/**").hasRole("ADMIN")
                .requestMatchers("/certification/tutor/**").hasRole("TUTOR")
                .requestMatchers("/certification/student/**").hasRole("STUDENT")
                .requestMatchers("/certification/**").authenticated()
                
                // Courses endpoints - permettre l'accès public en lecture
                .requestMatchers(HttpMethod.GET, "/courses/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/courses/**").permitAll()
                .requestMatchers(HttpMethod.PUT, "/courses/**").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/courses/**").permitAll()
                
                // Enrollment endpoints
                .requestMatchers("/enrollments/**").permitAll()
                
                // Lesson endpoints
                .requestMatchers("/lessons/**").permitAll()
                
                // Quiz endpoints
                .requestMatchers("/quizzes/**").permitAll()
                .requestMatchers("/quiz-attempts/**").permitAll()
                
                // Tout le reste autorisé pour le développement
                .anyRequest().permitAll()
            )
            
            // Configuration OAuth2 Resource Server (Keycloak JWT)
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
            );

        return http.build();
    }

    /**
     * Convertit les rôles Keycloak en GrantedAuthorities Spring Security.
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
        return converter;
    }

    /**
     * Configuration CORS pour permettre les requêtes depuis le frontend.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Origines autorisées (à adapter selon l'environnement)
        configuration.setAllowedOriginPatterns(List.of("http://localhost:*", "http://127.0.0.1:*"));
        
        // Méthodes HTTP autorisées
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        
        // Headers autorisés
        configuration.setAllowedHeaders(List.of("*"));
        
        // Autoriser les credentials (cookies, authorization headers)
        configuration.setAllowCredentials(true);
        
        // Headers exposés au client
        configuration.setExposedHeaders(List.of("Authorization", "Content-Type"));
        
        // Durée de cache de la configuration CORS (1 heure)
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
