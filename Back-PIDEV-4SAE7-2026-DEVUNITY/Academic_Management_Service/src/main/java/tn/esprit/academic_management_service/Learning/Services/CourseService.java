package tn.esprit.academic_management_service.Learning.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.academic_management_service.Learning.Entities.*;
import tn.esprit.academic_management_service.Learning.Repositories.CourseRepository;
import tn.esprit.academic_management_service.Learning.Repositories.EnrollmentRepository;
import tn.esprit.academic_management_service.Learning.Repositories.LessonRepository;
import tn.esprit.academic_management_service.Learning.Repositories.QuizRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final QuizRepository quizRepository;
    private final LessonRepository lessonRepository;
    private final EnrollmentRepository enrollmentRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    public List<Course> getCoursesByLevel(Level level) {
        return courseRepository.findByLevel(level);
    }

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public Course updateCourse(Long id, Course courseDetails) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        course.setTitle(courseDetails.getTitle());
        course.setDescription(courseDetails.getDescription());
        course.setLevel(courseDetails.getLevel());
        course.setPrice(courseDetails.getPrice());
        course.setLessonsNumber(courseDetails.getLessonsNumber());
        course.setHidden(courseDetails.isHidden());
        course.setImageUrl(courseDetails.getImageUrl());

        return courseRepository.save(course);
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    public Course assignLessons(Long id, List<Long> lessonIds) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));

        List<Lesson> currentLessons = lessonRepository.findByCourse(course);
        currentLessons.forEach(lesson -> {
            lesson.setCourse(null);
            lessonRepository.save(lesson);
        });

        List<Lesson> newLessons = lessonRepository.findAllById(lessonIds);
        newLessons.forEach(lesson -> {
            lesson.setCourse(course);
            lessonRepository.save(lesson);
        });

        course.setLessonsNumber(newLessons.size());
        return courseRepository.save(course);
    }

    public Course assignQuiz(Long id, Long quizId) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));

        Quiz previousQuiz = quizRepository.findByCourse(course);
        if (previousQuiz != null) {
            previousQuiz.setCourse(null);
            quizRepository.save(previousQuiz);
        }

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + quizId));
        quiz.setCourse(course);
        quizRepository.save(quiz);

        return courseRepository.save(course);
    }

    public Course toggleVisibility(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found: " + id));
        course.setHidden(!course.isHidden());
        return courseRepository.save(course);
    }

    public Course updateImageUrl(Long id, String imageUrl) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found: " + id));
        course.setImageUrl(imageUrl);
        return courseRepository.save(course);
    }

    public List<Course> getVisibleCourses() {
        return courseRepository.findByIsHiddenFalse();
    }

    public boolean isEnrolled(Integer userId, Long courseId) {
        return enrollmentRepository.existsByUserIdAndCourse_CourseId(userId, courseId);
    }

    public List<Course> getEnrolledCourses(Integer userId) {
        return enrollmentRepository.findByUserId(userId)
                .stream()
                .map(Enrollment::getCourse)
                .collect(Collectors.toList());
    }

    public List<Course> getCoursesByType(TypeCourse type) {
        return courseRepository.findByType(type);
    }
}
