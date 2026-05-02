package tn.esprit.community_engagement_service.Events.Services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.community_engagement_service.Clients.UserClient;
import tn.esprit.community_engagement_service.DTO.UserDTO;
import tn.esprit.community_engagement_service.Events.Entities.Events;
import tn.esprit.community_engagement_service.Events.Entities.RegistrationStatus;
import tn.esprit.community_engagement_service.Events.Entities.RegsitrationEvent;
import tn.esprit.community_engagement_service.Events.Repositories.EventRepo;
import tn.esprit.community_engagement_service.Events.Repositories.RegsitrationEventRepo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrationEventServiceImplTest {

    @Mock
    private RegsitrationEventRepo registrationEventRepo;
    @Mock
    private EventRepo eventRepo;
    @Mock
    private UserClient userClient;
    @Mock
    private QRCodeService qrCodeService;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private RegistrationEventServiceImpl registrationService;

    @Test
    void registerWithCapacityCheck_shouldConfirmAndSendEmailWhenCapacityAvailable() {
        Events event = new Events();
        event.setEventId(10L);
        event.setTitle("Open Day");
        event.setCapacity(2);

        UserDTO user = new UserDTO();
        user.setId(5);
        user.setEmail("student@devunity.com");

        when(eventRepo.findById(10L)).thenReturn(Optional.of(event));
        when(registrationEventRepo.countByEvents_EventIdAndStatus(10L, RegistrationStatus.CONFIRMED)).thenReturn(1L);
        when(registrationEventRepo.save(any(RegsitrationEvent.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userClient.getUserById(5)).thenReturn(user);

        RegsitrationEvent saved = registrationService.registerWithCapacityCheck(10L, "Ready", 5);

        assertEquals(RegistrationStatus.CONFIRMED, saved.getStatus());
        verify(emailService).sendRegistrationConfirmation("student@devunity.com", "Open Day");
    }

    @Test
    void promoteFromWaitlist_shouldPromoteFirstWaitlistedAndSendEmail() {
        Events event = new Events();
        event.setEventId(11L);
        event.setTitle("Hackathon");
        event.setCapacity(2);

        RegsitrationEvent waitlisted = new RegsitrationEvent();
        waitlisted.setId(77L);
        waitlisted.setEvents(event);
        waitlisted.setUser(9);
        waitlisted.setStatus(RegistrationStatus.WAITLISTED);
        waitlisted.setRegistrationDate(LocalDateTime.now().minusHours(1));

        UserDTO promotedUser = new UserDTO();
        promotedUser.setId(9);
        promotedUser.setEmail("wait@devunity.com");

        when(eventRepo.findById(11L)).thenReturn(Optional.of(event));
        when(registrationEventRepo.countByEvents_EventIdAndStatus(11L, RegistrationStatus.CONFIRMED)).thenReturn(1L);
        when(registrationEventRepo.findByEvents_EventIdAndStatusOrderByRegistrationDateAsc(11L, RegistrationStatus.WAITLISTED))
                .thenReturn(List.of(waitlisted));
        when(userClient.getUserById(9)).thenReturn(promotedUser);

        registrationService.promoteFromWaitlist(11L);

        assertEquals(RegistrationStatus.CONFIRMED, waitlisted.getStatus());
        verify(registrationEventRepo).save(waitlisted);
        verify(emailService).sendRegistrationConfirmation("wait@devunity.com", "Hackathon");
    }
}
