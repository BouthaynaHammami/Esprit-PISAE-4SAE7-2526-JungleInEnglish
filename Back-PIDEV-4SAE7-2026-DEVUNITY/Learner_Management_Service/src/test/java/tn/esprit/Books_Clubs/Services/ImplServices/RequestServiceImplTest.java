package tn.esprit.Books_Clubs.Services.ImplServices;

import org.junit.jupiter.api.Test;
import tn.esprit.Services.ImplServices.RequestServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RequestServiceImplTest {

    @Test
    void shouldReferenceTargetServiceClass() {
        assertEquals("RequestServiceImpl", RequestServiceImpl.class.getSimpleName());
    }

    @Test
    void shouldDeclareAtLeastOneNonSyntheticMethod() {
        long methodCount = java.util.Arrays.stream(RequestServiceImpl.class.getDeclaredMethods())
                .filter(method -> !method.isSynthetic())
                .count();

        assertTrue(methodCount > 0, "Expected declared methods in service class");
    }
}
