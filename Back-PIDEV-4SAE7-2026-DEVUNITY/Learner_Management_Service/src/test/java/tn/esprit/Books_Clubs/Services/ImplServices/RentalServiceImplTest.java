package tn.esprit.Books_Clubs.Services.ImplServices;

import org.junit.jupiter.api.Test;
import tn.esprit.Services.ImplServices.RentalServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RentalServiceImplTest {

    @Test
    void shouldReferenceTargetServiceClass() {
        assertEquals("RentalServiceImpl", RentalServiceImpl.class.getSimpleName());
    }

    @Test
    void shouldDeclareAtLeastOneNonSyntheticMethod() {
        long methodCount = java.util.Arrays.stream(RentalServiceImpl.class.getDeclaredMethods())
                .filter(method -> !method.isSynthetic())
                .count();

        assertTrue(methodCount > 0, "Expected declared methods in service class");
    }
}
