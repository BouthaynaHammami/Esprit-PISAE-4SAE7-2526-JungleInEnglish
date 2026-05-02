package tn.esprit.LevelTest.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Integer userId;
    private String firstName;
    private String lastName;
    private String email;
    private String cv;
    private LocalDateTime lastLogin;
}
