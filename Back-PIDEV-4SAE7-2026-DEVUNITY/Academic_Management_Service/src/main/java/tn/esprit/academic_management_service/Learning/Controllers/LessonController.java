package tn.esprit.academic_management_service.Learning.Controllers;

import org.springframework.web.bind.annotation.*;
import tn.esprit.academic_management_service.Learning.Entities.Lesson;
import tn.esprit.academic_management_service.Learning.Services.LessonService;

import java.util.List;

@RestController
@RequestMapping("/lessons")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping("/all")
    public List<Lesson> getAllLessons() {
        return lessonService.getAllLessons();
    }

    

    @GetMapping("/{id}")
    public Lesson getLessonById(@PathVariable Long id) {
        return lessonService.getLessonById(id).orElse(null);
    }

    @GetMapping("/course/{courseId}")
    public List<Lesson> getLessonsByCourseId(@PathVariable Long courseId) {
        return lessonService.getLessonsByCourseId(courseId);
    }

    @PostMapping("/add")
    public Lesson createLesson(@RequestBody Lesson lesson){
        return lessonService.createLesson(lesson);
    }

    @PutMapping("/update/{id}")
    public Lesson updateLesson(@PathVariable Long id,
                               @RequestBody Lesson input) {
        return lessonService.updateLesson(id, input);
    }

    @DeleteMapping("/delete/{id}")
    public Boolean deleteLesson(@PathVariable Long id) {
        lessonService.deleteLesson(id);
        return true;
    }
}