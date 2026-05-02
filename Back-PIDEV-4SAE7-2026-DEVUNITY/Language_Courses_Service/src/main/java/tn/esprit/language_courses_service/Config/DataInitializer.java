package tn.esprit.language_courses_service.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tn.esprit.language_courses_service.ChildrenEnglish.Entities.Course;
import tn.esprit.language_courses_service.ChildrenEnglish.Repositories.CourseRepository;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CourseRepository courseRepository;

    @Override
    public void run(String... args) {
        if (courseRepository.count() == 0) {
            Course course1 = new Course();
            course1.setTitle("English for Beginners");
            course1.setDescription("Learn basic English vocabulary and grammar");
            course1.setPrice(49.99f);
            courseRepository.save(course1);

            Course course2 = new Course();
            course2.setTitle("Intermediate English");
            course2.setDescription("Improve your English speaking and writing skills");
            course2.setPrice(79.99f);
            courseRepository.save(course2);

            Course course3 = new Course();
            course3.setTitle("Advanced English");
            course3.setDescription("Master advanced English grammar and vocabulary");
            course3.setPrice(99.99f);
            courseRepository.save(course3);

            System.out.println("✅ Sample courses initialized successfully!");
        }
    }
}
