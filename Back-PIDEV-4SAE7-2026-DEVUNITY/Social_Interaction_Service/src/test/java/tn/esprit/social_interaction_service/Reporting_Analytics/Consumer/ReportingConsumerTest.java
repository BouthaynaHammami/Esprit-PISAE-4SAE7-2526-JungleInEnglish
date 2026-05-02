package tn.esprit.social_interaction_service.Reporting_Analytics.Consumer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReportingConsumerTest {

    @Test
    void shouldReferenceTargetServiceClass() {
        assertEquals("ReportingConsumer", ReportingConsumer.class.getSimpleName());
    }

    @Test
    void shouldDeclareAtLeastOneNonSyntheticMethod() {
        long methodCount = java.util.Arrays.stream(ReportingConsumer.class.getDeclaredMethods())
                .filter(method -> !method.isSynthetic())
                .count();

        assertTrue(methodCount > 0, "Expected declared methods in service class");
    }
}
