package tn.esprit.language_courses_service.ChildrenEnglish.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.language_courses_service.ChildrenEnglish.Entities.Parent;
import tn.esprit.language_courses_service.ChildrenEnglish.Serivces.IParentService;

import java.util.List;

@RestController
@RequestMapping("/parents")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ParentController {
    
    private final IParentService parentService;
    
    @GetMapping
    public ResponseEntity<List<Parent>> getAllParents() {
        return ResponseEntity.ok(parentService.getAllParents());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Parent> getParentById(@PathVariable Long id) {
        Parent parent = parentService.getParent(id);
        return parent != null ? ResponseEntity.ok(parent) : ResponseEntity.notFound().build();
    }
    
    @PostMapping("/add")
    public ResponseEntity<Parent> addParent(@RequestBody Parent parent) {
        return ResponseEntity.ok(parentService.addParent(parent));
    }
    
    @PutMapping("/update")
    public ResponseEntity<Parent> updateParent(@RequestBody Parent parent) {
        return ResponseEntity.ok(parentService.updateParent(parent));
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteParent(@PathVariable Long id) {
        parentService.deleteParent(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<Parent> getParentByUserId(@PathVariable Integer userId) {
        Parent parent = parentService.getParentByUserId(userId);
        return parent != null ? ResponseEntity.ok(parent) : ResponseEntity.notFound().build();
    }
}
