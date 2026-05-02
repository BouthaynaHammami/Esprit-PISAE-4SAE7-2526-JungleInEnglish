package tn.esprit.academic_management_service.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private Integer userId;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private Long classId;
}