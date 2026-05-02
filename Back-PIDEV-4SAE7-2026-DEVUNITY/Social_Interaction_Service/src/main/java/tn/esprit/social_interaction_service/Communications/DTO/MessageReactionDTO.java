package tn.esprit.social_interaction_service.Communications.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageReactionDTO {
    private String emoji;
    private String user;
}
