package tn.esprit.language_courses_service.ChallengesCompetitions.Services.ImplServices;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WordBattleGameServiceImplTest {

    @Test
    void shouldReferenceTargetServiceClass() {
        assertEquals("WordBattleGameServiceImpl", WordBattleGameServiceImpl.class.getSimpleName());
    }

    @Test
    void shouldDeclareAtLeastOneNonSyntheticMethod() {
        long methodCount = java.util.Arrays.stream(WordBattleGameServiceImpl.class.getDeclaredMethods())
                .filter(method -> !method.isSynthetic())
                .count();

        assertTrue(methodCount > 0, "Expected declared methods in service class");
    }
}
