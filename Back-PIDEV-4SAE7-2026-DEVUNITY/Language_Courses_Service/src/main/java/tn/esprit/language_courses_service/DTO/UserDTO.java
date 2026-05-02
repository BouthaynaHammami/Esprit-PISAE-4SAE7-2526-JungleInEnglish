package tn.esprit.language_courses_service.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    Integer userId;
    String firstName;
    String lastName;
    String email;
    String password;
    LocalDateTime lastLogin;
    String cv;
    RoleDTO role;
}
