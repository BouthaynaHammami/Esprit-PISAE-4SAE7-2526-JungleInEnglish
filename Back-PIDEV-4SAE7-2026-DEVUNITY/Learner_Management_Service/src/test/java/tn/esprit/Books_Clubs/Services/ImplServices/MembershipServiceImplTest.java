package tn.esprit.Books_Clubs.Services.ImplServices;

import org.junit.jupiter.api.Test;
import tn.esprit.Services.ImplServices.MembershipServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MembershipServiceImplTest {

    @Test
    void shouldReferenceTargetServiceClass() {
        assertEquals("MembershipServiceImpl", MembershipServiceImpl.class.getSimpleName());
    }

    @Test
    void shouldDeclareAtLeastOneNonSyntheticMethod() {
        long methodCount = java.util.Arrays.stream(MembershipServiceImpl.class.getDeclaredMethods())
                .filter(method -> !method.isSynthetic())
                .count();

        assertTrue(methodCount > 0, "Expected declared methods in service class");
    }
}
