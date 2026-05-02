package tn.esprit.language_courses_service.Clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import tn.esprit.language_courses_service.DTO.UserDTO;

import java.util.List;

@FeignClient(name = "LEARNER-MANAGEMENT-SERVICE", path = "/learners/api")
public interface UserClient {

    @PostMapping("/users/add")
    UserDTO create(@RequestBody UserDTO user);

    @PutMapping("/users/update/{id}")
    UserDTO update(@PathVariable("id") Long id, @RequestBody UserDTO user);

    @GetMapping("/users")
    List<UserDTO> getAllUsers();

    @GetMapping("/users/email/{email}")
    UserDTO getUsersByEmail(@PathVariable("email") String email);

    @GetMapping("/users/{id}")
    UserDTO getUserById(@PathVariable("id") Long id);
}