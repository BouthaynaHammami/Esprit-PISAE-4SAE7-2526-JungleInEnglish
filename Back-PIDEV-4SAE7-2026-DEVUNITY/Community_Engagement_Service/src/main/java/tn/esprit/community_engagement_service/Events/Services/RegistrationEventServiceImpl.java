package tn.esprit.community_engagement_service.Events.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.community_engagement_service.Clients.UserClient;
import tn.esprit.community_engagement_service.DTO.UserDTO;
import tn.esprit.community_engagement_service.Events.DTO.TicketDTO;
import tn.esprit.community_engagement_service.Events.Entities.Events;
import tn.esprit.community_engagement_service.Events.Entities.RegistrationStatus;
import tn.esprit.community_engagement_service.Events.Entities.RegsitrationEvent;
import tn.esprit.community_engagement_service.Events.Repositories.EventRepo;
import tn.esprit.community_engagement_service.Events.Repositories.RegsitrationEventRepo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class RegistrationEventServiceImpl {

    @Autowired
    private RegsitrationEventRepo registrationEventRepo;

    @Autowired
    private EventRepo eventRepo;

    @Autowired
    private UserClient userClient;

    @Autowired
    private QRCodeService qrCodeService;

    @Autowired
    private EmailService emailService;


    // =====================================================
    // REGISTER
    // =====================================================

    @Transactional
    public RegsitrationEvent registerWithCapacityCheck(
            Long eventId,
            String comment,
            Integer userId) {

        Events event = eventRepo.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        long confirmedCount =
                registrationEventRepo.countByEvents_EventIdAndStatus(
                        eventId,
                        RegistrationStatus.CONFIRMED
                );

        RegsitrationEvent registration = new RegsitrationEvent();
        registration.setRegistrationDate(LocalDateTime.now());
        registration.setComment(comment);
        registration.setEvents(event);
        registration.setUser(userId);

        String ticketId = "TKT-" +
                UUID.randomUUID().toString()
                        .replace("-", "")
                        .substring(0, 8)
                        .toUpperCase();

        registration.setTicketId(ticketId);

        if (confirmedCount < event.getCapacity()) {
            registration.setStatus(RegistrationStatus.CONFIRMED);
        } else {
            registration.setStatus(RegistrationStatus.WAITLISTED);
        }

        RegsitrationEvent saved = registrationEventRepo.save(registration);

        if (saved.getStatus() == RegistrationStatus.CONFIRMED) {

            UserDTO user = userClient.getUserById(userId);

            emailService.sendRegistrationConfirmation(
                    user.getEmail(),
                    event.getTitle()
            );
        }

        return saved;
    }


    // =====================================================
    // GET ALL
    // =====================================================

    public List<RegsitrationEvent> getAllRegistrations() {
        return registrationEventRepo.findAll();
    }

    public RegsitrationEvent getRegistrationById(Long id) {
        return registrationEventRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Registration not found"));
    }

    public List<RegsitrationEvent> getMyRegistrations(Integer userId) {
        return registrationEventRepo.findByUser(userId);
    }


    // =====================================================
    // UPDATE STATUS
    // =====================================================

    @Transactional
    public RegsitrationEvent updateRegistrationStatus(Long id, String status) {

        RegsitrationEvent registration = getRegistrationById(id);

        RegistrationStatus oldStatus = registration.getStatus();
        RegistrationStatus newStatus =
                RegistrationStatus.valueOf(status.toUpperCase());

        registration.setStatus(newStatus);
        registrationEventRepo.save(registration);

        if (oldStatus == RegistrationStatus.CONFIRMED &&
                (newStatus == RegistrationStatus.CANCELLED ||
                        newStatus == RegistrationStatus.REJECTED)) {

            promoteFromWaitlist(registration.getEvents().getEventId());
        }

        return registration;
    }


    // =====================================================
    // CANCEL
    // =====================================================

    @Transactional
    public void cancelRegistration(Long id) {

        RegsitrationEvent registration = getRegistrationById(id);
        RegistrationStatus oldStatus = registration.getStatus();

        registration.setStatus(RegistrationStatus.CANCELLED);
        registrationEventRepo.save(registration);

        UserDTO user = userClient.getUserById(registration.getUser());

        emailService.sendCancellationEmail(
                user.getEmail(),
                registration.getEvents().getTitle()
        );

        if (oldStatus == RegistrationStatus.CONFIRMED) {
            promoteFromWaitlist(registration.getEvents().getEventId());
        }
    }


    // =====================================================
    // PROMOTE WAITLIST
    // =====================================================

    @Transactional
    public void promoteFromWaitlist(Long eventId) {

        Events event = eventRepo.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        long confirmedCount =
                registrationEventRepo.countByEvents_EventIdAndStatus(
                        eventId, RegistrationStatus.CONFIRMED
                );

        if (confirmedCount >= event.getCapacity()) return;

        List<RegsitrationEvent> waitlisted =
                registrationEventRepo
                        .findByEvents_EventIdAndStatusOrderByRegistrationDateAsc(
                                eventId,
                                RegistrationStatus.WAITLISTED
                        );

        if (waitlisted.isEmpty()) return;

        RegsitrationEvent first = waitlisted.get(0);
        first.setStatus(RegistrationStatus.CONFIRMED);
        registrationEventRepo.save(first);

        UserDTO user = userClient.getUserById(first.getUser());

        emailService.sendRegistrationConfirmation(
                user.getEmail(),
                first.getEvents().getTitle()
        );
    }


    // =====================================================
    // GENERATE TICKET
    // =====================================================

    public TicketDTO generateTicket(Long registrationId) throws Exception {

        RegsitrationEvent registration = getRegistrationById(registrationId);

        if (registration.getStatus() != RegistrationStatus.CONFIRMED) {
            throw new RuntimeException("Ticket only for confirmed registrations");
        }

        Events event = registration.getEvents();

        UserDTO user = userClient.getUserById(registration.getUser());

        String ticketId = registration.getTicketId();

        String qrContent =
                "TICKET:" + ticketId +
                        "|EVENT:" + event.getTitle() +
                        "|NAME:" + user.getFirstName() + " " + user.getLastName() +
                        "|DATE:" + event.getStartDate();

        String qrBase64 = qrCodeService.generateQRCodeBase64(qrContent);

        return TicketDTO.builder()
                .ticketId(ticketId)
                .registrationId(registration.getId())
                .eventTitle(event.getTitle())
                .eventDate(event.getStartDate())
                .eventLocation(event.getLocation())
                .eventImage(event.getImageUrl())
                .holderName(user.getFirstName() + " " + user.getLastName())
                .status(registration.getStatus().name())
                .generatedAt(registration.getRegistrationDate())
                .qrCodeBase64("data:image/png;base64," + qrBase64)
                .build();
    }


    // =====================================================
    // REMINDER
    // =====================================================

    @Scheduled(cron = "0 0 9 * * ?", zone = "Africa/Tunis")
    public void sendEventReminders() {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime limit = now.plusHours(24);

        List<RegsitrationEvent> registrations = registrationEventRepo.findAll();

        for (RegsitrationEvent reg : registrations) {

            if (reg.getStatus() != RegistrationStatus.CONFIRMED) continue;

            LocalDateTime eventDate = reg.getEvents().getStartDate();
            if (eventDate == null) continue;

            if (!eventDate.isBefore(now) && eventDate.isBefore(limit)) {

                UserDTO user = userClient.getUserById(reg.getUser());

                emailService.sendReminderEmail(
                        user.getEmail(),
                        reg.getEvents().getTitle(),
                        eventDate.toString()
                );
            }
        }
    }
}