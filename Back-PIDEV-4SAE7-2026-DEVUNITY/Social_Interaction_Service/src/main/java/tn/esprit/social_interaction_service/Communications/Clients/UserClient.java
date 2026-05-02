package tn.esprit.social_interaction_service.Communications.Clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tn.esprit.social_interaction_service.Communications.DTO.UserDTO;

@FeignClient(name = "learner-management-service", url = "http://localhost:8082/learners/api")
public interface UserClient {
    
    @GetMapping("/users/{id}")
    UserDTO getUserById(@PathVariable("id") Integer id);
    
    @GetMapping("/users/email/{email}")
    UserDTO getUserByEmail(@PathVariable("email") String email);
}
