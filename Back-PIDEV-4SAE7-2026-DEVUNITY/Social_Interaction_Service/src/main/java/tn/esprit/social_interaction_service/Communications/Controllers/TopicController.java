package tn.esprit.social_interaction_service.Communications.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.social_interaction_service.Communications.Entities.Topic;
import tn.esprit.social_interaction_service.Communications.Services.ITopicService;

import java.util.List;

@RestController
@RequestMapping("/api/topics")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class TopicController {
    
    private final ITopicService service;
    
    @PostMapping("/add")
    public Topic addTopic(@RequestBody Topic topic) {
        return service.addTopic(topic);
    }
    
    @PutMapping("/update")
    public Topic updateTopic(@RequestBody Topic topic) {
        return service.updateTopic(topic);
    }
    
    @DeleteMapping("/delete/{id}")
    public void deleteTopic(@PathVariable long id) {
        service.deleteTopic(id);
    }
    
    @GetMapping("/{id}")
    public Topic getTopic(@PathVariable long id) {
        return service.getTopic(id);
    }
    
    @GetMapping
    public List<Topic> getAllTopics() {
        return service.getAllTopics();
    }
}
