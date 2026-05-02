package tn.esprit.social_interaction_service.Communications.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tn.esprit.social_interaction_service.Communications.Entities.Topic;
import tn.esprit.social_interaction_service.Communications.Repositories.TopicRepository;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final TopicRepository topicRepository;

    @Override
    public void run(String... args) {
        if (topicRepository.count() == 0) {
            Topic topic1 = new Topic();
            topic1.setTitle("How to learn English effectively?");
            topic1.setDescription("Share your tips and tricks for learning English");
            topic1.setCreatedAt(LocalDate.now());
            topicRepository.save(topic1);

            Topic topic2 = new Topic();
            topic2.setTitle("Best English learning apps");
            topic2.setDescription("Discuss the best mobile apps for learning English");
            topic2.setCreatedAt(LocalDate.now());
            topicRepository.save(topic2);

            Topic topic3 = new Topic();
            topic3.setTitle("English grammar questions");
            topic3.setDescription("Ask and answer grammar-related questions");
            topic3.setCreatedAt(LocalDate.now());
            topicRepository.save(topic3);

            System.out.println("✅ Sample topics initialized successfully!");
        }
    }
}
