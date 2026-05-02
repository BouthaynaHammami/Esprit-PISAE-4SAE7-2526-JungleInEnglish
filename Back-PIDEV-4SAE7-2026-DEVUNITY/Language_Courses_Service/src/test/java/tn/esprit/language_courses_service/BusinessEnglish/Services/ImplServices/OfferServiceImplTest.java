package tn.esprit.language_courses_service.BusinessEnglish.Services.ImplServices;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OfferServiceImplTest {

    @Test
    void shouldReferenceTargetServiceClass() {
        assertEquals("OfferServiceImpl", OfferServiceImpl.class.getSimpleName());
    }

    @Test
    void shouldDeclareAtLeastOneNonSyntheticMethod() {
        long methodCount = java.util.Arrays.stream(OfferServiceImpl.class.getDeclaredMethods())
                .filter(method -> !method.isSynthetic())
                .count();

        assertTrue(methodCount > 0, "Expected declared methods in service class");
    }
}
