package tn.esprit.language_courses_service.ChildrenEnglish.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/leaderboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class LeaderboardController {
    
    @GetMapping
    public ResponseEntity<List<Object>> getLeaderboard(@RequestParam(defaultValue = "10") int limit) {
        // Return empty list for now - implement later with actual leaderboard data
        return ResponseEntity.ok(new ArrayList<>());
    }
}
