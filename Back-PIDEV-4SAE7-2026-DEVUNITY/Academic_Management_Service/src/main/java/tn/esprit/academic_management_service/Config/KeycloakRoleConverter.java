package tn.esprit.academic_management_service.Config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Convertisseur de rôles Keycloak vers Spring Security GrantedAuthorities.
 * 
 * Structure du JWT Keycloak:
 * {
 *   "realm_access": {
 *     "roles": ["STUDENT", "ADMIN", "TUTOR"]
 *   },
 *   "resource_access": {
 *     "devunity-app": {
 *       "roles": ["app-user"]
 *     }
 *   }
 * }
 * 
 * Ce converter extrait les rôles du realm et les préfixe avec "ROLE_"
 * pour que .hasRole("STUDENT") fonctionne correctement dans SecurityConfig.
 */
public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        // Extraire les rôles du realm
        Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
        
        if (realmAccess == null || !realmAccess.containsKey("roles")) {
            return Collections.emptyList();
        }

        @SuppressWarnings("unchecked")
        List<String> roles = (List<String>) realmAccess.get("roles");

        // Convertir en GrantedAuthority avec le préfixe ROLE_
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }
}
