package tn.esprit.language_courses_service.ChildrenEnglish.Serivces;

import tn.esprit.language_courses_service.ChildrenEnglish.Entities.Course;

import java.util.List;

public interface ICourseService {
    Course addCourse(Course course);
    Course updateCourse(Course course);
    void deleteCourse(long id);
    Course getCourse(long id);
    List<Course> getAllCourses();
}
