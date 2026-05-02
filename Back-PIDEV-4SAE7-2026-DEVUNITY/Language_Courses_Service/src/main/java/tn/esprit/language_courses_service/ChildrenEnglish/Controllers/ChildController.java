package tn.esprit.language_courses_service.ChildrenEnglish.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.language_courses_service.ChildrenEnglish.Entities.Child;
import tn.esprit.language_courses_service.ChildrenEnglish.Serivces.IChildService;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/children")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ChildController {
    
    private final IChildService childService;
    
    @GetMapping
    public ResponseEntity<List<Child>> getAllChildren() {
        return ResponseEntity.ok(childService.getAllChildren());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Child> getChildById(@PathVariable Long id) {
        Child child = childService.getChild(id);
        return child != null ? ResponseEntity.ok(child) : ResponseEntity.notFound().build();
    }
    
    @PostMapping("/add")
    public ResponseEntity<Child> addChild(@RequestBody Map<String, Object> childData) {
        Child child = new Child();
        child.setName((String) childData.get("name"));
        
        // Convert age to birthDate
        if (childData.containsKey("age")) {
            Integer age = (Integer) childData.get("age");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, -age);
            child.setBirthDate(cal.getTime());
        }
        
        return ResponseEntity.ok(childService.addChild(child));
    }
    
    @PutMapping("/update")
    public ResponseEntity<Child> updateChild(@RequestBody Child child) {
        return ResponseEntity.ok(childService.updateChild(child));
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteChild(@PathVariable Long id) {
        childService.deleteChild(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/parent/{parentId}")
    public ResponseEntity<List<Child>> getChildrenByParent(@PathVariable Long parentId) {
        return ResponseEntity.ok(childService.getChildrenByParent(parentId));
    }
    
    @GetMapping("/level/{levelId}")
    public ResponseEntity<List<Child>> getChildrenByLevel(@PathVariable Long levelId) {
        return ResponseEntity.ok(childService.getChildrenByLevel(levelId));
    }
    
    @PatchMapping("/{id}/xp")
    public ResponseEntity<Child> updateChildXp(@PathVariable Long id, @RequestBody Map<String, Integer> xpData) {
        Child child = childService.getChild(id);
        if (child == null) {
            return ResponseEntity.notFound().build();
        }
        
        Integer xpToAdd = xpData.get("xp");
        if (xpToAdd != null) {
            child.setXp(child.getXp() + xpToAdd);
            // Update level based on XP (every 1000 XP = 1 level)
            child.setLevel((child.getXp() / 1000) + 1);
            return ResponseEntity.ok(childService.updateChild(child));
        }
        
        return ResponseEntity.badRequest().build();
    }
}
