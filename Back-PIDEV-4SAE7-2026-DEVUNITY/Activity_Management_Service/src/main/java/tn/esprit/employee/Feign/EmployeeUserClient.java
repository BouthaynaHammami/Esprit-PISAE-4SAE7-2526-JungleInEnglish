package tn.esprit.employee.Feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import tn.esprit.employee.Dto.UserDTO;
import java.util.List;

@FeignClient(name = "user-s", url = "http://localhost:8089")
public interface EmployeeUserClient {

    @GetMapping("/users/")
    List<UserDTO> getAllUsers();

    @GetMapping("/users/{id}")
    UserDTO getUserById(@PathVariable("id") Long id);
}
