package tn.esprit.language_courses_service.Security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    /*private SecurityUtils() {}

    public static Long getCurrentUserId() {

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (auth == null || auth.getName() == null) {
            throw new RuntimeException("User not authenticated");
        }

        // sub == userId
        return Long.parseLong(auth.getName());
    }*/
}
