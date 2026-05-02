package tn.esprit.community_engagement_service.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .authorizeHttpRequests(auth -> auth
                        // events API
                        .requestMatchers("/events/**").permitAll()
                        // registrations API
                        .requestMatchers("/registrations/**").permitAll()
                        // kanban API
                        .requestMatchers("/events/kanban/**").permitAll()
                        // WebSocket endpoints
                        .requestMatchers("/ws-reminders/**").permitAll()
                        // uploaded files
                        .requestMatchers("/uploads/**").permitAll()
                        // everything else allowed
                        .anyRequest().permitAll()
                );
        return http.build();
    }
}
