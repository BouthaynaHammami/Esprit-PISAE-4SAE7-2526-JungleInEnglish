package tn.esprit.language_courses_service.BusinessEnglish.Services.ImplServices;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl {

    private final JavaMailSender mailSender;

    // ✅ APPROVE EMAIL
    public void sendInvitationEmail(String to, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Activation Code - Language Platform");

            String htmlContent = generateHtmlTemplate(
                    "Welcome to Language Platform!",
                    "<p>Hello,</p>" +
                            "<p>You have been invited to join our professional training program. We are excited to have you on board!</p>" +
                            "<div style='background-color: #FFDDD2; padding: 20px; border-radius: 8px; text-align: center; margin: 20px 0;'>" +
                            "  <p style='margin: 0; font-size: 14px; color: #006D77;'>Your activation code is:</p>" +
                            "  <h2 style='margin: 10px 0; color: #E29578; letter-spacing: 5px; font-size: 32px;'>" + code + "</h2>" +
                            "</div>" +
                            "<p>Please activate your account before the code expires to start your journey.</p>",
                    "Activate Account",
                    "#" // Placeholder for activation link if available
            );

            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send invitation email", e);
        }
    }

    // ❌ REJECT EMAIL
    public void sendRejectionEmail(String to, String offerName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Update Regarding Your Request");

            String htmlContent = generateHtmlTemplate(
                    "Application Update",
                    "<p>Hello,</p>" +
                            "<p>Thank you for your interest in our <strong>" + offerName + "</strong> offer.</p>" +
                            "<p>After careful consideration, we regret to inform you that your request has been rejected at this time.</p>" +
                            "<p>We appreciate the time you took to apply. Please feel free to explore other opportunities on our platform or contact support for more details.</p>",
                    "Contact Support",
                    "mailto:support@languageplatform.tn"
            );

            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send rejection email", e);
        }
    }

    private String generateHtmlTemplate(String title, String body, String buttonText, String buttonUrl) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "  <style>" +
                "    body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #EDF6F9; color: #006D77; margin: 0; padding: 0; }" +
                "    .container { max-width: 600px; margin: 40px auto; background-color: #ffffff; border-radius: 12px; overflow: hidden; box-shadow: 0 10px 30px rgba(0,109,119,0.1); }" +
                "    .header { background-color: #006D77; padding: 40px 20px; text-align: center; color: #EDF6F9; }" +
                "    .content { padding: 40px; line-height: 1.6; }" +
                "    .footer { background-color: #83C5BE; padding: 20px; text-align: center; color: #006D77; font-size: 12px; }" +
                "    .button { display: inline-block; padding: 14px 30px; background-color: #E29578; color: #ffffff !important; text-decoration: none; border-radius: 6px; font-weight: bold; margin-top: 20px; }" +
                "    h1 { margin: 0; font-size: 24px; }" +
                "  </style>" +
                "</head>" +
                "<body>" +
                "  <div class='container'>" +
                "    <div class='header'>" +
                "      <h1>" + title + "</h1>" +
                "    </div>" +
                "    <div class='content'>" +
                "      " + body +
                "      <div style='text-align: center;'>" +
                "        <a href='" + buttonUrl + "' class='button'>" + buttonText + "</a>" +
                "      </div>" +
                "    </div>" +
                "    <div class='footer'>" +
                "      <p>&copy; 2026 Language Platform. All rights reserved.</p>" +
                "      <p>Empowering your business communication.</p>" +
                "    </div>" +
                "  </div>" +
                "</body>" +
                "</html>";
    }
}