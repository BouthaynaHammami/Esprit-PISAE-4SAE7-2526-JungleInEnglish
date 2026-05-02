package tn.esprit.employee.Dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private String cv;
    private LocalDateTime lastLogin;


}
