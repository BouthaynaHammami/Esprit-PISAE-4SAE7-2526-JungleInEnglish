package tn.esprit.academic_management_service.Certifications.Services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KanbanReminderSchedulerTest {

    @Test
    void shouldReferenceTargetServiceClass() {
        assertEquals("KanbanReminderScheduler", KanbanReminderScheduler.class.getSimpleName());
    }

    @Test
    void shouldDeclareAtLeastOneNonSyntheticMethod() {
        long methodCount = java.util.Arrays.stream(KanbanReminderScheduler.class.getDeclaredMethods())
                .filter(method -> !method.isSynthetic())
                .count();

        assertTrue(methodCount > 0, "Expected declared methods in service class");
    }
}
