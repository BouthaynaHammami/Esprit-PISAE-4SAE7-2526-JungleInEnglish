package tn.esprit.community_engagement_service.Events.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.community_engagement_service.Events.DTO.TicketDTO;
import tn.esprit.community_engagement_service.Events.Entities.RegsitrationEvent;
import tn.esprit.community_engagement_service.Events.Services.RegistrationEventServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/registrations")
@RequiredArgsConstructor
public class RegistrationEventController {

    private final RegistrationEventServiceImpl registrationService;

    @PostMapping("/register")
    public RegsitrationEvent register(
            @RequestParam Long eventId,
            @RequestParam(required = false) String comment,
            @RequestParam Integer userId
    ) {
        return registrationService.registerWithCapacityCheck(eventId, comment, userId);
    }

    @GetMapping("/my/{userId}")
    public List<RegsitrationEvent> getMyRegistrations(@PathVariable Integer userId) {
        return registrationService.getMyRegistrations(userId);
    }

    @GetMapping("/{id}/ticket")
    public TicketDTO getTicket(@PathVariable Long id) throws Exception {
        return registrationService.generateTicket(id);
    }

    @DeleteMapping("/{id}/cancel")
    public void cancelRegistration(@PathVariable Long id) {
        registrationService.cancelRegistration(id);
    }

    @GetMapping("/all")
    public List<RegsitrationEvent> getAllRegistrations() {
        return registrationService.getAllRegistrations();
    }

    @PutMapping("/{id}/status")
    public RegsitrationEvent updateStatus(
            @PathVariable Long id,
            @RequestParam String status
    ) {
        return registrationService.updateRegistrationStatus(id, status);
    }
}