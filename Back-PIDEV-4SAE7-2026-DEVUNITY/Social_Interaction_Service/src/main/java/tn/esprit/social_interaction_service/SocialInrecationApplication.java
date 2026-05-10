package tn.esprit.social_interaction_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableElasticsearchRepositories(basePackages = "tn.esprit.social_interaction_service.Reporting_Analytics.Repositories")
public class SocialInrecationApplication {
    public static void main(String[] args) {
        SpringApplication.run(SocialInrecationApplication.class, args);
    }
}

