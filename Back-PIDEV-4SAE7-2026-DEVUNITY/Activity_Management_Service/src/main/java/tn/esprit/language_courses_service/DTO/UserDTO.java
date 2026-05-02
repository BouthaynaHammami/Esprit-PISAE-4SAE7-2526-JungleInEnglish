package tn.esprit.language_courses_service.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    Long userId;
    String firstName;
    String lastName;
    String email;
    Long classId;
    RoleDTO role;
}
