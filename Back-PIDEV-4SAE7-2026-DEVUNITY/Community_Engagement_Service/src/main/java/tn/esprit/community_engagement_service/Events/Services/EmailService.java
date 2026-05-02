package tn.esprit.community_engagement_service.Events.Services;

public interface EmailService {

    void sendRegistrationConfirmation(String to, String eventTitle);

    void sendCancellationEmail(String to, String eventTitle);

    void sendReminderEmail(String to, String eventTitle, String eventDate);

    // ── Envoie le billet HTML complet par email ──
    void sendTicketEmail(String to,
                         String holderName,
                         String eventTitle,
                         String eventLocation,
                         String eventDate,
                         String ticketId,
                         String qrCodeBase64);
}