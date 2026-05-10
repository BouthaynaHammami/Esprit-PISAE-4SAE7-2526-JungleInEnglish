package tn.esprit.Books_Clubs.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClubDTO {
    private Long clubId;
    private String name;
    private String description;
    private String type;
    private String status;
    private Date creationDate;
}
