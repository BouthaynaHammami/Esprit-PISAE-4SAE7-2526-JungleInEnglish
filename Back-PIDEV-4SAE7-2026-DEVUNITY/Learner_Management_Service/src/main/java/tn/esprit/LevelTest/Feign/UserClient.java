package tn.esprit.LevelTest.Feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tn.esprit.LevelTest.Dto.UserDTO;

import java.util.List;

@FeignClient(name = "user-s", url = "http://localhost:8089")
public interface UserClient {

    @GetMapping("/users/")
    List<UserDTO> getAllUsers();

    @GetMapping("/users/{id}")
    UserDTO getUserById(@PathVariable Long id);
}
