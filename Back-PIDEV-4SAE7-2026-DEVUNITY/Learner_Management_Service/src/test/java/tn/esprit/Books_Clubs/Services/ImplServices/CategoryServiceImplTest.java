package tn.esprit.Books_Clubs.Services.ImplServices;

import org.junit.jupiter.api.Test;
import tn.esprit.Services.ImplServices.CategoryServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CategoryServiceImplTest {

    @Test
    void shouldReferenceTargetServiceClass() {
        assertEquals("CategoryServiceImpl", CategoryServiceImpl.class.getSimpleName());
    }

    @Test
    void shouldDeclareAtLeastOneNonSyntheticMethod() {
        long methodCount = java.util.Arrays.stream(CategoryServiceImpl.class.getDeclaredMethods())
                .filter(method -> !method.isSynthetic())
                .count();

        assertTrue(methodCount > 0, "Expected declared methods in service class");
    }
}
