package tn.esprit.employee.Feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import tn.esprit.employee.Dto.UserDTO;
import java.util.List;

@FeignClient(name = "learner-management-service")
public interface EmployeeUserClient {

    @GetMapping("/learners/api/users/")
    List<UserDTO> getAllUsers();

    @GetMapping("/learners/api/users/{id}")
    UserDTO getUserById(@PathVariable("id") Long id);
}
