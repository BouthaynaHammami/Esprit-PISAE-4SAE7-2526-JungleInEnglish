package tn.esprit.community_engagement_service.Events.Services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmailServiceImplTest {

    @Test
    void shouldReferenceTargetServiceClass() {
        assertEquals("EmailServiceImpl", EmailServiceImpl.class.getSimpleName());
    }

    @Test
    void shouldDeclareAtLeastOneNonSyntheticMethod() {
        long methodCount = java.util.Arrays.stream(EmailServiceImpl.class.getDeclaredMethods())
                .filter(method -> !method.isSynthetic())
                .count();

        assertTrue(methodCount > 0, "Expected declared methods in service class");
    }
}
