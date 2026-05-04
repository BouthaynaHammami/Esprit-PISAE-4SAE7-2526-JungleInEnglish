package tn.esprit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
    "tn.esprit.learner_managment_service",
    "tn.esprit.LevelTest",
    "tn.esprit.Books_Clubs"
})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"tn.esprit.LevelTest"})
@EnableJpaRepositories(basePackages = {
    "tn.esprit.learner_managment_service.UserManagement",
    "tn.esprit.learner_managment_service.DropoutPrediction",
    "tn.esprit.LevelTest.Repositories",
    "tn.esprit.Books_Clubs.repositories"
})
@EntityScan(basePackages = {
    "tn.esprit.learner_managment_service.UserManagement",
    "tn.esprit.learner_managment_service.DropoutPrediction",
    "tn.esprit.LevelTest.Entities",
    "tn.esprit.Books_Clubs.entities"
})
public class LearnerManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearnerManagementApplication.class, args);
    }

}
