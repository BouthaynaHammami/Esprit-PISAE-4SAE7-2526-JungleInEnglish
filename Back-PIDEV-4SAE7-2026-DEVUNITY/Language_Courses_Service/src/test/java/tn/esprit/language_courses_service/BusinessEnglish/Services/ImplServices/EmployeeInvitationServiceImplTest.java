package tn.esprit.language_courses_service.BusinessEnglish.Services.ImplServices;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmployeeInvitationServiceImplTest {

    @Test
    void shouldReferenceTargetServiceClass() {
        assertEquals("EmployeeInvitationServiceImpl", EmployeeInvitationServiceImpl.class.getSimpleName());
    }

    @Test
    void shouldDeclareAtLeastOneNonSyntheticMethod() {
        long methodCount = java.util.Arrays.stream(EmployeeInvitationServiceImpl.class.getDeclaredMethods())
                .filter(method -> !method.isSynthetic())
                .count();

        assertTrue(methodCount > 0, "Expected declared methods in service class");
    }
}
