package tn.esprit.community_engagement_service.Clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tn.esprit.community_engagement_service.DTO.UserDTO;

@FeignClient(name = "learner-management-service", path = "/learners/api")
public interface UserClient {
    
    @GetMapping("/users/{id}")
    UserDTO getUserById(@PathVariable("id") Integer id);
}
