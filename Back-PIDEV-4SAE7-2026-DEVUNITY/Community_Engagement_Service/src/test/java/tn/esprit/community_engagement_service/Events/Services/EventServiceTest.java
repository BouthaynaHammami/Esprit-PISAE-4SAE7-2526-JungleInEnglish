package tn.esprit.community_engagement_service.Events.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.community_engagement_service.Events.Entities.Events;
import tn.esprit.community_engagement_service.Events.Repositories.EventRepo;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepo eventRepo;

    @InjectMocks
    private EventService eventService;

    private Events testEvent;

    @BeforeEach
    void setUp() {
        testEvent = new Events();
        testEvent.setEventId(1L);
        testEvent.setTitle("Tech Conference");
        testEvent.setDescription("Annual tech conference");
    }

    @Test
    void getAllEvents_shouldReturnAllEvents() {
        when(eventRepo.findAll()).thenReturn(List.of(testEvent));

        List<Events> result = eventService.getAllEvents();

        assertEquals(1, result.size());
        assertEquals("Tech Conference", result.get(0).getTitle());
        verify(eventRepo).findAll();
    }

    @Test
    void getEventById_shouldReturnEventWhenExists() {
        when(eventRepo.findById(1L)).thenReturn(Optional.of(testEvent));

        Events result = eventService.getEventById(1L);

        assertNotNull(result);
        assertEquals("Tech Conference", result.getTitle());
    }

    @Test
    void getEventById_shouldReturnNullWhenNotFound() {
        when(eventRepo.findById(999L)).thenReturn(Optional.empty());

        Events result = eventService.getEventById(999L);

        assertNull(result);
    }

    @Test
    void addEvent_shouldPersistEvent() {
        when(eventRepo.save(testEvent)).thenReturn(testEvent);

        Events result = eventService.addEvent(testEvent);

        assertNotNull(result);
        assertEquals("Tech Conference", result.getTitle());
        verify(eventRepo).save(testEvent);
    }

    @Test
    void deleteEvent_shouldCallRepositoryDelete() {
        eventService.deleteEvent(1L);

        verify(eventRepo).deleteById(1L);
    }

    @Test
    void updateEvent_shouldUpdateAndSave() {
        Events updates = new Events();
        updates.setTitle("Updated Conference");
        updates.setDescription("Updated description");

        when(eventRepo.findById(1L)).thenReturn(Optional.of(testEvent));
        when(eventRepo.save(any(Events.class))).thenReturn(testEvent);

        Events result = eventService.updateEvent(1L, updates);

        assertNotNull(result);
        verify(eventRepo).save(any(Events.class));
    }

    @Test
    void updateEvent_shouldReturnNullWhenEventNotFound() {
        Events updates = new Events();
        when(eventRepo.findById(999L)).thenReturn(Optional.empty());

        Events result = eventService.updateEvent(999L, updates);

        assertNull(result);
        verify(eventRepo, never()).save(any(Events.class));
    }
}
