package tn.esprit.community_engagement_service.Events.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketDTO {

    private String ticketId;
    private Long registrationId;

    private String eventTitle;
    private LocalDateTime eventDate;
    private String eventLocation;
    private String eventImage;      // ✅ Ajouté pour l'image du ticket

    private String holderName;

    private String status;
    private LocalDateTime generatedAt;
    private String qrCodeBase64;
}