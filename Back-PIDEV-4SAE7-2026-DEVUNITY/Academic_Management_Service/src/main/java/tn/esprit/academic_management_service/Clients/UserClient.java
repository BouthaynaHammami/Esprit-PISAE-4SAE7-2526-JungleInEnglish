package tn.esprit.academic_management_service.Clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import tn.esprit.academic_management_service.DTO.UserDTO;

import java.util.List;

@FeignClient(name = "LEARNER-MANAGEMENT-SERVICE", path = "/learners/api")
public interface UserClient {

    @GetMapping("/users/all")
    List<UserDTO> getAllUsers();

    @GetMapping("/users/{id}")
    UserDTO getUserById(@PathVariable("id") Integer id);

    @GetMapping("/users/email/{email}")
    UserDTO getUserByEmail(@PathVariable("email") String email);

    @PostMapping("/users/add")
    UserDTO create(@RequestBody UserDTO user);

    @PutMapping("/users/update/{id}")
    UserDTO update(@PathVariable("id") Integer id, @RequestBody UserDTO user);
}