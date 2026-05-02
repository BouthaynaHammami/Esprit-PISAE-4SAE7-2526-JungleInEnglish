package tn.esprit.language_courses_service.ChildrenEnglish.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class BadgeController {
    
    @GetMapping("/badges")
    public ResponseEntity<List<Object>> getAllBadges() {
        // Return empty list for now - implement later with actual badges
        return ResponseEntity.ok(new ArrayList<>());
    }
    
    @GetMapping("/rewards/{childId}")
    public ResponseEntity<List<Object>> getChildBadges(@PathVariable Long childId) {
        // Return empty list for now - implement later with actual child badges
        return ResponseEntity.ok(new ArrayList<>());
    }
    
    @PostMapping("/badges/check/{childId}")
    public ResponseEntity<List<Object>> checkForNewBadges(@PathVariable Long childId) {
        // Return empty list for now - implement later with badge checking logic
        return ResponseEntity.ok(new ArrayList<>());
    }
}
