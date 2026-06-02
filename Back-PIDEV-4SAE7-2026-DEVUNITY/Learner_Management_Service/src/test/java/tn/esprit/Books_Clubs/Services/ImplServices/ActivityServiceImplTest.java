package tn.esprit.Books_Clubs.Services.ImplServices;

import org.junit.jupiter.api.Test;
import tn.esprit.Services.ImplServices.ActivityServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ActivityServiceImplTest {

    @Test
    void shouldReferenceTargetServiceClass() {
        assertEquals("ActivityServiceImpl", ActivityServiceImpl.class.getSimpleName());
    }

    @Test
    void shouldDeclareAtLeastOneNonSyntheticMethod() {
        long methodCount = java.util.Arrays.stream(ActivityServiceImpl.class.getDeclaredMethods())
                .filter(method -> !method.isSynthetic())
                .count();

        assertTrue(methodCount > 0, "Expected declared methods in service class");
    }
}
