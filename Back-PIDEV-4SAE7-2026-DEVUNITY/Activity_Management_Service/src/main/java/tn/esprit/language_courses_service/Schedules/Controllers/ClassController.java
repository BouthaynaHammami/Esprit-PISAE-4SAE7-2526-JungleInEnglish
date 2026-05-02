package tn.esprit.language_courses_service.Schedules.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.language_courses_service.Schedules.Entities.ClassEntity;
import tn.esprit.language_courses_service.Schedules.Services.ClassService;

import java.util.List;

@RestController
@RequestMapping("/classes")
@RequiredArgsConstructor
public class ClassController {

    private final ClassService classService;

    @GetMapping("/all")
    public List<ClassEntity> getAllClasses() {
        return classService.getAllClasses();
    }

    @GetMapping("/{id}")
    public ClassEntity getClassById(@PathVariable Long id) {
        return classService.getClassById(id);
    }

    @GetMapping("/name/{name}")
    public ClassEntity getClassByName(@PathVariable String name) {
        return classService.getClassByName(name);
    }

    @PostMapping("/add")
    public ClassEntity createClass(@RequestBody ClassEntity classEntity) {
        return classService.createClass(classEntity);
    }

    @PutMapping("/update/{id}")
    public ClassEntity updateClass(@PathVariable Long id,
                                   @RequestBody ClassEntity classEntity) {
        return classService.updateClass(id, classEntity);
    }

    @DeleteMapping("/delete/{id}")
    public Boolean deleteClass(@PathVariable Long id) {
        classService.deleteClass(id);
        return true;
    }

    @GetMapping("/level/{level}")
    public List<ClassEntity> getClassesByLevel(@PathVariable String level) {
        return classService.getClassesByLevel(level);
    }

    @PutMapping("/assign/{userId}/{classId}")
    public ClassEntity assignClass(@PathVariable long userId, @PathVariable Long classId) {
        return classService.assignClassToStudent(classId,userId);
    }
}