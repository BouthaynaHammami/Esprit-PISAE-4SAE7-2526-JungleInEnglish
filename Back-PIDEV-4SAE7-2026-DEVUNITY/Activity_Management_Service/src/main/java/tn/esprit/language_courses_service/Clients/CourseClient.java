package tn.esprit.language_courses_service.Clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tn.esprit.language_courses_service.DTO.CourseDTO;

@FeignClient(name = "ACADEMIC-MANAGEMENT-SERVICE", path = "/academics/api")
public interface CourseClient {

    @GetMapping("/courses/{id}")
    CourseDTO getCourseById(@PathVariable("id") Long id);
}
