package tn.esprit.language_courses_service.Clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tn.esprit.language_courses_service.DTO.UserDTO;

import java.util.List;

@FeignClient(name = "LEARNER-MANAGEMENT-SERVICE", path = "/learners/api")
public interface UserClient {

    @GetMapping("/users")
    List<UserDTO> getAllUsers();

    @GetMapping("/users/{id}")
    UserDTO getUserById(@PathVariable("id") Long id);

    @GetMapping("/users/email/{email}")
    UserDTO getUserByEmail(@PathVariable("email") String email);

    @GetMapping("/companies/{companyId}/students")
    List<UserDTO> getCompanyStudents(@PathVariable("companyId") Long companyId);
}

