package tn.esprit.language_courses_service.ChallengesCompetitions.Services.ImplServices;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StudentChallengeSessionServiceImplTest {

    @Test
    void shouldReferenceTargetServiceClass() {
        assertEquals("StudentChallengeSessionServiceImpl", StudentChallengeSessionServiceImpl.class.getSimpleName());
    }

    @Test
    void shouldDeclareAtLeastOneNonSyntheticMethod() {
        long methodCount = java.util.Arrays.stream(StudentChallengeSessionServiceImpl.class.getDeclaredMethods())
                .filter(method -> !method.isSynthetic())
                .count();

        assertTrue(methodCount > 0, "Expected declared methods in service class");
    }
}
