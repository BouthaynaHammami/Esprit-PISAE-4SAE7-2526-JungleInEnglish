package tn.esprit.community_engagement_service.Events.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private final String FROM_EMAIL = "chaibimarwa18@gmail.com";

    @Async
    @Override
    public void sendRegistrationConfirmation(String to, String eventTitle) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(FROM_EMAIL);
            message.setTo(to);
            message.setSubject("Registration Confirmed 🎉");
            message.setText(
                    "Hello,\n\n" +
                            "Your registration for event '" + eventTitle +
                            "' is CONFIRMED.\n\nSee you soon!"
            );
            mailSender.send(message);
            System.out.println("EMAIL SENT → " + to);
        } catch (Exception e) {
            System.err.println("Email not sent: " + e.getMessage());
        }
    }

    @Async
    @Override
    public void sendCancellationEmail(String to, String eventTitle) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(FROM_EMAIL);
            message.setTo(to);
            message.setSubject("Registration Cancelled ❌");
            message.setText(
                    "Hello,\n\n" +
                            "Your registration for event '" + eventTitle +
                            "' has been CANCELLED."
            );
            mailSender.send(message);
            System.out.println("CANCELLATION EMAIL → " + to);
        } catch (Exception e) {
            System.err.println("Email not sent: " + e.getMessage());
        }
    }

    @Async
    @Override
    public void sendReminderEmail(String to, String eventTitle, String eventDate) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(FROM_EMAIL);
            message.setTo(to);
            message.setSubject("Event Reminder ⏰");
            message.setText(
                    "Reminder!\n\n" +
                            "Your event '" + eventTitle +
                            "' starts on " + eventDate +
                            ".\n\nDon't forget to attend!"
            );
            mailSender.send(message);
            System.out.println("REMINDER EMAIL → " + to);
        } catch (Exception e) {
            System.err.println("Email not sent: " + e.getMessage());
        }
    }

    @Async
    @Override
    public void sendTicketEmail(String to,
                                String holderName,
                                String eventTitle,
                                String eventLocation,
                                String eventDate,
                                String ticketId,
                                String qrCodeBase64) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(FROM_EMAIL);
            message.setTo(to);
            message.setSubject("🎫 Your Jungle Ticket — " + eventTitle);
            message.setText(
                    "Hello " + holderName + ",\n\n" +
                            "Your ticket is confirmed.\n\n" +
                            "Event   : " + eventTitle + "\n" +
                            "Date    : " + eventDate + "\n" +
                            "Location: " + eventLocation + "\n" +
                            "Ticket  : " + ticketId + "\n\n" +
                            "Please present this ticket at the entrance."
            );
            mailSender.send(message);
            System.out.println("TICKET EMAIL → " + to);
        } catch (Exception e) {
            System.err.println("Email not sent: " + e.getMessage());
        }
    }
}