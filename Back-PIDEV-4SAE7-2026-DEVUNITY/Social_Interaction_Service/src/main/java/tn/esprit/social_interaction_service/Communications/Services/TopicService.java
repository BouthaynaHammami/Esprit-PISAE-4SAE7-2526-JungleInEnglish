package tn.esprit.social_interaction_service.Communications.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.social_interaction_service.Communications.Clients.UserClient;
import tn.esprit.social_interaction_service.Communications.DTO.UserDTO;
import tn.esprit.social_interaction_service.Communications.Entities.Topic;
import tn.esprit.social_interaction_service.Communications.Repositories.TopicRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TopicService implements ITopicService {
    
    private final TopicRepository repository;
    private final UserClient userClient;
    
    @Override
    public Topic addTopic(Topic topic) {
        if (topic.getCreatedAt() == null) {
            topic.setCreatedAt(LocalDate.now());
        }
        return repository.save(topic);
    }
    
    @Override
    public Topic updateTopic(Topic topic) {
        return repository.save(topic);
    }
    
    @Override
    public void deleteTopic(long id) {
        repository.deleteById(id);
    }
    
    @Override
    public Topic getTopic(long id) {
        Topic topic = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Topic not found with id: " + id));
        
        // Récupérer les infos utilisateur via Feign Client
        if (topic.getUserId() != null) {
            try {
                UserDTO user = userClient.getUserById(topic.getUserId());
                topic.setUser(user);
            } catch (Exception e) {
                System.err.println("Error fetching user: " + e.getMessage());
            }
        }
        
        return topic;
    }
    
    @Override
    public List<Topic> getAllTopics() {
        List<Topic> topics = repository.findAll();
        
        // Récupérer les infos utilisateur pour chaque topic
        return topics.stream().map(topic -> {
            if (topic.getUserId() != null) {
                try {
                    UserDTO user = userClient.getUserById(topic.getUserId());
                    topic.setUser(user);
                } catch (Exception e) {
                    System.err.println("Error fetching user for topic " + topic.getTopicId());
                }
            }
            return topic;
        }).collect(Collectors.toList());
    }
}
