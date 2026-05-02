package tn.esprit.employee.Config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * This filter is intentionally a no-op passthrough.
 *
 * JWT authentication is handled by Spring Security's oauth2ResourceServer
 * configured in SecurityConfig, which correctly validates Keycloak RS256 tokens
 * via the JWKS endpoint. The old HMAC-based parsing here was incompatible with
 * RS256 and caused UnsupportedJwtException on every request.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        // Passthrough — JWT validation is handled by oauth2ResourceServer (Keycloak RS256)
        filterChain.doFilter(request, response);
    }
}
