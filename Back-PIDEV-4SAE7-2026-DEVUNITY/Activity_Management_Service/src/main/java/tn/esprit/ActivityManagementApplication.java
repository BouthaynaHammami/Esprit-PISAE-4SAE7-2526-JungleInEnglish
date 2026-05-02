package tn.esprit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"tn.esprit.language_courses_service", "tn.esprit.employee"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"tn.esprit.language_courses_service", "tn.esprit.employee"})
@EnableJpaRepositories(basePackages = {"tn.esprit.language_courses_service", "tn.esprit.employee.Repositories"})
@EntityScan(basePackages = {"tn.esprit.language_courses_service", "tn.esprit.employee.Entities"})
@EnableScheduling
public class ActivityManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActivityManagementApplication.class, args);
    }

}
